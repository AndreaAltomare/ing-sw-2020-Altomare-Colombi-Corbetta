package it.polimi.ingsw.model;

public class Move {
    private FloorDirection floorDirection;
    private LevelDirection levelDirection;
    private Cell selectedCell; // Cell where the Player wants to move

    public Move(FloorDirection floorDirection, LevelDirection levelDirection, Cell selectedCell) {
        this.floorDirection = floorDirection;
        this.levelDirection = levelDirection;
        this.selectedCell = selectedCell;
    }

    public FloorDirection getFloorDirection() {
        return floorDirection;
    }

    public LevelDirection getLevelDirection() {
        return levelDirection;
    }

    public Cell getSelectedCell() {
        return selectedCell;
    }
}
