package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.exceptions.OutOfBoardException;

/**
 * Class that extends Board and can be used to simulate the functioning of the (default) Island Board
 * @author giorgio
 */
public class IslandBoard extends Board {

    private final int xDim = 5;
    private final int yDim = 5;

    private Cell[][] board;


    /**
     * Class Constructor.
     */
    public IslandBoard(){
        board = new Cell[xDim][yDim];
        for(int y=0; y<yDim; y++)
            for(int x=0; x<xDim; x++)
                board[x][y] = new Cell(x, y, this);
    }


    /**
     * Method that returns the selected Cell from the Board
     *
     * @param x (the x coordinate of the searched Cell)
     * @param y (the y coordinate of the searched Cell)
     * @return (The Cell corresponding to the selected coordinates. throws exception if it is out of bound).
     */
    @Override
    public Cell getCellAt(int x, int y) throws OutOfBoardException {
        if (x<0||y<0||x>=xDim||y>=yDim) throw new OutOfBoardException("trying to access a Cell out of bounds");
        return board[x][y];
    }

    /**
     * Method that returns the x dimension of the board
     *
     * @return (the x dimension of the board)
     */
    @Override
    public int getXDim() {
        return xDim;
    }

    /**
     * Method that returns the y dimension of the board
     *
     * @return (the y dimension of the board)
     */
    @Override
    public int getYDim() {
        return yDim;
    }
}
