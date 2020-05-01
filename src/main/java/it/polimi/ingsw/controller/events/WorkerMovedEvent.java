package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Worker has been successfully moved.
 * [MVEvent]
 */
public class WorkerMovedEvent extends EventObject {
    private String worker;
    private int initialX, initialY;
    private int finalX, finalY;

    // TODO: 30/04/20 startCell : int startX, int startY (Se diventa un casino posso pure ricavarmelo io, è quasi solo per ccontrollare che // [Andrea: controllare che...? Comunque vediamo: se non è troppo difficile ti mando anche queste info]

    public WorkerMovedEvent(String worker, int initialX, int initialY, int finalX, int finalY) {
        super(new Object());
        this.worker = worker;
        this.initialX = initialX;
        this.initialY = initialY;
        this.finalX = finalX;
        this.finalY = finalY;
    }

    public String getWorker() {
        return worker;
    }

    public int getInitialX() {
        return initialX;
    }

    public int getInitialY() {
        return initialY;
    }

    public int getFinalX() {
        return finalX;
    }

    public int getFinalY() {
        return finalY;
    }
}
