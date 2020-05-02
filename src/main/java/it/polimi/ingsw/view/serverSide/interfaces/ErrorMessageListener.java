package it.polimi.ingsw.view.serverSide.interfaces;

import it.polimi.ingsw.controller.events.MessageEvent;

/**
 * Specialized interface for
 * Server error messages exchange.
 *
 * MVEvent (Events form Server)
 *
 * @author Giorgio Corbetta
 */
public interface ErrorMessageListener extends MessageListener {

    /**
     * DISPLAY ERROR MESSAGES:
     *
     * Player has run out of moves
     * Player is trying to build before a movement
     * Wrong worker selection
     * Denied move
     */
    @Override
    public void showMessage(MessageEvent message);
}
