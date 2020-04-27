package it.polimi.ingsw.view.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Player wants to remove a Worker.
 * [VCEvent]
 */
public class RemoveWorkerEvent extends EventObject {
    // TODO inserire attributi

    public RemoveWorkerEvent(Object o) {
        super(o);
    }
}
