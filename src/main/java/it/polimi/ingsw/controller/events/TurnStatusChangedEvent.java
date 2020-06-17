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
    private StateType state; // todo: send in broadcast a NONE StateType when it's not a Player's turn

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
