package it.polimi.ingsw.model.persistence.players;

import it.polimi.ingsw.model.board.placeables.PlaceableType;

/**
 * Bean class to enable serialization/deserialization of the last Construction move's information
 * by JSON files, and to encapsulate the actual state of it at a certain point.
 *
 * @author AndreaAltomare
 */
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
