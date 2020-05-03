package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Player has submitted the number of Players for the upcoming game.
 * [VCEvent]
 */
public class SetPlayersNumberEvent extends EventObject {
    private int numberOfPlayers;

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
