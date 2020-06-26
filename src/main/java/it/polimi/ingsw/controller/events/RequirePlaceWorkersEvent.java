package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Require the Start Player for this game.
 * [MVEvent]
 */
public class RequirePlaceWorkersEvent extends EventObject {
    private String player; // Player who is asked to place his/her own Workers to.
    private final String message;

    /**
     * Constructs a RequirePlaceWorkersEvent to inform the View about the event occurred.
     *
     * @param player Player who is asked to place his/her own Workers to
     */
    public RequirePlaceWorkersEvent(String player) {
        super(new Object());
        this.message = "Place your two workers on the board.";
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
