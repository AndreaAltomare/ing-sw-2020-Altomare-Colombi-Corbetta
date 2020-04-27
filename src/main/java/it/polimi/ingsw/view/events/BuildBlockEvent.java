package it.polimi.ingsw.view.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Player wants to make a Construction with a Worker.
 * [VCEvent]
 */
public class BuildBlockEvent extends EventObject {
    // TODO inserire attributi

    public BuildBlockEvent(Object o) {
        super(o);
    }
}
