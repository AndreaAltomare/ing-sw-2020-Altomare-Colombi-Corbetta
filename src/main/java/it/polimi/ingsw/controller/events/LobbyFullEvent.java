package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Lobby is full. No other players are allowed in.
 * [MVEvent]
 */
public class LobbyFullEvent extends EventObject {
    private final String message;

    /**
     * Constructs a LobbyFullEvent to inform the View about the event occurred.
     */
    public LobbyFullEvent() {
        super(new Object());
        this.message = "Lobby is full! No other players are allowed in until a new game starts.";
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
