package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.CardInfo;

import java.util.EventObject;
import java.util.List;

/**
 * Event: Card's information which a Player needs, provided.
 * [MVEvent]
 */
public class CardsInformationEvent extends EventObject {
    List<CardInfo> cards;

    public CardsInformationEvent(List<CardInfo> cards) {
        super(new Object());
        this.cards = cards;
    }

    public List<CardInfo> getCards() {
        return cards;
    }
}
