package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.StateType;

import java.util.EventObject;

/**
 * Event: Player's Turn Status has changed.
 * [MVEvent]
 */
public class TurnStatusChangedEvent extends EventObject {
    private String playerNickname;
    private StateType state; // todo: send in broadcast a NONE StateType when it's not a Player's turn

    public TurnStatusChangedEvent(String playerNickname, StateType state) {
        super(new Object());
        this.playerNickname = playerNickname;
        this.state = state;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public StateType getState() {
        return state;
    }
}
