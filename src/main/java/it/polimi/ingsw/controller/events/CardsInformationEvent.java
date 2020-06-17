package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.card.CardInfo;

import java.util.EventObject;
import java.util.List;

/**
 * Event: Card's information which a Player needs, provided.
 * [MVEvent]
 */
public class CardsInformationEvent extends EventObject {
    private String challenger; // Challenger's nickname
    private String player; // Player who is asked to select the Card
    private List<CardInfo> cards;

    public CardsInformationEvent(List<CardInfo> cards, String challenger, String player) {
        super(new Object());
        this.cards = cards;
        this.challenger = challenger;
        this.player = player;
    }

    public List<CardInfo> getCards() {
        return cards;
    }

    public String getChallenger() {
        return challenger;
    }

    public String getPlayer() {
        return player;
    }
}
