package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Player's Turn Status has changed.
 * [MVEvent]
 */
public class TurnStatusChangedEvent extends EventObject {
    // TODO per Giorgio: Inserire i todo per indicare gli attributi necessari
    // TODO: 30/04/20 String playerName 
    // TODO: 30/04/20 TurnPart part 

    public TurnStatusChangedEvent(Object o) {
        super(o);
    }
}
