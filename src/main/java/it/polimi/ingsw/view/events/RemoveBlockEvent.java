package it.polimi.ingsw.view.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Player wants to remove a block.
 * [VCEvent]
 */
public class RemoveBlockEvent extends EventObject {
    // TODO inserire attributi

    public RemoveBlockEvent(Object o) {
        super(o);
    }
}
