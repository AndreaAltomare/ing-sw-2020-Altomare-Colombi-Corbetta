package it.polimi.ingsw.controller.interfaces;

/**
 * Interface for Card selection from View.
 *
 * VCEvent (Events form Client)
 *
 * @author AndreaAltomare
 */
public interface CardSelectionListener extends GeneralListener {
    public void onCardSelection(String cardName);
}
