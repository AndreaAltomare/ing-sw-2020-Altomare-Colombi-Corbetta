package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Worker was removed successfully.
 * [MVEvent]
 */
public class WorkerRemovedEvent extends EventObject {
    private boolean success; // tell if the move was successful
    private String worker;
    private int x, y;

    public WorkerRemovedEvent(String worker, int x, int y, boolean success) {
        super(new Object());
        this.worker = worker;
        this.x = x;
        this.y = y;
        this.success = success;
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

    public boolean success() {
        return success;
    }
}
