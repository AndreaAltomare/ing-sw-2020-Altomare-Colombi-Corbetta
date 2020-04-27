package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Selected Card was correctly associated with the Player.
 * [MVEvent]
 */
public class CardSelectedEvent extends EventObject {
    // TODO per Giorgio: Inserire i todo per indicare gli attributi necessari

    public CardSelectedEvent(Object o) {
        super(o);
    }
}
