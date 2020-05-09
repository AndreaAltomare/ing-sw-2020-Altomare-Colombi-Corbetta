package it.polimi.ingsw.controller.events;

import java.util.EventObject;
import java.util.List;

/**
 * Event: Require the Start Player for this game.
 * [MVEvent]
 */
public class RequireStartPlayerEvent extends EventObject {
    private final String message;
    private List<String> players;

    public RequireStartPlayerEvent(List<String> players) {
        super(new Object());
        this.message = "Choose the Start Player for this game";
        this.players = players;
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
