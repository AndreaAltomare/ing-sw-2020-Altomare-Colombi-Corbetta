package it.polimi.ingsw.model.persistence.board;

import java.io.Serializable;

/**
 * Bean class to enable serialization/deserialization of a board state
 * by JSON files, and to encapsulate the actual state of the board at a certain point.
 *
 * @author AndreaAltomare
 */
public class BoardState implements Serializable {
    private CellState[][] board;
    private int xSize; // board x dimension
    private int ySize; // board y dimension

    /* Default Constructor */
    public BoardState() {}

    public CellState[][] getBoard() {
        return board;
    }

    public void setBoard(CellState[][] board) {
        this.board = board;
    }

    public int getXSize() {
        return xSize;
    }

    public void setXSize(int xSize) {
        this.xSize = xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public void setYSize(int ySize) {
        this.ySize = ySize;
    }
}
