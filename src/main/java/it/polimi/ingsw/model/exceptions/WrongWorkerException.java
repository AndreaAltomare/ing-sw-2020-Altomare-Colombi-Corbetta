package it.polimi.ingsw.model.exceptions;

/**
 * Specialized Exception to notify when a Player
 * wants to make a Move with a Worker he/she has
 * not chosen at the Turn's start.
 *
 * @author AndreaAltomare
 */
public class WrongWorkerException extends Exception {

    /**
     * Default message constructor
     */
    public WrongWorkerException() {
        super("Cannot perform a Move with this Worker! You must choose the worker you started this Turn with.");
    }

    /**
     * Custom message constructor
     *
     * @param message (Custom message)
     */
    public WrongWorkerException(String message) {
        super(message);
    }
}
