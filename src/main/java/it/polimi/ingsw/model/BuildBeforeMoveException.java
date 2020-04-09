package it.polimi.ingsw.model;

/**
 * Specialized Exception to notify when a Player
 * tries to make a Construction before making a Movement
 *
 * @author AndreaAltomare
 */
public class BuildBeforeMoveException extends Exception {

    /**
     * Default message constructor
     */
    public BuildBeforeMoveException() {
        super("Cannot build now! Must move a Worker first.");
    }

    /**
     * Custom message constructor
     *
     * @param message (Custom message)
     */
    public BuildBeforeMoveException(String message) {
        super(message);
    }
}
