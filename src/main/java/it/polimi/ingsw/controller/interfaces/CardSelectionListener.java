package it.polimi.ingsw.controller.interfaces;

import it.polimi.ingsw.observer.GeneralListener;
import it.polimi.ingsw.view.events.CardSelectionEvent;

/**
 * Interface for Card selection from View.
 *
 * VCEvent (Events form Client)
 *
 * @author AndreaAltomare
 */
public interface CardSelectionListener extends GeneralListener {
    public void onCardSelection(CardSelectionEvent card, String playerNickname);
}
