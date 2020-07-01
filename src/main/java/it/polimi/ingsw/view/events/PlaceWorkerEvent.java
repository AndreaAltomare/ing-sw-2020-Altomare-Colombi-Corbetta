package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Player has placed a Worker.
 * [VCEvent]
 */
public class PlaceWorkerEvent extends EventObject {
    //private String workerId; // univocal Worker identifier (no need for this attribute when placing a Worker: its ID is provided when a Worker object will be instantiated in the Model)
    private int x;
    private int y;

    /**
     * Constructs a PlaceWorkerEvent to notify the server the intention of
     * the Player to Place a Worker on the given Cell.
     *
     * @param x (the x_position on the Cell on which it wants to place the Worker).
     * @param y (the y_position on the Cell on which it wants to place the Worker).
     */
    public PlaceWorkerEvent(int x, int y) {
        super(new Object());
        this.x = x;
        this.y = y;
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
