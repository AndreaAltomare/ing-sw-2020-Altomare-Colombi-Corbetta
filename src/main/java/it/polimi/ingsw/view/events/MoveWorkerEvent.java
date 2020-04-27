package it.polimi.ingsw.view.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Player wants to make a Movement with a Worker.
 * [VCEvent]
 */
public class MoveWorkerEvent extends EventObject {
    // TODO inserire attributi

    public MoveWorkerEvent(Object o) {
        super(o);
    }
}
