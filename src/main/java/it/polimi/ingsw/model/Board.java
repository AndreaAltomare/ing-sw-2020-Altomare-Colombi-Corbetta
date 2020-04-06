package it.polimi.ingsw.model;

/**
 * Class to abstractly model the Board
 * @author giorgio
 */
public abstract class Board {


    /**
     * Method that returns the Cell at the passed position.
     *
     * @param x (the x coordinate of the searched Cell)
     * @param y (the y coordinate of the searched Cell)
     * @return  (the searched Cell)
     * @throws OutOfBoardException (if the searched Cell is out of board Bounds)
     */
    public abstract Cell getCellAt(int x, int y) throws OutOfBoardException;

    /**
     * Method that returns the x dimension of the board
     *
     * @return (the x dimension of the board)
     */
    public abstract int getXDim();

    /**
     * Method that returns the y dimension of the board
     *
     * @return (the y dimension of the board)
     */
    public abstract int getYDim();

    public void clearCells(){
        int xDim = getXDim();
        int yDim = getYDim();
        for(int x=0; x<xDim; x++)
            for(int y=0; y<yDim; y++) {
                try {
                    getCellAt(x, y).clearStatus();
                } catch (OutOfBoardException e) {
                    ; //DO NOTHING
                }
            }
    }
}
