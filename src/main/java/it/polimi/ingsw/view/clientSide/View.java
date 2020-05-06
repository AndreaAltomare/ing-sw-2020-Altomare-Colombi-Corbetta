package it.polimi.ingsw.view.clientSide;

import it.polimi.ingsw.connection.client.ClientConnection;
import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.model.StateType;
import it.polimi.ingsw.observer.MVEventListener;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.events.*;
import it.polimi.ingsw.view.serverSide.ClientStatus;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class View extends Observable<Object> implements MVEventListener, Runnable { // todo: maybe this class extends Observable<Object> for proper interaction with Network Handler
    Thread tConnection;
    private ClientConnection connection;
    private Scanner in; // Scanner unique reference
    private String input; // unique input
    private String nickname; // Player's nickname
    private boolean connectionActive;

    public View(Scanner in, ClientConnection connection) {
        this.in = in;
        this.connection = connection;
    }

    // TODO: con questo sistema viene verificato anche che la comunicazione con Pattern Observer funzioni correttamente su thread diversi
    // TODO: ricordarsi che il metodo run() gira su un thread differente rispetto quello dove "girano" i metodi update() --> i metodi update() vengono chiamati sullo stesso thread di client.ClientConnection
    @Override
    public void run() {
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

    /* Server general listener */
    @Override
    public void update(NextStatusEvent nextStatus) {
        // TESTED ONLY WHEN IT'S TIME TO SUBMIT THE NICKNAME
        System.out.println(nextStatus); // todo: test also if overridden method toString() works correctly.
    }

    @Override
    public void update(ServerSendDataEvent serverSentData) {
        System.out.println("Server has sent data requested:");
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


    /* Game preparation listener */
    @Override
    public void update(WorkerPlacedEvent workerPlaced) {
        System.out.println("Worker named '" + workerPlaced.getWorker() + "' was PLACED correctly in position: (" + workerPlaced.getX() + " , " + workerPlaced.getY() + ")");
    }

    @Override
    public void update(CardSelectedEvent cardSelected) {
        System.out.println("Card " + cardSelected.getCardName() + " has been selected correctly.");
    }


    /* Move executed listener */
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


    /* Message listener */
    @Override
    public void update(MessageEvent message) {
        System.out.println("MESSAGE: " + message);
    }


    /* Error message listener */
    @Override
    public void update(ErrorMessageEvent message) {
        System.out.println("ERROR MESSAGE: " + message);
    }


    /* Generic update method */
    @Override
    public void update(Object o) {
        System.out.println("A generic Object was received.");
    }



    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isConnectionActive() {
        return connectionActive;
    }

    public void setConnectionActive(boolean connectionActive) {
        this.connectionActive = connectionActive;
    }
}
