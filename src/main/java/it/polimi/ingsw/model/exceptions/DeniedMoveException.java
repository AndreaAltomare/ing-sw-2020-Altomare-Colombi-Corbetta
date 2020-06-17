package it.polimi.ingsw.model.exceptions;

/**
 * Specialized Exception to notify when a move
 * is not allowed.
 *
 * @author AndreaAltomare
 */
public class DeniedMoveException extends Exception {

    /**
     * Default message constructor
     */
    public DeniedMoveException() {
        super("Move not allowed!");
    }

    /**
     * Custom message constructor
     *
     * @param message (Custom message)
     */
    public DeniedMoveException(String message) {
        super(message);
    }
}
