package it.polimi.ingsw.view.clientSide;

import it.polimi.ingsw.connection.client.ClientConnection;
import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.model.CardInfo;
import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.model.StateType;
import it.polimi.ingsw.observer.MVEventListener;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.events.*;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class View extends Observable<Object> implements MVEventListener, Runnable { // todo: maybe this class extends Observable<Object> for proper interaction with Network Handler
    Thread tConnection;
    private ClientConnection connection;
    private Scanner in; // Scanner unique reference
    private String input; // unique input
    private String nickname; // Player's nickname
    private boolean connectionActive;

    //Per fare debugging
    public static final boolean debugging = true;

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
                    //notify(new SetNicknameEvent(input)); // submit nickname
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
        /*ViewWorker myWorker;

        if(workerPlaced.success()) {
            try {
                myWorker = new ViewWorker(workerPlaced.getWorker(), ViewSubTurn.getActual().getPlayer());
                myWorker.placeOn(workerPlaced.getX(), workerPlaced.getY());
                if(View.debugging)
                    System.out.println(workerPlaced.getWorker() + "(" + workerPlaced.getX()+":"+workerPlaced.getY()+")");
            } catch (NotFoundException | WrongViewObjectException e) {
                ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
                e.printStackTrace();
            }
        }else if((ViewNickname.getMyNickname()).equals(ViewSubTurn.getActual().getPlayer())){
            ViewMessage.populateAndSend("Cannot place theere your worker. retry", ViewMessage.MessageType.FROM_SERVER_MESSAGE);
        }
        Viewer.setAllRefresh();*/
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

            if(!turnStatusChange.getPlayerNickname().equals(ViewSubTurn.getActual().getPlayer())){
                ViewBoard.getBoard().setSelectedCell(null);
                ViewWorker.selectWorker((ViewWorker) null);
            }

            //Checks if it's a change of status
            if(!turnStatusChange.getPlayerNickname().equals(ViewSubTurn.getActual().getPlayer())){
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

        /*ViewSubTurn.set(turnStatusChange.getState().toString(), turnStatusChange.getPlayerNickname());
        ViewSubTurn.getActual().setPlayer(turnStatusChange.getPlayerNickname());
        Viewer.setAllSubTurnViewer(ViewSubTurn.getActual().getSubViewer());
        //System.out.println("Turn status changed to: " + turnStatusChange.getState().toString());*/
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
        /*if(View.debugging)
            System.out.println("[FROM SERVER] " + cardSelected.getPlayerNickname() + " selected: " + cardSelected.getCardName());*/

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
        /*try {
            ((ViewWorker)ViewWorker.search(workerMoved.getWorker())).placeOn(workerMoved.getFinalX(), workerMoved.getFinalY());
        } catch (NotFoundException | WrongViewObjectException e) {
            ViewMessage.populateAndSend("Wrong cell recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
        }*/
    }

    @Override
    public void update(BlockBuiltEvent blockBuilt) {
        try {
            ViewCell.populate(blockBuilt);
        } catch (WrongEventException e) {
            ViewMessage.populateAndSend("Wrong message recived recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
        }
        Viewer.setAllRefresh();
        /*ViewCell cell;
        try {
            cell = ((ViewBoard) ViewBoard.search(ViewBoard.getClassId())).getCellAt(blockBuilt.getX(), blockBuilt.getY());
        } catch (NotFoundException | WrongViewObjectException e) {
            ViewMessage.populateAndSend("Wrong cell recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
            return;
        }
        if(blockBuilt.getBlockType().equals(PlaceableType.BLOCK))
            cell.buildLevel();
        else if(blockBuilt.getBlockType().equals(PlaceableType.DOME))
            cell.buildDome();
        else
            ViewMessage.populateAndSend("Wrong block recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);*/
    }

    /**
     * Method that signals that a player wins.
     *
     * @param playerWin (PlayerWinEvent from the server)
     */
    @Override
    public void update(PlayerWinEvent playerWin) {
        if (ViewNickname.getMyNickname().equals(playerWin.getPlayerNickname())) {
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
        if (ViewNickname.getMyNickname().equals(playerLose.getPlayerNickname())) {
            ViewMessage.populateAndSend(playerLose.getMessage(), ViewMessage.MessageType.LOOSE_MESSAGE);
        }
    }


    //##################SOON TESTED####################


    @Override
    public void update(BlockRemovedEvent blockRemoved) {
        try {
            ViewCell.populate(blockRemoved);
        } catch (WrongEventException e) {
            ViewMessage.populateAndSend("Wrong message recived recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
        }
        /*ViewCell cell;
        try {
            cell = ((ViewBoard) ViewBoard.search(ViewBoard.getClassId())).getCellAt(blockRemoved.getX(), blockRemoved.getY());
        } catch (NotFoundException | WrongViewObjectException e) {
            ViewMessage.populateAndSend("Wrong cell recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
            return;
        }
        if(cell.isDoomed())
            cell.removeDome();
        else
            cell.removeLevel();*/
    }

    @Override
    public void update(WorkerRemovedEvent workerRemoved) {
        try {
            ViewWorker.populate(workerRemoved);
        } catch (WrongEventException e) {
            ViewMessage.populateAndSend("Wrong message recived recived", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
        }
/*
        try {
            ((ViewWorker)ViewWorker.search(workerRemoved.getWorker())).removeWorker();
        } catch (NotFoundException | WrongViewObjectException e) {
            ViewMessage.populateAndSend("Cannot remove worker", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
        }*/
    }


    //##################NOT YET TESTED####################

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
        // TODO: close connection, and gracefully close the whole application
    }



    @Override
    public void update(GameOverEvent gameOver) {

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
        // todo add code to handle disconnection
    }

    //#########################END UPDATE###################################

    public String getNickname() {
        String ret = ViewNickname.getMyNickname();
        return ret==null?"":ret;
    }

    //todo: serve?
    public void setNickname(String nickname) { throw new NullPointerException("Questo metodo non dovrebbe essere chiamato"); }

    public boolean isConnectionActive() {
        return connectionActive;
    }

    public void setConnectionActive(boolean connectionActive) {
        this.connectionActive = connectionActive;
    }






    @Override
    public void update(RequireStartPlayerEvent requireStartPlayer) {

    }



    @Override
    public void update(WorkerSelectedEvent workerSelected) {
        if (workerSelected.success()){
            try {
                ViewWorker.populate(workerSelected);
            } catch (WrongEventException ignore) {
            }
        }
        System.out.println("Worker named '" + workerSelected.getWorker() + "was correctly SELECTED");
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
