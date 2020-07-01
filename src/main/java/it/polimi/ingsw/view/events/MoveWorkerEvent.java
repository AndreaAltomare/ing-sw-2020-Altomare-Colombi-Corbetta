package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Player wants to make a Movement with a Worker.
 * [VCEvent]
 */
public class MoveWorkerEvent extends EventObject {
    private String workerId; // univocal Worker identifier (who made this move)
    private int x;
    private int y;

    /**
     * Constructs a MoveWorkerEvent to notify the server the intention of
     * the Player to Move a Worker to the given Cell.
     *
     * @param workerId (the id of the Worker trying to move)
     * @param x (the x_position on the Cell on which it wants to move).
     * @param y (the y_position on the Cell on which it wants to move).
     */
    public MoveWorkerEvent(String workerId, int x, int y) {
        super(new Object());
        this.workerId = workerId;
        this.x = x;
        this.y = y;
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
