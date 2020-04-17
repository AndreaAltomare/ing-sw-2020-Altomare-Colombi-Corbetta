package it.polimi.ingsw.model;

/**
 * This class represent properties for a type-agnostic
 * move (both Movement and Construction type).
 *
 * @author AndreaAltomare
 */
public class Move {
    protected FloorDirection floorDirection;
    protected LevelDirection levelDirection;
    protected int levelDepth; // depth of level UP/DOWN
    protected Cell selectedCell; // Cell where the Player wants to move

    public Move(Cell currentPosition, Cell selectedCell) {
        this.floorDirection = Move.calculateFloorDirection(currentPosition, selectedCell);
        this.levelDirection = Move.calculateLevelDirection(currentPosition, selectedCell);
        this.levelDepth = Move.calculateLevelDepth(currentPosition,selectedCell);
        this.selectedCell = selectedCell;
    }

    /**
     * Class method: Given two Cells, calculate the Cardinal direction between them.
     *
     * @param currentCell (Cell from which calculate the Cardinal direction)
     * @param nextCell (Cell by which calculate the Cardinal direction starting from the currentCell)
     * @return Floor direction (calculated direction)
     */
    private static FloorDirection calculateFloorDirection(Cell currentCell, Cell nextCell) {
        FloorDirection direction;

        if(nextCell.getX() < currentCell.getX() && nextCell.getY() == currentCell.getY())
            direction = FloorDirection.NORTH;
        else if(nextCell.getX() > currentCell.getX() && nextCell.getY() == currentCell.getY())
            direction = FloorDirection.SOUTH;
        else if(nextCell.getX() == currentCell.getX() && nextCell.getY() > currentCell.getY())
            direction = FloorDirection.EAST;
        else if(nextCell.getX() == currentCell.getX() && nextCell.getY() < currentCell.getY())
            direction = FloorDirection.WEST;
        else if(nextCell.getX() < currentCell.getX() && nextCell.getY() > currentCell.getY())
            direction = FloorDirection.NORTH_EAST;
        else if(nextCell.getX() < currentCell.getX() && nextCell.getY() < currentCell.getY())
            direction = FloorDirection.NORTH_WEST;
        else if(nextCell.getX() > currentCell.getX() && nextCell.getY() > currentCell.getY())
            direction = FloorDirection.SOUTH_EAST;
        else if(nextCell.getX() > currentCell.getX() && nextCell.getY() < currentCell.getY())
            direction = FloorDirection.SOUTH_WEST;
        else
            direction = FloorDirection.NONE;

        return direction;
    }

    /**
     * Class method: Given two Cells, calculate the Level direction between them.
     *
     * @param currentCell (Cell from which calculate the Level direction)
     * @param nextCell (Cell by which calculate the Level direction starting from the currentCell)
     * @return Level direction (calculated direction)
     */
    private static LevelDirection calculateLevelDirection(Cell currentCell, Cell nextCell) {
        LevelDirection direction = LevelDirection.NONE;
        int levelDepth;

        levelDepth = calculateLevelDepth(currentCell, nextCell);

        if(levelDepth > 0)
            direction = LevelDirection.UP;
        else if(levelDepth == 0)
            direction = LevelDirection.SAME;
        else
            direction = LevelDirection.DOWN;

        return direction;
    }

    /**
     * Class method: Given two Cells, calculate the Level depth between them.
     *
     * @param currentCell (Cell from which calculate the Level depth)
     * @param nextCell (Cell by which calculate the Level depth starting from the currentCell)
     * @return level depth (calculated)
     */
    private static int calculateLevelDepth(Cell currentCell, Cell nextCell) {
        return nextCell.getLevel() - currentCell.getLevel();
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
