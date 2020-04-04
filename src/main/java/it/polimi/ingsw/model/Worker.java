package it.polimi.ingsw.model;

public class Worker extends Placeable {

    private Player owner;

    /**
     * Class constructor.
     * @author Giorgio Corbetta
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
     * @author Giorgio Corbetta.
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
     * Method that puts the Worker on the Cell
     * @author Giorgio Corbetta
     *
     * @param destination (Cell on which the Worker has to be put)
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
     * This method returns true iif this is a worker
     * @author Giorgio Corbetta
     *
     * @return true
     */
    @Override
    public boolean isWorker(){
        return true;
    }


    /**
     * Method that returns the owner of the worker
     * @author Giorgio Corbetta
     *
     * @return (The owner of the Worker)
     */
    public Player getOwner(){
        return this.owner;
    }

}