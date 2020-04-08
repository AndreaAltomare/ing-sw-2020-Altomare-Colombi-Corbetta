package it.polimi.ingsw.model;

/**
 * This class extends Move class in order to specify
 * information useful when a Construction move occurs
 */
public class BuildMove extends Move {
    private PlaceableType blockType;

    public BuildMove(Cell currentPosition, Cell selectedCell, PlaceableType blockType) {
        super(currentPosition, selectedCell);
        this.blockType = blockType;
    }

    public PlaceableType getBlockType() {
        return blockType;
    }
}
