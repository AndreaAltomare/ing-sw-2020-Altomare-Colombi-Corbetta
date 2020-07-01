package it.polimi.ingsw.view.events;

import java.util.EventObject;
import java.util.List;

/**
 * Event: Challenger has chosen Cards for the upcoming game.
 * [VCEvent]
 */
public class CardsChoosingEvent extends EventObject {
    List<String> cards; // Cards' name list

    /**
     * Construct a CardsChoosingEvent to notify the Server which
     * cards the player -challenger- has chosen to be available in the game.
     *
     * @param cards (the List of chosen cards)
     */
    public CardsChoosingEvent(List<String> cards) {
        super(new Object());
        this.cards = cards;
    }

    public List<String> getCards() {
        return cards;
    }
}
