package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Require the number of Players for the upcoming game.
 * [MVEvent]
 */
public class RequirePlayersNumberEvent extends EventObject {
    private final String message;

    public RequirePlayersNumberEvent() {
        super(new Object());
        this.message = "You are the first player!\n\nChoose the number of player for this game (you included).\nType 2 or 3";
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
