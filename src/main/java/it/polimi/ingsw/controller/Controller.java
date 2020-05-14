package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.model.CardInfo;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.model.StateType;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.VCEventListener;
import it.polimi.ingsw.storage.ResourceManager;
import it.polimi.ingsw.view.events.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Game Controller.
 *
 * @author AndreaAltomare
 */
public class Controller extends Observable<Object> implements VCEventListener, Runnable {
    // TODO: [IMPORTANTE!!] Usare Model.isGameStarted per controllare che gli eventi ricevuti (come MoveWorkerEvent) siano "validi" solo a gioco iniziato!
    private Model model; // Object reference to the Game Model
    // todo capire quale di qeuste variabili devono essere nel model
    private List<String> players;
    private String challenger; // Challenger Player
    private List<String> cardsInGame; // Cards used for this game match
    private volatile String genericResponse; // String used for generic response from client
    private volatile Boolean clientResponded; // used to control generic responses from Players
    private volatile Boolean challengerHasChosen; // Marked as volatile because it is accessed by different threads // TODO: ensure that "volatile" attribute mark does not clash with "synchronized" methods that update this very attribute
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
        this.DEFAULT_WAITING_TIME = 1000; // time is in milliseconds
        this.workersPlaced = 0;
    }

    /**
     * Run() method for Controller class, to let the game flow
     * be controlled in a separated thread from the one into which
     * run the connection Socket.
     *
     * This method is run when a new game needs to start.
     */
    @Override
    public void run() { // TODO: maybe refactor this into a more readable method (every step should be encapsulated in a same-abstraction-level method)
        /* 0- Send to the Client general info for this game */
        notify(new ServerSendDataEvent()); // todo populate

        /* 1- Select the -Challenger- Player */
        challenger = players.get(0); // Choose the Challenger (this can be done in any possible way)
        Model.setChallenger(challenger); // set Challenger into the Game Model
        notify(new MessageEvent("The Challenger Player has been chosen. It's: " + challenger)); // notify players that the Challenger has been chosen

        /* 2- Challenger choose the Cards for this game match */
        notify(new NextStatusEvent("Game preparation"));
        // 2.1- Prepare Card's information
        List<CardInfo> cardInfoList = ResourceManager.getCardsInformation();
        List<String> possibleCards = cardInfoList.stream().map(c -> c.getName()).collect(Collectors.toList()); // get just Cards' name // todo: check if this works correctly
        // 2.2- Notify other Players that Cards are being chosen by the Challenger // todo remove this block
//        players.forEach(p -> {
//            if(!p.equals(challenger)) {
//                notify(new MessageEvent(challenger + " is choosing the God Cards for this game! Wait..."));
//            }
//        });
        while(!challengerHasChosen) {
            // 2.2- Broadcast Card's information
            notify(new CardsInformationEvent(cardInfoList)); // todo maybe CardInformationEvent can act as a kind of "NextStatusEvent" (needs to be broadcasted of course)
            // 2.3- Waits until Challenger hasn't chosen the Cards for this game
            synchronized (challengerHasChosen) {
                while (!challengerHasChosen) { // works like a wait // todo (IT SHOULD WORK, check it... [or if it has thread-related problems])
                    try {
                        challengerHasChosen.wait(); // TODO: don't know if this works correctly
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            // 2.4- The number of chosen Cards needs to be equal to the number of the Players in the game (and Cards chosen must be valid)
            if (cardsInGame.size() != players.size()) {
                notify(new ErrorMessageEvent("You must choose a number of cards equal to the number of players in this game!"), challenger);
                setChallengerHasChosen(false); // SYNCHRONOUSLY set the attribute to false
            }
            else if (!(possibleCards.containsAll(cardsInGame))) {
                notify(new ErrorMessageEvent("Your choice is invalid! Please, try again."), challenger);
                setChallengerHasChosen(false); // SYNCHRONOUSLY set the attribute to false
            }
            else {
                Model.setCardsInGame(cardsInGame);
                notify(new MessageEvent("Your choice has been registered!"), challenger);
            }
        }

        /* 3- In "clockwise" order, every Player choose a Card (among the Cards chosen by the Challenger) */
        // 3.1- Prepare Cards information
        List<CardInfo> cardsToChoose = cardInfoList.stream().filter(c -> cardsInGame.contains(c.getName())).collect(Collectors.toList());
        // 3.2- Notify every Player in "clockwise" order
        for(int i = 1; i < players.size(); i++) { // for cycle starts by 1 (one) because Player n. 0 is the Challenger
            clientResponded = false;
            cardsToChoose = requestPlayerForCardChoice(cardsToChoose, players.get(i));
        }
        // 3.3- Set the last Card to the Challenger
        List<String> lastCardList = cardsToChoose.stream().map(c -> c.getName()).collect(Collectors.toList());
        String lastCard = lastCardList.get(0);
        Model.setPlayerCard(lastCard, challenger);
        notify(new CardSelectedEvent(lastCard, challenger)); // ANSWER FROM THE CONTROLLER (Notify the View)
        cardsToChoose.removeIf(c -> c.getName().equals(lastCard));

        /* 4- Ask the Challenger for the Start Player */
        challengerHasChosen = false;
        while(!challengerHasChosen) {
            // 4.1- Send to the Challenger a request
            notify(new RequireStartPlayerEvent(players)); // todo this event is broadcasted
            // 4.2- Waits until Challenger hasn't chosen the Start Player for this game
            synchronized (challengerHasChosen) {
                while (!challengerHasChosen) { // works like a wait // todo (IT SHOULD WORK, check it... [or if it has thread-related problems])
                    try {
                        challengerHasChosen.wait(); // TODO: don't know if this works correctly
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            // 4.3- Check if the choice is valid
            synchronized (startPlayer) {
                if (!players.contains(startPlayer)) {
                    notify(new ErrorMessageEvent("Your choice is invalid! Please, try again."), challenger);
                    setChallengerHasChosen(false); // SYNCHRONOUSLY set the attribute to false
                }
                else {
                    Model.setStartPlayer(startPlayer);
                    notify(new MessageEvent(startPlayer + " will be the Start Player for this game!"));
                }
            }
        }
        notify(new MessageEvent("Other players are placing their workers. Wait...")); // notify other Players // todo maybe it's useless: to remove

        /* 5- Sort the list of Players */
        players.remove(startPlayer);
        players.add(0, startPlayer);

        /* 6- In "clockwise" order, starting from Start Player, every Player places his/her Workers on the Board */
        for(int i = 0; i < players.size(); i++) {
            setClientResponded(false);
            setWorkersPlaced(0);
            while(!clientResponded) {
                // 6.1- Ask the Player to place his/her Worker
                notify(new RequirePlaceWorkersEvent());
                // 6.2- Wait for the response
                synchronized (clientResponded) {
                    while (!clientResponded) {
                        try {
                            clientResponded.wait(); // TODO: don't know if this works correctly
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                // 6.3- Validation of Worker palcement is done in the update method.
            }
        }

        /* 7- Game preparation phase is over. The game match can start */
        notify(new NextStatusEvent("The game has started!"));
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
            // todo: maybe modify NextStatusEvent in a simple MessageEvent
            notify(new NextStatusEvent("Choose your Card!"), playerNickname); // notify the Player he/she needs to choose a Card // todo maybe it's to remove
            notify(new CardsInformationEvent(cardsToChoose)); // send Cards information to the Player
            // 3.2.2- Wait for the response
            synchronized (clientResponded) {
                while (!clientResponded) {
                    try {
                        clientResponded.wait(); // TODO: don't know if this works correctly
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            // 3.2.3- Check for the correctness of the choice made
            synchronized (genericResponse) {
                if (!(validCards.contains(genericResponse))) {
                    notify(new ErrorMessageEvent("Your choice is invalid! Please, try again."), playerNickname);
                    setClientResponded(false); // SYNCHRONOUSLY set the attribute to false
                }
                else {
                    Model.setPlayerCard(genericResponse, playerNickname);
                    notify(new CardSelectedEvent(genericResponse, playerNickname)); // ANSWER FROM THE CONTROLLER (Notify the View)
                    cardsToChoose.removeIf(c -> c.getName().equals(genericResponse));
                }
            }
        }

        return cardsToChoose;
    }
















    // TODO: In tutti i metodi update, controllare tramite apposito Booleano in Model che la partita sia cominciata, altrimenti si rischia di ricevere pacchetti di eventi non pertinenti con lo scenario di avanzamento del flusso di gioco in cui ci si trova



    // TODO: in all these methods, write actual code to interact with the Model
    /* Client general listener */
    @Override
    public synchronized void update(SetNicknameEvent submittedNickname) {
        System.out.println("SetNicknameEvent received: " + submittedNickname);
        notify(new MessageEvent("Server respond: Nickname '" + submittedNickname + "' received."));
    }

    @Override
    public synchronized void update(SetPlayersNumberEvent playersNumber) {
        System.out.println("SetPlayersNumberEvent received. The number of Players set by the Client is: " + playersNumber.getNumberOfPlayers());
        notify(new MessageEvent("Server respond: number of Players: '" + playersNumber.getNumberOfPlayers() + "' received."));
    }

    @Override
    public synchronized void update(QuitEvent quit, String playerNickname) {
        // TODO: add operations to handle disconnections of Players
        System.out.println("QuitEvent received from Player " + playerNickname + ": quitting...");
    }

    @Override
    public synchronized void update(ViewRequestDataEvent dataRequest, String playerNickname) {
        System.out.println("ViewRequestDataEvent received form Player: " + playerNickname);

        /* ANSWER FROM THE CONTROLLER (Notify the View) */
        // TODO: maybe this method/event is useless from a Distributed-MVC Pattern point of view. Maybe it's t remove
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
        if(playerNickname.equals(challenger)) {
            synchronized (challengerHasChosen) {
                cardsInGame = chosenCards.getCards();
                challengerHasChosen = true; // todo remember to set this to false when a new game starts (if necessary)
                challengerHasChosen.notifyAll();
            }
        }
    }

    @Override
    public synchronized void update(CardSelectionEvent card, String playerNickname) {
        synchronized (clientResponded) {
            genericResponse = card.getCardName();
            clientResponded = true;
            clientResponded.notifyAll();
        }
    }

    /**
     * This method is reserved for the Challenger Player.
     *
     * @param startPlayer (SetStartPlayerEvent received for the Player)
     * @param playerNickname (Player's nickname)
     */
    @Override
    public void update(SetStartPlayerEvent startPlayer, String playerNickname) {
        if(playerNickname.equals(challenger)) {
            synchronized (challengerHasChosen) {
                this.startPlayer = startPlayer.getStartPlayer();
                challengerHasChosen = true; // todo remember to set this to false when a new game starts (if necessary)
                challengerHasChosen.notifyAll();
            }
        }
    }

    @Override
    public synchronized void update(PlaceWorkerEvent workerToPlace, String playerNickname) {
        synchronized (clientResponded) {
            if(model.placeWorker(workerToPlace.getX(), workerToPlace.getY(), playerNickname)) {
                workerPlaced++;
                notify(new WorkerPlacedEvent()); // broadcast notification of a worker placed event
            }
            else {
                notify(new ErrorMessageEvent("Your choice is invalid! Try to place your worker again."), playerNickname);
            }

            if(workerPlaced == model.WORKERS_PER_PLAYER) {
                clientResponded = true;
                clientResponded.notifyAll();
            }
        }
    }


    /* Move listener */
    @Override
    public synchronized void update(SelectWorkerEvent selectedWorker, String playerNickname) {
        System.out.println("SelectWorkerEvent received form Player: " + playerNickname);

        /* ANSWER FROM THE CONTROLLER (Notify the View) */
        notify(new WorkerSelectedEvent(playerNickname, selectedWorker.getWorkerId()));
    }

    @Override
    public synchronized void update(MoveWorkerEvent move, String playerNickname) {
        System.out.println("MoveWorkerEvent received form Player: " + playerNickname);

        /* ANSWER FROM THE CONTROLLER (Notify the View) */
        notify(new WorkerMovedEvent(move.getWorkerId(),1,1,move.getX(),move.getY()));
    }

    @Override
    public synchronized void update(BuildBlockEvent build, String playerNickname) {
        System.out.println("BuildBlockEvent received form Player: " + playerNickname);

        /* ANSWER FROM THE CONTROLLER (Notify the View) */
        notify(new BlockBuiltEvent(build.getX(),build.getY(), build.getBlockType()));
    }

    @Override
    public synchronized void update(RemoveWorkerEvent workerToRemove, String playerNickname) {
        System.out.println("RemoveWorkerEvent received form Player: " + playerNickname);

        /* ANSWER FROM THE CONTROLLER (Notify the View) */
        notify(new WorkerRemovedEvent(workerToRemove.getWorkerId(),workerToRemove.getX(),workerToRemove.getY())); // todo: retrieve X and Y position by using Worker's unique ID
    }

    @Override
    public synchronized void update(RemoveBlockEvent blockToRemove, String playerNickname) {
        System.out.println("RemoveBlockEvent received form Player: " + playerNickname);

        /* ANSWER FROM THE CONTROLLER (Notify the View) */
        notify(new BlockRemovedEvent(blockToRemove.getX(),blockToRemove.getY(), PlaceableType.DOME));
    }


    /* Turn status change listener */
    @Override
    public synchronized void update(TurnStatusChangeEvent turnStatus, String playerNickname) {
        System.out.println("TurnStatusChangeEvent received form Player: " + playerNickname);

        /* ANSWER FROM THE CONTROLLER (Notify the View) */
        notify(new TurnStatusChangedEvent(playerNickname, turnStatus.getTurnStatus()), playerNickname);
    }

    /* Generic update method */
    @Override
    public synchronized void update(Object o) {
        // todo: maybe these statements are to remove
        System.out.println("Generic VCEvent received");

        /* ANSWER FROM THE CONTROLLER (Notify the View) */
        notify(new MessageEvent("Received a generic Object from your Client"));
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
        return startPlayer;
    }

    public synchronized void setStartPlayer(String startPlayer) {
        this.startPlayer = startPlayer;
    }

    public synchronized boolean gameIsStarted() {
        return model.isGameStarted();
    }

    public int getWorkersPlaced() {
        return workersPlaced;
    }

    public synchronized void setWorkersPlaced(int workersPlaced) {
        this.workersPlaced = workersPlaced;
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
