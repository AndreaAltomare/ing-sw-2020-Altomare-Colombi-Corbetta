package it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.ViewObject;
import it.polimi.ingsw.view.exceptions.AlreadySetException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;
import it.polimi.ingsw.view.interfaces.Addressable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * Class intended to represent the Worker in the View Packace.
 *
 * @author giorgio
 */
public class ViewWorker implements ViewObject {

    private int id;
    private ViewPlayer player;
    private ViewCell position;

    private static List<ViewWorker> myList = new ArrayList<ViewWorker>();

    /**
     * Getter method of the id of the worker
     *
     * @return (int id of the worker)
     */
    public String getId(){ return Integer.toString(id); }

    /**
     * Getter method for the player owner of the worker.
     *
     * @return (ViewPlayer owner of this worker)
     */
    public ViewPlayer getPlayer(){ return player; }

    /**
     * Getter for the current position o this
     *
     * @return (the Cell on which this is)
     * @throws NotFoundException (if it is on no cell).
     */
    public ViewCell getPosition() throws NotFoundException {
        if(this.position == null)
            throw new NotFoundException();
        return position;
    }

    /**
     * Setter for position.
     *
     * @param position (the new cell on which it has to be put (null if no cell))
     */
    public void moveOn(ViewCell position){ this.position = position; }

    @Override
    /**
     * Method returning a unique String for each class.
     *
     * @return (unique string for each class)
     */
    public String getMyClassId() {
        return getClassId();
    }

    @Override
    /**
     * Compares this with pl. return true iif represent the same Object.
     *
     * @param pl (the Addressable to be checked)
     * @return (true iif this == pl)
     */
    public boolean equals(Addressable obj) {
        return this.isThis(obj.toString());
    }

    @Override
    /**
     * Method checking weather the given string is identifying this.
     *
     * @param st (String that will possibly represent this)
     * @return (true iif st==this.toString())
     */
    public boolean isThis(String st) {
        return st.equals(this.toString());
    }

    /**
     * Method that returns the String identifying the object built as: "[ClassId] \t objId".
     *
     * @return (String identifyinng the object)
     */
    public String toString(){ return getClassId() + "\t" + id; }

    /**
     * Method to compare two ViewObjects
     *
     * @param obj (compared object)
     * @return (true iif this == obj)
     */
    public boolean equals(ViewObject obj){
        return this.toString().equals(obj.toString());
    }

    /**
     * function that returns for each Class the Base of its objects identificators as "[ClassId]".
     *
     * @return (String the base of Class identificators)
     */
    public static String getClassId(){
        return "[Worker]";
    }


    /**
     * Method to check weather the passed id is of this class or not.
     *
     * @param id (String to check)
     * @return (True iif the String will correspond to the id of an object of this class).
     */
    public static boolean isOfThisClass( @NotNull String id){
        return id.startsWith(getClassId());
    }

    /**
     * Method that will search the object with the passed id.
     *
     * @param id (String, the toString result of the searched Object)
     * @return (The searched Object)
     * @throws NotFoundException (If it doesn't find the object)
     * @throws WrongViewObjectException (If the object is not of this Class).
     */
    public static ViewObject search( @NotNull String id) throws NotFoundException, WrongViewObjectException{
        if(!isOfThisClass(id))
            throw new WrongViewObjectException();
        return cSearch(id);
    }

    /**
     * Method that will search the object with the passed id; if it doesn't exists then try to create it.
     *
     * @param id (String, the toString result of the searched Object)
     * @return (The searched Object)
     * @throws NotFoundException (If it doesn't find the object and cannot build it)
     * @throws WrongViewObjectException (If the object is not of this Class).
     */
    public static ViewObject find( @ NotNull String id) throws NotFoundException, WrongViewObjectException{
        if(!isOfThisClass(id))
            throw new WrongViewObjectException();
        return cSearch(id);
    }

    /**
     * Method that will be called on the arrival of an event on this object.
     *
     * @param event (The Event to be notified)
     * @return (true iif the event is notified in the right way)
     * @throws WrongEventException (if the Event is not used for this object)
     */
    public boolean notifyEvent( @NotNull EventObject event) throws WrongEventException{
        //TODO: implement it
        return true;
    }

    /**
     * Method that will be called on the arrival of an event to build a new Object.
     *
     * @param event (the Event arrived)
     * @return (the new object created)
     * @throws WrongEventException (if the Event is not supported by this Class)
     */
    public static ViewObject populate( @NotNull EventObject event) throws WrongEventException{
        //TODO: implement it
        throw new WrongEventException();
    }

    /**
     * Method to discard all the objects of the Class.
     */
    public static void clear(){
        myList.clear();
    }

    /**
     * Method that will return a String to write the ViewObject on the Terminal.
     *
     * @return (String representing the object and its status)
     */
    public String toTerminal(){ return this.toString(); }


    /**
     * Method that will return a (Object) that will represent the ViewObject on the CLI.
     *
     * @return (representation of Object for the CLI)
     */
    public Object toCLI(){ return null; }

    /**
     * Method that will return a (Object) that will represent the ViewObject on the GUI.
     *
     * @return (representation of Object for the GI)
     */
    public Object toGUI(){ return null; }

    /**
     * Method that will search the object with the passed id.
     *
     * @param id (String, the toString result of the searched Object)
     * @return (The searched Object)
     * @throws NotFoundException (If it doesn't find the object)
     */
    private static ViewWorker cSearch( @NotNull String id) throws NotFoundException {
        for (ViewWorker i: myList)
            if(i.toString().equals(id))
                return i;
        throw new NotFoundException();
    }

    /**
     * Constructor
     *
     * @param id (int id of the Worker)
     * @param player (ViewPlayer the player owning the worker)
     * @throws NotFoundException (iif player = null)
     */
    public ViewWorker(int id, ViewPlayer player) throws NotFoundException{
        if(player == null)
            throw new NotFoundException();
        this.id = id;
        try {
            player.addWorker(this);
        } catch (AlreadySetException e) {
            throw new NotFoundException();
        }
        this.player = player;
        this.position = null;

        myList.add(this);
    }

    /**
     * Constructor
     *
     * @param id (int id of the Worker)
     * @param player (id of the player owning the worker)
     * @throws NotFoundException (iif player doesn't exists)
     */
    public ViewWorker(int id, @NotNull String player) throws NotFoundException, WrongViewObjectException {
        this(id, (ViewPlayer)ViewPlayer.find(player));
    }

    /**
     * Constructor
     *
     * @param id (String id of the Worker)
     * @param player (ViewPlayer the player owning the worker)
     * @throws NotFoundException (iif player = null)
     */
    public ViewWorker(String id, ViewPlayer player) throws NotFoundException{
        if((player == null)||(!isOfThisClass(id)))
            throw new NotFoundException();
        this.id = Integer.parseInt(id.substring(getClassId().length()));
        this.player = player;
        try {
            player.addWorker(this);
        } catch (AlreadySetException e) {
            throw new NotFoundException();
        }
        this.position = null;

        myList.add(this);
    }

    /**
     * Constructor
     *
     * @param id (String id of the Worker)
     * @param player (id of the player owning the worker)
     * @throws NotFoundException (iif player doesn't exists)
     */
    public ViewWorker(String id, @NotNull String player) throws NotFoundException, WrongViewObjectException {
        this(id, (ViewPlayer)ViewPlayer.find(player));
    }
}
