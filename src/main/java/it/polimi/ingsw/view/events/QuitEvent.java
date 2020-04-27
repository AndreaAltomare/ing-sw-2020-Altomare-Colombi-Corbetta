package it.polimi.ingsw.view.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Player wants to quit the game.
 * [VCEvent]
 */
public class QuitEvent extends EventObject {
    // TODO inserire attributi

    public QuitEvent(Object o) {
        super(o);
    }
}
