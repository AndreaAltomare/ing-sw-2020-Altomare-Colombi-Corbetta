package it.polimi.ingsw.view.clientSide.viewCore.data;

import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;
import org.jetbrains.annotations.NotNull;
//import sun.jvm.hotspot.types.WrongTypeException;

import java.util.EventObject;

/**
 * Interface for all the data representations available to the View side
 *
 * @author giorgio
 */
public interface ViewObject {

    /**
     * Method that returns the String identifying the object built as: "[ClassId] \t objId".
     *
     * @return (String identifyinng the object)
     */
    public String toString();

    /**
     * Method to compare two ViewObjects
     *
     * @param obj (compared object)
     * @return (true iif this == obj)
     */
    public boolean equals(ViewObject obj);

    /**
     * function that returns for each Class the Base of its objects identificators as "[ClassId]".
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
    static boolean isOfThisClass( @NotNull String id){
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
    boolean notifyEvent( @NotNull EventObject event) throws WrongEventException;

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
     * Method that will return a String to write the ViewObject on the Terminal.
     *
     * @return (String representing the object and its status)
     */
    String toTerminal();


    /**
     * Method that will return a (Object) that will represent the ViewObject on the CLI.
     *
     * @return (representation of Object for the CLI)
     */
    Object toCLI();

    /**
     * Method that will return a (Object) that will represent the ViewObject on the GUI.
     *
     * @return (representation of Object for the GI)
     */
    Object toGUI();

}
