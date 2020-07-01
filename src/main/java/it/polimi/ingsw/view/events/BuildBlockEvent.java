package it.polimi.ingsw.view.events;

import it.polimi.ingsw.model.board.placeables.PlaceableType;

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

    /**
     * Constructs a BuildBlockEvent to notify the server the intention of
     * the Player to Build a Block of the given type on the given Cell.
     *
     * @param workerId (the id of the Worker trying to build)
     * @param x (the x_position on the Cell on which build).
     * @param y (the y_position on the Cell on which build).
     * @param blockType (the type of the Block to be built).
     */
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
