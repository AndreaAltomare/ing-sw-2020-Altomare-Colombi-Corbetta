package it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.ViewObject;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;
import org.jetbrains.annotations.NotNull;

import java.util.EventObject;

/**
 * Class to rapresent the board.
 * !! there will be no more than one instance in each client!!
 *
 * @author giorgio
 */
public class ViewBoard implements ViewObject {

    private int xDim;
    private int yDim;

    private ViewCell[][] realBoard;

    private static ViewBoard board;

    /**
     * Getter of the x-dimension of the board.
     *
     * @return (the x-dimension of the board)
     */
    public int getXDim(){ return xDim; }
    /**
     * Getter of the y-dimension of the board.
     *
     * @return (the y-dimension of the board)
     */
    public int getYDim(){ return yDim; }

    /**
     * Method that returns the Cell at the selected position.
     *
     * @param x (x-position)
     * @param y (y-position)
     * @return (the cell on the selected position)
     * @throws NotFoundException (iif it's accessing outside the borders)
     */
    public ViewCell getCellAt(int x, int y) throws NotFoundException {
        if(x<0 || x>this.getXDim() || y<0 || y>this.getYDim())
            throw new NotFoundException();
        return realBoard[x][y];
    }

    /**
     * Method that returns the String identifying the object built as: "[ClassId] \t objId".
     *
     * @return (String identifyinng the object)
     */
    public String toString(){ return getClassId(); }

    /**
     * Method to compare two ViewObjects
     *
     * @param obj (compared object)
     * @return (true iif this == obj)
     */
    public boolean equals(ViewObject obj){ return this.toString().equals(obj.toString()); }

    /**
     * function that returns for each Class the Base of its objects identificators as "[ClassId]".
     *
     * @return (String the base of Class identificators)
     */
    public static String getClassId(){ return "[Board]\t"; }

    /**
     * Method to check weather the passed id is of this class or not.
     *
     * @param id (String to check)
     * @return (True iif the String will correspond to the id of an object of this class).
     */
    public static boolean isOfThisClass( @NotNull String id){ return id.startsWith(getClassId()); }

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
        if(board == null)
            throw new NotFoundException();
        return board;
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
        if(board == null)
            throw new NotFoundException();
        return board;
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
        return false;
    }

    /**
     * Method that will be called on the arrival of an event to build a new Object.
     *
     * @param event (the Event arrived)
     * @return (the new object created)
     * @throws WrongEventException (if the Event is not supported by this Class)
     */
    public static ViewObject populate( @NotNull EventObject event) throws WrongEventException{
        //todo implement it
        throw new WrongEventException();
    }

    /**
     * Method to discard all the objects of the Class.
     */
    public static void clear(){
        board = null;
    }

    /**
     * Method that will return a String to write the ViewObject on the Terminal.
     *
     * @return (String representing the object and its status)
     */
    public String toTerminal(){
        String ret = this.toString() + "\n";
        for(int x = 0; x< this.getXDim(); x++) {
            for (int y = 0; y < this.getYDim(); y++) {
                try {
                    ret += this.getCellAt(x, y).toString();
                } catch (NotFoundException ignore) {   }
            }
            ret += "\n";
        }
        return ret;
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
}