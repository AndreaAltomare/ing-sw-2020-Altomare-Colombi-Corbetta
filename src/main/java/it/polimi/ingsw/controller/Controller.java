package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.VCEventListener;
import it.polimi.ingsw.storage.ResourceManager;
import it.polimi.ingsw.view.events.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Game Controller.
 *
 * @author AndreaAltomare
 */
public class Controller extends Observable<Object> implements VCEventListener, Runnable {
    /* Members for Controller-Model interaction */
    private Model model; // Object reference to the Game Model
    /* Members for generic game info */
    private List<String> players; // these are JUST NICKNAMES (used to make interaction through Observer Pattern simpler)
    private List<String> cardsInGame; // Cards used for this game match
    /* Members for concurrency handling */
    private volatile String genericResponse; // String used for generic response from client
    private volatile boolean clientResponded; // used to control generic responses from Players
    private volatile boolean challengerHasChosen; // Marked as volatile because it is accessed by different threads // TODO: ensure that "volatile" attribute mark does not clash with "synchronized" methods that update this very attribute
    private final Object clientLock; // lock used for responses from Client(s)
    private final Object challengerLock; // lock used for responses from Challenger
    private volatile String startPlayer; // the Start Player for this game
    private final int DEFAULT_WAITING_TIME; // time is in milliseconds
    /* Miscellaneous */
    private volatile int workersPlaced;

    /**
     * Constructor for Controller class.
     * An instance of Model is passed to let the Controller
     * interact with the Model.
     *
     * @param model (Model instance)
     */
    public Controller(Model model, List<String> players) {
        this.model = model;
        this.players = new ArrayList<>(players);
        this.clientResponded = false;
        this.challengerHasChosen = false;
        this.clientLock = new Object();
        this.challengerLock = new Object();
        this.DEFAULT_WAITING_TIME = 1000; // time is in milliseconds
        this.workersPlaced = 0;
    }




    /* ##################################### MAIN LOGIC ##################################### */




    /**
     * Run() method for Controller class, to let the game flow
     * be controlled in a separated thread from the one into which
     * run the connection Socket.
     *
     * This method is run when a new game needs to start.
     */
    @Override
    public void run() { // TODO: maybe refactor this into a more readable method (every step should be encapsulated in a same-abstraction-level method)
        /* 0- Initialize the Model and Send to the Client general info for this game */
        System.out.println("\n##### Running Controller... #####\n"); // Server control message
        model.initialize(this, players);
        notify(new ServerSendDataEvent(model.getBoardXSize(), model.getBoardYSize(), players, model.getWorkersToPlayers()));

        /* 1- Select the -Challenger- Player */
        System.out.println("##### Game Preparation phase #####"); // Server control message
        setChallenger();
        System.out.println(" - Challenger Player set: '" + model.challenger() + "'"); // Server control message
        notify(new MessageEvent("The Challenger Player has been chosen. It's: " + model.challenger())); // notify players that the Challenger has been chosen

        /* 2- Challenger choose the Cards for this game match */
        notify(new NextStatusEvent("Game preparation"));
        List<CardInfo> cardInfoList = ResourceManager.getCardsInformation();
        challengerChoosesCards(cardInfoList);
        printCardsInGame(cardsInGame);

        /* 3- In "clockwise" order, every Player choose a Card (among the Cards chosen by the Challenger) */
        List<CardInfo> cardsToChoose = cardInfoList.stream().filter(c -> cardsInGame.contains(c.getName())).collect(Collectors.toList());
        playersChooseCards(cardsToChoose);
        //System.out.println(""); // Server control message   --->   in playersChooseCards(...) method

        /* 4- Ask the Challenger for the Start Player */
        challengerChoosesStartPlayer();
        System.out.println(" - Challenger Player has chosen Start Player: '" + model.startPlayer() + "'"); // Server control message
        //notify(new MessageEvent("Other players are placing their workers. Wait...")); // notify other Players // todo maybe it's useless: to remove

        /* 5- Sort the list of Players */
        sortPlayers();

        /* 6- In "clockwise" order, starting from Start Player, every Player places his/her Workers on the Board */
        playersPlaceWorkers();

        /* 7- Send general game info data to all Views so the game can start */
        System.out.println(" - Notifying Players that the game is starting..."); // Server control message
        notify(new ServerSendDataEvent(model.getBoardXSize(), model.getBoardYSize(), players, model.getWorkersToPlayers()));

        /* 8- Game preparation phase is over. The game match can start */
        notify(new NextStatusEvent("The game has started!"));
        startGame();
        System.out.println("\n##### Game started #####\n"); // Server control message
    }




