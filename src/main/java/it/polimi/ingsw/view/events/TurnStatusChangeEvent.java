package it.polimi.ingsw.view.events;

import it.polimi.ingsw.model.StateType;

import java.util.EventObject;

/**
 * Event: Player wants to change his/her Turn Status
 * (to make a Movement or a Construction).
 * [VCEvent]
 */
public class TurnStatusChangeEvent extends EventObject {
    private StateType turnStatus;

    public TurnStatusChangeEvent(StateType turnStatus) {
        super(new Object());
        this.turnStatus = turnStatus;
    }

    public StateType getTurnStatus() {
        return turnStatus;
    }

    public void setTurnStatus(StateType turnStatus) {
        this.turnStatus = turnStatus;
    }
}
