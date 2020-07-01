package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Player has selected a Card.
 * [VCEvent]
 */
public class CardSelectionEvent extends EventObject {
    private String cardName;

    /**
     * Constructs a CardSelectionEvent to notify the Server which
     * card the player has chosen
     *
     * @param cardName (the name of the card chosen by the player)
     */
    public CardSelectionEvent(String cardName) {
        super(new Object());
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    @Override
    public String toString() {
        return cardName;
    }
}
