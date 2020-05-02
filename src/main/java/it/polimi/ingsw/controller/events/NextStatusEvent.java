package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: The application has moved into the next status.
 * [MVEvent]
 */
public class NextStatusEvent extends EventObject {
    private String message; // Message accompanying the event

    public NextStatusEvent(String message) {
        super(new Object());
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
