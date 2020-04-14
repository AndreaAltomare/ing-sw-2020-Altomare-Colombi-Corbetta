package it.polimi.ingsw.model;

/**
 * Specialized Exception to notify when a Player
 * runs out of moves for a specific State.
 *
 * @author AndreaAltomare
 */
public class RunOutMovesException extends Exception {
    private StateType state;

    /**
     * Default message constructor
     *
     * @param state (Player's Turn State whose moves run out)
     */
    public RunOutMovesException(StateType state) {
        super("Cannot perform " + state + "moves! Type of moves has run out.");
        this.state = state;
    }

    /**
     * Custom message constructor
     *
     * @param state (Player's Turn State whose moves run out)
     * @param message (Custom message)
     */
    public RunOutMovesException(StateType state, String message) {
        super(message);
        this.state = state;
    }
}
