package it.polimi.ingsw.model.exceptions;

/**
 * Specialized Exception to notify when a Player's
 * Turn is over.
 *
 * @author AndreaAltomare
 */
public class TurnOverException extends Exception {

    /**
     * Default message constructor
     */
    public TurnOverException() {
        super("Player's Turn is over.");
    }

    /**
     * Custom message constructor
     *
     * @param message (Custom message)
     */
    public TurnOverException(String message) {
        super(message);
    }
}
