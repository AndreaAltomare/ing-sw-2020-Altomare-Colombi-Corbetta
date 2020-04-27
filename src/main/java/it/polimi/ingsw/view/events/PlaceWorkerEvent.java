package it.polimi.ingsw.view.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Player has placed a Worker.
 * [VCEvent]
 */
public class PlaceWorkerEvent extends EventObject {
    // TODO inserire attributi

    public PlaceWorkerEvent(Object o) {
        super(o);
    }
}
