package it.polimi.ingsw.model;

public class ConstructionData extends MovementData {
    private PlaceableType lastMoveBlockType; // last move

    /* Default Constructor */
    public ConstructionData() {}

    public PlaceableType getLastMoveBlockType() {
        return lastMoveBlockType;
    }

    public void setLastMoveBlockType(PlaceableType lastMoveBlockType) {
        this.lastMoveBlockType = lastMoveBlockType;
    }
}
