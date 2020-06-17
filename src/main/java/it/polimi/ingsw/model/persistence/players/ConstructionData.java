package it.polimi.ingsw.model.persistence.players;

import it.polimi.ingsw.model.board.placeables.PlaceableType;

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
