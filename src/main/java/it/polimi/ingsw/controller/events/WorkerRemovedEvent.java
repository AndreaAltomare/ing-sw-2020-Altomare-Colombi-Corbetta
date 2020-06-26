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

    /**
     * Constructs a WorkerRemovedEvent to inform the View about the event occurred.
     *
     * @param worker Removed Worker
     * @param x Worker's X position on the Board
     * @param y Worker's Y position on the Board
     * @param success True if Worker was actually removed from the Board
     */
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
