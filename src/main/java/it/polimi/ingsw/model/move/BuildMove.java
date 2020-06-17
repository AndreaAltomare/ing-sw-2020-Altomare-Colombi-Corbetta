package it.polimi.ingsw.model.move;

import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.placeables.PlaceableType;

/**
 * This class extends Move class in order to specify
 * information useful when a Construction move occurs.
 *
 * @author AndreaAltomare
 */
public class BuildMove extends Move {
    private PlaceableType blockType;

    public BuildMove(Cell currentPosition, Cell selectedCell, PlaceableType blockType) {
        super(currentPosition, selectedCell);
        this.blockType = blockType;
    }

    /**
     * Constructor used when restoring game data.
     *
     * @param floorDirection Floor cardinal direction
     * @param levelDirection Level direction (up/down)
     * @param levelDepth Level depth
     * @param selectedCell Selected cell to move to
     * @param blockType Type of block to build
     */
    public BuildMove(FloorDirection floorDirection, LevelDirection levelDirection, int levelDepth, Cell selectedCell, PlaceableType blockType) {
        super(floorDirection, levelDirection, levelDepth, selectedCell);
        this.blockType = blockType;
    }

    public PlaceableType getBlockType() {
        return blockType;
    }




    /* ##### METHOD USED WHEN RESTORING DATA ##### */
    public void setBlockType(PlaceableType blockType) {
        this.blockType = blockType;
    }
}
