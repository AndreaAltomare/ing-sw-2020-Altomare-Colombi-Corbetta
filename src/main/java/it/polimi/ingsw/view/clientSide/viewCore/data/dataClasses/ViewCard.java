package it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses;

//import com.sun.tools.javac.comp.Resolve;
import it.polimi.ingsw.view.clientSide.viewCore.data.ViewObject;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;
import it.polimi.ingsw.view.interfaces.Addressable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * Class intended to represent the Card.
 *
 * @author giorgio
 */
public class ViewCard extends ViewObject {

    private String name;
    private String epiteth;
    private String description;

    private static List<ViewCard> myList = new ArrayList<ViewCard>();

    public String getName(){ return name; }
    public String getEpiteth() { return epiteth; }
    public String getDescription() { return description; }

    @Override
    /**
     * Method returning a unique string for each object inside the Class.
     *
     * @return (unique String identifying the object)
     */
    public String getId() {
        return name;
    }

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
        return "[Card]";
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
        /*if(!isOfThisClass(id))
            throw new WrongViewObjectException();*/
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
    public static ViewObject populate( @NotNull EventObject event) throws WrongEventException{
        //todo immplement it
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
        return this.toString() + "\n" + epiteth + "\n" + description + "\n";
    }

    /**
     * Method that will return a (Object) that will represent the ViewObject on the CLI.
     *
     * @return (representation of Object for the CLI)
     */
    public Object toCLI(){
        return null;
    }

    /**
     * Method that will return a (Object) that will represent the ViewObject on the GUI.
     *
     * @return (representation of Object for the GI)
     */
    public Object toGUI(){
        return null;
    }

    /**
     * Method that will search the object with the passed id.
     *
     * @param id (String, the toString result of the searched Object)
     * @return (The searched Object)
     * @throws NotFoundException (If it doesn't find the object)
     */
    private static ViewCard cSearch( @NotNull String id) throws NotFoundException {
        for (ViewCard i: myList) {
            if (i.toString().equals(id))
                return i;
            if (i.getId().equals(id))
                return i;
        }
        throw new NotFoundException();
    }

    /**
     * Constructor
     *
     * @param name (String name)
     * @param epiteth (String epiteth)
     * @param description (tring description)
     */
    public ViewCard( @NotNull String name,  @NotNull String epiteth,  @NotNull String description){
        this.name = name;
        this.epiteth = epiteth;
        this.description = description;

        myList.add(this);
    }

    /**
     * Deprecated constructor useful for the tests.
     *
     * @param name (String name of the (test) card)
     */
    @Deprecated
    public ViewCard( @NotNull String name){
        this.name = name;
        this.epiteth = "";
        this.description = "";

        myList.add(this);
    }

}
