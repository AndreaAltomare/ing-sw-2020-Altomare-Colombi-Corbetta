package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.StateType;

/**
 * Interface for Card selection from View.
 *
 * VCEvent (Events form Client)
 */
public interface TurnStatusChangeListener {
    public void onTurnStatusChange(StateType turnStatus);
}
