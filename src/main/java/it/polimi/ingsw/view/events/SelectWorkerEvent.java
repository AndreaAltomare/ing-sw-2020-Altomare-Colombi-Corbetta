package it.polimi.ingsw.view.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Player has selected a Worker to make a move.
 * [VCEvent]
 */
public class SelectWorkerEvent extends EventObject {
    // TODO inserire attributi

    public SelectWorkerEvent(Object o) {
        super(o);
    }
}
