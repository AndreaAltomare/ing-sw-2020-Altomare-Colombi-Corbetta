package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.Color;

import java.util.EventObject;

/**
 * Event: Worker was correctly placed.
 * [MVEvent]
 */
public class WorkerPlacedEvent extends EventObject {
    private boolean success; // tell if the move was successful
    private String worker;
    private int x, y;
    private Color color;

    public WorkerPlacedEvent(String worker, int x, int y, Color color, boolean success) {
        super(new Object());
        this.worker = worker;
        this.x = x;
        this.y = y;
        this.color = color;
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

    public Color getColor() {
        return color;
    }

    public boolean success() {
        return success;
    }
}
