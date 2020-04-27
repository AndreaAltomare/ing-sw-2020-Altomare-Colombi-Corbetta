package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Worker has been successfully moved.
 * [MVEvent]
 */
public class WorkerMovedEvent extends EventObject {
    // TODO per Giorgio: Inserire i todo per indicare gli attributi necessari

    public WorkerMovedEvent(Object o) {
        super(o);
    }
}
