package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Player has submitted the number of Players for the upcoming game.
 * [VCEvent]
 */
public class SetPlayersNumberEvent extends EventObject {
    private int numberOfPlayers;

    /**
     * Constructs a SetPlayersNumberEvent to notify the server the number
     * of player for this game chosen by the Player.
     *
     * @param numberOfPlayers (the number of players for this game).
     */
    public SetPlayersNumberEvent(int numberOfPlayers) {
        super(new Object());
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
}
