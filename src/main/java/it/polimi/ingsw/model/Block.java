package it.polimi.ingsw.model;

/**
 * This class is intended to represent the Block on the Board
 *
 * @author Giorgio Corbetta
 */
public class Block extends Placeable {

    /**
     * Class constructor.
     */
    public Block(){
        super();
    }

    /**
     * Constructor with specified destination.
     * When called create the object and sets the position to be the passed destination
     *
     * @param destination (Cell where the Block is; it'll be returned by the getPosition())
     */
    public Block (Cell destination){
        super();
        this.setPosition(destination);
    }

    /**
     * Method that puts the Block on the Cell
     *
     * @param destination (Cell on which the Block has to be put)
     * @return (had the method's invocation success? true: false)
     */
    @Override
    public boolean place(Cell destination) {
        if (destination.placeOn(this)){
            this.setPosition(destination);
            return true;
        }
        return true;
    }

    /**
     * This method returns true iif this is a block
     *
     * @return true
     */
    @Override
    public boolean isBlock(){
        return true;
    }

}
