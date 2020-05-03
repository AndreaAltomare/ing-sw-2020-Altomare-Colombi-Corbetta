package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.model.StateType;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.VCEventListener;
import it.polimi.ingsw.view.events.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Game Controller.
 *
 * @author AndreaAltomare
 */
public class Controller extends Observable<Object> implements VCEventListener {
    private Model model; // Object reference to the Game Model

    // TODO: Write Javadoc here to explain, for example, why a Model object is passed by argument
    public Controller(Model model) {
        this.model = model;
    }

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
        System.out.println("QuitEvent received from Player " + playerNickname + ": quitting...");
    }

    @Override
    public synchronized void update(ViewRequestDataEvent dataRequest, String playerNickname) {
        System.out.println("ViewRequestDataEvent received form Player: " + playerNickname);

        /* ANSWER FROM THE CONTROLLER (Notify the View) */
        // Board size data preparation
        int boardXSize = 5;
        int boardYSize = 5;
        // Players data preparation
        List<String> players = new ArrayList<>();
        players.add(playerNickname);
        players.add("Tizio");
        players.add("Caio");
        // Workers associated to Players data preparation
        Map<String, List<String>> workersToPlayer = new HashMap<>();
        List<String> workers1 = new ArrayList<>();
        workers1.add("Worker1@" + playerNickname); // with this string format we ensure also strings are correctly sent.
        workers1.add("Worker2@" + playerNickname);
        List<String> workers2 = new ArrayList<>();
        workers2.add("Worker1@Tizio");
        workers2.add("Worker2@Tizio");
        List<String> workers3 = new ArrayList<>();
        workers3.add("Worker1@Caio");
        workers3.add("Worker2@Caio");
        workersToPlayer.put(playerNickname,workers1);
        workersToPlayer.put("Tizio",workers2);
        workersToPlayer.put("Caio",workers3);
        notify(new ServerSendDataEvent(boardXSize,boardYSize,players,workersToPlayer));
    }


    /* Card selection listener */
    @Override
    public synchronized void update(CardSelectionEvent card, String playerNickname) {
        System.out.println("CardSelectionEvent received form Player: " + playerNickname);
        System.out.println("Card selected: " + card); // todo a questo punto check se il metodo overridden toString() funziona. Inoltre, se funziona questo metodo, vuol dire che sicuramente la serializzazione/deserializzazione e relativi metodi funzionano correttamente

        /* ANSWER FROM THE CONTROLLER (Notify the View) */
        notify(new CardSelectedEvent(card.getCardName(), playerNickname));
    }


    /* Move listener */
    @Override
    public synchronized void update(PlaceWorkerEvent workerToPlace, String playerNickname) {
        System.out.println("PlaceWorkerEvent received form Player: " + playerNickname);

        /* ANSWER FROM THE CONTROLLER (Notify the View) */
        notify(new WorkerPlacedEvent("worker1@" + playerNickname, workerToPlace.getX(),workerToPlace.getY()));
    }

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
        notify(new TurnStatusChangedEvent(playerNickname, turnStatus.getTurnStatus()));
    }

    /* Generic update method */
    @Override
    public synchronized void update(Object o) {
        // todo: maybe these statements are to remove
        System.out.println("Generic VCEvent received");

        /* ANSWER FROM THE CONTROLLER (Notify the View) */
        notify(new MessageEvent("Received a generic Object from your Client"));
    }


























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
