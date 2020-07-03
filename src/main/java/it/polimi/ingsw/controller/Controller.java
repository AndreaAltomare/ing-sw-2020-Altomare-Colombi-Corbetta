package it.polimi.ingsw.controller;

import it.polimi.ingsw.connection.utility.ServerResetException;
import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.controller.undo.UndoManager;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.card.CardInfo;
import it.polimi.ingsw.model.exceptions.LoseException;
import it.polimi.ingsw.model.move.MoveOutcomeType;
import it.polimi.ingsw.model.persistence.GameState;
import it.polimi.ingsw.model.persistence.players.PlayerData;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.turn.StateType;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.VCEventListener;
import it.polimi.ingsw.storage.ResourceManager;
import it.polimi.ingsw.view.events.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Game Controller.
 * This class is the core of the software system.
 * It communicates with the Model and the View(s)
 * to let the game evolve properly.
 *
 * Controller class listens for events being send
 * from the View(s), updates Model, then notifies
 * View(s) about any event which occurred.
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
    private ExecutorService executor = Executors.newFixedThreadPool(10); // for async operations
    private volatile String genericResponse; // String used for generic response from client
    private volatile boolean clientResponded; // used to control generic responses from Players
    private volatile boolean challengerHasChosen; // Marked as volatile because it is accessed by different threads
    private final Object clientLock; // lock used for responses from Client(s)
    private final Object challengerLock; // lock used for responses from Challenger
    private volatile String startPlayer; // the Start Player for this game
    private volatile boolean resumeGame; // if the game needs to be resumed
    private volatile boolean undoRequested; // tells if an Undo action has been requested
    /* Miscellaneous */
    private volatile int workersPlaced;
    private UndoManager undoManager;
    private volatile boolean gameWasQuit;

    /**
     * Constructor for Controller class.
     * An instance of Model is passed to let the Controller
     * interact with the Model.
     *
     * @param model (Model instance)
     * @param players List of players
     */
    public Controller(Model model, List<String> players) {
        this.model = model;
        this.players = new ArrayList<>(players);
        this.clientResponded = false;
        this.challengerHasChosen = false;
        this.clientLock = new Object();
        this.challengerLock = new Object();
        this.workersPlaced = 0;
        this.gameWasQuit = false;
        this.resumeGame = false;
        this.undoRequested = false;
        this.undoManager = new UndoManager(clientLock);
    }




    /* ##################################### MAIN LOGIC ##################################### */




    /**
     * {@code Run()} method for Controller class, to let the game flow
     * be controlled in a separated thread from the one into which
     * run the connection Socket.
     *
     * This method is run when a new game needs to start.
     */
    @Override
    public void run() {
        /* 0- Initialize the Model and Send to the Client general info for this game */
        System.out.println("\n##### Running Controller... #####\n"); // Server control message
        model.initialize(this, players);
        notify(new ServerSendDataEvent(model.getBoardXSize(), model.getBoardYSize(), players, model.getWorkersToPlayers()));

        /* 1- Select the -Challenger- Player */
        System.out.println("##### Game Preparation phase #####"); // Server control message
        setChallenger();
        System.out.println(" - Challenger Player set: '" + model.challenger() + "'"); // Server control message
        //notify(new MessageEvent("The Challenger Player has been chosen. It's: " + model.challenger())); // notify players that the Challenger has been chosen

        /* --- CHECK FOR GAME SAVINGS --- */
        checkForSavings();

        /* --- PREPARE A NEW GAME ONLY IF A PREVIOUSLY SAVED GAME WAS NOT RESUMED --- */
        if(!resumeGame) {
            System.out.println("[SERVER] No game saving loaded.\nPreparing a new game...");

            //prepareGame(3); // [debug and simulation for testing]

            /* 2- Challenger choose the Cards for this game match */
            notify(new NextStatusEvent("Game preparation"));
            List<CardInfo> cardInfoList = ResourceManager.getCardsInformation();
            challengerChoosesCards(cardInfoList);
            printCardsInGame(cardsInGame);

            /* 3- In "clockwise" order, every Player choose a Card (among the Cards chosen by the Challenger) */
            List<CardInfo> cardsToChoose = cardInfoList.stream().filter(c -> cardsInGame.contains(c.getName())).collect(Collectors.toList());
            playersChooseCards(cardsToChoose);
            registerTurnObservers();
            //System.out.println(""); // Server control message   --->   in playersChooseCards(...) method

            /* 4- Ask the Challenger for the Start Player */
            challengerChoosesStartPlayer();
            System.out.println(" - Challenger Player has chosen Start Player: '" + model.startPlayer() + "'"); // Server control message
            //notify(new MessageEvent("Other players are placing their workers. Wait...")); // notify other Players

            /* 5- Sort the list of Players */
            sortPlayers();

            /* 6- In "clockwise" order, starting from Start Player, every Player places his/her Workers on the Board */
            playersPlaceWorkers();
            clearOperations();

            /* 7- Send general game info data to all Views so the game can start */
            System.out.println(" - Notifying Players that the game is starting..."); // Server control message
            notify(new ServerSendDataEvent(model.getBoardXSize(), model.getBoardYSize(), players, model.getWorkersToPlayers()));

            /* 8- Game preparation phase is over. The game match can start */
            notify(new NextStatusEvent("The game has started!"));
            startGame();
            saveGame(); // save newly started game
            System.out.println("\n##### Game started #####\n"); // Server control message
            System.out.println("### It's " + model.startPlayer() + "'s turn.");

            //simulateGame(5); // [debug and simulation for testing]
        }
    }












    /* ############################## FIRST LEVEL METHOD LOGIC ############################## */




    /**
     * This method initialize observers for Players' Turns.
     */
    private void registerTurnObservers() {
        model.registerTurnObservers();
    }

    /**
     * Clear some Model properties
     * to remain in a consistent state
     * if a new game starts.
     */
    private void clearOperations() {
        model.clearOperations();
    }

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
            notify(new RequireStartPlayerEvent(players, model.challenger()));
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
        List<String> possibleCards = cardInfoList.stream().map(c -> c.getName()).collect(Collectors.toList()); // get just Cards' name

        challengerHasChosen = false;
        while(!challengerHasChosen) {
            // 2.2- Broadcast Card's information
            notify(new CardsInformationEvent(cardInfoList, model.challenger(), ""));
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
            //notify(new MessageEvent("Your choice has been registered!"), model.challenger());
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
        synchronized (startPlayer) {
            if (!players.contains(startPlayer)) {
                notify(new ErrorMessageEvent("Your choice is invalid! Please, try again."), model.challenger());
                setChallengerHasChosen(false); // SYNCHRONOUSLY set the attribute to false
                return false;
            }
            else {
                model.setStartPlayer(startPlayer);
                //notify(new MessageEvent(model.startPlayer() + " will be the Start Player for this game!"));
                return true;
            }
        }
    }

    /**
     * (Synchronized) Waits for a response from the Challenger.
     */
    private void waitForChallengerResponse() {
        synchronized (challengerLock) {
            while (!challengerHasChosen) { // works like a wait
                try {
                    challengerLock.wait();
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
                    clientLock.wait();
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

    /**
     * Computes a SetNicknameEvent.
     * In this case, no action is taken since
     * the Player's nickname is set in Login phase.
     *
     * @param submittedNickname Event: Submitted nickname
     */
    @Override
    public synchronized void update(SetNicknameEvent submittedNickname) {
        System.err.println("ERROR: Unexpected SetNicknameEvent received: '" + submittedNickname + "'");
        notify(new ErrorMessageEvent("Bad Request: Server cannot process your request!"), submittedNickname.getNickname());
    }

    /**
     * Computes a SetPlayerNumberEvent.
     * In this case, no action is taken since
     * the number of Players for the game is set
     * in Login phase.
     *
     * @param playersNumber Event: Number of Players
     */
    @Override
    public synchronized void update(SetPlayersNumberEvent playersNumber) {
        System.err.println("ERROR: Unexpected SetPlayersNumberEvent received!");
        //notify(new ErrorMessageEvent("Bad Request: Server cannot process your request!"));
    }

    /**
     * Computes a GameResumingResponseEvent.
     * This method is reserved for the Challenger Player.
     *
     * @param gameResumingResponse Event: Response from Challenger about resuming a previously saved Game
     * @param playerNickname Player who sent the Event
     */
    public synchronized void update(GameResumingResponseEvent gameResumingResponse, String playerNickname) {
        if(!model.hasGameStarted() && playerNickname.equals(model.challenger())) {
            System.out.println("[GameResumingResponseEvent] received from Player: '" + playerNickname + "'"); // Server control message
            if (playerNickname.equals(model.challenger())) {
                synchronized (challengerLock) {
                    this.resumeGame = gameResumingResponse.isWantToResumeGame();
                    challengerHasChosen = true;
                    challengerLock.notifyAll();
                }
            }
        }
        else {
            System.out.println("!INVALID! [GameResumingResponseEvent] received from Player: '" + playerNickname + "'"); // Server control message
            notify(new ErrorMessageEvent("Game has already started! You can no longer resume any previous game."), playerNickname);
        }
    }

    /**
     * Computes a QuitEvent.
     * Calls operations for game quitting.
     *
     * @param quit Event: Quit the game
     * @param playerNickname Player who quit
     */
    @Override
    public synchronized void update(QuitEvent quit, String playerNickname) {
        System.out.println("[QuitEvent] received from Player " + playerNickname + ": quitting...");
        gameWasQuit = true;
        this.stopGame();
        notify(new GameOverEvent("A player quit. This game can no longer continue."));
        //resetGame();
    }

    /* Not actually necessary for Distributed-MVC Pattern */
    @Override
    public synchronized void update(ViewRequestDataEvent dataRequest, String playerNickname) {
        System.err.println("ERROR: Unexpected ViewRequestDataEvent received!");
        notify(new ErrorMessageEvent("Bad Request: Server cannot process your request!"), playerNickname);
    }


    /* Game preparation listener */

    /**
     * Computes a CardsChoosingEvent.
     * This method is reserved for the Challenger Player.
     *
     * @param chosenCards Event: CardsChoosingEvent received for the Player
     * @param playerNickname Player's nickname
     */
    @Override
    public synchronized void update(CardsChoosingEvent chosenCards, String playerNickname) {
        if(!model.hasGameStarted()) {
            System.out.println("[CardsChoosingEvent] received from Player: '" + playerNickname + "'"); // Server control message
            if (playerNickname.equals(model.challenger())) {
                synchronized (challengerLock) {
                    cardsInGame = chosenCards.getCards();
                    challengerHasChosen = true;
                    challengerLock.notifyAll();
                }
            }
        }
        else {
            System.out.println("!INVALID! [CardsChoosingEvent] received from Player: '" + playerNickname + "'"); // Server control message
            notify(new ErrorMessageEvent("Game has already started! You cannot choose Cards for this game."), playerNickname);
        }
    }

    /**
     * Computes a CardSelectionEvent.
     * Calls operations for Card selection and assignment.
     *
     * @param card Event: Selected Card
     * @param playerNickname Player who selected the Card
     */
    @Override
    public synchronized void update(CardSelectionEvent card, String playerNickname) {
        if(!model.hasGameStarted()) {
            System.out.println("[CardSelectionEvent] received from Player: '" + playerNickname + "'"); // Server control message
            synchronized (clientLock) {
                genericResponse = card.getCardName();
                clientResponded = true;
                clientLock.notifyAll();
            }
        }
        else {
            System.out.println("!INVALID! [CardsSelectionEvent] received from Player: '" + playerNickname + "'"); // Server control message
            notify(new ErrorMessageEvent("Game has already started! You can no longer select a Card for this game."), playerNickname);
        }
    }

    /**
     * Computes a SetStartPlayerEvent.
     * This method is reserved for the Challenger Player.
     *
     * @param startPlayer Event: SetStartPlayerEvent received for the Player
     * @param playerNickname Player's nickname
     */
    @Override
    public synchronized void update(SetStartPlayerEvent startPlayer, String playerNickname) {
        if(!model.hasGameStarted()) {
            System.out.println("[SetStartPlayerEvent] received from Player: '" + playerNickname + "'"); // Server control message
            if (playerNickname.equals(model.challenger())) {
                synchronized (challengerLock) {
                    this.startPlayer = startPlayer.getStartPlayer();
                    challengerHasChosen = true;
                    challengerLock.notifyAll();
                }
            }
        }
        else {
            System.out.println("!INVALID! [SetStartPlayerEvent] received from Player: '" + playerNickname + "'"); // Server control message
            notify(new ErrorMessageEvent("Game has already started! You cannot choose the Start Player for this game."), playerNickname);
        }
    }

    /**
     * Computes a PlaceWorkerEvent.
     * Calls operations Worker placement.
     *
     * @param workerToPlace Event: Worker placement
     * @param playerNickname Player who place the Worker
     */
    @Override
    public synchronized void update(PlaceWorkerEvent workerToPlace, String playerNickname) {
        if(!model.hasGameStarted()) {
            System.out.println("[PlaceWorkerEvent] received from Player: '" + playerNickname + "'"); // Server control message
            synchronized (clientLock) {
                WorkerPlacedEvent workerPlaced = model.placeWorker(workerToPlace.getX(), workerToPlace.getY(), playerNickname);
                if (workerPlaced != null) {
                    workersPlaced++;
                    notify(workerPlaced); // broadcast notification of a worker placed event
                    System.out.println(" - Player '" + playerNickname + "' placed a Worker in position: ( " + workerToPlace.getX() + " , " + workerToPlace.getY() + " )"); // Server control message
                } else {
                    notify(new ErrorMessageEvent("Your choice is invalid! Try to place your worker again."), playerNickname);
                }

                if (workersPlaced == model.WORKERS_PER_PLAYER) {
                    clientResponded = true;
                    clientLock.notifyAll();
                }
            }
        }
        else {
            System.out.println("!INVALID! [PlaceWorkerEvent] received from Player: '" + playerNickname + "'"); // Server control message
            notify(new ErrorMessageEvent("Game has already started! Your Workers have already been placed."), playerNickname);
        }
    }


    /* Move listener */

    /**
     * Computes a SelectWorkerEvent.
     * Calls operations for Worker selection.
     *
     * @param selectedWorker Event: Worker selection
     * @param playerNickname Player who selected the Worker
     */
    @Override
    public synchronized void update(SelectWorkerEvent selectedWorker, String playerNickname) {
        if(model.hasGameStarted()) {
            System.out.println("[SelectWorkerEvent] received form Player: '" + playerNickname + "'"); // Server control message
            WorkerSelectedEvent workerSelected = null;
            if(!undoManager.isActive())
                workerSelected = model.selectWorker(selectedWorker.getWorkerId(), playerNickname);
            if (workerSelected != null)
                notify(workerSelected); // ANSWER FROM THE CONTROLLER (Notify the View)
            if(workerSelected != null && workerSelected.success())
                System.out.println("- Worker " + selectedWorker.getWorkerId() + " has been correctly selected.");
            else
                System.out.println("- Worker " + selectedWorker.getWorkerId() + " NOT selected.");
        }
        else {
            System.out.println("!INVALID! [SelectWorkerEvent] received from Player: '" + playerNickname + "'"); // Server control message
            notify(new ErrorMessageEvent("Game has not started yet! Hold on..."), playerNickname);
        }
    }

    /**
     * Computes a MoveWorkerEvent.
     * Calls operations for Worker movement.
     *
     * @param move Event: Worker movement
     * @param playerNickname Player who moved the Worker
     */
    @Override
    public synchronized void update(MoveWorkerEvent move, String playerNickname) {
        if(model.hasGameStarted()) {
            System.out.println("[MoveWorkerEvent] received form Player: '" + playerNickname + "'"); // Server control message
            List<WorkerMovedEvent> workersMoved = null;
            if(!undoManager.isActive() && model.getPlayingPlayerState() == StateType.MOVEMENT)
                workersMoved = model.moveWorker(move.getWorkerId(), move.getX(), move.getY(), playerNickname);
            if (workersMoved != null) {
                notifyWorkersMoved(workersMoved);
                postExecutionOperations(playerNickname, model.getMainWorkerMoved().getMoveOutcome());
            }
        }
        else {
            System.out.println("!INVALID! [MoveWorkerEvent] received from Player: '" + playerNickname + "'"); // Server control message
            notify(new ErrorMessageEvent("Game has not started yet! Hold on..."), playerNickname);
        }
    }

    /**
     * Computes a BuildBlockEvent.
     * Calls operations for building a block.
     *
     * @param build Event: Block building
     * @param playerNickname Player who build the block
     */
    @Override
    public synchronized void update(BuildBlockEvent build, String playerNickname) {
        if(model.hasGameStarted()) {
            System.out.println("[BuildBlockEvent] received form Player: '" + playerNickname + "'"); // Server control message
            BlockBuiltEvent blockBuilt = null;
            if(!undoManager.isActive() && model.getPlayingPlayerState() == StateType.CONSTRUCTION)
                blockBuilt = model.buildBlock(build.getWorkerId(), build.getX(), build.getY(), build.getBlockType(), playerNickname);
            if (blockBuilt != null) {
                notify(blockBuilt); // ANSWER FROM THE CONTROLLER (Notify the View)
                postExecutionOperations(playerNickname, blockBuilt.getMoveOutcome());
            }
        }
        else {
            System.out.println("!INVALID! [BuildBlockEvent] received from Player: '" + playerNickname + "'"); // Server control message
            notify(new ErrorMessageEvent("Game has not started yet! Hold on..."), playerNickname);
        }
    }

    /**
     * Computes a RemoveWorkerEvent.
     * Calls operations for removing a Worker.
     *
     * [This method is not actually working with
     * the current set of rules.]
     *
     * @param workerToRemove Event: Worker removal
     * @param playerNickname Player who removed the Worker
     */
    @Override
    public synchronized void update(RemoveWorkerEvent workerToRemove, String playerNickname) {
        if(model.hasGameStarted()) {
            System.out.println("[RemoveWorkerEvent] received form Player: '" + playerNickname + "'"); // Server control message
            WorkerRemovedEvent workerRemoved = null;
            if(!undoManager.isActive())
                workerRemoved = model.removeWorker(workerToRemove.getWorkerId(), workerToRemove.getX(), workerToRemove.getY(), playerNickname);
            if (workerRemoved != null)
                notify(workerRemoved); // ANSWER FROM THE CONTROLLER (Notify the View)
        }
        else {
            System.out.println("!INVALID! [RemoveWorkerEvent] received from Player: '" + playerNickname + "'"); // Server control message
            notify(new ErrorMessageEvent("Game has not started yet! Hold on..."), playerNickname);
        }
    }

    /**
     * Computes a RemoveBlockEvent.
     * Calls operations for removing a block.
     *
     * [This method is not actually working with
     * the current set of rules.]
     *
     * @param blockToRemove Event: Block removal
     * @param playerNickname Player who removed the block
     */
    @Override
    public synchronized void update(RemoveBlockEvent blockToRemove, String playerNickname) {
        if(model.hasGameStarted()) {
            System.out.println("[RemoveBlockEvent] received form Player: '" + playerNickname + "'"); // Server control message
            BlockRemovedEvent blockRemoved = null;
            if(!undoManager.isActive())
                blockRemoved = model.removeBlock(blockToRemove.getWorkerId(), blockToRemove.getX(), blockToRemove.getY(), playerNickname);
            if (blockRemoved != null)
                notify(blockRemoved); // ANSWER FROM THE CONTROLLER (Notify the View)
        }
        else {
            System.out.println("!INVALID! [RemoveBlockEvent] received from Player: '" + playerNickname + "'"); // Server control message
            notify(new ErrorMessageEvent("Game has not started yet! Hold on..."), playerNickname);
        }
    }

    /**
     * Computes an UndoActionEvent.
     * Calls operations for handling an undo
     * requested by a Player.
     *
     * @param undoAction Event: Undo action
     * @param playerNickname Player who requested to undo his/her last action
     */
    @Override
    public void update(UndoActionEvent undoAction, String playerNickname) {
        if (model.hasGameStarted()) {
            if (model.getPlayingPlayer().equals(playerNickname) && undoManager.isActive()) {
                System.out.println("[UndoActionEvent] received form Player: '" + playerNickname + "'"); // Server control message
                synchronized (clientLock) {
                    this.undoRequested = true;
                    clientResponded = true;
                    undoManager.undoReceived();
                    //clientLock.notifyAll();
                }
            } else {
                System.out.println("!INVALID! [UndoActionEvent] received from Player: '" + playerNickname + "'"); // Server control message
                notify(new ErrorMessageEvent("Cannot undo any action now..."), playerNickname);
            }
        } else {
            System.out.println("!INVALID! [UndoActionEvent] received from Player: '" + playerNickname + "'"); // Server control message
            notify(new ErrorMessageEvent("Game has not started yet! Hold on..."), playerNickname);
        }
    }


    /* Turn status change listener */

    /**
     * Computes a TurnStatusChangeEvent.
     * Calls operations for changing the Turn state of a Player.
     *
     * @param turnStatus Event: Turn change
     * @param playerNickname Player who wants to change his/her Turn state
     */
    @Override
    public synchronized void update(TurnStatusChangeEvent turnStatus, String playerNickname) {
        if(model.hasGameStarted()) {
            System.out.println("[TurnStatusChangeEvent] received form Player: '" + playerNickname + "'"); // Server control message
            TurnStatusChangedEvent turnStatusChanged = null;
            if(!undoManager.isActive())
                turnStatusChanged = model.changeTurnStatus(turnStatus.getTurnStatus(), playerNickname);
            if (turnStatusChanged != null) {
                notify(turnStatusChanged); // ANSWER FROM THE CONTROLLER (Notify the View)
                checkForSwitching(playerNickname);
                if(turnStatusChanged.success() && turnStatusChanged.getState() == StateType.NONE)
                    saveGame();
            }
            if(turnStatusChanged != null && turnStatusChanged.success())
                System.out.println("- " + playerNickname + "'s Turn has been correctly changed: " + turnStatusChanged.getState());
            else
                System.out.println("- " + playerNickname + "'s Turn NOT changed.");
        }
        else {
            System.out.println("!INVALID! [TurnStatusChangeEvent] received from Player: '" + playerNickname + "'"); // Server control message
            notify(new ErrorMessageEvent("Game has not started yet! Hold on..."), playerNickname);
        }
    }


    /* Generic update method */

    /**
     * Computes a generic Event received by View(s).
     *
     * [This method is not actually called anytime
     * because View is not supposed to ever generate it.
     * So Receiving this kind of generic Event means an
     * error occurred in the View.]
     *
     * @param o Event: Generic Object representing an Event
     */
    @Override
    public synchronized void update(Object o) {
        System.err.println("ERROR: Unexpected [Generic] VCEvent received!");
        //notify(new ErrorMessageEvent("Bad Request: Server cannot process your request!"));
    }









    /* ########################### AUXILIARY AND SUPPORT METHODS ############################ */


    /**
     * Notify View about all Workers' Movements happened in this turn.
     *
     * @param workersMoved List of Workers moved
     */
    private void notifyWorkersMoved(List<WorkerMovedEvent> workersMoved) {
        workersMoved.forEach(this::notify);
    }

    /**
     * Executes operations after a Movement or a Construction.
     *
     * Checks for a possible Undo requested by the Player who
     * made the last move, then (if necessary) proceeds with
     * eventual Turn state switching and game saving.
     *
     * @param playerNickname Player who made the last move
     * @param moveOutcome Move's outcome
     */
    private void postExecutionOperations(String playerNickname, MoveOutcomeType moveOutcome) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                if (moveSucceeded(moveOutcome)) {
                    System.out.println("- Move succeeded.");
                    checkUndo();
                    if (undoRequested)
                        undoHandler();
                    else {
                        checkForSwitching(playerNickname);
                        saveGame();
                    }
                }else{
                    System.out.println("- Move NOT executed.");
                }
            }
        });
    }


    /**
     * Tells if a move's outcome is positive or negative
     * (i.e. if it succeeded or not).
     *
     * @param moveOutcome Move's outcome to check
     * @return (Move succeeded ? true : false)
     */
    private boolean moveSucceeded(MoveOutcomeType moveOutcome) {
        return (moveOutcome == MoveOutcomeType.EXECUTED || moveOutcome == MoveOutcomeType.TURN_SWITCHED || moveOutcome == MoveOutcomeType.TURN_OVER || moveOutcome == MoveOutcomeType.LOSS || moveOutcome == MoveOutcomeType.WIN);
    }


    /**
     * Handles Undo when one is requested.
     */
    private void undoHandler() {
        /* Print log */
        System.out.println("--- Previously action is being undo...");
        /* Restore Game State in Model */
        model.restoreGameState();
        /* Register Turn Observers */
        registerTurnObservers();
        /* Notify Players about action undo */
        notify(new UndoOkEvent(model.getLastBoardState(), model.getPlayingPlayer(), model.getPlayingPlayerState()));
        /* Done */
        System.out.println("--- Action was undone.");
    }


    /**
     * Check for an Undo request by a Player
     * by waiting for it.
     *
     * @return True if an Undo is requested
     */
    private boolean checkUndo() {
        undoRequested = false;

        executor.submit(undoManager);
        waitForUndo();
        undoManager.setActive(false); // disable UndoManager

        return undoRequested;
    }


    /**
     * (Synchronized) Waits for an Undo-Action from a Player.
     */
    private void waitForUndo() {
        synchronized (clientLock) {
            clientResponded = false;
            while (!clientResponded) { // works like a wait
                try {
                    clientLock.wait();
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                clientResponded = true; // Need this because clientLock can be released even without receiving any UndoActionEvent
            }
        }
    }


    /**
     * Check for game savings.
     */
    public void checkForSavings() {
        GameState gameLoaded = loadGame();

        if(gameLoaded == null) {
            System.out.println("[SERVER] No game saving found.");
        }
        else {
            /* 0- Check if last game was ongoing */
            resumeGame = checkGameOngoing(gameLoaded);
            if(!resumeGame)
                return;

            /* 1- Check if all (last) playing players are online now */
            List<String> playingPlayers = getPlayingPlayers(gameLoaded);
            resumeGame = checkForOnlinePlayers(playingPlayers);
            if(!resumeGame)
                return;

            /* 2- Notify the Challenger about the possibility to restore a saved Game */
            askChallengerToRestoreGame();

            /* 3- If Challenger wants to resume the Game, resume it */
            if(resumeGame) {
                System.out.println("[SERVER] A saved game is being loaded.");
                resumeSavedGame(gameLoaded);
            }
        }
    }

    /**
     * Resumes a Game form the Game state passed
     * as argument.
     *
     * @param gameState Game state
     */
    private void resumeSavedGame(GameState gameState) {
        /* Restore Game State in Model */
        model.restoreGameState();
        /* Notify Players in game */
        notify(new MessageEvent("A previously saved game was resumed! The game can start."));
        /* Remove non-playing Players */
        removeNonPlayingPlayers(gameState);
        /* Register Turn Observers */
        registerTurnObservers();
        /* Notify Players about resumed game */
        System.out.println(" - Notifying Players that the game is starting..."); // Server control message
        notify(new GameResumingEvent(gameState));
        /* Start resumed game */
        startResumedGame();
    }

    /**
     * Starts a resumed game.
     */
    private void startResumedGame() {
        /* Preliminary action */
        //model.sortPlayers(players); // sort the players' list before stating
        Player playingPlayer = model.getPlayingPlayerReference();
        notify(new NextStatusEvent("The game has started!"));
        /* Notify about the Turn changed */
        notify(new TurnStatusChangedEvent(playingPlayer.getNickname(), playingPlayer.getTurnType(), true));
        //startGame(); //no need for this
        /* Game has started */
        System.out.println("\n##### Game started #####\n"); // Server control message
        System.out.println("### It's " + model.getPlayingPlayer() + "'s turn.");
    }

    /**
     * Remove non-playing Players from the Player List
     * into the Model when a previously saved game is resumed.
     *
     * @param gameState Game state
     */
    private void removeNonPlayingPlayers(GameState gameState) {
        List<String> playersInGame = new ArrayList<>(model.players());
        List<String> playersSaved = new ArrayList<>(gameState.getPlayers().getData().keySet());
        /* Remove all non-playing players from the game */
        for(String nickname : playersInGame)
            if(!playersSaved.contains(nickname))
                model.removePlayer(nickname);
    }

    /**
     * Notifies challenger about the possibility to
     * resume a previously saved game; then waits
     * for a response.
     */
    private void askChallengerToRestoreGame() {
        challengerHasChosen = false;
        while(!challengerHasChosen) {
            // Send to the Challenger a request
            notify(new GameResumingEvent(), model.challenger());
            // Waits until Challenger has mad a choice
            waitForChallengerResponse();
        }
    }

    /**
     * Gets a list of last playing Players' nicknames.
     *
     * @param gameState State of the Game
     * @return List of Players' nicknames
     */
    private List<String> getPlayingPlayers(GameState gameState) {
        Map<String, PlayerData> playersMap = gameState.getPlayers().getData();
        List<PlayerData> playersData = new ArrayList<>(3);

        playersMap.forEach((key, value) -> playersData.add(value));
        return playersData.stream().map(PlayerData::getNickname).collect(Collectors.toList());
    }

    /**
     * Tells if current online Players are the same
     * into the list of Players provided.
     *
     * @param playingPlayers List of Players (nicknames)
     * @return (Current online Players are the same into the list of Players provided ? true : false)
     */
    private boolean checkForOnlinePlayers(List<String> playingPlayers) {
        return this.players.containsAll(playingPlayers);
    }

    /**
     *
     * @param gameState State of the Game
     * @return (The game was ongoing ? true : false)
     */
    private boolean checkGameOngoing(GameState gameState) {
        return gameState.isGameStarted();
    }


    /**
     * Starts the game.
     */
    private void startGame() {
        model.sortPlayers(players); // sort the players' list before stating
        try {
            model.startGame(); // start the game
            model.setGameStarted(true);
            String playingPlayer = model.getPlayingPlayer();
            //notify(new TurnStatusChangedEvent(playingPlayer, StateType.MOVEMENT, true), playingPlayer);
            notify(new TurnStatusChangedEvent(playingPlayer, StateType.MOVEMENT, true));
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
        /* 0- Stop timer(s) */
        undoManager.stop();
        /* 1- Stop the game */
        model.stopGame();
        /* 2- Save the match */
        //saveGame(); // no need for this, since the game state is saved every move.
    }

    /**
     * Saves the game.
     */
    private void saveGame() {
        if(!gameWasQuit) {
            System.out.println("[SERVER] The game is being saved...");
            /* 1- Get game state from Model */
            GameState gameState = model.createGameState();
            /* 2- Save game state */
            ResourceManager.saveGameState(gameState);
        }
    }

    /**
     * Loads a saved game.
     *
     * @return A reference to the loaded game saving
     */
    private GameState loadGame() {
        /* 1- Load game state */
        GameState gameState = ResourceManager.loadGameState();
        /* 2- Restore game state to the Model */
        if(gameState != null)
            model.setGameState(gameState);
        else
            System.out.println("[SERVER] No game saving found.");

        return gameState;
    }

    public synchronized void setChallengerHasChosen(boolean challengerHasChosen) {
        this.challengerHasChosen = challengerHasChosen;
    }

    public synchronized void setClientResponded(Boolean clientResponded) {
        this.clientResponded = clientResponded;
    }

    public synchronized void setWorkersPlaced(int workersPlaced) {
        this.workersPlaced = workersPlaced;
    }

    /**
     * After the execution of a move, this method checks
     * if any switching-player condition is triggered.
     *
     * @param playerNickname Player who just made the move
     */
    private void checkForSwitching(String playerNickname) {
        /* After the Player notification, if the player's turn is over (or other triggering conditions), switch the playing Player */
        if (model.getMoveOutcome() == MoveOutcomeType.TURN_SWITCHED)
            notify(new TurnStatusChangedEvent(playerNickname, StateType.CONSTRUCTION, true));
        else if (model.getMoveOutcome() == MoveOutcomeType.TURN_OVER)
            model.switchPlayer();
        else if (model.getMoveOutcome() == MoveOutcomeType.LOSS) {
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

    public void gameOver() {
        printControlMessage("\n##### Game Over #####\n");
        List<String> players = model.players();
        players.forEach(this::gameOverMessage);
        //resetGame();
    }

    /**
     * Notify a given Player that the Game is Over.
     *
     * @param nickname (Player's nickname)
     */
    public void gameOverMessage(String nickname) {
        notify(new GameOverEvent("Game Over!"), nickname);
    }

    /**
     * Resets the game.
     * (To Server)
     */
    private void resetGame() {
        (new Thread(() -> {
            throw new ServerResetException(); // disconnect all the Players and reset the Server for a new game.
        })).start();
    }

    /**
     * Notifies a Player that he/she is being disconnected.
     *
     * @param nickname Player's nickname
     */
    public void disconnectPlayer(String nickname) {
        notify(new ServerQuitEvent("You are being disconnected from the game..."), nickname);
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








    /* ######################### CODE FOR GAME SIMULATION [INTEGRATION TESTING] ######################### */
//    /* ######################### GAME PREPARATION SIMULATION ########################## */
//
//    /* Method for integration testing [Game Preparation phase] */
//    private void prepareGame(int nPlayer) {
//        // prepare cards
//        cardsInGame = new ArrayList<>(nPlayer);
//        cardsInGame.add("Pan");
//        cardsInGame.add("Apollo");
//        if(nPlayer == 3)
//            cardsInGame.add("Minotaur");
//        model.setCardsInGame(cardsInGame);
//        // set cards
//        model.setPlayerCard("Pan", "giorgio");
//        model.setPlayerCard("Apollo", "andrea");
//        if(nPlayer == 3)
//            model.setPlayerCard("Minotaur", "marco");
//        registerTurnObservers();
//        // set start player
//        model.setStartPlayer("andrea");
//        // sort players
//        sortPlayers();
//        // place workers
//        model.placeWorker(0, 0, "andrea"); // worker's starting position
//        model.placeWorker(4, 4, "andrea"); // worker's starting position
//        model.placeWorker(0, 1, "giorgio"); // worker's starting position
//        model.placeWorker(3, 3, "giorgio"); // worker's starting position
//        if(nPlayer == 3) {
//            model.placeWorker(3, 2, "marco"); // worker's starting position
//            model.placeWorker(1, 3, "marco"); // worker's starting position
//        }
//    }
//
//    /* Method for integration testing [Playing phase]*/
//    private void simulateGame(int suite) {
//        // game simulation
//        String andrea = "andrea";
//        String giorgio = "giorgio";
//        String marco = "marco";
//        String worker0 = "[Worker]\t0";
//        String worker1 = "[Worker]\t1";
//        String worker2 = "[Worker]\t2";
//        String worker3 = "[Worker]\t3";
//        String worker4 = "[Worker]\t4";
//        String worker5 = "[Worker]\t5";
//
//        switch(suite) {
//            /* 2-Players Game: a Player wins */
//            case 1:
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 1, 1, andrea);
//                model.buildBlock(worker0, 0, 1, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 0, 3, giorgio);
//                model.buildBlock(worker2, 0, 4, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 1, 2, andrea);
//                model.buildBlock(worker0, 1, 1, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker3, giorgio);
//                model.moveWorker(worker3, 3, 4, giorgio);
//                model.buildBlock(worker3, 2, 4, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 0, 2, andrea);
//                model.buildBlock(worker0, 1, 1, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 1, 2, giorgio);
//                model.buildBlock(worker2, 1, 1, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 0, 1, andrea);
//                model.buildBlock(worker0, 1, 1, PlaceableType.DOME, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 0, 3, giorgio);
//                model.buildBlock(worker2, 0, 2, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 1, 2, andrea);
//                model.buildBlock(worker0, 0, 2, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 1, 3, giorgio);
//                model.buildBlock(worker2, 0, 2, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 0, 1, andrea);
//                model.buildBlock(worker0, 0, 0, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker3, giorgio);
//                model.moveWorker(worker3, 2, 3, giorgio);
//                model.buildBlock(worker3, 2, 2, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 0, 0, andrea);
//                model.buildBlock(worker0, 0, 1, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker3, giorgio);
//                model.moveWorker(worker3, 2, 2, giorgio);
//                model.buildBlock(worker3, 2, 3, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 0, 1, andrea);
//                model.buildBlock(worker0, 0, 0, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker3, giorgio);
//                model.moveWorker(worker3, 2, 3, giorgio);
//                model.buildBlock(worker3, 2, 4, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker1, andrea);
//                model.moveWorker(worker1, 4, 3, andrea);
//                model.buildBlock(worker1, 4, 4, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker3, giorgio);
//                model.moveWorker(worker3, 2, 4, giorgio);
//                model.buildBlock(worker3, 2, 3, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                // win
////                model.selectWorker(worker0, andrea);
////                model.moveWorker(worker0, 0, 2, andrea);
//                break;
//
//            /* 2-Players Game: a Player loses when starting its Turn (both of its Workers cannot make any Movement) */
//            case 2:
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 1, 0, andrea);
//                model.buildBlock(worker0, 0, 1, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 1, 2, giorgio);
//                model.buildBlock(worker2, 0, 1, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 0, 0, andrea);
//                model.buildBlock(worker0, 1, 1, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 2, 1, giorgio);
//                model.buildBlock(worker2, 1, 0, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 1, 1, andrea);
//                model.buildBlock(worker0, 1, 0, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker3, giorgio);
//                model.moveWorker(worker3, 4, 3, giorgio);
//                model.buildBlock(worker3, 3, 4, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                // at this point worker0 shall lose
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 0, 0, andrea);
//                model.buildBlock(worker0, 1, 1, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker3, giorgio);
//                model.moveWorker(worker3, 3, 4, giorgio);
//                model.buildBlock(worker3, 4, 3, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer(); // here check if worker0 has lost (when switching player)
//
//                model.selectWorker(worker0, andrea); // check that a selection of worker0 is now denied (worker has lost)
//                model.selectWorker(worker1, andrea);
//                model.moveWorker(worker1, 4, 3, andrea);
//                model.buildBlock(worker1, 3, 3, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker3, giorgio);
//                model.moveWorker(worker3, 2, 4, giorgio);
//                model.buildBlock(worker3, 3, 3, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker1, andrea);
//                model.moveWorker(worker1, 4, 4, andrea);
//                model.buildBlock(worker1, 4, 3, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                // at this point worker1 shall lose
//                model.selectWorker(worker3, giorgio);
//                model.moveWorker(worker3, 2, 3, giorgio);
//                model.buildBlock(worker3, 3, 4, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer(); // by this point, when trying to switch the player, 'andrea' should lose and 'giorgio' should be declared the winner.
//                break;
//
//            /* 2-Players Game: a Player loses when changing its Turn from Construction to Movement
//             * (cannot make any Movement within the already chosen Worker).
//             */
//            case 3:
//                model.selectWorker(worker0, andrea);
//                model.changeTurnStatus(StateType.CONSTRUCTION, andrea);
//                model.buildBlock(worker0, 1, 0, PlaceableType.BLOCK, andrea);
//                model.changeTurnStatus(StateType.MOVEMENT, andrea);
//                model.moveWorker(worker0, 0, 1, andrea);
//                model.buildBlock(worker0, 1, 1, PlaceableType.BLOCK, andrea);
//                model.switchPlayer(); // check if everything is ok by this point
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 1, 2, giorgio);
//                model.buildBlock(worker2, 0, 2, PlaceableType.DOME, giorgio); // check if the DOME is correctly built
//                model.switchPlayer();
//
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 0, 0, andrea);
//                model.buildBlock(worker0, 1, 0, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 1, 1, giorgio);
//                model.buildBlock(worker2, 0, 1, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                // at this point worker0 shall lose
//                model.selectWorker(worker0, andrea);
//                model.changeTurnStatus(StateType.CONSTRUCTION, andrea);
//                model.buildBlock(worker0, 0, 1, PlaceableType.BLOCK, andrea);
////                model.changeTurnStatus(StateType.MOVEMENT, andrea); // by this point, when trying to switch the Turn Type, 'andrea' should lose and 'giorgio' should be declared the winner.
////                model.moveWorker(worker0, 0, 1, andrea);
////                model.buildBlock(worker0, 1, 1, PlaceableType.BLOCK, andrea);
////                model.switchPlayer();
//                break;
//
//            /* 2-Players Game: a Player loses when changing its Turn from Movement to Construction
//             * (cannot make any Construction within the already chosen Worker).
//             */
//            case 4:
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 1, 0, andrea);
//                model.buildBlock(worker0, 0, 1, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 0, 2, giorgio);
//                model.buildBlock(worker2, 0, 1, PlaceableType.DOME, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 1, 1, andrea);
//                model.buildBlock(worker0, 1, 2, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 1, 2, giorgio);
//                model.buildBlock(worker2, 1, 3, PlaceableType.DOME, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker1, andrea);
//                model.moveWorker(worker1, 3, 3, andrea); // here: check if Apollo's power works correctly.
//                model.buildBlock(worker1, 3, 4, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 0, 3, giorgio);
//                model.buildBlock(worker2, 1, 2, PlaceableType.DOME, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker1, andrea);
//                model.moveWorker(worker1, 4, 3, andrea);
//                model.buildBlock(worker1, 4, 2, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 0, 2, giorgio);
//                model.buildBlock(worker2, 0, 3, PlaceableType.DOME, giorgio);
//                model.switchPlayer();
//
//                // at this point worker0 shall lose
//                model.selectWorker(worker0, andrea);
////                model.moveWorker(worker0, 0, 2, andrea); // after done the Movement, worker0 cannot Build, so Player 'andrea' lose the game.
////                model.buildBlock(worker0, 1, 2, PlaceableType.BLOCK, andrea);
////                model.switchPlayer();
//                break;
//
//            /* 3-Players Game:
//             *     - Complete match (Turn flow)
//             *     - A Player loses (Workers removal)
//             *     - A Player wins
//             */
//            case 5:
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 1, 0, andrea);
//                model.buildBlock(worker0, 0, 1, PlaceableType.BLOCK, andrea);
//                model.switchPlayer(); // check which player is being chosen
//
//                model.selectWorker(worker4, marco);
//                model.moveWorker(worker4, 2, 1, marco);
//                model.buildBlock(worker4, 2, 0, PlaceableType.BLOCK, marco);
//                model.switchPlayer(); // check if Player n. 3 is correctly chosen by this point
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 0, 2, giorgio);
//                model.buildBlock(worker2, 0, 1, PlaceableType.DOME, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 1, 1, andrea);
//                model.buildBlock(worker0, 1, 2, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker4, marco);
//                model.moveWorker(worker4, 2, 2, marco);
//                model.buildBlock(worker4, 2, 1, PlaceableType.BLOCK, marco);
//                model.switchPlayer();
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 1, 2, giorgio);
//                model.buildBlock(worker2, 1, 3, PlaceableType.DOME, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker1, andrea);
//                model.moveWorker(worker1, 3, 3, andrea); // here: check if Apollo's power works correctly.
//                model.buildBlock(worker1, 3, 4, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker4, marco);
//                model.moveWorker(worker4, 2, 3, marco);
//                model.buildBlock(worker4, 2, 2, PlaceableType.BLOCK, marco);
//                model.switchPlayer();
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 0, 3, giorgio);
//                model.buildBlock(worker2, 1, 2, PlaceableType.DOME, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker1, andrea);
//                model.moveWorker(worker1, 4, 3, andrea);
//                model.buildBlock(worker1, 4, 2, PlaceableType.BLOCK, andrea);
//                model.switchPlayer();
//
//                model.selectWorker(worker4, marco);
//                model.moveWorker(worker4, 3, 2, marco);
//                model.buildBlock(worker4, 2, 2, PlaceableType.BLOCK, marco);
//                model.switchPlayer();
//
//                model.selectWorker(worker2, giorgio);
//                model.moveWorker(worker2, 0, 2, giorgio);
//                model.buildBlock(worker2, 0, 3, PlaceableType.DOME, giorgio);
//                model.switchPlayer();
//
//                // at this point worker0 shall lose
//                model.selectWorker(worker0, andrea);
//                model.moveWorker(worker0, 0, 2, andrea); // after done the Movement, worker0 cannot Build, so Player 'andrea' lose the game.
//                checkForSwitching(andrea);
//
//                // by this point, check if the selection of the next playing worker is correct.
//                model.selectWorker(worker4, marco); // check if cells where andreas's workers where placed are now free. ( 0 , 2 ) and ( 4 , 3 )
//                model.moveWorker(worker4, 2, 1, marco);
//                model.buildBlock(worker4, 2, 2, PlaceableType.BLOCK, marco);
//                model.switchPlayer(); // check if next player is chosen correctly
//
//                model.selectWorker(worker3, giorgio);
//                model.moveWorker(worker3, 4, 3, giorgio);
//                model.buildBlock(worker3, 4, 4, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer(); // again: check if next worker is selected correctly.
//
//                model.selectWorker(worker4, marco);
//                model.moveWorker(worker4, 2, 0, marco);
//                model.buildBlock(worker4, 2, 1, PlaceableType.BLOCK, marco);
//                model.switchPlayer();
//
//                model.selectWorker(worker3, giorgio);
//                model.moveWorker(worker3, 4, 2, giorgio);
//                model.buildBlock(worker3, 4, 3, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                model.selectWorker(worker4, marco);
//                model.moveWorker(worker4, 2, 1, marco);
//                model.buildBlock(worker4, 3, 1, PlaceableType.BLOCK, marco);
//                model.switchPlayer();
//
//                model.selectWorker(worker3, giorgio);
//                model.moveWorker(worker3, 4, 1, giorgio);
//                model.buildBlock(worker3, 3, 1, PlaceableType.BLOCK, giorgio);
//                model.switchPlayer();
//
//                // win
//                model.selectWorker(worker4, marco);
////                model.moveWorker(worker4, 2, 2, marco); // at this point Player 'marco' shall win
////                model.buildBlock(worker4, 3, 1, PlaceableType.BLOCK, marco);
////                model.switchPlayer();
//                break;
//
//            default:
//                break;
//        }
//    }
}
