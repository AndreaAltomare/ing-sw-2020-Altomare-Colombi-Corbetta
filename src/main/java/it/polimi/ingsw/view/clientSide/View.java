package it.polimi.ingsw.view.clientSide;

import it.polimi.ingsw.connection.client.ClientConnection;
import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.model.CardInfo;
import it.polimi.ingsw.model.MoveOutcomeType;
import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.model.StateType;
import it.polimi.ingsw.observer.MVEventListener;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.events.*;
import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.observer.MVEventListener;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.BuildBlockExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.GUIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.TerminalViewer;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;
import it.polimi.ingsw.view.serverSide.ClientStatus;
import it.polimi.ingsw.view.serverSide.ClientStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class View extends Observable<Object> implements MVEventListener, Runnable { // todo: maybe this class extends Observable<Object> for proper interaction with Network Handler
    Thread tConnection;
    private ClientConnection connection;
    private Scanner in; // Scanner unique reference
    private String input; // unique input
    private String nickname; // Player's nickname
    private boolean nicknameSet;
    private boolean connectionActive;

    /* ########## TESTING ########## */
    private int boardXsize, boardYsize;
    private List<String> players;
    private Map<String, List<String>> workersToPlayer;

    public View(Scanner in, ClientConnection connection) {
        this.in = in;
        this.connection = connection;
    }

    // TODO: con questo sistema viene verificato anche che la comunicazione con Pattern Observer funzioni correttamente su thread diversi
    // TODO: ricordarsi che il metodo run() gira su un thread differente rispetto quello dove "girano" i metodi update() --> i metodi update() vengono chiamati sullo stesso thread di client.ClientConnection
    @Override
    public void run() {
        // TODO: commentato perché ho bisogno di fare ancora qualche test...
        /*new TerminalViewer();
        new CLIViewer();
        new GUIViewer();
        System.out.println("Hello World");

        ViewStatus.init();
        Viewer.setAllStatusViewer(ViewStatus.getActual().getViewer());

        ViewStatus.nextStatus();
        Viewer.setAllStatusViewer(ViewStatus.getActual().getViewer());*/

        nicknameSet = false;

        // todo istanziare connessione ecc...
        tConnection = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connection.run();
                }
                catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        tConnection.start();


        //System.out.println("Welcome! Type something.");
        input = in.nextLine();
        while(!input.equals("quit")) {
            switch (input) {
                case "alto":
                case "andrea":
                case "giorgio":
                case "marco":
                    if(!nicknameSet) {
                        nickname = input;
                        notify(new SetNicknameEvent(nickname)); // submit nickname
                        nicknameSet = true;
                    }
                    else {
                        notify(new SetStartPlayerEvent(input)); // submit nickname
                    }
                    break;


                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                    notify(new SetPlayersNumberEvent(Integer.parseInt(input))); // by this way we ensure auto-boxing from int to Object type works correctly
                    break;


                case "y":
                    notify(new GameResumingResponseEvent(true));
                    break;
                case "n":
                    notify(new GameResumingResponseEvent(false));
                    break;


                case "start_player":
                    input = in.nextLine();
                    notify(new SetStartPlayerEvent(input)); // submit nickname
                    break;


                case "build_block":
                    System.out.println("Type the Worker's ID (just the number) by which make a build: ");
                    int id = Integer.parseInt(in.nextLine());
                    String workerId = "[Worker]\t" + id;
                    System.out.println("X position: ");
                    int x = Integer.parseInt(in.nextLine());
                    System.out.println("Y position: ");
                    int y = Integer.parseInt(in.nextLine());
                    System.out.println("Select the type of block you want to build (type its number):\n1 - BLOCK\n2 - DOME");
                    int block = Integer.parseInt(in.nextLine());
                    PlaceableType blockType = PlaceableType.DOME;
                    if(block == 1)
                        blockType = PlaceableType.BLOCK;
                    notify(new BuildBlockEvent(workerId, x, y, blockType));
                    break;


                case "challenger_card":
                    String card;
                    int n = players.size();
                    List<String> cards = new ArrayList<>(n);
                    System.out.println("Choose " + n + " Cards for this game.");
                    for(int i = 0; i < n; i++) {
                        System.out.println((n - i) + " remaining Cards to choose. Type a name of a Card:");
                        card = in.nextLine();
                        cards.add(card);
                    }
                    notify(new CardsChoosingEvent(cards));
                    break;


                case "select_card":
                    card = in.nextLine();
                    notify(new CardSelectionEvent(card));
                    break;


                case "move_worker":
                    System.out.println("Type the Worker's ID (just the number) by which make a movement: ");
                    id = Integer.parseInt(in.nextLine());
                    workerId = "[Worker]\t" + id;
                    System.out.println("X position: ");
                    x = Integer.parseInt(in.nextLine());
                    System.out.println("Y position: ");
                    y = Integer.parseInt(in.nextLine());
                    notify(new MoveWorkerEvent(workerId, x, y));
                    break;


                case "place_worker":
                    System.out.println("Place a Worker onto the game board.");
                    System.out.println("X position: ");
                    x = Integer.parseInt(in.nextLine());
                    System.out.println("Y position: ");
                    y = Integer.parseInt(in.nextLine());
                    notify(new PlaceWorkerEvent(x, y));
                    break;


                case "remove_block":
                    System.out.println("Type the Worker's ID (just the number) by which remove a block from the game board: ");
                    id = Integer.parseInt(in.nextLine());
                    workerId = "[Worker]\t" + id;
                    System.out.println("X position of the block to remove: ");
                    x = Integer.parseInt(in.nextLine());
                    System.out.println("Y position of the block to remove: ");
                    y = Integer.parseInt(in.nextLine());
                    notify(new RemoveBlockEvent(workerId, x, y));
                    break;


                case "remove_worker":
                    System.out.println("Type the Worker's ID (just the number) of the Worker to remove from the game board: ");
                    id = Integer.parseInt(in.nextLine());
                    workerId = "[Worker]\t" + id;
                    System.out.println("X position of the Worker to remove: ");
                    x = Integer.parseInt(in.nextLine());
                    System.out.println("Y position of the Worker to remove: ");
                    y = Integer.parseInt(in.nextLine());
                    notify(new RemoveWorkerEvent(workerId, x, y));
                    break;


                case "select_worker":
                    System.out.println("Type the Worker's ID (just the number) of the Worker you want to select: ");
                    id = Integer.parseInt(in.nextLine());
                    workerId = "[Worker]\t" + id;
                    notify(new SelectWorkerEvent(workerId));
                    break;


                case "turn_change":
                    System.out.println("Select the turn you want to switch at:\n1 - MOVEMENT\n2 - CONSTRUCTION\n3 - PASS TURN");
                    int turn = Integer.parseInt(in.nextLine());
                    StateType turnType = StateType.NONE;
                    if(turn == 1)
                        turnType = StateType.MOVEMENT;
                    else if(turn == 2)
                        turnType = StateType.CONSTRUCTION;
                    notify(new TurnStatusChangeEvent(turnType));
                    break;


                case "undo":
                    notify(new UndoActionEvent());
                    break;


                case "data_request":
                    notify(new ViewRequestDataEvent());
                    break;


                /* ########## CONTROL COMMANDS ########## */
                case "game_info":
                    printGameInfo();
                    break;


                default:
                    System.out.println("Invalid request. Try again.");
                    break;
            }

            input = in.nextLine();
        }

        notify(new QuitEvent());
        // TODO: close connection, and gracefully close the whole application
    }



    /**
     * Method called to make the status go ahead of one step.
     *
     * @param nextStatus (NextStatusEvent send from the server)
     */
    /* Server general listener */
    @Override
    public void update(NextStatusEvent nextStatus) {
//        ViewMessage.populateAndSend(nextStatus.toString(), ViewMessage.MessageType.CHANGE_STATUS_MESSAGE);
//        ViewStatus.nextStatus();
        System.out.println("[NextStatusEvent] " + nextStatus.getMessage()); // todo [debug]
    }

    /**
     * Method that notify the view that the selected nickname is not valid.
     *
     * @param invalidNickname (InvalidNickNameEvent from the server)
     */
    @Override
    public void update(InvalidNicknameEvent invalidNickname) {
        //It is in the LOGIN status...so we only need to display the error message
//        ViewNickname.clear();
//        ViewMessage.populateAndSend(invalidNickname.getMessage(), ViewMessage.MessageType.FROM_SERVER_ERROR);
        System.out.println("[InvalidNicknameEvent] Invalid nickname! Try again."); // todo [debug]
    }

    /**
     * Method that notify the view that is needed to select the number of players.
     *
     * @param requirePlayersNumber (RequirePlayersNumberEvent from the server)
     */
    @Override
    public void update(RequirePlayersNumberEvent requirePlayersNumber) {
//        ViewMessage.populateAndSend(requirePlayersNumber.getMessage(), ViewMessage.MessageType.CHANGE_STATUS_MESSAGE);
//        ViewStatus.setStatus("NUMBER_PLAYER");
        System.out.println("[RequirePlayersNumberEvent] You are the first player. Choose the number of players for this game (2 or 3):"); // todo [debug]
    }

    /**
     * Method that the lobby is full and closes the application.
     *
     * @param lobbyFull (LobbyFullEvent from the server)
     */
    @Override
    public void update(LobbyFullEvent lobbyFull) {
//        ViewStatus.setStatus("CLOSING");
//        ViewMessage.populateAndSend(lobbyFull.getMessage(), ViewMessage.MessageType.CHANGE_STATUS_MESSAGE);
//        ViewMessage.populateAndSend(lobbyFull.getMessage(), ViewMessage.MessageType.FROM_SERVER_ERROR);
        System.out.println("[LobbyFullEvent] Lobby is full!"); // todo [debug]
        // TODO: close connection, and gracefully close the whole application
    }

    /**
     * Method that signals that a player wins.
     *
     * @param playerWin (PlayerWinEvent from the server)
     */
    @Override
    public void update(PlayerWinEvent playerWin) {
//        if (ViewNickname.getMyNickname().equals(playerWin.getPlayerNickname())) {
//            ViewMessage.populateAndSend(playerWin.getWinnerMessage(), ViewMessage.MessageType.WIN_MESSAGE);
//        } else {
//            ViewMessage.populateAndSend(playerWin.getLosersMessage(), ViewMessage.MessageType.LOOSE_MESSAGE);
//        }
        System.out.println("[PlayerWinEvent] Player " + playerWin.getPlayerNickname() + " has won!"); // todo [debug]
    }

    /**
     * Method that signals that the player has lost.
     *
     * @param playerLose (PlayerLoseEvent from the server)
     */
    @Override
    public void update(PlayerLoseEvent playerLose) {
//        if (ViewNickname.getMyNickname().equals(playerLose.getPlayerNickname())) {
//            ViewMessage.populateAndSend(playerLose.getMessage(), ViewMessage.MessageType.LOOSE_MESSAGE);
//        }
        System.out.println("[PlayerLoseEvent] Player " + playerLose.getPlayerNickname() + " has lost!"); // todo [debug]
    }

    /**
     * Method that changes the turn status.
     *
     * @param turnStatusChange (TurnStatusChangedEvent from the server)
     */
    @Override
    public void update(TurnStatusChangedEvent turnStatusChange) {
//        ViewSubTurn.set(turnStatusChange.getState().toString());
//        ViewSubTurn.getActual().setPlayer(turnStatusChange.getPlayerNickname());
//        Viewer.setAllSubTurnViewer(ViewSubTurn.getActual().getSubViewer());
        //System.out.println("Turn status changed to: " + turnStatusChange.getState().toString()); // todo: check what toString() of an enum prints... [si, funziona.]
        if(turnStatusChange.getPlayerNickname().equals(nickname))
            if(turnStatusChange.success()) {
                if(turnStatusChange.getState().equals(StateType.NONE))
                    System.out.println("[TurnStatusChangedEvent] You have passed your turn!"); // todo [debug]
                else
                    System.out.println("[TurnStatusChangedEvent] Your turn is now: " + turnStatusChange.getState().toString()); // todo [debug]
            }
            else
                System.out.println("[TurnStatusChangedEvent] Cannot change to state: " + turnStatusChange.getState().toString()); // todo [debug]
        else
            System.out.println("[TurnStatusChangedEvent] Player " + turnStatusChange.getPlayerNickname() + "'s turn is now: " + turnStatusChange.getState().toString());
    }

    @Override
    public void update(GameOverEvent gameOver) {
        System.out.println("[GameOverEvent] Game over!"); // todo [debug]
    }

    @Override
    public void update(GameResumingEvent gameResuming) {
        System.out.println("[GameResumingEvent] A saved game was found. Do you want to resume it?\ny / n");
    }


    /**
     * Method that notify that the view has to quit.
     *
     * @param serverQuit (ServerQuitEvent from the server)
     */
    @Override
    public void update(ServerQuitEvent serverQuit) {
//        ViewStatus.setStatus("CLOSING");
//        ViewMessage.populateAndSend(serverQuit.getMessage(), ViewMessage.MessageType.CHANGE_STATUS_MESSAGE);
//        ViewMessage.populateAndSend(serverQuit.getMessage(), ViewMessage.MessageType.FROM_SERVER_ERROR);
        System.out.println("[ServerQuitEvent] Server quit the connection.\nMessage: " + serverQuit.getMessage()); // todo [debug]
        // todo add code to handle disconnection
    }

    /* Message listener */
    /**
     * Method that send a message from the server to the player
     *
     * @param message (MessageEvent from the server)
     */
    @Override
    public void update(MessageEvent message) {
//        ViewMessage.populateAndSend(message.getMessage(), ViewMessage.MessageType.FROM_SERVER_MESSAGE);
        System.out.println("[MessageEvent] " + message.getMessage()); // todo [debug]
    }


    /* Error message listener */
    /**
     * Method that send an error message from the server to the player
     *
     * @param message (ErrorMessageEvent from the server)
     */
    @Override
    public void update(ErrorMessageEvent message) {
//        ViewMessage.populateAndSend(message.getMessage(), ViewMessage.MessageType.FROM_SERVER_ERROR);
        System.out.println("[ErrorMessageEvent] " + message.getMessage()); // todo [debug]
    }


    /**
     * Method that populates all the data send by the Server with the ServerSendDataEvent.
     *
     * @param serverSentData (ServerSendData from the server)
     */
    @Override
    public void update(ServerSendDataEvent serverSentData) {
//        try {
//            ViewBoard.populate(serverSentData);
//            ViewPlayer.populate(serverSentData);
//        } catch (WrongEventException e) {
//            e.printStackTrace();
//        }
        /* Retrieve data */
        this.boardXsize = serverSentData.getBoardXsize();
        this.boardXsize = serverSentData.getBoardYsize();
        this.players = serverSentData.getPlayers();
        this.workersToPlayer = serverSentData.getWorkersToPlayer();
        /* Print info */
        System.out.println("[ServerSendDataRequest]");
        /* Board size */
        System.out.println("\nBoard X size: " + serverSentData.getBoardXsize());
        System.out.println("Board Y size: " + serverSentData.getBoardYsize());
        /* Players list and workers associated to each */
        System.out.println("\nPlayers list and their Workers: ");
        for (String player : serverSentData.getPlayers()) {
            System.out.println(player);
            List<String> workers = serverSentData.getWorkersToPlayer().get(player);
            for (String worker : workers)
                System.out.println("     - " + worker);
        }
    }


    /**
     * Method that sets the information of the cards.
     *
     * @param cardsInformation (CardsInformationEvent from the server)
     */
    @Override
    public void update(CardsInformationEvent cardsInformation) {
//        List<CardInfo> cardList = cardsInformation.getCards();
//        for (CardInfo card: cardList) {
//            new ViewCard(card.getName(), card.getEpithet(), card.getDescription());
//        }
        // todo [debug]
        if(cardsInformation.getPlayer().equals("")) {
            if(cardsInformation.getChallenger().equals(nickname)) {
                System.out.println("[CardsInformationEvent] You are the challenger.\nChoose a number of cards equal to the number of players for this game."); // todo [debug]
                System.out.println("Cards: ");
                List<CardInfo> cardList = cardsInformation.getCards();
                cardList.forEach(x -> System.out.println("    - " + x.getName()));
            }
            else {
                System.out.println("[CardsInformationEvent] Challenger is choosing cards for this game...");
            }
        }
        else if(cardsInformation.getPlayer().equals(nickname)) {
            System.out.println("[CardsInformationEvent] Select your Card.");
            System.out.println("Cards:");
            List<CardInfo> cardList = cardsInformation.getCards();
            cardList.forEach(x -> System.out.println("    - " + x.getName()));
        }
        else {
            System.out.println("[CardsInformationEvent] Another Player is choosing his/her Card. Wait...");
        }
    }

    /**
     * Method that will be called if a card is selected.
     *
     * @param cardSelected (CardSelectedEvent from server)
     */
    @Override
    public void update(CardSelectedEvent cardSelected) {
//        ViewNickname.setMyCard(cardSelected.getCardName());
        // todo [debug]
        if(cardSelected.getPlayerNickname().equals(nickname)) {
            if (cardSelected.success())
                System.out.println("[CardSelectedEvent] You have correctly selected the card: " + cardSelected.getCardName()); // todo [debug]
            else
                System.out.println("[CardSelectedEvent] The Card you have selected is not valid!"); // todo [debug]
        }
        else {
            if (cardSelected.success())
                System.out.println("[CardSelectedEvent] Player " + cardSelected.getPlayerNickname() + " has correctly selected the card: " + cardSelected.getCardName()); // todo [debug]
            else
                System.out.println("[CardSelectedEvent] The Card Player " + cardSelected.getPlayerNickname() + " has selected is not valid!"); // todo [debug]
        }
    }

    @Override
    public void update(WorkerPlacedEvent workerPlaced) {
//        try {
//            ((ViewWorker)ViewWorker.search(workerPlaced.getWorker())).placeOn(workerPlaced.getX(), workerPlaced.getY());
//        } catch (NotFoundException | WrongViewObjectException e) {
//            ViewMessage.populateAndSend("Wrong placed recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
//        }
        if(workerPlaced.success())
            System.out.println("[WorkerPlacedEvent] Worker '" + workerPlaced.getWorker() + "' was correctly placed in position: ( " + workerPlaced.getX() + " , " + workerPlaced.getY() + " )"); // todo [debug]
        else
            System.out.println("[WorkerPlacedEvent] A Worker was not correctly placed!");
    }

    @Override
    public void update(WorkerMovedEvent workerMoved) {
//        try {
//            ((ViewWorker)ViewWorker.search(workerMoved.getWorker())).placeOn(workerMoved.getFinalX(), workerMoved.getFinalY());
//        } catch (NotFoundException | WrongViewObjectException e) {
//            ViewMessage.populateAndSend("Wrong cell received", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
//        }
        if(workerMoved.getMoveOutcome() == MoveOutcomeType.EXECUTED || workerMoved.getMoveOutcome() == MoveOutcomeType.TURN_SWITCHED || workerMoved.getMoveOutcome() == MoveOutcomeType.TURN_OVER || workerMoved.getMoveOutcome() == MoveOutcomeType.LOSS || workerMoved.getMoveOutcome() == MoveOutcomeType.WIN) {
            System.out.println("[WorkerMovedEvent] Worker '" + workerMoved.getWorker() + "' was correctly moved:"); // todo [debug]
            System.out.println("    Initial position: ( " + workerMoved.getInitialX() + " , " + workerMoved.getInitialY() + " )"); // todo [debug]
            System.out.println("    Current position: ( " + workerMoved.getFinalX() + " , " + workerMoved.getFinalY() + " )"); // todo [debug]
        }
        else
            System.out.println("[WorkerMovedEvent] Worker '" + workerMoved.getWorker() + "' movement is not valid!"); // todo [debug]
    }

    @Override
    public void update(BlockBuiltEvent blockBuilt) {
//        ViewCell cell;
//        try {
//            cell = ((ViewBoard) ViewBoard.search(ViewBoard.getClassId())).getCellAt(blockBuilt.getX(), blockBuilt.getY());
//        } catch (NotFoundException | WrongViewObjectException e) {
//            ViewMessage.populateAndSend("Wrong cell recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
//            return;
//        }
//        if(blockBuilt.getBlockType().equals(PlaceableType.BLOCK))
//            cell.buildLevel();
//        else if(blockBuilt.getBlockType().equals(PlaceableType.DOME))
//            cell.buildDome();
//        else
//            ViewMessage.populateAndSend("Wrong block recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
        if(blockBuilt.getMoveOutcome() == MoveOutcomeType.EXECUTED || blockBuilt.getMoveOutcome() == MoveOutcomeType.TURN_SWITCHED || blockBuilt.getMoveOutcome() == MoveOutcomeType.TURN_OVER || blockBuilt.getMoveOutcome() == MoveOutcomeType.LOSS || blockBuilt.getMoveOutcome() == MoveOutcomeType.WIN)
            System.out.println("[BlockBuiltEvent] A '" + blockBuilt.getBlockType().toString() + "' was correctly built in place: ( " + blockBuilt.getX() + " , " + blockBuilt.getY() + " )"); // todo [debug]
        else
            System.out.println("[BlockBuiltEvent] '" + blockBuilt.getBlockType().toString() + "' was NOT built. Invalid BuildMove."); // todo [debug]
    }

    @Override
    public void update(BlockRemovedEvent blockRemoved) {
//        ViewCell cell;
//        try {
//            cell = ((ViewBoard) ViewBoard.search(ViewBoard.getClassId())).getCellAt(blockRemoved.getX(), blockRemoved.getY());
//        } catch (NotFoundException | WrongViewObjectException e) {
//            ViewMessage.populateAndSend("Wrong cell recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
//            return;
//        }
//        if(cell.isDoomed())
//            cell.removeDome();
//        else
//            cell.removeLevel();
        // todo: This kind of event should not be displayed!
        System.err.println("[BlockRemovedEvent] A block was correctly removed. (This kind of event SHALL NOT be displayed!)"); // todo [debug]
    }

    @Override
    public void update(WorkerRemovedEvent workerRemoved) {
//        try {
//            ((ViewWorker)ViewWorker.search(workerRemoved.getWorker())).removeWorker();
//        } catch (NotFoundException | WrongViewObjectException e) {
//            ViewMessage.populateAndSend("Cannot remove worker", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
//        }
        System.out.println("[WorkerRemovedEvent] Worker '" + workerRemoved.getWorker() + "' was correctly removed from position: ( " + workerRemoved.getX() + " , " + workerRemoved.getY() + " )"); // todo [debug]
    }

    @Override
    public void update(RequireStartPlayerEvent requireStartPlayer) {
        if(requireStartPlayer.getChallenger().equals(nickname)) {
            System.out.println("[RequireStartPlayerEvent] You are the challenger.\nChoose the Start Player: "); // todo [debug]
            List<String> players = requireStartPlayer.getPlayers();
            players.forEach(x -> System.out.println("    - " + x));
        }
        else
            System.out.println("Challenger is choosing the Start Player. Wait...");
    }

    @Override
    public void update(RequirePlaceWorkersEvent requirePlaceWorkers) {
        if(requirePlaceWorkers.getPlayer().equals(nickname)) {
            System.out.println("[RequirePlaceWorkersEvent] Place worker."); // todo [debug]
        }
        else
            System.out.println("Other players are placing their Workers. Wait...");
    }

    @Override
    public void update(WorkerSelectedEvent workerSelected) {
        if(workerSelected.getPlayerNickname().equals(nickname)) {
            if(workerSelected.success())
                System.out.println("[WorkerSelectedEvent] Worker '" + workerSelected.getWorker() + "' was correctly SELECTED.");
            else
                System.out.println("[WorkerSelectedEvent] Worker '" + workerSelected.getWorker() + "' CANNOT BE SELECTED!");
        }
    }

    @Override
    public void update(UndoOkEvent undoOk) {
        System.out.println("[UndoOkEvent] Previous move was undone.");
    }


    @Override
    public void update(Object o) {
//        ViewMessage.populateAndSend("Recived a wrong message", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
        System.err.println("[ERROR] Received a generic Object (UNEXPECTED!)");
    }



    /* ########## AUXILIARY TEST METHODS ########## */
    private void printGameInfo() {
        System.out.println("Players list and their Workers: ");
        for (String player : players) {
            System.out.println(player);
            List<String> workers = workersToPlayer.get(player);
            for (String worker : workers)
                System.out.println("     - " + worker);
        }
    }




    public String getNickname() {
        String ret = ViewNickname.getMyNickname();
        return ret==null?"":ret;
    }

    //todo: serve? [Andrea: Forse. In caso non serva, lo togliamo al momento del refactoring generale]
    public void setNickname(String nickname) { throw new NullPointerException("Questo metodo non dovrebbe essere chiamato"); }

    public boolean isConnectionActive() {
        return connectionActive;
    }

    public void setConnectionActive(boolean connectionActive) {
        this.connectionActive = connectionActive;
    }















    // TODO: 09/05/20 metodi per il test 
    /*// TODO: con questo sistema viene verificato anche che la comunicazione con Pattern Observer funzioni correttamente su thread diversi
    // TODO: ricordarsi che il metodo run() gira su un thread differente rispetto quello dove "girano" i metodi update() --> i metodi update() vengono chiamati sullo stesso thread di client.ClientConnection
    @Override
    public void run() {
        // TODO: commentato perché ho bisogno di fare ancora qualche test...
        *//*new TerminalViewer();
        new CLIViewer();
        new GUIViewer();
        System.out.println("Hello World");

        ViewStatus.init();
        Viewer.setAllStatusViewer(ViewStatus.getActual().getViewer());

        ViewStatus.nextStatus();
        Viewer.setAllStatusViewer(ViewStatus.getActual().getViewer());*//*



        // todo istanziare connessione ecc...
        tConnection = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connection.run();
                }
                catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        });
        tConnection.start();


        //System.out.println("Welcome! Type something.");
        input = in.nextLine();
        while(!input.equals("quit")) {
            switch (input) {
                case "alto":
                case "andrea":
                case "giorgio":
                case "marco":
                    nickname = input;
                    notify(new SetNicknameEvent(input)); // submit nickname
                    break;
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                    notify(new SetPlayersNumberEvent(Integer.parseInt(input))); // by this way we ensure auto-boxing from int to Object type works correctly
                    break;
                case "build_block":
                    notify(new BuildBlockEvent("Worker1@" + nickname, 3, 5, PlaceableType.BLOCK));
                    break;
                case "select_card":
                    notify(new CardSelectionEvent("zeus"));
                    break;
                case "move_worker":
                    notify(new MoveWorkerEvent("Worker1@" + nickname, 3, 3));
                    break;
                case "place_worker":
                    notify(new PlaceWorkerEvent(1, 1));
                    break;
                case "remove_block":
                    notify(new RemoveBlockEvent("Worker1@" + nickname, 4, 1));
                    break;
                case "remove_worker":
                    notify(new RemoveWorkerEvent("Worker2@" + nickname, 0, 0));
                    break;
                case "select_worker":
                    notify(new SelectWorkerEvent("Worker1@" + nickname));
                    break;
                case "turn_change":
                    notify(new TurnStatusChangeEvent(StateType.MOVEMENT));
                    break;
                case "data_request":
                    notify(new ViewRequestDataEvent());
                    break;
                default:
                    System.out.println("Invalid request. Try again.");
                    break;
            }

            input = in.nextLine();
        }

        notify(new QuitEvent());
        // TODO: close connection, and gracefully close the whole application
    }

    *//* Server general listener *//*
    @Override
    public void update(NextStatusEvent nextStatus) {
        // TESTED ONLY WHEN IT'S TIME TO SUBMIT THE NICKNAME
        //System.out.println(nextStatus); // todo: test also if overridden method toString() works correctly.

        ViewStatus.nextStatus();
    }

    @Override
    public void update(ServerSendDataEvent serverSentData) {
        System.out.println("Server has sent data requested:");
        *//* Board size *//*
        System.out.println("\nBoard X size: " + serverSentData.getBoardXsize());
        System.out.println("Board Y size: " + serverSentData.getBoardYsize());
        *//* Players list and workers associated to each *//*
        System.out.println("\nPlayers list and their Workers: ");
        for (String player : serverSentData.getPlayers()) {
            System.out.println(player);
            List<String> workers = serverSentData.getWorkersToPlayer().get(player);
            for (String worker : workers)
                System.out.println("     - " + worker);
        }
    }

    @Override
    public void update(InvalidNicknameEvent invalidNickname) {
        System.out.println(invalidNickname);
    }

    @Override
    public void update(RequirePlayersNumberEvent requirePlayersNumber) {
        System.out.println(requirePlayersNumber);
    }

    @Override
    public void update(LobbyFullEvent lobbyFull) {
        System.out.println(lobbyFull);
        // TODO: close connection, and gracefully close the whole application
    }

    @Override
    public void update(PlayerWinEvent playerWin) {}

    @Override
    public void update(PlayerLoseEvent playerLose) {}

    @Override
    public void update(TurnStatusChangedEvent turnStatusChange) {
        System.out.println("Turn status changed to: " + turnStatusChange.getState().toString()); // todo: check what toString() of an enum prints... [si, funziona.]
    }

    @Override
    public void update(ServerQuitEvent serverQuit) {
        // todo add code to handle disconnection
        System.out.println(serverQuit.getMessage());
    }


    *//* Game preparation listener *//*
    @Override
    public void update(WorkerPlacedEvent workerPlaced) {
        System.out.println("Worker named '" + workerPlaced.getWorker() + "' was PLACED correctly in position: (" + workerPlaced.getX() + " , " + workerPlaced.getY() + ")");
    }

    @Override
    public void update(CardSelectedEvent cardSelected) {
        System.out.println("Card " + cardSelected.getCardName() + " has been selected correctly.");
    }

    @Override
    public void update(CardsInformationEvent cardsInformation) {
        // todo something
    }

    @Override
    public void update(RequireStartPlayerEvent requireStartPlayer) {
        // todo something
    }

    @Override
    public void update(RequirePlaceWorkersEvent requirePlaceWorkers) {
        // todo something
    }


    *//* Move executed listener *//*
    @Override
    public void update(WorkerMovedEvent workerMoved) {
        System.out.println("Worker named '" + workerMoved.getWorker() + "' was MOVED correctly in position: (" + workerMoved.getFinalX() + " , " + workerMoved.getFinalY() + ")");
    }

    @Override
    public void update(BlockBuiltEvent blockBuilt) {
        System.out.println("A block of type '" + blockBuilt.getBlockType() + "' was correctly BUILT in position: (" + blockBuilt.getX() + " , " + blockBuilt.getY() + ")");
    }

    @Override
    public void update(WorkerRemovedEvent workerRemoved) {
        System.out.println("Worker named '" + workerRemoved.getWorker() + "' was REMOVED correctly from position: (x=" + workerRemoved.getX() + " , y=" + workerRemoved.getY() + ")");
    }

    @Override
    public void update(BlockRemovedEvent blockRemoved) {
        System.out.println("A block of type '" + blockRemoved.getBlockType() + "' was correctly REMOVED from position: (x=" + blockRemoved.getX() + " , y=" + blockRemoved.getY() + ")");
    }

    @Override
    public void update(WorkerSelectedEvent workerSelected) {
        System.out.println("Worker named '" + workerSelected.getWorker() + "was correctly SELECTED");
    }


    *//* Message listener *//*
    @Override
    public void update(MessageEvent message) {
        System.out.println("MESSAGE: " + message);
    }


    *//* Error message listener *//*
    @Override
    public void update(ErrorMessageEvent message) {
        System.out.println("ERROR MESSAGE: " + message);
    }


    *//* Generic update method *//*
    @Override
    public void update(Object o) {
        System.out.println("A generic Object was received.");
    }*/
}
