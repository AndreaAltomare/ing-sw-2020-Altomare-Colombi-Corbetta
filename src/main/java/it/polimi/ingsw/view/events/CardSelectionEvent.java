package it.polimi.ingsw.view.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Player has selected a Card.
 * [VCEvent]
 */
public class CardSelectionEvent extends EventObject {
    // TODO inserire attributi

    public CardSelectionEvent(Object o) {
        super(o);
    }
}
