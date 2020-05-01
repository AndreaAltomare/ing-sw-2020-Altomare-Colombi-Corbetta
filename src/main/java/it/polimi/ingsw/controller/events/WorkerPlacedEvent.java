package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Worker was correctly placed.
 * [MVEvent]
 */
public class WorkerPlacedEvent extends EventObject {
    private String worker;
    private int x, y;

    public WorkerPlacedEvent(String worker, int x, int y) {
        super(new Object());
        this.worker = worker;
        this.x = x;
        this.y = y;
    }

    public String getWorker() {
        return worker;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
