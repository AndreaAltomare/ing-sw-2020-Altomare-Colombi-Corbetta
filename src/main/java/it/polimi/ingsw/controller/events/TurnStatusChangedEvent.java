package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.player.turn.StateType;

import java.util.EventObject;

/**
 * Event: Player's Turn Status has changed.
 * [MVEvent]
 */
public class TurnStatusChangedEvent extends EventObject {
    private boolean success; // tell if the move was successful
    private String playerNickname;
    private StateType state;

    /**
     * Constructs a TurnStatusChangedEvent to inform the View about the event occurred.
     *
     * @param playerNickname Players associated to the Turn change event
     * @param state Turn state of the Player
     * @param success True if the Turn state for the Player was actually changed
     */
    public TurnStatusChangedEvent(String playerNickname, StateType state, boolean success) {
        super(new Object());
        this.playerNickname = playerNickname;
        this.state = state;
        this.success = success;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public StateType getState() {
        return state;
    }

    public boolean success() {
        return success;
    }
}
