package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: The application has moved into the next status.
 * [MVEvent]
 */
public class NextStatusEvent extends EventObject {
    // TODO per Giorgio: Inserire i todo per indicare gli attributi necessari

    public NextStatusEvent(Object o) {
        super(o);
    }
}
