package it.polimi.ingsw.model.exceptions;

import it.polimi.ingsw.model.player.Player;

/**
 * Specialized Exception to notify when a Player
 * loses.
 *
 * @author AndreaAltomare
 */
public class LoseException extends Exception {
    private Player loserPlayer;

    /**
     * Default message constructor
     *
     * @param loser (Loser Player)
     */
    public LoseException(Player loser) {
        super("Player " + loser.getNickname() + "has lost!");
        loserPlayer = loser;
    }

    /**
     * Custom message constructor
     *
     * @param loser (Loser Player)
     * @param message (Custom message)
     */
    public LoseException(Player loser, String message) {
        super(message);
        loserPlayer = loser;
    }
}
