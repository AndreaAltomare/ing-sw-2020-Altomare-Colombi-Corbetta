package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Worker was correctly placed.
 * [MVEvent]
 */
public class WorkerPlacedEvent extends EventObject {
    // TODO: 30/04/20 String worker 
    // TODO: 30/04/20 cell: int x, int y 

    public WorkerPlacedEvent(Object o) {
        super(o);
    }
}
