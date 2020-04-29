package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Player wants to quit the game.
 * [VCEvent]
 */
public class QuitEvent extends EventObject {

    public QuitEvent() {
        super(new Object());
    }
}
