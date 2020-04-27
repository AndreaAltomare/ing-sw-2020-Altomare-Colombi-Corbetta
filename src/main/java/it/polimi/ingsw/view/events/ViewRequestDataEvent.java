package it.polimi.ingsw.view.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: View has requested general game data.
 * [VCEvent]
 */
public class ViewRequestDataEvent extends EventObject {
    // TODO inserire attributi

    public ViewRequestDataEvent(Object o) {
        super(o);
    }
}
