package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Player wants to remove a block.
 * [VCEvent]
 */
public class RemoveBlockEvent extends EventObject {
    private String workerId; // univocal Worker identifier (who EVENTUALLY made this move)
    private int x;
    private int y;

    /**
     * Constructs a RemoveBlockEvent to notify the server the intention
     * to remove a Block from the given Cell.
     *
     * @param workerId (the Id of the Worker wanting to remove the Block).
     * @param x (the x_position on the Cell on which it wants to remove the Block).
     * @param y (the y_position on the Cell on which it wants to remove the Block).
     */
    public RemoveBlockEvent(String workerId, int x, int y) {
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
