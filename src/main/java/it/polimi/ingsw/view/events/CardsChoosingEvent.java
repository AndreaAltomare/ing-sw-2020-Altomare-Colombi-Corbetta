package it.polimi.ingsw.view.events;

import java.util.EventObject;
import java.util.List;

/**
 * Event: Challenger has chosen Cards for the upcoming game.
 * [VCEvent]
 */
public class CardsChoosingEvent extends EventObject {
    List<String> cards;

    public CardsChoosingEvent(List<String> cards) {
        super(new Object());
        this.cards = cards;
    }

    public List<String> getCards() {
        return cards;
    }
}