    /* ############################## FIRST LEVEL METHOD LOGIC ############################## */




    /**
     * This method encapsulates the logic for the Workers placement by every Player.
     */
    private void playersPlaceWorkers() {
        for(int i = 0; i < players.size(); i++) {
            setClientResponded(false);
            setWorkersPlaced(0);
            while(!clientResponded) {
                // 6.1- Ask the Player to place his/her Worker
                notify(new RequirePlaceWorkersEvent(players.get(i)));
                // 6.2- Wait for response
                waitForPlayerResponse();
                // 6.3- Validation of Worker placement is done in the update method.
            }
        }
    }

    /**
     * Sort the list of Players based on the Start Player.
     */
    private void sortPlayers() {
        players.remove(model.startPlayer());
        players.add(0, model.startPlayer());
    }

    /**
     * This method encapsulates the logic for the choice of the Start Player by the Challenger.
     */
    private void challengerChoosesStartPlayer() {
        challengerHasChosen = false;
        while(!challengerHasChosen) {
            // 4.1- Send to the Challenger a request
            notify(new RequireStartPlayerEvent(players, model.challenger())); // todo this event is broadcasted
            // 4.2- Waits until Challenger hasn't chosen the Start Player for this game
            waitForChallengerResponse();
            // 4.3- Check if the choice is valid
            challengerHasChosen = checkValidStartPlayer();
        }
    }

    /**
     * This method encapsulates the logic for the choice of Cards by the Players.
     *
     * @param cardsToChoose (Cards among the Players can choose (chosen previously by the Challenger))
     */
    private void playersChooseCards(List<CardInfo> cardsToChoose) {
        // 3.2- Notify every Player in "clockwise" order
        for(int i = 1; i < players.size(); i++) { // for cycle starts by 1 (one) because Player n. 0 is the Challenger
            clientResponded = false;
            cardsToChoose = requestPlayerForCardChoice(cardsToChoose, players.get(i));
        }
        // 3.3- Set the last Card to the Challenger
        setLastCard(cardsToChoose);
    }

    /**
     * This method encapsulates the logic for the choice of the Cards in this game by the Challenger.
     *
     * @param cardInfoList (Cards among Challenger can choose)
     */
    private void challengerChoosesCards(List<CardInfo> cardInfoList) {
        List<String> possibleCards = cardInfoList.stream().map(c -> c.getName()).collect(Collectors.toList()); // get just Cards' name // todo: check if this works correctly
        // 2.2- Notify other Players that Cards are being chosen by the Challenger // todo remove this block
//        players.forEach(p -> {
//            if(!p.equals(challenger)) {
//                notify(new MessageEvent(challenger + " is choosing the God Cards for this game! Wait..."));
//            }
//        });
        while(!challengerHasChosen) {
            // 2.2- Broadcast Card's information
            notify(new CardsInformationEvent(cardInfoList, model.challenger(), "")); // todo maybe CardInformationEvent can act as a kind of "NextStatusEvent" (needs to be broadcasted of course)
            // 2.3- Waits until Challenger hasn't chosen the Cards for this game
            waitForChallengerResponse();
            // 2.4- The number of chosen Cards needs to be equal to the number of the Players in the game (and Cards chosen must be valid)
            challengerHasChosen = checkValidChosenCards(possibleCards);
        }
    }

    /**
     * Set the Challenger Player for this game.
     */
    private void setChallenger() {
        String challenger = players.get(0); // Choose the Challenger (this can be done in any possible way)
        model.setChallenger(challenger); // set Challenger into the Game Model
    }

    /**
     * Prints Cards chosen for this game.
     * (Server-side control message)
     *
     * @param cardsInGame (List of cards in game)
     */
    private void printCardsInGame(List<String> cardsInGame) {
        int i = 1;
        System.out.println(" - Challenger has chosen Cards for this game: "); // Server control message
        for(String card : cardsInGame) {
            System.out.println("    " + i + ". " + card);
            i++;
        }
    }




    /* ############################## SECOND LEVEL METHOD LOGIC ############################## */




