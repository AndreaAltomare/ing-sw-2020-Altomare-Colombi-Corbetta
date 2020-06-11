package it.polimi.ingsw.view.clientSide.viewCore.data;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.interfaces.ClientAddressable;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;
import it.polimi.ingsw.view.interfaces.Addressable;
import org.jetbrains.annotations.NotNull;
//import sun.jvm.hotspot.types.WrongTypeException;

import javax.swing.*;
import java.util.EventObject;

/**
 * Interface for all the data representations available to the View side.
 *
 * Last review: 8/06/2020
 * @author giorgio
 */
public abstract class ViewObject implements ClientAddressable {

    /**
     * Method that returns the String identifying the object built as: "[ClassId] \t objId".
     *
     * @return (String identifying the object)
     */
    @Override
    public String toString(){
        String tmp = this.getId();
        if(tmp.equals(""))
            return this.getMyClassId();
        return this.getMyClassId() + "\t" + this.getId();
    }


    /**
     * Method that returns the id of the instance inside the class.
     *
     * @return (String the id of the object or "" if no id is needed).
     */
    @Override
    public abstract String getId();

    /**
     * Method returning a unique String for each class.
     *
     * @return (unique string for each class)
     */
    @Override
    public abstract String getMyClassId();

    /**
     * Method to compare two ViewObjects.
     *
     * @param obj (compared object)
     * @return (true iif this == obj)
     */
    @Override
    public boolean equals(Addressable obj){
        if (obj == null)
            return false;
        return isThis(obj.toString());
    }

    /**
     * Method checking weather the given string is identifying this.
     *
     * @param st (String that will possibly represent this)
     * @return (true iif st==this.toString())
     */
    @Override
    public boolean isThis (String st){
        return st.equals(this.toString());
    }

    /**
     * function that returns for each Class the Base of its objects identifications as "[ClassId]".
     *
     * @return (String the base of Class identificators)
     */
    static String getClassId(){
        return "[ViewObject]\t";
    }


    /**
     * Method to check weather the passed id is of this class or not.
     *
     * @param id (String to check)
     * @return (True iif the String will correspond to the id of an object of this class).
     */
    protected static boolean isOfThisClass(@NotNull String id){
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
    static ViewObject search( @NotNull String id) throws NotFoundException, WrongViewObjectException{
        throw new WrongViewObjectException();
    }

    /**
     * Method that will search the object with the passed id; if it doesn't exists then try to create it.
     *
     * @param id (String, the toString result of the searched Object)
     * @return (The searched Object)
     * @throws NotFoundException (If it doesn't find the object and cannot build it)
     * @throws WrongViewObjectException (If the object is not of this Class).
     */
    static ViewObject find( @ NotNull String id) throws NotFoundException, WrongViewObjectException{
        throw new WrongViewObjectException();
    }

    /**
     * Method that will be called on the arrival of an event on this object.
     *
     * @param event (The Event to be notified)
     * @return (true iif the event is notified in the right way)
     * @throws WrongEventException (if the Event is not used for this object)
     */
    boolean notifyEvent( @NotNull EventObject event) throws WrongEventException{
        throw new WrongEventException();
    }

    /**
     * Method that will be called on the arrival of an event to build a new Object.
     *
     * @param event (the Event arrived)
     * @return (the new object created)
     * @throws WrongEventException (if the Event is not supported by this Class)
     */
    static ViewObject populate( @NotNull EventObject event) throws WrongEventException{
        throw new WrongEventException();
    }

    /**
     * Method to discard all the objects of the Class.
     */
    public static void clear(){

    }

    /**
     * Method that clears all the data represented on the View.
     */
    public static void clearAll(){
        ViewBoard.clear();
        ViewCard.clear();
        ViewCell.clear();
        ViewPlayer.clear();
        ViewWorker.clear();
        ViewObject.clear();
    }

    /**
     * Method that will return a String to write the ViewObject on the Terminal.
     *
     * @return (String representing the object and its status)
     */
    public String toTerminal(){return "404: object not found";}

    /**
     * Method that will return a String tha represent the ViewObject on the Terminal CLI
     * @return (String representing the object and its status)
     */
    public String toWTerminal() {return null;}

    /**
     * Method that will return a (Object) that will represent the ViewObject on the CLI.
     *
     * @return (representation of Object for the CLI)
     */
    public String toCLI(){return null;}

    /**
     * Method that will return a (Object) that will represent the ViewObject on the GUI.
     *
     * @return (representation of Object for the GI)
     */
    public JPanel toGUI(){return null;}

}
