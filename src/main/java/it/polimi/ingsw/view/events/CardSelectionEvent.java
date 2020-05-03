package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Player has selected a Card.
 * [VCEvent]
 */
public class CardSelectionEvent extends EventObject {
    private String cardName;

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
