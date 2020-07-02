package it.polimi.ingsw.view.clientSide;

import it.polimi.ingsw.chat.ChatMessageEvent;
import it.polimi.ingsw.chat.ChatMessageListener;
import it.polimi.ingsw.connection.client.ClientConnection;
import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.model.card.CardInfo;
import it.polimi.ingsw.model.persistence.GameState;
import it.polimi.ingsw.model.persistence.players.PlayerData;
import it.polimi.ingsw.model.persistence.players.PlayersState;
import it.polimi.ingsw.model.persistence.players.WorkerData;
import it.polimi.ingsw.model.player.turn.StateType;
import it.polimi.ingsw.model.player.worker.ChooseType;
import it.polimi.ingsw.observer.MVEventListener;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.clientSide.viewCore.data.ViewObject;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.FirstPlayerExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.UndoExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.interfaces.ViewSender;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.PlayerMessages;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.events.QuitEvent;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.polimi.ingsw.model.player.turn.StateType.CONSTRUCTION;
import static it.polimi.ingsw.model.player.turn.StateType.MOVEMENT;

/**
 * Class that connects the View with the connection (and so with the Server).
 */
public class View extends Observable<Object> implements MVEventListener, Runnable, ViewSender {
    /* Multi-threading operations */
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    /* General */
    Thread tConnection;
    private ClientConnection connection;
    private Scanner in; // Scanner unique reference
    private Viewer viewer; // specific viewer for the Client
    private String input; // unique input
    private String nickname; // Player's nickname
    private boolean nicknameSet;
    private boolean connectionActive;

    //Per fare debugging
    public static final boolean debugging = false;

    private boolean meWinner = false;

    /* ########## TESTING ########## */
//    private int boardXsize, boardYsize;
//    private List<String> players;
//    private Map<String, List<String>> workersToPlayer;

    /**
     * Constructor
     *
     * @param in          (the Scanner from which read the input for the test/debugging).
     * @param connection  (the ClientConnection to be used for the connection).
     * @param viewer      (the Viewer to be used for this execution).
     */
    public View(Scanner in, ClientConnection connection, Viewer viewer) {
        this.in = in;
        this.connection = connection;
        this.viewer = viewer;
        connection.setChatMessageHandler(new ChatMessageReceiver());
    }


    /**
     * Method that starts th Viewer, make it visible an does all the things to run properly the Client.
     */
    @Override
    public void run() {
        viewer.start();
        Executer.setSender(this);
        ViewStatus.init();



        tConnection = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connection.run();
                }
                catch (IOException ex) {
                    if (View.debugging) {
                        System.err.println(ex.getMessage());
                    }
                    ViewMessage.populateAndSend(ex.getMessage(), ViewMessage.MessageType.FROM_SERVER_ERROR);
                }
            }
        });
        tConnection.start();




        /* ########## USEFUL FOR TESTING ########## */
        //System.out.println("Welcome! Type something.");
