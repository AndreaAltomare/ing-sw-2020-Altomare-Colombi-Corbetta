package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: The application has moved into the next status.
 * [MVEvent]
 */
public class NextStatusEvent extends EventObject {

    public NextStatusEvent() {
        super(new Object());
    }
}
