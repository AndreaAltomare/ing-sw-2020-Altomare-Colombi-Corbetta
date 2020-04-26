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
 * Class intended to represent the player in the view package.
 *
 * @author giorgio
 */
public class ViewPlayer implements ViewObject {

    private String name;
    private ViewCard card;
    private ViewWorker[] workers = new ViewWorker[2];

    private static List<ViewPlayer> myList = new ArrayList<ViewPlayer>();

    /**
     * Getter for the name
     *
     * @return (String the name of this player)
     */
    public String getName(){ return name; }

    /**
     * Getter for this player card
     *
     * @return (ViewCard, the card of this player if set)
     * @throws NotFoundException (iif card is null)
     */
    public ViewCard getCard() throws NotFoundException{
        if(card == null)
            throw new NotFoundException();
        return card;
    }

    /**
     * Getter for the workers of this player.
     *
     * @return (ViewWorker[] the array of the workers of this player)
     * @throws NotFoundException (iif the workers are not set)
     */
    public ViewWorker[] getWorkers() throws NotFoundException {
        if(this.workers[0] == null || this.workers[1] == null)
            throw new NotFoundException();
        return workers;
    }

    /**
     * Method to set the ard for this player.
     *
     * @param card (card to be set for this worker)
     * @throws AlreadySetException (if the card of this worker was already set)
     */
    public void setCard( @NotNull ViewCard card) throws AlreadySetException{
        if(this.card != null)
            throw new AlreadySetException();
        this.card = card;
    }

    /**
     * Method that adds a worker to this player.
     *
     * @param worker (The worker to be added to this player)
     * @return (true iif it is added, false iif it was already in this player's workers)
     * @throws AlreadySetException (if worker isn't into this player's workers and cannot add it)
     */
    public boolean addWorker( @NotNull ViewWorker worker) throws AlreadySetException{
        if(this.workers[0] == null){
            this.workers[0] = worker;
            return true;
        }else if(this.workers[0].equals(worker)){
            return false;
        }else if(this.workers[1]==null){
            this.workers[1] = worker;
            return true;
        }else if(this.workers[1].equals(worker)){
            return false;
        }
        throw new AlreadySetException();
    }

    @Override
    /**
     * Method returning a unique string for each object inside the Class.
     *
     * @return (unique String identifying the object)
     */
    public String getId() {
        return getName();
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
    public String toString(){ return getClassId() + "\t" + name; }

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
    public static String getClassId(){ return "[Player]"; }


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
        try {
            return cSearch(id);
        }catch (NotFoundException e){
            try{
                return new ViewPlayer(id);
            }catch (Exception p){
                throw new NotFoundException();
            }
        }
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
        //todo: implement it
        throw new WrongEventException();
    }

    /**
     * Method to discard all the objects of the Class.
     */
    public static void clear(){ myList.clear(); }

    /**
     * Method that will return a String to write the ViewObject on the Terminal.
     *
     * @return (String representing the object and its status)
     */
    public String toTerminal(){ return this.toString() + "\n\t" + card.toString() + "\n\t" + (workers[0]!=null?workers[0].toString():"null") + "\n\t" + (workers[1]!=null?workers[1].toString():"null") + "\n"; }


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
    private static ViewPlayer cSearch( @NotNull String id) throws NotFoundException {
        for (ViewPlayer i: myList)
            if(i.toString().equals(id))
                return i;
        throw new NotFoundException();
    }

    /**
     * Methodd to search for the player witha certain name.
     *
     * @param name (the name of the searched player)
     * @return (the player with the searched name)
     * @throws NotFoundException (iif the searched name is of no player)
     */
    public static ViewPlayer searchByName( @NotNull String name) throws NotFoundException {
        for (ViewPlayer i: myList)
            if(i.getName().equals(name))
                return i;
        throw new NotFoundException();
    }

    public ViewPlayer(String name){
        this.name = name;
        this.card = null;
        this.workers[0] = null;
        this.workers[1] = null;

        myList.add(this);
    }

}
