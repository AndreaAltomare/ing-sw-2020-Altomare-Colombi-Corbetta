package it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.ViewObject;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * Class intended to represent the Cell in the View package.
 *
 * @author giorgio
 */
public class ViewCell implements ViewObject {

    private int x;
    private int y;
    private int level;

    private boolean doomed;
    private boolean thereIsWorker;

    private ViewWorker worker;

    private static List<ViewCell> myList = new ArrayList<ViewCell>();

    /**
     * Getter for the x-coordinate.
     *
     * @return (int x-coordinate)
     */
    public int getX(){ return x; }

    /**
     * Getter for the y-coordinate.
     *
     * @return (int y-coordinate)
     */
    public int getY(){ return y; }

    /**
     * Getter for the level.
     *
     * @return (int the level of the construction on this cell)
     */
    public int getLevel(){
        if(level<0)
            level = 0;
        return level;
    }


    /**
     * Getter of doomed status.
     *
     * @return (true iif there is a dome on it)
     */
    public boolean isDoomed(){ return doomed; }

    /**
     * pseudo-getter for the worker
     *
     * @return (true iif there is a worker on this cell)
     */
    public boolean isThereWorker(){ return thereIsWorker; }

    /**
     * Getter of the worker on this cell.
     *
     * @return (ViewWorker on this cell (null if there is none))
     */
    public ViewWorker getWorker(){ return worker; }

    /**
     * Getter of the worker on this cell.
     *
     * @return (String id of the worker on the cell (or "" if none))
     */
    public String getWorkerString(){ return (worker!=null?worker.toString():""); }

    /**
     * Method to build a dome on this cell
     */
    void buildDome(){ doomed = true; }
    /**
     * Method to remove a dome on this cell
     */
    void removeDome(){ doomed = false; }

    /**
     * Method to build a level on this cell
     */
    void buildLevel(){ level++; }
    /**
     * Method to remove a dome on this cell
     */
    void removeLevel(){ level=(level>0?level-1: 0); }
    /**
     * Method to set the level of this cell.
     *
     * @param level (int level of this cell)
     */
    void setLevel(int level){ this.level = (level>0?level:0); }

    /**
     * Method to remove the ViewWorker on this cell.
     */
    void removeWorker(){
        thereIsWorker = false;
        worker = null;
    }

    /**
     * Method to place a ViewWorker on this cell.
     *
     * @param worker (the worker to place on it)
     */
    void placeWorker( @NotNull ViewWorker worker){
        thereIsWorker = true;
        this.worker = worker;
    }

    /**
     * Method that returns the String identifying the object built as: "[ClassId] \t objId".
     *
     * @return (String identifyinng the object)
     */
    public String toString(){ return getClassId() + "(" + this.getX() + ", " + this.getY() + ")"; }

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
    public static String getClassId(){ return "[Cell]\t"; }


    /**
     * Method to check weather the passed id is of this class or not.
     *
     * @param id (String to check)
     * @return (True iif the String will correspond to the id of an object of this class).
     */
    public static boolean isOfThisClass(@NotNull String id){ return id.startsWith(getClassId()); }

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
    public static ViewObject find( @NotNull String id) throws NotFoundException, WrongViewObjectException{
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
        //todo: implement it
        return true;
    }

    /**
     * Method that will be called on the arrival of an event to build a new Object.
     *
     * @param event (the Event arrived)
     * @return (the new object created)
     * @throws WrongEventException (if the Event is not supported by this Class)
     */
    static ViewObject populate( @NotNull EventObject event) throws WrongEventException{
        //todo: implement it
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
    public String toTerminal(){
        return this.toString() + " " + this.getLevel() + " D: " + (this.isDoomed()?'t': 'f') + " W: " + (this.isThereWorker()?worker.getId()%10:"no") + "| ";

    }


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
    private static ViewCell cSearch( @NotNull String id) throws NotFoundException {
        for (ViewCell i: myList)
            if(i.toString().equals(id))
                return i;
        throw new NotFoundException();
    }

    /**
     * Constructor
     *
     * @param x (x coordinate)
     * @param y (y coordinate)
     */
    public ViewCell(int x, int y){
        this.x = x;
        this.y = y;
        this.level = 0;
        this.doomed = false;
        this.thereIsWorker = false;
        this.worker = null;
    }


}