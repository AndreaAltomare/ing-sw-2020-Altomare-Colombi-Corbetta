package it.polimi.ingsw.view;

/**
 * Interface for Server messages exchange.
 *
 * MVEvent (Events form Server)
 *
 * @author Giorgio Corbetta
 */
public interface MessageListener {

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
