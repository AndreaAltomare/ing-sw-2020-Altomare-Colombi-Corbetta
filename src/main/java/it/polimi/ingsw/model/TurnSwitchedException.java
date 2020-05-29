package it.polimi.ingsw.model;

/**
 * Specialized Exception to notify when a Player's
 * Turn needs to be switched.
 *
 * @author AndreaAltomare
 */
public class TurnSwitchedException extends Exception {

    /**
     * Default message constructor
     */
    public TurnSwitchedException() {
        super("Player's Turn has switched.");
    }

    /**
     * Custom message constructor
     *
     * @param message (Custom message)
     */
    public TurnSwitchedException(String message) {
        super(message);
    }
}
