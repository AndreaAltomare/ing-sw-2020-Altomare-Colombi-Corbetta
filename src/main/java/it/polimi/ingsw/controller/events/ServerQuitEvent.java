package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Server has closed the connection.
 * [MVEvent]
 */
public class ServerQuitEvent extends EventObject {
    private final String message;

    /**
     * Constructs a ServerQuitEvent.
     * By providing a message, it lets the Client know
     * if the connection was either closed on Client's request
     * or by the Server itself.
     *
     * @param message (Message to send to the Client before closing the connection)
     */
    public ServerQuitEvent(String message) {
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