//        input = in.nextLine();
//        while(!input.equals("quit")) {
//            switch (input) {
//                case "alto":
//                case "andrea":
//                case "giorgio":
//                case "marco":
//                    if(!nicknameSet) {
//                        nickname = input;
//                        notify(new SetNicknameEvent(nickname)); // submit nickname
//                        nicknameSet = true;
//                    }
//                    else {
//                        notify(new SetStartPlayerEvent(input)); // submit nickname
//                    }
//                    break;
//
//
//                case "1":
//                case "2":
//                case "3":
//                case "4":
//                case "5":
//                    notify(new SetPlayersNumberEvent(Integer.parseInt(input))); // by this way we ensure auto-boxing from int to Object type works correctly
//                    break;
//
//
//                case "y":
//                    notify(new GameResumingResponseEvent(true));
//                    break;
//                case "n":
//                    notify(new GameResumingResponseEvent(false));
//                    break;
//
//
//                case "start_player":
//                    input = in.nextLine();
//                    notify(new SetStartPlayerEvent(input)); // submit nickname
//                    break;
//
//
//                case "build_block":
//                    System.out.println("Type the Worker's ID (just the number) by which make a build: ");
//                    int id = Integer.parseInt(in.nextLine());
//                    String workerId = "[Worker]\t" + id;
//                    System.out.println("X position: ");
//                    int x = Integer.parseInt(in.nextLine());
//                    System.out.println("Y position: ");
//                    int y = Integer.parseInt(in.nextLine());
//                    System.out.println("Select the type of block you want to build (type its number):\n1 - BLOCK\n2 - DOME");
//                    int block = Integer.parseInt(in.nextLine());
//                    PlaceableType blockType = PlaceableType.DOME;
//                    if(block == 1)
//                        blockType = PlaceableType.BLOCK;
//                    notify(new BuildBlockEvent(workerId, x, y, blockType));
//                    break;
//
//
//                case "challenger_card":
//                    String card;
//                    int n = players.size();
//                    List<String> cards = new ArrayList<>(n);
//                    System.out.println("Choose " + n + " Cards for this game.");
//                    for(int i = 0; i < n; i++) {
//                        System.out.println((n - i) + " remaining Cards to choose. Type a name of a Card:");
//                        card = in.nextLine();
//                        cards.add(card);
//                    }
//                    notify(new CardsChoosingEvent(cards));
//                    break;
//
//
//                case "select_card":
//                    card = in.nextLine();
//                    notify(new CardSelectionEvent(card));
//                    break;
//
//
//                case "move_worker":
//                    System.out.println("Type the Worker's ID (just the number) by which make a movement: ");
//                    id = Integer.parseInt(in.nextLine());
//                    workerId = "[Worker]\t" + id;
//                    System.out.println("X position: ");
//                    x = Integer.parseInt(in.nextLine());
//                    System.out.println("Y position: ");
//                    y = Integer.parseInt(in.nextLine());
//                    notify(new MoveWorkerEvent(workerId, x, y));
//                    break;
//
//
//                case "place_worker":
//                    System.out.println("Place a Worker onto the game board.");
//                    System.out.println("X position: ");
//                    x = Integer.parseInt(in.nextLine());
//                    System.out.println("Y position: ");
//                    y = Integer.parseInt(in.nextLine());
//                    notify(new PlaceWorkerEvent(x, y));
//                    break;
//
//
//                case "remove_block":
//                    System.out.println("Type the Worker's ID (just the number) by which remove a block from the game board: ");
//                    id = Integer.parseInt(in.nextLine());
//                    workerId = "[Worker]\t" + id;
//                    System.out.println("X position of the block to remove: ");
//                    x = Integer.parseInt(in.nextLine());
//                    System.out.println("Y position of the block to remove: ");
//                    y = Integer.parseInt(in.nextLine());
//                    notify(new RemoveBlockEvent(workerId, x, y));
//                    break;
//
//
//                case "remove_worker":
//                    System.out.println("Type the Worker's ID (just the number) of the Worker to remove from the game board: ");
//                    id = Integer.parseInt(in.nextLine());
//                    workerId = "[Worker]\t" + id;
//                    System.out.println("X position of the Worker to remove: ");
//                    x = Integer.parseInt(in.nextLine());
//                    System.out.println("Y position of the Worker to remove: ");
//                    y = Integer.parseInt(in.nextLine());
//                    notify(new RemoveWorkerEvent(workerId, x, y));
//                    break;
//
//
//                case "select_worker":
//                    System.out.println("Type the Worker's ID (just the number) of the Worker you want to select: ");
//                    id = Integer.parseInt(in.nextLine());
//                    workerId = "[Worker]\t" + id;
//                    notify(new SelectWorkerEvent(workerId));
//                    break;
//
//
//                case "turn_change":
//                    System.out.println("Select the turn you want to switch at:\n1 - MOVEMENT\n2 - CONSTRUCTION\n3 - PASS TURN");
//                    int turn = Integer.parseInt(in.nextLine());
//                    StateType turnType = StateType.NONE;
//                    if(turn == 1)
//                        turnType = StateType.MOVEMENT;
//                    else if(turn == 2)
//                        turnType = StateType.CONSTRUCTION;
//                    notify(new TurnStatusChangeEvent(turnType));
//                    break;
//
//
//                case "undo":
//                    notify(new UndoActionEvent());
//                    break;
//
//
//                case "chat":
//                    System.out.println("Write something as a chat message to send everyone: ");
//                    String message = in.nextLine();
//                    notify(new ChatMessageEvent(nickname, message));
//                    break;
//
//
//                case "data_request":
//                    notify(new ViewRequestDataEvent());
//                    break;
//
//
//                /* ########## CONTROL COMMANDS ########## */
//                case "game_info":
//                    printGameInfo();
//                    break;
//
//
//                default:
//                    System.out.println("Invalid request. Try again.");
//                    break;
//            }
//
//            input = in.nextLine();
//        }
//
//        notify(new QuitEvent());
    }

    //#######################UPDATE##########################

    //##################TESTED####################

    /**
     * Method called to make the status go ahead of one step.
     *
     * @param nextStatus (NextStatusEvent send from the server)
     */
    /* Server general listener */
    @Override
    public void update(NextStatusEvent nextStatus) {
        ViewMessage.populateAndSend(nextStatus.toString(), ViewMessage.MessageType.CHANGE_STATUS_MESSAGE);
        ViewStatus.nextStatus();
    }

    /**
     * Method that notify the view that is needed to select the number of players.
     *
     * @param requirePlayersNumber (RequirePlayersNumberEvent from the server)
     */
    @Override
    public void update(RequirePlayersNumberEvent requirePlayersNumber) {
        ViewMessage.populateAndSend(requirePlayersNumber.getMessage(), ViewMessage.MessageType.CHANGE_STATUS_MESSAGE);
        ViewStatus.setStatus("NUMBER_PLAYER");
    }

    /**
     * Method that populates all the data send by the Server with the ServerSendDataEvent.
     *
     * @param serverSentData (ServerSendData from the server)
     */
    @Override
    public void update(ServerSendDataEvent serverSentData) {
        try {
            ViewBoard.populate(serverSentData);
            ViewPlayer.populate(serverSentData);
        } catch (WrongEventException e) {
            if(View.debugging)
                e.printStackTrace();
        }
    }

    /**
     * Method that sets the information of the cards.
     *
     * @param cardsInformation (CardsInformationEvent from the server)
     */
    @Override
    public void update(CardsInformationEvent cardsInformation) {
        //Non uso il populate su ViewCard per mantenereuna lista di carte selezionate in modo efficiente
        List<CardInfo> cardList = cardsInformation.getCards();
        List<ViewCard> cards = new ArrayList<>();

        //Popolo cards list e creo le carte mancanti
        for (CardInfo card: cardList) {
            try{
                cards.add((ViewCard)ViewCard.search(card.getName()));
            }catch(WrongViewObjectException | NotFoundException e){
                cards.add(new ViewCard(card.getName(), card.getEpithet(), card.getDescription()));
            }
        }

        if(!cardsInformation.getPlayer().equals("")){
            //Controllo se è il challenger
            if(cardsInformation.getPlayer().equals(ViewNickname.getMyNickname())){
                Viewer.setAllCardSelection(new CardSelection(cards, false));
            }
        }else{
            //Controllo se è un giocatre qualsiasi
            if(cardsInformation.getChallenger().equals(ViewNickname.getMyNickname())){
                Viewer.setAllCardSelection(new CardSelection(cards, true));
            }
        }
    }

    /**
     * Method that notifies the placement of a worker on the board.
     *
     * @param workerPlaced (WorkerPlacedEvent from the server notifying the placement of the worker)
     */
    @Override
    public void update(WorkerPlacedEvent workerPlaced) {
        if(workerPlaced.success()) {
            try {
                ViewWorker.populate(workerPlaced);
            } catch (WrongEventException e) {
                ViewMessage.populateAndSend("Wrong message recived recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
            }
            Viewer.setAllRefresh();
        }else if((ViewNickname.getMyNickname()).equals(ViewSubTurn.getActual().getPlayer())){
            ViewMessage.populateAndSend("Cannot place theere your worker. retry", ViewMessage.MessageType.FROM_SERVER_MESSAGE);
        }
    }

    /**
     * Method that request one player to place its worker.
     *
     * @param requirePlaceWorkers (RequirePlaceWorkersEvent from the server)
     */
    @Override
    public void update(RequirePlaceWorkersEvent requirePlaceWorkers) {
        ViewSubTurn.setSubTurn(ViewSubTurn.PLACEWORKER, requirePlaceWorkers.getPlayer());
        ViewSubTurn.getActual().setPlayer(requirePlaceWorkers.getPlayer());
        Viewer.setAllSubTurnViewer(ViewSubTurn.getActual());
    }

    /**
     * Method that send an error message from the server to the player
     *
     * @param message (ErrorMessageEvent from the server)
     */

    @Override
    public void update(ErrorMessageEvent message) {
        ViewMessage.populateAndSend(message.getMessage(), ViewMessage.MessageType.FROM_SERVER_ERROR);
    }

    /**
     * Method that send a message from the server to the player
     *
     * @param message (MessageEvent from the server)
     */

    @Override
    public void update(MessageEvent message) {
        ViewMessage.populateAndSend(message.getMessage(), ViewMessage.MessageType.FROM_SERVER_MESSAGE);
    }

    /**
     * Default method.
     *
     * @param o (Object parameter)
     */
    @Override
    public void update(Object o) {
        ViewMessage.populateAndSend("Recived a wrong message", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
    }


    /**
     * Method that changes the turn status.
     *
     * @param turnStatusChange (TurnStatusChangedEvent from the server)
     */
    @Override
    public void update(TurnStatusChangedEvent turnStatusChange) {

        if (turnStatusChange.success()){

            UndoExecuter.canSendEvent();

            ViewSubTurn.setMacroStatus(turnStatusChange.getState());

            if(!turnStatusChange.getPlayerNickname().equals(ViewSubTurn.getActual().getPlayer())){
                ViewBoard.getBoard().setSelectedCell(null);
                ViewWorker.selectWorker((ViewWorker) null);
            }

            //Checks if it's a change of status
            if(((!turnStatusChange.getPlayerNickname().equals(ViewSubTurn.getActual().getPlayer()))&&(turnStatusChange.getState().equals(StateType.MOVEMENT)))||(ViewWorker.getSelected()==null)){
                ViewSubTurn.setSubTurn(ViewSubTurn.SELECTWORKER, turnStatusChange.getPlayerNickname());
            }else{
                //Else sets the turnStatus
                ViewSubTurn.set(turnStatusChange.getState().toString(), turnStatusChange.getPlayerNickname());
            }

            //notify the changing of the subturn
            Viewer.setAllSubTurnViewer(ViewSubTurn.getActual().getSubViewer());
        }else if(turnStatusChange.getPlayerNickname().equals(ViewNickname.getMyNickname())){
            ViewMessage.populateAndSend("You cannot do this turn change", ViewMessage.MessageType.FROM_SERVER_ERROR);
        }
    }

    /**
     * Method that notify the view that the selected nickname is not valid.
     *
     * @param invalidNickname (InvalidNickNameEvent from the server)
     */
    @Override
    public void update(InvalidNicknameEvent invalidNickname) {
        //It is in the LOGIN status...so we only need to display the error message
        ViewNickname.clear();
        ViewMessage.populateAndSend(invalidNickname.getMessage(), ViewMessage.MessageType.FROM_SERVER_ERROR);
    }

    /**
     * Method that will be called if a card is selected.
     *
     * @param cardSelected (CardSelectedEvent from server)
     */
    @Override
    public void update(CardSelectedEvent cardSelected) {
        try {
            ViewPlayer.populate(cardSelected);
        } catch (WrongEventException e) {
            ViewMessage.populateAndSend("Wrong card selected recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
        }

    }

    @Override
    public void update(WorkerMovedEvent workerMoved) {
        try {
            ViewWorker.populate(workerMoved);
        } catch (WrongEventException e) {
            ViewMessage.populateAndSend("Wrong message recived recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
        }
        Viewer.setAllRefresh();

        //checks for the undo
        if(ViewSubTurn.getActual().isMyTurn() && ViewObject.outcome(workerMoved.getMoveOutcome()))
            UndoExecuter.startUndo();
    }

    @Override
    public void update(BlockBuiltEvent blockBuilt) {
        try {
            ViewCell.populate(blockBuilt);
        } catch (WrongEventException e) {
            ViewMessage.populateAndSend("Wrong message recived recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
        }
        Viewer.setAllRefresh();

        //check for the undo
        if(ViewSubTurn.getActual().isMyTurn() && ViewObject.outcome(blockBuilt.getMoveOutcome()))
            UndoExecuter.startUndo();
    }

    /**
     * Method that signals that a player wins.
     *
     * @param playerWin (PlayerWinEvent from the server)
     */
    @Override
    public void update(PlayerWinEvent playerWin) {
        UndoExecuter.canSendEvent();
        if (ViewNickname.getMyNickname().equals(playerWin.getPlayerNickname())) {
            meWinner = true;
            ViewMessage.populateAndSend(playerWin.getWinnerMessage(), ViewMessage.MessageType.WIN_MESSAGE);
        } else {
            ViewMessage.populateAndSend(playerWin.getLosersMessage(), ViewMessage.MessageType.LOOSE_MESSAGE);
        }
    }

    /**
     * Method that signals that the player has lost.
     *
     * @param playerLose (PlayerLoseEvent from the server)
     */
    @Override
    public void update(PlayerLoseEvent playerLose) {
        UndoExecuter.canSendEvent();
        if (ViewNickname.getMyNickname().equals(playerLose.getPlayerNickname())) {
            ViewMessage.populateAndSend(playerLose.getMessage(), ViewMessage.MessageType.LOOSE_MESSAGE);
        }
    }

    /**
     * Method that signals that Worker has been selected.
     *
     * @param workerSelected (WorkerSelectedEvent from the server).
     */
    @Override
    public void update(WorkerSelectedEvent workerSelected) {
        if (workerSelected.success()){
            try {
                ViewWorker.populate(workerSelected);
            } catch (WrongEventException | NullPointerException ignore) {
            }

            ViewSubTurn.afterSelection();

            Viewer.setAllSubTurnViewer(ViewSubTurn.getActual());
            if(View.debugging)
                System.out.println("Worker named '" + workerSelected.getWorker() + "was correctly SELECTED");
        }else if (ViewSubTurn.getActual().getPlayer().equals(ViewNickname.getMyNickname())){
            ViewMessage.populateAndSend("Cannot select the worker", ViewMessage.MessageType.FROM_SERVER_ERROR);
        }
    }

    /**
     * Method that signals that has been done the undo.
     *
     * @param undoOk (UndoOkEvent from the server).
     */
    @Override
    public void update(UndoOkEvent undoOk) {
        ViewBoard.populate(undoOk.getBoardState());

        UndoExecuter.stop();

        ViewSubTurn.setMacroStatus(undoOk.getStateType());
        if(undoOk.getStateType() == StateType.MOVEMENT){
            ViewSubTurn.set("SELECTWORKER", ViewSubTurn.getActual().getPlayer());
        }else{
            ViewSubTurn.set("CONSTRUCTION", ViewSubTurn.getActual().getPlayer());
        }
        Viewer.setAllSubTurnViewer(ViewSubTurn.getActual());
        Viewer.setAllRefresh();
    }

    /**
     * Method that signals that the placeable has been removed.
     *
     * @param blockRemoved (BlockRemovedEvent from the server).
     */
    @Override
    public void update(BlockRemovedEvent blockRemoved) {
        try {
            ViewCell.populate(blockRemoved);
        } catch (WrongEventException e) {
            ViewMessage.populateAndSend("Wrong message recived recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
        }
    }

    /**
     * Method that signals that the worker has been removed.
     *
     * @param workerRemoved (WorkerRemovedEvent from the server).
     */
    @Override
    public void update(WorkerRemovedEvent workerRemoved) {
        try {
            ViewWorker.populate(workerRemoved);
        } catch (WrongEventException e) {
            ViewMessage.populateAndSend("Wrong message recived recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
        }
    }

    /**
     * Method that the lobby is full and closes the application.
     *
     * @param lobbyFull (LobbyFullEvent from the server)
     */
    @Override
    public void update(LobbyFullEvent lobbyFull) {
        ViewStatus.setStatus("CLOSING");
        ViewMessage.populateAndSend(lobbyFull.getMessage(), ViewMessage.MessageType.CHANGE_STATUS_MESSAGE);
        ViewMessage.populateAndSend(lobbyFull.getMessage(), ViewMessage.MessageType.FROM_SERVER_ERROR);
    }

    /**
     * Method that the game is over.
     *
     * @param gameOver (GameOverEvent from the server)
     */
    @Override
    public void update(GameOverEvent gameOver) {
        if(!ViewStatus.getActual().getId().equals("GAME_OVER")){
            UndoExecuter.canSendEvent();
            ViewStatus.setStatus("GAME_OVER");
            Viewer.setAllStatusViewer(ViewStatus.getActual().getViewer());
            if(meWinner){
                send(new QuitEvent());
            }
        }
    }

    /**
     * Method that the game is being resumed.
     *
     * @param gameResuming (GameResumingEvent from the server)
     */
    @Override
    public void update(GameResumingEvent gameResuming) {
        GameState gameState = gameResuming.getGameState();
        String selectedWorker = null;


        if(gameState == null){
            ViewMessage.populateAndSend("Threre is an unfinished game", ViewMessage.MessageType.CHANGE_STATUS_MESSAGE);
            ViewStatus.setStatus("REQUEST_RESUMING");
        }else{
            ViewMessage.populateAndSend("Resuming old game", ViewMessage.MessageType.CHANGE_STATUS_MESSAGE);
            ViewStatus.setStatus("RESUMING");

            PlayersState playersState = gameState.getPlayers();
            ArrayList<String> playersName = new ArrayList<String>(3);

            for(String name: playersState.getData().keySet()){
                ViewPlayer myPlayer;
                playersName.add(name);

                StateType turn = playersState.getData().get(name).getTurn();

                new ViewPlayer(name);

                if(turn == CONSTRUCTION || turn == MOVEMENT){
                    ViewSubTurn.setMacroStatus(turn);
                    ViewSubTurn.getActual().setPlayer(name);
                    if(turn == CONSTRUCTION){
                        ViewSubTurn.setSubTurn(ViewSubTurn.BUILD);
                    }else{
                        ViewSubTurn.setSubTurn(ViewSubTurn.SELECTWORKER);
                    }
                }

                try {
                    myPlayer = ViewPlayer.searchByName(name);
                } catch (NotFoundException e) {
                    if(View.debugging)
                        e.printStackTrace();
                    continue;
                }
                PlayerData playerData = playersState.getData().get(name);

                myPlayer.setCard(((ViewCard) ViewCard.populate(playerData.getCard())).getName());

                for(WorkerData workerId: playerData.getWorkers()){
                    try {
                        new ViewWorker(workerId.getWorkerId(), myPlayer).setWorkerColor(workerId.getColor());
                        if(workerId.getChosen()== ChooseType.CHOSEN){
                            selectedWorker = workerId.getWorkerId();
                        }
                    } catch (NotFoundException e) {
                        if(View.debugging)
                            e.printStackTrace();
                    }
                }

            }

            ViewBoard.populate(gameState.getBoard());

            if(selectedWorker != null) {
                ViewWorker.selectWorker(selectedWorker);
            }else{
                ViewSubTurn.setSubTurn(ViewSubTurn.SELECTWORKER);
            }
        }
    }

    /**
     * Method that notify that the view has to quit.
     *
     * @param serverQuit (ServerQuitEvent from the server)
     */
    @Override
    public void update(ServerQuitEvent serverQuit) {
        ViewStatus.setStatus("CLOSING");
        ViewMessage.populateAndSend(serverQuit.getMessage(), ViewMessage.MessageType.CHANGE_STATUS_MESSAGE);
        ViewMessage.populateAndSend(serverQuit.getMessage(), ViewMessage.MessageType.FROM_SERVER_ERROR);
        // disconnection
        connection.closeConnection();
    }

    /**
     * Method to notify the challenger that it's needed it choose the
     * starting Player for the current game.
     *
     * @param requireStartPlayer Event: Require Start Player
     */
    @Override
    public void update(RequireStartPlayerEvent requireStartPlayer) {

        if(requireStartPlayer.getChallenger().equals(ViewNickname.getMyNickname())) {
            ViewSubTurn.setSubTurn(ViewSubTurn.CHOOSE_FIRST_PLAYER, ViewNickname.getMyNickname());
            ((FirstPlayerExecuter) ViewSubTurn.getActual().getExecuter()).populate(requireStartPlayer);
            Viewer.setAllSubTurnViewer(ViewSubTurn.getActual());
        }

    }

    //#########################END UPDATE###################################

    /**
     * Method used to retrieve the player's nickname.
     *
     * @return (the player's nickname).
     */
    public String getNickname() {
        String ret = ViewNickname.getMyNickname();
        return ret==null?"":ret;
    }

    //Unused and dangerous to be called.
    //public void setNickname(String nickname) { throw new NullPointerException("Questo metodo non dovrebbe essere chiamato"); }

    /**
     * Method to retrieve the connectionActive.
     *
     * @return (the connectionActive).
     */
    public boolean isConnectionActive() {
        return connectionActive;
    }

    /**
     * Method to set the connectionActive.
     *
     * @param connectionActive (the new value of connectionActive).
     */
    public void setConnectionActive(boolean connectionActive) {
        this.connectionActive = connectionActive;
    }


    /* ################ USEFUL FOR TESTING ################# */
//    /**
//     * Method called to make the status go ahead of one step.
//     *
//     * @param nextStatus (NextStatusEvent send from the server)
//     */
//    /* Server general listener */
//    @Override
//    public void update(NextStatusEvent nextStatus) {
////        ViewMessage.populateAndSend(nextStatus.toString(), ViewMessage.MessageType.CHANGE_STATUS_MESSAGE);
////        ViewStatus.nextStatus();
//        System.out.println("[NextStatusEvent] " + nextStatus.getMessage());
//    }
//
//    /**
//     * Method that notify the view that the selected nickname is not valid.
//     *
//     * @param invalidNickname (InvalidNickNameEvent from the server)
//     */
//    @Override
//    public void update(InvalidNicknameEvent invalidNickname) {
//        //It is in the LOGIN status...so we only need to display the error message
////        ViewNickname.clear();
////        ViewMessage.populateAndSend(invalidNickname.getMessage(), ViewMessage.MessageType.FROM_SERVER_ERROR);
//        System.out.println("[InvalidNicknameEvent] Invalid nickname! Try again.");
//    }
//
//    /**
//     * Method that notify the view that is needed to select the number of players.
//     *
//     * @param requirePlayersNumber (RequirePlayersNumberEvent from the server)
//     */
//    @Override
//    public void update(RequirePlayersNumberEvent requirePlayersNumber) {
////        ViewMessage.populateAndSend(requirePlayersNumber.getMessage(), ViewMessage.MessageType.CHANGE_STATUS_MESSAGE);
////        ViewStatus.setStatus("NUMBER_PLAYER");
//        System.out.println("[RequirePlayersNumberEvent] You are the first player. Choose the number of players for this game (2 or 3):");
//    }
//
//    /**
//     * Method that the lobby is full and closes the application.
//     *
//     * @param lobbyFull (LobbyFullEvent from the server)
//     */
//    @Override
//    public void update(LobbyFullEvent lobbyFull) {
////        ViewStatus.setStatus("CLOSING");
////        ViewMessage.populateAndSend(lobbyFull.getMessage(), ViewMessage.MessageType.CHANGE_STATUS_MESSAGE);
////        ViewMessage.populateAndSend(lobbyFull.getMessage(), ViewMessage.MessageType.FROM_SERVER_ERROR);
//        System.out.println("[LobbyFullEvent] Lobby is full!");
//    }
//
//    /**
//     * Method that signals that a player wins.
//     *
//     * @param playerWin (PlayerWinEvent from the server)
//     */
//    @Override
//    public void update(PlayerWinEvent playerWin) {
////        if (ViewNickname.getMyNickname().equals(playerWin.getPlayerNickname())) {
////            ViewMessage.populateAndSend(playerWin.getWinnerMessage(), ViewMessage.MessageType.WIN_MESSAGE);
////        } else {
////            ViewMessage.populateAndSend(playerWin.getLosersMessage(), ViewMessage.MessageType.LOOSE_MESSAGE);
////        }
//        System.out.println("[PlayerWinEvent] Player " + playerWin.getPlayerNickname() + " has won!");
//    }
//
//    /**
//     * Method that signals that the player has lost.
//     *
//     * @param playerLose (PlayerLoseEvent from the server)
//     */
//    @Override
//    public void update(PlayerLoseEvent playerLose) {
////        if (ViewNickname.getMyNickname().equals(playerLose.getPlayerNickname())) {
////            ViewMessage.populateAndSend(playerLose.getMessage(), ViewMessage.MessageType.LOOSE_MESSAGE);
////        }
//        System.out.println("[PlayerLoseEvent] Player " + playerLose.getPlayerNickname() + " has lost!");
//    }
//
//    /**
//     * Method that changes the turn status.
//     *
//     * @param turnStatusChange (TurnStatusChangedEvent from the server)
//     */
//    @Override
//    public void update(TurnStatusChangedEvent turnStatusChange) {
////        ViewSubTurn.set(turnStatusChange.getState().toString());
////        ViewSubTurn.getActual().setPlayer(turnStatusChange.getPlayerNickname());
////        Viewer.setAllSubTurnViewer(ViewSubTurn.getActual().getSubViewer());
//        //System.out.println("Turn status changed to: " + turnStatusChange.getState().toString());
//        if(turnStatusChange.getPlayerNickname().equals(nickname))
//            if(turnStatusChange.success()) {
//                if(turnStatusChange.getState().equals(StateType.NONE))
//                    System.out.println("[TurnStatusChangedEvent] You have passed your turn!");
//                else
//                    System.out.println("[TurnStatusChangedEvent] Your turn is now: " + turnStatusChange.getState().toString());
//            }
//            else
//                System.out.println("[TurnStatusChangedEvent] Cannot change to state: " + turnStatusChange.getState().toString());
//        else
//            System.out.println("[TurnStatusChangedEvent] Player " + turnStatusChange.getPlayerNickname() + "'s turn is now: " + turnStatusChange.getState().toString());
//    }
//
//    @Override
//    public void update(GameOverEvent gameOver) {
//        System.out.println("[GameOverEvent] Game over!");
//    }
//
//    @Override
//    public void update(GameResumingEvent gameResuming) {
//        System.out.println("[GameResumingEvent] A saved game was found. Do you want to resume it?\ny / n");
//    }
//
//
//    /**
//     * Method that notify that the view has to quit.
//     *
//     * @param serverQuit (ServerQuitEvent from the server)
//     */
//    @Override
//    public void update(ServerQuitEvent serverQuit) {
////        ViewStatus.setStatus("CLOSING");
////        ViewMessage.populateAndSend(serverQuit.getMessage(), ViewMessage.MessageType.CHANGE_STATUS_MESSAGE);
////        ViewMessage.populateAndSend(serverQuit.getMessage(), ViewMessage.MessageType.FROM_SERVER_ERROR);
//        System.out.println("[ServerQuitEvent] " + serverQuit.getMessage());
//        connection.closeConnection();
//    }
//
//    /* Message listener */
//    /**
//     * Method that send a message from the server to the player
//     *
//     * @param message (MessageEvent from the server)
//     */
//    @Override
//    public void update(MessageEvent message) {
////        ViewMessage.populateAndSend(message.getMessage(), ViewMessage.MessageType.FROM_SERVER_MESSAGE);
//        System.out.println("[MessageEvent] " + message.getMessage());
//    }
//
//
//    /* Error message listener */
//    /**
//     * Method that send an error message from the server to the player
//     *
//     * @param message (ErrorMessageEvent from the server)
//     */
//    @Override
//    public void update(ErrorMessageEvent message) {
////        ViewMessage.populateAndSend(message.getMessage(), ViewMessage.MessageType.FROM_SERVER_ERROR);
//        System.out.println("[ErrorMessageEvent] " + message.getMessage());
//    }
//
//
//    /**
//     * Method that populates all the data send by the Server with the ServerSendDataEvent.
//     *
//     * @param serverSentData (ServerSendData from the server)
//     */
//    @Override
//    public void update(ServerSendDataEvent serverSentData) {
////        try {
////            ViewBoard.populate(serverSentData);
////            ViewPlayer.populate(serverSentData);
////        } catch (WrongEventException e) {
////            e.printStackTrace();
////        }
//        /* Retrieve data */
//        this.boardXsize = serverSentData.getBoardXsize();
//        this.boardXsize = serverSentData.getBoardYsize();
//        this.players = serverSentData.getPlayers();
//        this.workersToPlayer = serverSentData.getWorkersToPlayer();
//        /* Print info */
//        System.out.println("[ServerSendDataRequest]");
//        /* Board size */
//        System.out.println("\nBoard X size: " + serverSentData.getBoardXsize());
//        System.out.println("Board Y size: " + serverSentData.getBoardYsize());
//        /* Players list and workers associated to each */
//        System.out.println("\nPlayers list and their Workers: ");
//        for (String player : serverSentData.getPlayers()) {
//            System.out.println(player);
//            List<String> workers = serverSentData.getWorkersToPlayer().get(player);
//            for (String worker : workers)
//                System.out.println("     - " + worker);
//        }
//    }
//
//
//    /**
//     * Method that sets the information of the cards.
//     *
//     * @param cardsInformation (CardsInformationEvent from the server)
//     */
//    @Override
//    public void update(CardsInformationEvent cardsInformation) {
////        List<CardInfo> cardList = cardsInformation.getCards();
////        for (CardInfo card: cardList) {
////            new ViewCard(card.getName(), card.getEpithet(), card.getDescription());
////        }
//
//        if(cardsInformation.getPlayer().equals("")) {
//            if(cardsInformation.getChallenger().equals(nickname)) {
//                System.out.println("[CardsInformationEvent] You are the challenger.\nChoose a number of cards equal to the number of players for this game.");
//                System.out.println("Cards: ");
//                List<CardInfo> cardList = cardsInformation.getCards();
//                cardList.forEach(x -> System.out.println("    - " + x.getName()));
//            }
//            else {
//                System.out.println("[CardsInformationEvent] Challenger is choosing cards for this game...");
//            }
//        }
//        else if(cardsInformation.getPlayer().equals(nickname)) {
//            System.out.println("[CardsInformationEvent] Select your Card.");
//            System.out.println("Cards:");
//            List<CardInfo> cardList = cardsInformation.getCards();
//            cardList.forEach(x -> System.out.println("    - " + x.getName()));
//        }
//        else {
//            System.out.println("[CardsInformationEvent] Another Player is choosing his/her Card. Wait...");
//        }
//    }
//
//    /**
//     * Method that will be called if a card is selected.
//     *
//     * @param cardSelected (CardSelectedEvent from server)
//     */
//    @Override
//    public void update(CardSelectedEvent cardSelected) {
////        ViewNickname.setMyCard(cardSelected.getCardName());
//
//        if(cardSelected.getPlayerNickname().equals(nickname)) {
//            if (cardSelected.success())
//                System.out.println("[CardSelectedEvent] You have correctly selected the card: " + cardSelected.getCardName());
//            else
//                System.out.println("[CardSelectedEvent] The Card you have selected is not valid!");
//        }
//        else {
//            if (cardSelected.success())
//                System.out.println("[CardSelectedEvent] Player " + cardSelected.getPlayerNickname() + " has correctly selected the card: " + cardSelected.getCardName());
//            else
//                System.out.println("[CardSelectedEvent] The Card Player " + cardSelected.getPlayerNickname() + " has selected is not valid!");
//        }
//    }
//
//    @Override
//    public void update(WorkerPlacedEvent workerPlaced) {
////        try {
////            ((ViewWorker)ViewWorker.search(workerPlaced.getWorker())).placeOn(workerPlaced.getX(), workerPlaced.getY());
////        } catch (NotFoundException | WrongViewObjectException e) {
////            ViewMessage.populateAndSend("Wrong placed recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
////        }
//        if(workerPlaced.success())
//            System.out.println("[WorkerPlacedEvent] Worker '" + workerPlaced.getWorker() + "' was correctly placed in position: ( " + workerPlaced.getX() + " , " + workerPlaced.getY() + " )");
//        else
//            System.out.println("[WorkerPlacedEvent] A Worker was not correctly placed!");
//    }
//
//    @Override
//    public void update(WorkerMovedEvent workerMoved) {
////        try {
////            ((ViewWorker)ViewWorker.search(workerMoved.getWorker())).placeOn(workerMoved.getFinalX(), workerMoved.getFinalY());
////        } catch (NotFoundException | WrongViewObjectException e) {
////            ViewMessage.populateAndSend("Wrong cell received", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
////        }
//        if(workerMoved.getMoveOutcome() == MoveOutcomeType.EXECUTED || workerMoved.getMoveOutcome() == MoveOutcomeType.TURN_SWITCHED || workerMoved.getMoveOutcome() == MoveOutcomeType.TURN_OVER || workerMoved.getMoveOutcome() == MoveOutcomeType.LOSS || workerMoved.getMoveOutcome() == MoveOutcomeType.WIN) {
//            System.out.println("[WorkerMovedEvent] Worker '" + workerMoved.getWorker() + "' was correctly moved:");
//            System.out.println("    Initial position: ( " + workerMoved.getInitialX() + " , " + workerMoved.getInitialY() + " )");
//            System.out.println("    Current position: ( " + workerMoved.getFinalX() + " , " + workerMoved.getFinalY() + " )");
//        }
//        else
//            System.out.println("[WorkerMovedEvent] Worker '" + workerMoved.getWorker() + "' movement is not valid!");
//    }
//
//    @Override
//    public void update(BlockBuiltEvent blockBuilt) {
////        ViewCell cell;
////        try {
////            cell = ((ViewBoard) ViewBoard.search(ViewBoard.getClassId())).getCellAt(blockBuilt.getX(), blockBuilt.getY());
////        } catch (NotFoundException | WrongViewObjectException e) {
////            ViewMessage.populateAndSend("Wrong cell recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
////            return;
////        }
////        if(blockBuilt.getBlockType().equals(PlaceableType.BLOCK))
////            cell.buildLevel();
////        else if(blockBuilt.getBlockType().equals(PlaceableType.DOME))
////            cell.buildDome();
////        else
////            ViewMessage.populateAndSend("Wrong block recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
//        if(blockBuilt.getMoveOutcome() == MoveOutcomeType.EXECUTED || blockBuilt.getMoveOutcome() == MoveOutcomeType.TURN_SWITCHED || blockBuilt.getMoveOutcome() == MoveOutcomeType.TURN_OVER || blockBuilt.getMoveOutcome() == MoveOutcomeType.LOSS || blockBuilt.getMoveOutcome() == MoveOutcomeType.WIN)
//            System.out.println("[BlockBuiltEvent] A '" + blockBuilt.getBlockType().toString() + "' was correctly built in place: ( " + blockBuilt.getX() + " , " + blockBuilt.getY() + " )");
//        else
//            System.out.println("[BlockBuiltEvent] '" + blockBuilt.getBlockType().toString() + "' was NOT built. Invalid BuildMove.");
//    }
//
//    @Override
//    public void update(BlockRemovedEvent blockRemoved) {
////        ViewCell cell;
////        try {
////            cell = ((ViewBoard) ViewBoard.search(ViewBoard.getClassId())).getCellAt(blockRemoved.getX(), blockRemoved.getY());
////        } catch (NotFoundException | WrongViewObjectException e) {
////            ViewMessage.populateAndSend("Wrong cell recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
////            return;
////        }
////        if(cell.isDoomed())
////            cell.removeDome();
////        else
////            cell.removeLevel();
//        System.err.println("[BlockRemovedEvent] A block was correctly removed. (This kind of event SHALL NOT be displayed!)");
//    }
//
//    @Override
//    public void update(WorkerRemovedEvent workerRemoved) {
////        try {
////            ((ViewWorker)ViewWorker.search(workerRemoved.getWorker())).removeWorker();
////        } catch (NotFoundException | WrongViewObjectException e) {
////            ViewMessage.populateAndSend("Cannot remove worker", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
////        }
//        System.out.println("[WorkerRemovedEvent] Worker '" + workerRemoved.getWorker() + "' was correctly removed from position: ( " + workerRemoved.getX() + " , " + workerRemoved.getY() + " )");
//    }
//
//    @Override
//    public void update(RequireStartPlayerEvent requireStartPlayer) {
//        if(requireStartPlayer.getChallenger().equals(nickname)) {
//            System.out.println("[RequireStartPlayerEvent] You are the challenger.\nChoose the Start Player: ");
//            List<String> players = requireStartPlayer.getPlayers();
//            players.forEach(x -> System.out.println("    - " + x));
//        }
//        else
//            System.out.println("Challenger is choosing the Start Player. Wait...");
//    }
//
//    @Override
//    public void update(RequirePlaceWorkersEvent requirePlaceWorkers) {
//        if(requirePlaceWorkers.getPlayer().equals(nickname)) {
//            System.out.println("[RequirePlaceWorkersEvent] Place worker.");
//        }
//        else
//            System.out.println("Other players are placing their Workers. Wait...");
//    }
//
//    @Override
//    public void update(WorkerSelectedEvent workerSelected) {
//        if(workerSelected.getPlayerNickname().equals(nickname)) {
//            if(workerSelected.success())
//                System.out.println("[WorkerSelectedEvent] Worker '" + workerSelected.getWorker() + "' was correctly SELECTED.");
//            else
//                System.out.println("[WorkerSelectedEvent] Worker '" + workerSelected.getWorker() + "' CANNOT BE SELECTED!");
//        }
//    }
//
//    @Override
//    public void update(UndoOkEvent undoOk) {
//        System.out.println("[UndoOkEvent] Previous move was undone.");
//    }
//
//
//    @Override
//    public void update(Object o) {
////        ViewMessage.populateAndSend("Recived a wrong message", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
//        System.err.println("[ERROR] Received a generic Object (UNEXPECTED!)");
//    }


    /* ########## AUXILIARY TEST METHODS ########## */
//    private void printGameInfo() {
//        if(!View.debugging)
//            return;
//        System.out.println("Players list and their Workers: ");
//        for (String player : players) {
//            System.out.println(player);
//            List<String> workers = workersToPlayer.get(player);
//            for (String worker : workers)
//                System.out.println("     - " + worker);
//        }
//    }

    /**
     * Helper inner class aimed to handle chat messages received
     * form the connection socket.
     *
     * @author AndreaAltomare
     */
    private class ChatMessageReceiver implements ChatMessageListener {

        /**
         * Method called on the arrival of a  ChatMessageEvent from Server
         * to notify it to all the Viewers which need it.
         *
         * @param chatMessage (Chat message).
         */
        @Override
        public synchronized void update(ChatMessageEvent chatMessage) {
            if(View.debugging){
                System.out.println("Recived Message");
            }
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    if(View.debugging){
                        System.out.println(chatMessage);
                    }

                    PlayerMessages.addMsg(chatMessage);
                }
            });
        }
    }

    /**
     * Wrapper method used to send Events to the server.
     *
     * @param o (Event to be send to the server)
     */
    @Override
    public void send(Object o){
        notify(o);
    }
}
