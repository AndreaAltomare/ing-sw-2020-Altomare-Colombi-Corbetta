package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Worker was removed successfully.
 * [MVEvent]
 */
public class WorkerRemovedEvent extends EventObject {
    private String worker;
    private int x, y;

    public WorkerRemovedEvent(String worker, int x, int y) {
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
