package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.StateType;

import java.util.EventObject;

/**
 * Event: Player's Turn Status has changed.
 * [MVEvent]
 */
public class TurnStatusChangedEvent extends EventObject {
    private String playerNickname;
    // TODO: 30/04/20 view.serverSide.ClientSubTurn part // [Andrea: non si possono usare i turni (StateType) definiti nel Model?]
    private StateType state; // todo: temporary

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
