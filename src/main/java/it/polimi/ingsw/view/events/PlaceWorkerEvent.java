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
