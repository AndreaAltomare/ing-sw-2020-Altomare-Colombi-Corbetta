package it.polimi.ingsw.view.serverSide.interfaces;

import it.polimi.ingsw.observer.GeneralListener;

/**
 * Interface for Server messages exchange.
 *
 * MVEvent (Events form Server)
 *
 * @author Giorgio Corbetta
 */
public interface MessageListener extends GeneralListener {

    /**
     * DISPLAY INFO MESSAGES:
     *
     * Card information
     * Player has won
     * Player has lost
     * Player's turn is over
     */
    public void showMessage(String message);
}
