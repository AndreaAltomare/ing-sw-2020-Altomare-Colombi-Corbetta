package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Require the Start Player for this game.
 * [MVEvent]
 */
public class RequirePlaceWorkersEvent extends EventObject {
    private final String message;

    public RequirePlaceWorkersEvent() {
        super(new Object());
        this.message = "Place your two workers on the board.";
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