    /**
     * Checks if the Cards chosen by the Challenger are valid.
     *
     * @param possibleCards (Possible Cards among which the Challenger can choose)
     * @return (Cards chosen are valid ? true : false)
     */
    private boolean checkValidChosenCards(List<String> possibleCards) {
        boolean chosenCardsAreValid = true;

        if (cardsInGame.size() != players.size()) {
            notify(new ErrorMessageEvent("You must choose a number of cards equal to the number of players in this game!"), model.challenger());
            setChallengerHasChosen(false); // SYNCHRONOUSLY set the attribute to false
            chosenCardsAreValid = false;
        }
        else if (!(possibleCards.containsAll(cardsInGame)) || !itemsAllDifferent(cardsInGame)) {
            notify(new ErrorMessageEvent("Your choice is invalid! Please, try again."), model.challenger());
            setChallengerHasChosen(false); // SYNCHRONOUSLY set the attribute to false
            chosenCardsAreValid = false;
        }
        else {
            model.setCardsInGame(cardsInGame);
            notify(new MessageEvent("Your choice has been registered!"), model.challenger());
        }

        return chosenCardsAreValid;
    }

    /**
     * Sets the last Card left to the Challenger.
     *
     * @param cardsToChoose (Cards among which the Player can choose)
     */
    private void setLastCard(List<CardInfo> cardsToChoose) {
        List<String> lastCardList = cardsToChoose.stream().map(c -> c.getName()).collect(Collectors.toList());
        String lastCard = lastCardList.get(0);
        model.setPlayerCard(lastCard, model.challenger());
        notify(new CardSelectedEvent(lastCard, model.challenger(), true)); // ANSWER FROM THE CONTROLLER (Notify the View)
        cardsToChoose.removeIf(c -> c.getName().equals(lastCard));
        System.out.println(" - Player '" + model.challenger() + "' was set with the last Card left: '" + lastCard + "'"); // Server control message
    }

    /**
     * Checks if the Start Player chosen by the Challenger is valid.
     *
     * @return (Start Player chosen is valid ? true : false)
     */
    private boolean checkValidStartPlayer() {
        synchronized (startPlayer) { // todo: this should work (it's not used properly as a Lock)
            if (!players.contains(startPlayer)) {
                notify(new ErrorMessageEvent("Your choice is invalid! Please, try again."), model.challenger());
                setChallengerHasChosen(false); // SYNCHRONOUSLY set the attribute to false
                return false;
            }
            else {
                model.setStartPlayer(startPlayer);
                notify(new MessageEvent(model.startPlayer() + " will be the Start Player for this game!"));
                return true;
            }
        }
    }

