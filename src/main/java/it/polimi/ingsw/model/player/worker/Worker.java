package it.polimi.ingsw.model.player.worker;

import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.placeables.Placeable;
import it.polimi.ingsw.model.player.Player;

/**
 * This class is assumed to represent the workers on to the board.
 *
 * @author Giorgio Corbetta
 */
public class Worker extends Placeable {

    //Addes later
    private static int lastId = 0;
    private static int lastColor = 0;
    private static Color[] colors = { Color.BLUE, Color.BLUE, Color.GREY, Color.GREY, Color.BROWN, Color.BROWN };
    private int id;
    private Color color;

    private Player owner;
    private ChooseType chosen; // tell whether a Worker can either be chosen for a turn or not, or it has already been chosen

    /**
     * Class constructor.
     *
     * @param owner (The player to which the worker belongs to)
     */
    public Worker(Player owner){
        super();
        this.owner = owner;
        this.chosen = ChooseType.CAN_BE_CHOSEN;

        //Added later to make each worker unique
        id = lastId;
        lastId++;
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
        this.chosen = ChooseType.CAN_BE_CHOSEN;

        //Added later to make each worker unique
        id = lastId;
        lastId++;
    }


    /**
     * Method that puts the Worker on the Cell and removes from previous one
     *
     * @param destination (Cell on which the Worker has to be put)
     * @return (had the method's invocation success? true: false)
     */
    @Override
    public boolean place(Cell destination) {
        if(position()!=null)
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


    /**
     * Tells if the Worker is the Chosen one
     * for the Turn that is being played
     *
     * @return (chosen == ChooseType.CHOSEN)
     * @author AndreaAltomare
     */
    public boolean isChosen() {
        return (chosen == ChooseType.CHOSEN);
    }

    /**
     * Gets the choose status for the Worker
     *
     * @return chosen (ChooseType status)
     * @author AndreaAltomare
     */
    public ChooseType getChosenStatus() {
        return chosen;
    }

    /**
     * Sets the Choose status for the Worker
     *
     * @param chosen (ChooseType status to set)
     * @author AndreaAltomare
     */
    public void setChosen(ChooseType chosen) {
        this.chosen = chosen;
    }

    //added later to make each worker identified by a unique String
    /**
     * Method that return a unique string for each worker
     *
     * @return (unique String denoting eachWorker).
     */
    @Override
    public String toString(){
        return "[Worker]\t"+id;
    }

    /**
     * An alternative method to get a Worker's identifier.
     *
     * @return The unique Worker's identifier.
     * @author AndreaAltomare
     */
    public String getWorkerId() {
        return this.toString();
    }

    /**
     *
     * @return Worker's color
     * @author AndreaAltomare
     */
    public Color getColor() {
        return color;
    }

    /**
     * Registers the color for a Worker just placed.
     *
     * @author AndreaAltomare
     */
    public void registerColor() {
        this.color = colors[lastColor];
        lastColor++;
    }

    /**
     * Given an ID as a String, it retrieves
     * its numeric ({@code int}) ID and sets it.
     *
     * @param workerId Worker's ID
     * @author AndreaAltomare
     */
    public void setId(String workerId) {
        int id = 0;

        String[] splittedId = workerId.split("\t");
        //String stringPart = splittedId[0];
        String numericPart = splittedId[1];
        id = Integer.parseInt(numericPart);

        this.id = id;
    }

    /**
     *
     * @param color Worker's color
     * @author AndreaAltomare
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Resets Class members once all Workers have been placed.
     *
     * @author AndreaAltomare
     */
    public static void resetIdAndColorIndex() {
        lastId = 0;
        lastColor = 0;
    }
}
