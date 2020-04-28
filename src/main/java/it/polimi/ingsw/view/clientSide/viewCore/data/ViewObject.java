package it.polimi.ingsw.view.clientSide.viewCore.data;

import it.polimi.ingsw.view.clientSide.viewCore.interfaces.ClientAddressable;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;
import it.polimi.ingsw.view.interfaces.Addressable;
import org.jetbrains.annotations.NotNull;
//import sun.jvm.hotspot.types.WrongTypeException;

import java.util.EventObject;

/**
 * Interface for all the data representations available to the View side
 *
 * @author giorgio
 */
public abstract class ViewObject implements ClientAddressable {

    /**
     * Method that returns the String identifying the object built as: "[ClassId] \t objId".
     *
     * @return (String identifying the object)
     */
    public String toString(){
        String tmp = this.getId();
        if(tmp.equals(""))
            return this.getMyClassId();
        return this.getMyClassId() + "\t" + this.getId();
    }


    /**
     * Method that returns the id of the specific object in the class
     *
     * @return (String the id of the object or "" if no id is needed).
     */
    public abstract String getId();

    /**
     * Method returning a unique String for each class.
     *
     * @return (unique string for each class)
     */
    public abstract String getMyClassId();

    /**
     * Method to compare two ViewObjects
     *
     * @param obj (compared object)
     * @return (true iif this == obj)
     */
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
    public boolean isThis ( @NotNull String st){
        return st.equals(this.toString());
    }

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
     * Method that will return a String to write the ViewObject on the Terminal.
     *
     * @return (String representing the object and its status)
     */
    String toTerminal(){return "404: object not found";}


    /**
     * Method that will return a (Object) that will represent the ViewObject on the CLI.
     *
     * @return (representation of Object for the CLI)
     */
    Object toCLI(){return null;}

    /**
     * Method that will return a (Object) that will represent the ViewObject on the GUI.
     *
     * @return (representation of Object for the GI)
     */
    Object toGUI(){return null;}

}