    /**
     * (Synchronized) Waits for a response from the Challenger.
     */
    private void waitForChallengerResponse() {
        synchronized (challengerLock) {
            while (!challengerHasChosen) { // works like a wait // todo (IT SHOULD WORK, check it... [or if it has thread-related problems])
                try {
                    challengerLock.wait(); // TODO: don't know if this works correctly
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * (Synchronized) Waits for a response from a Player.
     */
    private void waitForPlayerResponse() {
        synchronized (clientLock) {
            while (!clientResponded) {
                try {
                    clientLock.wait(); // TODO: don't know if this works correctly
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Ask a given Player for a Card choice.
     * Perform some checks to verify the choice
     * made is valid.
     *
     * @param cardsToChoose (Cards among which the Player can choose)
     * @param playerNickname (Player's nickname)
     * @return An updated version of cardsToChoose List
     */
    private List<CardInfo> requestPlayerForCardChoice(List<CardInfo> cardsToChoose, String playerNickname) {
        List<String> validCards = cardsToChoose.stream().map(c -> c.getName()).collect(Collectors.toList());// cards that can be chosen as a response from the Player
        notify(new NextStatusEvent("Choose your Card!"), playerNickname); // notify the Player he/she needs to choose a Card // todo maybe it's to remove
        while (!clientResponded) {
            // 3.2.1- Send messages to the Player
            notify(new CardsInformationEvent(cardsToChoose, model.challenger(), playerNickname)); // send Cards information to the Player
            // 3.2.2- Wait for the response
            waitForPlayerResponse();
            // 3.2.3- Check for the correctness of the choice made
            cardsToChoose = checkValidSelectedCards(cardsToChoose, playerNickname, validCards);
        }

        return cardsToChoose;
    }




    /* ############################## THIRD LEVEL METHOD LOGIC ############################## */




    /**
     * Checks if Cards SELECTED by a Player are valid.
     *
     * @param cardsToChoose (Cards among which the Player can choose)
     * @param playerNickname (Player's nickname)
     * @param validCards (List of String representing valid Cards which can be chosen)
     * @return An updated version of cardsToChoose List
     */
    private List<CardInfo> checkValidSelectedCards(List<CardInfo> cardsToChoose, String playerNickname, List<String> validCards) {
        synchronized (genericResponse) {
            if (!(validCards.contains(genericResponse))) {
                notify(new ErrorMessageEvent("Your choice is invalid! Please, try again."), playerNickname);
                setClientResponded(false); // SYNCHRONOUSLY set the attribute to false
            }
            else {
                model.setPlayerCard(genericResponse, playerNickname);
                notify(new CardSelectedEvent(genericResponse, playerNickname, true)); // ANSWER FROM THE CONTROLLER (Notify the View)
                cardsToChoose.removeIf(c -> c.getName().equals(genericResponse));
                System.out.println(" - Player '" + playerNickname + "' has selected his/her Card: '" + genericResponse + "'"); // Server control message
            }
        }

        return cardsToChoose;
    }

    /**
     * Tells if all items in a provided List are different.
     *
     * @param cardsInGame (List of items)
     * @return (All items in a provided List are different ? true : false)
     */
    private boolean itemsAllDifferent(List<String> cardsInGame) {
        boolean cardUnique = false;
        Set<String> cardsSet = new HashSet<>(cardsInGame.size());

        for(String card : cardsInGame) {
            cardUnique = cardsSet.add(card);

            if(!cardUnique)
                return false;
        }

        return true;
    }









    /* ############### UPDATE METHODS FOR VCEvents LISTENER (OBSERVER PATTERN) ############## */







    /* Client general listener */
    @Override
    public synchronized void update(SetNicknameEvent submittedNickname) {
        System.err.println("ERROR: Unexpected SetNicknameEvent received: '" + submittedNickname + "'");
        notify(new ErrorMessageEvent("Bad Request: Server cannot process your request!"), submittedNickname.getNickname());
    }

    @Override
    public synchronized void update(SetPlayersNumberEvent playersNumber) {
        System.err.println("ERROR: Unexpected SetPlayersNumberEvent received!");
        //notify(new ErrorMessageEvent("Bad Request: Server cannot process your request!"));
    }

    @Override
    public synchronized void update(QuitEvent quit, String playerNickname) {
        // TODO: add operations to handle disconnections of Players
        System.out.println("[QuitEvent] received from Player " + playerNickname + ": quitting...");
        this.stopGame();
        notify(new GameOverEvent("A player quit. This game can no longer continue."));
        // TODO: inserire operazioni per distruggere oggetti nel Server e tornare in uno stato in cui si accettano nuovi giocatori
    }

    /* Not actually necessary for Distributed-MVC Pattern */
    @Override
    public synchronized void update(ViewRequestDataEvent dataRequest, String playerNickname) {
        System.err.println("ERROR: Unexpected ViewRequestDataEvent received!");
        notify(new ErrorMessageEvent("Bad Request: Server cannot process your request!"), playerNickname);
    }


    /* Game preparation listener */
    /**
     * This method is reserved for the Challenger Player.
     *
     * @param chosenCards (CardsChoosingEvent received for the Player)
     * @param playerNickname (Player's nickname)
     */
    @Override
    public synchronized void update(CardsChoosingEvent chosenCards, String playerNickname) {
        System.out.println("[CardsChoosingEvent] received from Player: '" + playerNickname + "'"); // Server control message
        if(playerNickname.equals(model.challenger())) {
            synchronized (challengerLock) {
                cardsInGame = chosenCards.getCards();
                challengerHasChosen = true; // todo remember to set this to false when a new game starts (if necessary)
                challengerLock.notifyAll();
            }
        }
    }

    @Override
    public synchronized void update(CardSelectionEvent card, String playerNickname) {
        System.out.println("[CardSelectionEvent] received from Player: '" + playerNickname + "'"); // Server control message
        synchronized (clientLock) {
            genericResponse = card.getCardName();
            clientResponded = true;
            clientLock.notifyAll();
        }
    }

    /**
     * This method is reserved for the Challenger Player.
     *
     * @param startPlayer (SetStartPlayerEvent received for the Player)
     * @param playerNickname (Player's nickname)
     */
    @Override
    public synchronized void update(SetStartPlayerEvent startPlayer, String playerNickname) {
        System.out.println("[SetStartPlayerEvent] received from Player: '" + playerNickname + "'"); // Server control message
        if(playerNickname.equals(model.challenger())) {
            synchronized (challengerLock) {
                this.startPlayer = startPlayer.getStartPlayer();
                challengerHasChosen = true; // todo remember to set this to false when a new game starts (if necessary)
                challengerLock.notifyAll();
            }
        }
    }

    @Override
    public synchronized void update(PlaceWorkerEvent workerToPlace, String playerNickname) {
        System.out.println("[PlaceWorkerEvent] received from Player: '" + playerNickname + "'"); // Server control message
        synchronized (clientLock) {
            String workerId = model.placeWorker(workerToPlace.getX(), workerToPlace.getY(), playerNickname);
            if(workerId != null) {
                workersPlaced++;
                notify(new WorkerPlacedEvent(workerId, workerToPlace.getX(), workerToPlace.getY(), true)); // broadcast notification of a worker placed event
                System.out.println(" - Player '" + playerNickname + "' placed a Worker in position: ( " + workerToPlace.getX() + " , " + workerToPlace.getY() + " )"); // Server control message
            }
            else {
                notify(new ErrorMessageEvent("Your choice is invalid! Try to place your worker again."), playerNickname);
            }

            if(workersPlaced == model.WORKERS_PER_PLAYER) {
                clientResponded = true;
                clientLock.notifyAll();
            }
        }
    }


    /* Move listener */
    @Override
    public synchronized void update(SelectWorkerEvent selectedWorker, String playerNickname) {
        if(model.hasGameStarted()) {
            System.out.println("[SelectWorkerEvent] received form Player: '" + playerNickname + "'"); // Server control message
            WorkerSelectedEvent workerSelected = model.selectWorker(selectedWorker.getWorkerId(), playerNickname);
            if (workerSelected != null)
                notify(workerSelected); // ANSWER FROM THE CONTROLLER (Notify the View)
        /*else
            notify(new ErrorMessageEvent("This Worker cannot be selected! Please try again."), playerNickname);*/
        }
        else
            notify(new ErrorMessageEvent("Game has not started yet! Hold on..."), playerNickname);
    }

    @Override
    public synchronized void update(MoveWorkerEvent move, String playerNickname) {
        if(model.hasGameStarted()) {
            System.out.println("[MoveWorkerEvent] received form Player: '" + playerNickname + "'"); // Server control message
            WorkerMovedEvent workerMoved = model.moveWorker(move.getWorkerId(), move.getX(), move.getY(), playerNickname);
            if (workerMoved != null) {
                notify(workerMoved); // ANSWER FROM THE CONTROLLER (Notify the View)
                checkForSwitching(playerNickname);
            }
        }
        else
            notify(new ErrorMessageEvent("Game has not started yet! Hold on..."), playerNickname);
    }

    @Override
    public synchronized void update(BuildBlockEvent build, String playerNickname) {
        if(model.hasGameStarted()) {
            System.out.println("[BuildBlockEvent] received form Player: '" + playerNickname + "'"); // Server control message
            BlockBuiltEvent blockBuilt = model.buildBlock(build.getWorkerId(), build.getX(), build.getY(), build.getBlockType(), playerNickname);
            if (blockBuilt != null) {
                notify(blockBuilt); // ANSWER FROM THE CONTROLLER (Notify the View)
                checkForSwitching(playerNickname);
            }
        }
        else
            notify(new ErrorMessageEvent("Game has not started yet! Hold on..."), playerNickname);
    }

    @Override
    public synchronized void update(RemoveWorkerEvent workerToRemove, String playerNickname) {
        if(model.hasGameStarted()) {
            System.out.println("[RemoveWorkerEvent] received form Player: '" + playerNickname + "'"); // Server control message
            WorkerRemovedEvent workerRemoved = model.removeWorker(workerToRemove.getWorkerId(), workerToRemove.getX(), workerToRemove.getY(), playerNickname);
            if (workerRemoved != null)
                notify(workerRemoved); // ANSWER FROM THE CONTROLLER (Notify the View)

            /* ANSWER FROM THE CONTROLLER (Notify the View) */
            /*notify(new WorkerRemovedEvent(workerToRemove.getWorkerId(),workerToRemove.getX(),workerToRemove.getY()));*/ // todo: retrieve X and Y position by using Worker's unique ID
        }
        else
            notify(new ErrorMessageEvent("Game has not started yet! Hold on..."), playerNickname);
    }

    @Override
    public synchronized void update(RemoveBlockEvent blockToRemove, String playerNickname) {
        if(model.hasGameStarted()) {
            System.out.println("[RemoveBlockEvent] received form Player: '" + playerNickname + "'"); // Server control message
            BlockRemovedEvent blockRemoved = model.removeBlock(blockToRemove.getWorkerId(), blockToRemove.getX(), blockToRemove.getY(), playerNickname);
            if (blockRemoved != null)
                notify(blockRemoved); // ANSWER FROM THE CONTROLLER (Notify the View)

            /* ANSWER FROM THE CONTROLLER (Notify the View) */
            /*notify(new BlockRemovedEvent(blockToRemove.getX(),blockToRemove.getY(), PlaceableType.DOME));*/
        }
        else
            notify(new ErrorMessageEvent("Game has not started yet! Hold on..."), playerNickname);
    }


    /* Turn status change listener */
    @Override
    public synchronized void update(TurnStatusChangeEvent turnStatus, String playerNickname) {
        if(model.hasGameStarted()) {
            System.out.println("[TurnStatusChangeEvent] received form Player: '" + playerNickname + "'"); // Server control message
            TurnStatusChangedEvent turnStatusChanged = model.changeTurnStatus(turnStatus.getTurnStatus(), playerNickname);
            if (turnStatusChanged != null) {
                notify(turnStatusChanged, playerNickname); // ANSWER FROM THE CONTROLLER (Notify the View)
                checkForSwitching(playerNickname);
            }

            /* ANSWER FROM THE CONTROLLER (Notify the View) */
            /*notify(new TurnStatusChangedEvent(playerNickname, turnStatus.getTurnStatus()), playerNickname);*/
        }
        else
            notify(new ErrorMessageEvent("Game has not started yet! Hold on..."), playerNickname);
    }


    /* Generic update method */
    @Override
    public synchronized void update(Object o) {
        System.err.println("ERROR: Unexpected [Generic] VCEvent received!");
        //notify(new ErrorMessageEvent("Bad Request: Server cannot process your request!"));
    }









    /* ########################### AUXILIARY AND SUPPORT METHODS ############################ */




    /**
     * Starts the game.
     */
    private void startGame() {
        model.sortPlayers(players); // sort the players' list before stating
        try {
            model.startGame(); // start the game
            model.setGameStarted(true);
            String playingPlayer = model.getPlayingPlayer();
            notify(new TurnStatusChangedEvent(playingPlayer, StateType.MOVEMENT, true));
            // Notify other Players too
            for(String player : players)
                if (!player.equals(playingPlayer))
                    notify(new TurnStatusChangedEvent(playingPlayer, StateType.NONE, true));
        }
        catch (LoseException ex) {
            System.err.println("An error has occurred during the starting of the game!");
            model.setGameStarted(false);
        }
    }

    /**
     * Stops the game.
     */
    private void stopGame() {
        /* 1- Stop the game */
        model.stopGame();
        /* 2- Save the match */ // TODO: [MAYBE] For "Persistence" FA
        saveGame();
    }

    /**
     * Save the game.
     */
    private void saveGame() { // TODO: [MAYBE] For "Persistence" FA
        System.out.println("The game is being saved..."); // todo [debug]
        /* 1- Get game state from Model */
//        GameState gameState = model.getGameState();
//        /* 2- Save game state */
//        ResourceManager.saveGameState(gameState);
    }

    /**
     * Load a saved game.
     */
    private void loadGame() { // TODO: [MAYBE] For "Persistence" FA
        /* 1- Load game state */
//        GameState gameState = ResourceManager.loadGameState();
//        /* 2- Restore game state to the Model */
//        model.restoreGameState(gameState);
    }

    public synchronized void setChallengerHasChosen(boolean challengerHasChosen) {
        this.challengerHasChosen = challengerHasChosen;
    }

    public Boolean getClientResponded() {
        return clientResponded;
    }

    public synchronized void setClientResponded(Boolean clientResponded) {
        this.clientResponded = clientResponded;
    }

    public String getStartPlayer() {
        return model.startPlayer();
    }

    public synchronized void setStartPlayer(String startPlayer) {
        this.startPlayer = startPlayer;
    }

    public synchronized boolean gameIsStarted() {
        return model.hasGameStarted();
    }

    public int getWorkersPlaced() {
        return workersPlaced;
    }

    public synchronized void setWorkersPlaced(int workersPlaced) {
        this.workersPlaced = workersPlaced;
    }

    /**
     * After the execution of a move, this method checks
     * if any switching-player condition is triggered.
     *
     * @param playerNickname (Player who just made the move)
     */
    private void checkForSwitching(String playerNickname) {
        /* After the Player notification, if the player's turn is over (or other triggering conditions), switch the playing Player */
        if (model.getMoveOutcome() == MoveOutcomeType.TURN_OVER)
            model.switchPlayer();
        else if (model.getMoveOutcome() == MoveOutcomeType.LOSS) {
            notify(new PlayerLoseEvent(playerNickname, "Player " + playerNickname + " has lost the game!"));
            model.handlePlayerLoss(playerNickname);
            model.switchPlayer();
        } else if (model.getMoveOutcome() == MoveOutcomeType.WIN) {
            model.handlePlayerWin(playerNickname);
        }
    }

    /**
     * Method used to let the Model easily send notification to Players.
     *
     * @param o (Object to be notified)
     */
    public void notifyFromModel(Object o) {
        notify(o);
    }

    /**
     * Method used to let the Model easily send notification to a specific Player.
     *
     * @param o (Object to be notified)
     * @param playerNickname (Player's nickname)
     */
    public void notifyFromModel(Object o, String playerNickname) {
        notify(o, playerNickname);
    }

    /**
     * Remove the Virtual View bounded to the provided Player.
     *
     * @param nickname (Player's nickname)
     */
    public void removeView(String nickname) {
        notify(new GameOverEvent("You are being disconnected from the game..."), nickname);
    }

    /**
     * Print a control message.
     * (Server-side).
     *
     * @param message (Message to print)
     */
    public void printControlMessage(String message) {
        System.out.println(message); // Server control message
    }


// todo /* ########## CODE FOR TEST ########## */ [This code has already been tested. Maybe to remove]

//    /* Client general listener */
//    @Override
//    public synchronized void update(SetNicknameEvent submittedNickname) {
//        System.out.println("SetNicknameEvent received: " + submittedNickname);
//        notify(new MessageEvent("Server respond: Nickname '" + submittedNickname + "' received."));
//    }
//
//    @Override
//    public synchronized void update(SetPlayersNumberEvent playersNumber) {
//        System.out.println("SetPlayersNumberEvent received. The number of Players set by the Client is: " + playersNumber.getNumberOfPlayers());
//        notify(new MessageEvent("Server respond: number of Players: '" + playersNumber.getNumberOfPlayers() + "' received."));
//    }
//
//    @Override
//    public synchronized void update(QuitEvent quit, String playerNickname) {
//        // TODO: add operations to handle disconnections of Players
//        System.out.println("QuitEvent received from Player " + playerNickname + ": quitting...");
//    }
//
//    @Override
//    public synchronized void update(ViewRequestDataEvent dataRequest, String playerNickname) {
//        System.out.println("ViewRequestDataEvent received form Player: " + playerNickname);
//
//        /* ANSWER FROM THE CONTROLLER (Notify the View) */
//        // Board size data preparation
//        int boardXSize = 5;
//        int boardYSize = 5;
//        // Players data preparation
//        List<String> players = new ArrayList<>();
//        players.add(playerNickname);
//        players.add("Tizio");
//        players.add("Caio");
//        // Workers associated to Players data preparation
//        Map<String, List<String>> workersToPlayer = new HashMap<>();
//        List<String> workers1 = new ArrayList<>();
//        workers1.add("Worker1@" + playerNickname); // with this string format we ensure also strings are correctly sent.
//        workers1.add("Worker2@" + playerNickname);
//        List<String> workers2 = new ArrayList<>();
//        workers2.add("Worker1@Tizio");
//        workers2.add("Worker2@Tizio");
//        List<String> workers3 = new ArrayList<>();
//        workers3.add("Worker1@Caio");
//        workers3.add("Worker2@Caio");
//        workersToPlayer.put(playerNickname,workers1);
//        workersToPlayer.put("Tizio",workers2);
//        workersToPlayer.put("Caio",workers3);
//        notify(new ServerSendDataEvent(boardXSize,boardYSize,players,workersToPlayer), playerNickname); // TODO: here, check if unicast communication works correctly
//    }
//
//
//    /* Card selection listener */
//    @Override
//    public synchronized void update(CardSelectionEvent card, String playerNickname) {
//        System.out.println("CardSelectionEvent received form Player: " + playerNickname);
//        System.out.println("Card selected: " + card); // todo a questo punto check se il metodo overridden toString() funziona. Inoltre, se funziona questo metodo, vuol dire che sicuramente la serializzazione/deserializzazione e relativi metodi funzionano correttamente
//
//        /* ANSWER FROM THE CONTROLLER (Notify the View) */
//        notify(new CardSelectedEvent(card.getCardName(), playerNickname));
//    }
//
//    @Override
//    public void update(CardsChoosingEvent chosenCards, String playerNickname) {
//        // todo something
//    }
//
//    @Override
//    public void update(SetStartPlayerEvent startPlayer, String playerNickname) {
//        // todo something
//    }
//
//
//    /* Move listener */
//    @Override
//    public synchronized void update(PlaceWorkerEvent workerToPlace, String playerNickname) {
//        System.out.println("PlaceWorkerEvent received form Player: " + playerNickname);
//
//        /* ANSWER FROM THE CONTROLLER (Notify the View) */
//        notify(new WorkerPlacedEvent("worker1@" + playerNickname, workerToPlace.getX(),workerToPlace.getY()));
//    }
//
//    @Override
//    public synchronized void update(SelectWorkerEvent selectedWorker, String playerNickname) {
//        System.out.println("SelectWorkerEvent received form Player: " + playerNickname);
//
//        /* ANSWER FROM THE CONTROLLER (Notify the View) */
//        notify(new WorkerSelectedEvent(playerNickname, selectedWorker.getWorkerId()));
//    }
//
//    @Override
//    public synchronized void update(MoveWorkerEvent move, String playerNickname) {
//        System.out.println("MoveWorkerEvent received form Player: " + playerNickname);
//
//        /* ANSWER FROM THE CONTROLLER (Notify the View) */
//        notify(new WorkerMovedEvent(move.getWorkerId(),1,1,move.getX(),move.getY()));
//    }
//
//    @Override
//    public synchronized void update(BuildBlockEvent build, String playerNickname) {
//        System.out.println("BuildBlockEvent received form Player: " + playerNickname);
//
//        /* ANSWER FROM THE CONTROLLER (Notify the View) */
//        notify(new BlockBuiltEvent(build.getX(),build.getY(), build.getBlockType()));
//    }
//
//    @Override
//    public synchronized void update(RemoveWorkerEvent workerToRemove, String playerNickname) {
//        System.out.println("RemoveWorkerEvent received form Player: " + playerNickname);
//
//        /* ANSWER FROM THE CONTROLLER (Notify the View) */
//        notify(new WorkerRemovedEvent(workerToRemove.getWorkerId(),workerToRemove.getX(),workerToRemove.getY())); // todo: retrieve X and Y position by using Worker's unique ID
//    }
//
//    @Override
//    public synchronized void update(RemoveBlockEvent blockToRemove, String playerNickname) {
//        System.out.println("RemoveBlockEvent received form Player: " + playerNickname);
//
//        /* ANSWER FROM THE CONTROLLER (Notify the View) */
//        notify(new BlockRemovedEvent(blockToRemove.getX(),blockToRemove.getY(), PlaceableType.DOME));
//    }
//
//
//    /* Turn status change listener */
//    @Override
//    public synchronized void update(TurnStatusChangeEvent turnStatus, String playerNickname) {
//        System.out.println("TurnStatusChangeEvent received form Player: " + playerNickname);
//
//        /* ANSWER FROM THE CONTROLLER (Notify the View) */
//        notify(new TurnStatusChangedEvent(playerNickname, turnStatus.getTurnStatus()), playerNickname);
//    }
//
//    /* Generic update method */
//    @Override
//    public synchronized void update(Object o) {
//        // todo: maybe these statements are to remove
//        System.out.println("Generic VCEvent received");
//
//        /* ANSWER FROM THE CONTROLLER (Notify the View) */
//        notify(new MessageEvent("Received a generic Object from your Client"));
//    }





    // TODO: Other code to remove
    //@Override
    /*public void onCardSelection(String cardName, String playerNickname) {
        // todo instantiate card for the player who choose it
    }*/

    //@Override
    /*public void onNicknameSubmit(String nickname) {
        // todo useless: to remove here
    }*/

    //@Override
    /*public void onPlayerQuit(String playerNickname) {
        // todo if the player has lost, just remove it from the players list
    }*/

    //@Override
    /*public void viewRequestData() {
        // todo code: maybe require a broadcast data forwarding
    }*/

    //@Override
    /*public void onWorkerMovement(String worker, int x, int y) {
        // todo handle the case in which the worker does not belong to this player
    }*/

    //@Override
    /*public void onWorkerConstruction(String worker, int x, int y) {
        // todo same here as onWorkerMovement(...) method
    }

    //@Override
    public void onTurnStatusChange(StateType turnStatus) {
        // todo change in: public void onTurnStatusChange(EventObject StatusChangeEvent e, String playerNickname);
    }*/














    // TODO: This block of code probably needs to be removed
    /*
     * General update() method for Observer Pattern.
     * Receive generic messages from VirtualView.
     *
     * @param o (Object message)
     */
    //@Override
    /*public void update(Object o) {
        // todo code
        System.out.println("Received a generic message: " + (String)o);
    }*/

    /*
     * General update() method for Observer Pattern.
     * Receive messages from a specific Player's VirtualView.
     *
     * @param o (Object message)
     * @param playerNickname (Player's unique nickname)
     */
    //@Override
    /*public void update(Object o, String playerNickname) {
        System.out.println("Received a message from: " + playerNickname + "\nIt says: " + (String)o);
    }*/
}
