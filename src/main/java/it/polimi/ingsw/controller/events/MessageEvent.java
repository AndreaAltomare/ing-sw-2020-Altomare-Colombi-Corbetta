package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: General message from Controller (Server).
 * [MVEvent]
 */
public class MessageEvent extends EventObject {
    private final String message;

    /**
     * Constructs a MessageEvent to inform the View about the event occurred.
     * Usually general information are sent through this Event.
     *
     * @param message Custom message
     */
    public MessageEvent(String message) {
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
