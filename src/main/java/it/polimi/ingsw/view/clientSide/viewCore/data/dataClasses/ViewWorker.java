package it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.ViewObject;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.exceptions.AlreadySetException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * Class intended to represent the Worker in the View Packace.
 *
 * @author giorgio
 */
public class ViewWorker extends ViewObject {

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

    /**
     * function that returns for each Class the Base of its objects identificators as "[ClassId]".
     *
     * @return (String the base of Class identificators)
     */
    public static String getClassId(){
        return "[Worker]";
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
    public ImagePanel toGUI(){
        if(ViewNickname.getMyNickname().equals(getPlayer().getName()))
            return new ImagePanel(1, 1, 0 ,0 , "/img/board/cells/my_worker.png");
        else
            return new ImagePanel(1, 1, 0 ,0 , "/img/board/cells/adv_worker.png");
    }

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
        this.id = Integer.parseInt(id.substring(getClassId().length()+1));
        this.player = player;
        try {
            player.addWorker(this);
        } catch (AlreadySetException e) {
            throw new NotFoundException();
        }
        this.position = null;

        myList.add(this);
    }

    public static boolean isOfThisClass(String id){
        return id.startsWith(getClassId());
    }

    /**
     * Constructor
     *
     * @param id (String id of the Worker)
     * @param player (id of the player owning the worker)
     * @throws NotFoundException (iif player doesn't exists)
     */
    public ViewWorker(String id, @NotNull String player) throws NotFoundException, WrongViewObjectException {
        this(id, findOrSearch(player));
    }

    public void placeOn(int x, int y){
        if(position != null) position.removeWorker();
        try {
            position = ((ViewBoard)ViewBoard.search(ViewBoard.getClassId())).getCellAt(x, y);
        } catch (NotFoundException | WrongViewObjectException e) {
            position = null;
            return;
        }
        position.placeWorker(this);
    }

    public void removeWorker(){
        if(position!=null) position.removeWorker();
    }

    protected static ViewPlayer findOrSearch(String name) throws NotFoundException{
        try {
            return (ViewPlayer)ViewPlayer.find(name);
        }  catch (WrongViewObjectException e) {
            return ViewPlayer.searchByName(name);
        }
    }
}
