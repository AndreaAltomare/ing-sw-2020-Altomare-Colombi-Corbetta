package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.interfaces.*;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.StateType;
import it.polimi.ingsw.observer.GeneralListener;
import it.polimi.ingsw.observer.Observable;
//implements ClientGeneralListener, MoveListener, TurnStatusChangeListener, CardSelectionListener

/**
 * Game Controller.
 *
 * @author AndreaAltomare
 */
public class Controller extends Observable<Object> implements GeneralListener, ClientGeneralListener, MoveListener, TurnStatusChangeListener, CardSelectionListener {
    private Model model; // Object reference to the Game Model

    // TODO: Write Javadoc here to explain, for example, why a Model object is passed by argument
    public Controller(Model model) {
        this.model = model;
    }

    @Override
    public void onCardSelection(String cardName, String playerNickname) {
        // todo instantiate card for the player who choose it
    }

    @Override
    public void onNicknameSubmit(String nickname) {
        // todo useless: to remove here
    }

    @Override
    public void onPlayerQuit(String playerNickname) {
        // todo if the player has lost, just remove it from the players list
    }

    @Override
    public void viewRequestData() {
        // todo code: maybe require a broadcast data forwarding
    }

    @Override
    public void onWorkerMovement(String worker, int x, int y) {
        // todo handle the case in which the worker does not belong to this player
    }

    @Override
    public void onWorkerConstruction(String worker, int x, int y) {
        // todo same here as onWorkerMovement(...) method
    }

    @Override
    public void onTurnStatusChange(StateType turnStatus) {
        // todo change in: public void onTurnStatusChange(EventObject StatusChangeEvent e, String playerNickname);
    }

    /**
     * General update() method for Observer Pattern.
     * Receive generic messages from VirtualView.
     *
     * @param o (Object message)
     */
    @Override
    public void update(Object o) {
        // todo code
        System.out.println("Received a generic message: " + (String)o);
    }

    /**
     * General update() method for Observer Pattern.
     * Receive messages from a specific Player's VirtualView.
     *
     * @param o (Object message)
     * @param playerNickname (Player's unique nickname)
     */
    @Override
    public void update(Object o, String playerNickname) {
        System.out.println("Received a message from: " + playerNickname + "\nIt says: " + (String)o);
    }
}
