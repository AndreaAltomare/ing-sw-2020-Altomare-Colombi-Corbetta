package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: The game is over.
 * [MVEvent]
 */
public class GameOverEvent extends EventObject {
    private final String message;

    /**
     * Constructs a GameOverEvent to inform the View about the event occurred.
     *
     * @param message Custom message
     */
    public GameOverEvent(String message) {
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
