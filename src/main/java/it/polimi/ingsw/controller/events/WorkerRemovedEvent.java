package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Worker was removed successfully.
 * [MVEvent]
 */
public class WorkerRemovedEvent extends EventObject {
    // TODO per Giorgio: Inserire i todo per indicare gli attributi necessari

    public WorkerRemovedEvent(Object o) {
        super(o);
    }
}
