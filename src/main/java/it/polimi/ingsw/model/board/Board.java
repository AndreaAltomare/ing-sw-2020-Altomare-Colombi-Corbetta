package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.worker.Worker;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to abstractly model the Board
 *
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

    /**
     * Method that returns the adjacent Cell to the one given.
     *
     * @param cell (Cell to check the adjacent)
     * @return (list of all the adjacent Cells)
     */
    public List<Cell> getAdjacentCells(Cell cell){
        int x = cell.getX();
        int y = cell.getY();
        if(cell.getBoard()!=this)
            throw new InvalidParameterException("getAdjacentCell has been called with a Cell of another Board");

        //"A little bug fixed, a great feaure lost"
        /*try {
            this.getCellAt(x, y);
        } catch (OutOfBoardException e) {
            throw new InvalidParameterException("getAdjacentCell has been called with a Cell out of Board");
        }*/

        List<Cell> ret = new ArrayList<>();
        for(int i=-1; i<2; i++)
            for(int j=-1; j<2; j++){
                if(i==j&& i==0) continue;
                try {
                    ret.add(this.getCellAt(x+i, y+j));
                } catch (OutOfBoardException ignored){

                }
            }
        return ret;
    }

    /**
     * Method that removes all the workers relative to the given player from the Board.
     *
     * @param player (the Player which Workers have to be removed).
     */
    public void removeWorkers(Player player){
        int xDim = getXDim();
        int yDim = getYDim();
        
        for(int i = 0; i<xDim; i++){
            for(int j = 0; j<yDim; j++){
                try {
                    Cell cell = getCellAt(i, j);
                    Worker worker = cell.getWorker();
                    if(worker != null && worker.getOwner().equals(player))
                        cell.removeWorker();
                } catch (OutOfBoardException ignore) {
                }

            }
        }
    }

    /**
     * method that clears the status of all the Cell of the Board.
     * @see Cell
     */
    void clearCells(){
        int xDim = getXDim();
        int yDim = getYDim();
        for(int x=0; x<xDim; x++)
            for(int y=0; y<yDim; y++) {
                try {
                    getCellAt(x, y).clearStatus();
                } catch (OutOfBoardException ignore) {
                }
            }
    }


    /**
     * method that removes all the Workers and the Placeables from the Board.
     */
    public void clear(){
        int xDim = getXDim();
        int yDim = getYDim();
        for(int y=0; y<yDim; y++){
            for(int x=0; x<xDim; x++){
                try {
                    Cell cell = getCellAt(x, y);
                    cell.removeWorker();
                    if(cell.isDomed()){
                        cell.removePlaceable();
                    }
                    while(cell.getHeigth()>0){
                        cell.removePlaceable();
                    }
                } catch (OutOfBoardException ignore) {
                }
            }
        }
    }
}
