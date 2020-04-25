package it.polimi.ingsw.controller.interfaces;

import it.polimi.ingsw.model.StateType;

/**
 * Interface for Card selection from View.
 *
 * VCEvent (Events form Client)
 *
 * @author AndreaAltomare
 */
public interface TurnStatusChangeListener extends GeneralListener {
    public void onTurnStatusChange(StateType turnStatus);
}
