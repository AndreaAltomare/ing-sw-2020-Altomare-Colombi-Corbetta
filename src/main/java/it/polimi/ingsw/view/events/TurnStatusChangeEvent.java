package it.polimi.ingsw.view.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Player wants to change his/her Turn Status
 * (to make a Movement or a Construction).
 * [VCEvent]
 */
public class TurnStatusChangeEvent extends EventObject {
    // TODO inserire attributi

    public TurnStatusChangeEvent(Object o) {
        super(o);
    }
}
