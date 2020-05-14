package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Require the Start Player for this game.
 * [MVEvent]
 */
public class RequirePlaceWorkersEvent extends EventObject {
    private String player; // Player who is asked to place his/her own Workers to.
    private final String message;

    public RequirePlaceWorkersEvent(String player) {
        super(new Object());
        this.message = "Place your two workers on the board."; // TODO: maybe it's to remove (useless)
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
