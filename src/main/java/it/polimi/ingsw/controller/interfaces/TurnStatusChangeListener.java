package it.polimi.ingsw.controller.interfaces;

import it.polimi.ingsw.model.StateType;
import it.polimi.ingsw.observer.GeneralListener;
import it.polimi.ingsw.view.events.TurnStatusChangeEvent;

/**
 * Interface for Card selection from View.
 *
 * VCEvent (Events form Client)
 *
 * @author AndreaAltomare
 */
public interface TurnStatusChangeListener extends GeneralListener {
    public void onTurnStatusChange(TurnStatusChangeEvent turnStatus, String playerNickname);
}
