package it.polimi.ingsw.controller.events;

import java.util.EventObject;
import java.util.List;

/**
 * Event: Require the Start Player for this game.
 * [MVEvent]
 */
public class RequireStartPlayerEvent extends EventObject {
    private String challenger; // Challenger's nickname
    private final String message;
    private List<String> players;

    /**
     * Constructs a RequireStartPlayerEvent to inform the View about the event occurred.
     *
     * @param players List of Players which the Start Player can be chosen among
     * @param challenger Challenger Player
     */
    public RequireStartPlayerEvent(List<String> players, String challenger) {
        super(new Object());
        this.message = "Choose the Start Player for this game";
        this.players = players;
        this.challenger = challenger;
    }

    public String getChallenger() {
        return challenger;
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
