package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Player wants to remove a Worker.
 * [VCEvent]
 */
public class RemoveWorkerEvent extends EventObject {
    private String workerId; // univocal Worker identifier (who EVENTUALLY made this move)
    private int x;
    private int y;

    /**
     * Constructs a RemoveWorkerEvent to notify the server the intention
     * to remove a Worker from the given Cell.
     *
     * @param workerId (the Id of the Worker wanting to make the action).
     * @param x (the x_position on the Cell on which it wants to remove the Worker).
     * @param y (the y_position on the Cell on which it wants to remove the Worker).
     */
    public RemoveWorkerEvent(String workerId, int x, int y) {
        super(new Object());
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
