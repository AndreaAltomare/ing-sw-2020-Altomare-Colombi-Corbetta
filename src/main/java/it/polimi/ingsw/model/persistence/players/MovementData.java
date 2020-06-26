package it.polimi.ingsw.model.persistence.players;

import it.polimi.ingsw.model.move.FloorDirection;
import it.polimi.ingsw.model.move.LevelDirection;

import java.io.Serializable;

/**
 * Bean class to enable serialization/deserialization of the last Movement move's information
 * by JSON files, and to encapsulate the actual state of it at a certain point.
 *
 * @author AndreaAltomare
 */
public class MovementData implements Serializable {
    private int startingPositionX; // starting position Cell
    private int startingPositionY; // starting position Cell
    private FloorDirection lastMoveFloorDirection; // last move
    private LevelDirection lastMoveLevelDirection; // last move
    private int lastMoveLevelDepth; // last move
    private int lastMoveSelectedCellX; // last move
    private int lastMoveSelectedCellY; // last move
    private int movesLeft;

    /* Default Constructor */
    public MovementData() {}

    public int getStartingPositionX() {
        return startingPositionX;
    }

    public void setStartingPositionX(int startingPositionX) {
        this.startingPositionX = startingPositionX;
    }

    public int getStartingPositionY() {
        return startingPositionY;
    }

    public void setStartingPositionY(int startingPositionY) {
        this.startingPositionY = startingPositionY;
    }

    public FloorDirection getLastMoveFloorDirection() {
        return lastMoveFloorDirection;
    }

    public void setLastMoveFloorDirection(FloorDirection lastMoveFloorDirection) {
        this.lastMoveFloorDirection = lastMoveFloorDirection;
    }

    public LevelDirection getLastMoveLevelDirection() {
        return lastMoveLevelDirection;
    }

    public void setLastMoveLevelDirection(LevelDirection lastMoveLevelDirection) {
        this.lastMoveLevelDirection = lastMoveLevelDirection;
    }

    public int getLastMoveLevelDepth() {
        return lastMoveLevelDepth;
    }

    public void setLastMoveLevelDepth(int lastMoveLevelDepth) {
        this.lastMoveLevelDepth = lastMoveLevelDepth;
    }

    public int getLastMoveSelectedCellX() {
        return lastMoveSelectedCellX;
    }

    public void setLastMoveSelectedCellX(int lastMoveSelectedCellX) {
        this.lastMoveSelectedCellX = lastMoveSelectedCellX;
    }

    public int getLastMoveSelectedCellY() {
        return lastMoveSelectedCellY;
    }

    public void setLastMoveSelectedCellY(int lastMoveSelectedCellY) {
        this.lastMoveSelectedCellY = lastMoveSelectedCellY;
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    public void setMovesLeft(int movesLeft) {
        this.movesLeft = movesLeft;
    }
}
