package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: The game is over.
 * [MVEvent]
 */
public class GameOverEvent extends EventObject {
    private final String message;

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
