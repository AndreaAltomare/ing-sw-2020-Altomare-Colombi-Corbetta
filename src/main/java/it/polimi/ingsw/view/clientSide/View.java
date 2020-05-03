package it.polimi.ingsw.view.clientSide;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.model.StateType;
import it.polimi.ingsw.observer.MVEventListener;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.events.*;
import it.polimi.ingsw.view.serverSide.ClientStatus;

import java.util.List;
import java.util.Scanner;

public class View extends Observable<Object> implements MVEventListener, Runnable { // todo: maybe this class extends Observable<Object> for proper interaction with Network Handler
    private String nickname; // Player's nickname
    private boolean connectionActive;

    // TODO: con questo sistema viene verificato anche che la comunicazione con Pattern Observer funzioni correttamente su thread diversi
    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        String input;

        System.out.println("Welcome! Type something.");
        input = in.nextLine();
        while(!input.equals("quit")) {
            switch (input) {
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
        }

        notify(new QuitEvent());
        // TODO: close connection, and gracefully close the whole application
    }

    /* Server general listener */
    @Override
    public void update(ClientStatus clientStatus) {} // TODO: questo metodo non è già coperto da un evento apposito?

    @Override
    public void update(NextStatusEvent nextStatus) {
        // TESTED ONLY WHEN IT'S TIME TO SUBMIT THE NICKNAME
        Scanner in = new Scanner(System.in);
        System.out.println(nextStatus); // todo: test also if overridden method toString() works correctly.
        String nickname = in.nextLine();

        notify(new SetNicknameEvent(nickname)); // submit nickname
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
        Scanner in = new Scanner(System.in);
        System.out.println(invalidNickname);
        String nickname = in.nextLine();

        notify(new SetNicknameEvent(nickname)); // submit nickname
    }

    @Override
    public void update(RequirePlayersNumberEvent requirePlayersNumber) {
        Scanner in = new Scanner(System.in);
        System.out.println(requirePlayersNumber);
        int numberOfPlayers = in.nextInt();

        notify(numberOfPlayers); // by this way we ensure auto-boxing from int to Object type works correctly
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
        System.out.println("Turn status changed to: " + turnStatusChange.getState().toString()); // todo: check what toString() of an enum prints...
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
        System.out.println("Worker named '" + workerSelected.getWorker());
    }


    /* Message listener */
    @Override
    public void update(MessageEvent message) {
        System.out.println("A message was received. It says: '" + message + "'");
    }


    /* Error message listener */
    @Override
    public void update(ErrorMessageEvent message) {
        System.out.println("An ERROR message was received. It says: '" + message + "'");
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
