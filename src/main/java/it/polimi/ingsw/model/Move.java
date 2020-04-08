package it.polimi.ingsw.model;

/**
 * This class represent properties for a type-agnostic
 * move (both Movement and Construction type)
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

    public FloorDirection getFloorDirection() {
        return floorDirection;
    }

    public LevelDirection getLevelDirection() {
        return levelDirection;
    }

    public Cell getSelectedCell() {
        return selectedCell;
    }

    /**
     * Class method: Given two Cells, calculate the direction between them
     *
     * @param currentCell
     * @param nextCell
     * @return floor direction (calculated direction)
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
     * Class method: Given two Cells, calculate the direction between them
     *
     * @param currentCell
     * @param nextCell
     * @return level direction (calculated direction)
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
     * Class method: Given two Cells, calculate the direction between them
     *
     * @param currentCell
     * @param nextCell
     * @return level depth (calculated)
     */
    private static int calculateLevelDepth(Cell currentCell, Cell nextCell) {
        return nextCell.getLevel() - currentCell.getLevel();
    }
}
