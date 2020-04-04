package it.polimi.ingsw.model;

public class Dome extends Placeable {

    /**
     * Class Constructor.
     */
    public Dome(){
        super();
    }

    /**
     * Constructor with specified destination.
     * When called create the object and sets the position to be the passed destination
     * @author Giorgio Corbetta
     *
     * @param destination (Cell where the Dome is; it'll be returned by the getPosition())
     */
    public Dome (Cell destination){
        super();
        this.setPosition(destination);
    }

    /**
     * Method that puts the Dome on the Cell
     * @author Giorgio Corbetta
     *
     * @param destination (Cell on which the Dome has to be put)
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
     * This method returns true iif this is a dome
     * @author Giorgio Corbetta
     *
     * @return true
     */
    @Override
    public boolean isDome(){
        return true;
    }

}