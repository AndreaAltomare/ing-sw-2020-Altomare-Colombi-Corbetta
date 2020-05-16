package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Selected Card was correctly associated with the Player.
 * [MVEvent]
 */
public class CardSelectedEvent extends EventObject {
    private boolean success; // tell if the move was successful
    private String cardName;
    private String playerNickname; // Player who is now associated with this card

    public CardSelectedEvent(String cardName, String playerNickname, boolean success) {
        super(new Object());
        this.cardName = cardName;
        this.playerNickname = playerNickname;
        this.success = success;
    }

    public String getCardName() {
        return cardName;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public boolean success() {
        return success;
    }
}
