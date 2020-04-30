package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Worker has been successfully moved.
 * [MVEvent]
 */
public class WorkerMovedEvent extends EventObject {
    // TODO: 30/04/20 String worker
    // TODO: 30/04/20 startCell : int startX, int startY (Se diventa un casino posso pure ricavarmelo io, è quasi solo per ccontrollare che
    // TODO: 30/04/20 finalCell: int finalX, int finalY

    public WorkerMovedEvent(Object o) {
        super(o);
    }
}
