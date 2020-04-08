package it.polimi.ingsw.model;

/**
 * Specialized Exception to notify when a Player
 * wins, and thus to stop the game.
 *
 * @author AndreaAltomare
 */
public class WinException extends Exception {
    private Player winnerPlayer;

    /**
     * Default message constructor
     *
     * @param winner (Winner Player)
     */
    public WinException(Player winner) {
        super("Player " + winner.getNickname() + "has won!");
        winnerPlayer = winner;
    }

    /**
     * Custom message constructor
     *
     * @param winner (Winner Player)
     * @param message (Custom message)
     */
    public WinException(Player winner, String message) {
        super(message);
        winnerPlayer = winner;
    }
}
