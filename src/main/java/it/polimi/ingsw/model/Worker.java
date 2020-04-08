package it.polimi.ingsw.model;

/**
 * This class is assumed to represent the workers on to the board.
 *
 * @author Giorgio Corbetta
 */
public class Worker extends Placeable {

    private Player owner;

    /**
     * Class constructor.
     *
     * @param owner (The player to which the worker belongs to)
     */
    public Worker(Player owner){
        super();
        this.owner = owner;
    }

    /**
     * Constructor with specified destination.
     * When called create the object and sets the position to be the passed destination
     *
     * @param destination (Cell where the Block is; it'll be returned by the getPosition()).
     * @param owner (The player to which the worker belongs to).
     */
    public Worker (Cell destination, Player owner){
        super();
        this.setPosition(destination);
        this.owner = owner;
    }


    /**
     * Method that puts the Worker on the Cell and removes from previous one
     *
     * @param destination (Cell on which the Worker has to be put)
     * @return (had the method's invocation success? true: false)
     */
    @Override
    public boolean place(Cell destination) {

        position().removeThisWorker(this);

        if (destination.placeOn(this)){
            this.setPosition(destination);
            return true;
        }
        return false;
    }

    /**
     * This method returns true iif this is a worker
     *
     * @return true
     */
    @Override
    public boolean isWorker(){
        return true;
    }


    /**
     * Method that returns the owner of the worker
     *
     * @return (The owner of the Worker)
     */
    public Player getOwner(){
        return this.owner;
    }

}