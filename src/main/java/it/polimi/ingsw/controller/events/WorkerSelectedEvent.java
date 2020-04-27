package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Worker has been correctly selected.
 * [MVEvent]
 */
public class WorkerSelectedEvent extends EventObject {
    // TODO per Giorgio: Inserire i todo per indicare gli attributi necessari

    public WorkerSelectedEvent(Object o) {
        super(o);
    }
}
