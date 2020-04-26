package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.observer.GeneralListener;
import it.polimi.ingsw.observer.Observable;
//implements ClientGeneralListener, MoveListener, TurnStatusChangeListener, CardSelectionListener

/**
 * Game Controller.
 *
 * @author AndreaAltomare
 */
public class Controller extends Observable<Object> implements GeneralListener {
    private Model model; // Object reference to the Game Model

    // TODO: Write Javadoc here to explain, for example, why a Model object is passed by argument
    public Controller(Model model) {
        this.model = model;
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
