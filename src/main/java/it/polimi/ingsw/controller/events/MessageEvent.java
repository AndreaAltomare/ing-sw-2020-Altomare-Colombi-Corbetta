package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: General message from Controller (Server).
 * [MVEvent]
 */
public class MessageEvent extends EventObject {
    private final String message;

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
