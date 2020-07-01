package it.polimi.ingsw.view.events;

import it.polimi.ingsw.model.player.turn.StateType;

import java.util.EventObject;

/**
 * Event: Player wants to change his/her Turn Status
 * (to make a Movement or a Construction).
 * [VCEvent]
 */
public class TurnStatusChangeEvent extends EventObject {
    private StateType turnStatus;

    /**
     * Constructs a TurnStatusChangeEvent to notify the Server the player's
     * intention to change the TurnStatus.
     *
     * @param turnStatus (the new StateType in which the player wants to be switched).
     */
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
