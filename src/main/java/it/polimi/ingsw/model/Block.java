package it.polimi.ingsw.model;

public class Block extends Placeable {

    /**
     * Class constructor.
     * @author Giorgio Corbetta
     */
    public Block(){
        super();
    }

    /**
     * Constructor with specified destination.
     * When called create the object and sets the position to be the passed destination
     * @author Giorgio Corbetta
     *
     * @param destination (Cell where the Block is; it'll be returned by the getPosition())
     */
    public Block (Cell destination){
        super();
        this.setPosition(destination);
    }

    /**
     * Method that puts the Block on the Cell
     * @author Giorgio Corbetta
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
     * @author Giorgio Corbetta
     *
     * @return true
     */
    @Override
    public boolean isBlock(){
        return true;
    }

}
