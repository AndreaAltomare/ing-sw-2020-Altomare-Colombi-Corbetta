package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Selected Card was correctly associated with the Player.
 * [MVEvent]
 */
public class CardSelectedEvent extends EventObject {
    // TODO: 30/04/20 String cardName
    // TODO: 30/04/20 String playerName (per poter mandare il messaggio in broadcast)

    public CardSelectedEvent(Object o) {
        super(o);
    }
}
