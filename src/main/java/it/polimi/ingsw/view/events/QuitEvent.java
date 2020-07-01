package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Player wants to quit the game.
 * [VCEvent]
 */
public class QuitEvent extends EventObject {

    /**
     * Constructs a QuitEvent to notify the Server the intention of the player to quit.
     */
    public QuitEvent() {
        super(new Object());
    }
}
