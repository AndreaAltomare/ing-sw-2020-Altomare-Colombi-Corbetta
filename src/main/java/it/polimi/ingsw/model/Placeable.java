package it.polimi.ingsw.model;

/**
 * This class is the superclass of Block, Dome, Worker.
 * Informally: They are the only "things" that can be put on the Cells of the Board.
 */
public abstract class Placeable {

    /**
     * In this attribute we store the actual position of the Placeable
     */
    private Cell myPosition;

    /**
     * Class Constructor
     * @author Giorgio Corbetta
     */
    public Placeable(){
        myPosition = null;
    }


    /**
     * Method to place this Placeable on the destination Cell
     * @author Giorgio Corbetta
     *
     * @param destination (Cell on which place the Placeable)
     * @return (had the method's invocation success? true: false)
     */
    public abstract boolean place(Cell destination);

    /**
     * This method sets the position
     * @author Giorgio Corbetta
     *
     * @param position (Cell on which the Placeable is)
     */
    protected void setPosition (Cell position){
        myPosition = position;
    }

    /**
     * This method returns the Cell on which this is
     * @author Giorgio Corbetta
     *
     * @return (the Cell on which this is)
     */
    public Cell position(){
        return myPosition;
    }

    /**
     * This method returns true iif this is a block
     * @author Giorgio Corbetta
     *
     * @return ((is this a Block)? true : false)
     */
    public boolean isBlock(){
        return false;
    }

    /**
     * This method returns true iif this is a dome
     * @author Giorgio Corbetta
     *
     * @return ((is this a Dome)? true : false)
     */
    public boolean isDome(){
        return false;
    }

    /**
     * This method returns true iif this is a worker
     * @author Giorgio Corbetta
     *
     * @return ((is this a worker)? true : false)
     */
    public boolean isWorker(){
        return false;
    }

}
