package it.polimi.ingsw.view.events;

import it.polimi.ingsw.model.PlaceableType;

import java.util.EventObject;

/**
 * Event: Player wants to make a Construction with a Worker.
 * [VCEvent]
 */
public class BuildBlockEvent extends EventObject {
    private String workerId; // univocal Worker identifier (who made this move)
    private int x;
    private int y;
    private PlaceableType blockType;

    public BuildBlockEvent(String workerId, int x, int y, PlaceableType blockType) {
        super(new Object());
        this.workerId = workerId;
        this.x = x;
        this.y = y;
        this.blockType = blockType;
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

    public PlaceableType getBlockType() {
        return blockType;
    }

    public void setBlockType(PlaceableType blockType) {
        this.blockType = blockType;
    }
}
