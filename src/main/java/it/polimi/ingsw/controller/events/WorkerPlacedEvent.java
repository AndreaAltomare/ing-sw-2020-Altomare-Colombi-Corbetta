package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Worker was correctly placed.
 * [MVEvent]
 */
public class WorkerPlacedEvent extends EventObject {
    // TODO per Giorgio: Inserire i todo per indicare gli attributi necessari

    public WorkerPlacedEvent(Object o) {
        super(o);
    }
}
