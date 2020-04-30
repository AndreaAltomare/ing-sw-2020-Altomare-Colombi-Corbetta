package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Worker has been correctly selected.
 * [MVEvent]
 */
public class WorkerSelectedEvent extends EventObject {
    // TODO: 30/04/20 String playerName 
    // TODO: 30/04/20 String worker 

    public WorkerSelectedEvent(Object o) {
        super(o);
    }
}
