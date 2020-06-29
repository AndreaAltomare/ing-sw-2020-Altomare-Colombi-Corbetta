package it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.ViewObject;
import it.polimi.ingsw.view.exceptions.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.EventObject;

/**
 * Class to represent the nickname of the current player
 *
 * @author giorgio
 */
public class ViewNickname extends ViewObject {

    private String name;

    private ViewCard card;

    private static ViewNickname el = null;

    public static ViewNickname getNickname(){ return el; }

    public ViewCard getCard(){ return this.card; }

    /**
     * Method returning the nickname of the player.
     *
     * @return (the nickname of the player).
     */
    @Override
    public String getId() {
        return name;
    }

    /**
     * Method returning a unique String for each class.
     * For ViewNickname it's: "[Nickname]"
     *
     * @return ("[Nickname]")
     */
    @Override
    public String getMyClassId() {
        return getClassId();
    }


    /**
     * function that returns a string identifying the Class: "[Nickname]".
     *
     * @return ("[Nickname]")
     */
    public static String getClassId(){
        return "[Nickname]";
    }

    /**
     * Method that will search the ViewBoard with the passed id.
     * If it is searched any instance of the <code>ViewNickname</code>, it returns the only one that is available.
     *
     * @param id (String, the toString result of the searched Object)
     * @return (The searched Object)
     * @throws NotFoundException (If it doesn't find the object)
     * @throws WrongViewObjectException (If the object is not of this Class).
     */
    public static ViewObject search( @NotNull String id) throws NotFoundException, WrongViewObjectException{
        if(!isOfThisClass(id)||el==null)
            throw new WrongViewObjectException();
        return el;
    }

    /**
     * Method that will search the ViewBoard with the passed id.
     * If it is searched any instance of the <code>ViewNickname</code>, it returns the only one that is available.
     * If the ViewNickname doesn't exist, than it'll throw WrongViewObjectException
     *
     * @param id (String, the toString result of the searched Object)
     * @return (The searched Object)
     * @throws NotFoundException (If it doesn't find the object)
     * @throws WrongViewObjectException (If the object is not of this Class).
     */
    public static ViewObject find( @NotNull String id) throws NotFoundException, WrongViewObjectException{
        if(!isOfThisClass(id)||el==null)
            throw new WrongViewObjectException();
        return el;
    }

    /**
     * Method that will be called on the arrival of an event on this object.
     * It'll do nothing.
     *
     * @param event (The Event to be notified)
     * @return (true iif the event is notified in the right way)
     * @throws WrongEventException (if the Event is not used for this object)
     */
    public boolean notifyEvent( @NotNull EventObject event) throws WrongEventException{
        return true;
    }

    /**
     * Method that will be called on the arrival of an event to build a new Object.
     * It'll do nothing
     *
     * @param event (the Event arrived)
     * @return (the new object created)
     * @throws WrongEventException (if the Event is not supported by this Class)
     */
    public static ViewObject populate( @NotNull EventObject event) throws WrongEventException{
        throw new WrongEventException();
    }

    /**
     * Method to discard all the objects of the Class.
     */
    public static void clear(){
        el = null;
    }

    /**
     * Method that will return a String to write the ViewObject on the Terminal.
     *
     * @return (String representing the object and its status)
     */
    @Override
    public String toTerminal(){
        return this.toString();
    }

    /**
     * Method that will return a (Object) that will represent the ViewObject on the CLI.
     *
     * @return (representation of Object for the CLI)
     */
    @Override
    public String toWTerminal(){
        return null;
    }

    /**
     * Method that will return a (Object) that will represent the ViewObject on the CLI.
     *
     * @return (representation of Object for the CLI)
     */
    @Override
    public String toCLI(){
        return null;
    }

    /**
     * Method that will return a (Object) that will represent the ViewObject on the GUI.
     *
     * @return (representation of Object for the GI)
     */
    @Override
    public JPanel toGUI(){
        return null;
    }

    /**
     * Method checking weather the given string is identifying this.
     *
     * @param st (String that will possibly represent this)
     * @return (true iif st==this.toString())
     */
    public boolean isThis (String st) {
        if(st==null) return false;
        return (st.equals(this.toString())||st.equals(this.getId()));
    }

    /**
     * Constructor.
     *
     * @param nickname (String, nickname dell'utente)
     */
    private ViewNickname(String nickname){
        this.name = nickname;
        this.card = null;
        el = this;
    }

    /**
     * Method that sets the nickname of the player.
     *
     * @param nickname  (the nickname of the player).
     * @throws AlreadySetException  (iif there has been already a nickname set).
     * @throws WrongParametersException (if the nickname is invalid).
     */
    public static void setNickname(String nickname) throws AlreadySetException, WrongParametersException {
        if(el != null) throw new AlreadySetException();
        if(false) throw new WrongParametersException();
        new ViewNickname(nickname);
    }

    /**
     * Method that will return the nickname.
     *
     * @return (String: nickname or null)
     */
    public static String getMyNickname(){
        if(el!=null) return el.getId();
        return null;
    }

    /**
     * Method that returns the card choosen by the player.
     *
     * @return (ViewCard choosen by the player; null if not chosen)
     */
    public static ViewCard getMyard(){
        if(el!=null) return el.getCard();
        return null;
    }

    /**
     * Method to set the card choosen by the player.
     *
     * @param card (the <code>ViewCard</code> choosen by the player).
     */
    public void setCard(ViewCard card){ this.card = card; }

    /**
     * Method to set the card choosen by the player.
     *
     * @param card (the name of the choosen by the player).
     */
    public void setCard(String card){
        try {
            this.setCard((ViewCard)ViewCard.search(card));
        } catch (NotFoundException | WrongViewObjectException ignore) {
        }
    }


    /**
     * static setter of player's card.
     * It'll set the card iif the player nickname has already been set.
     *
     * @param card (the <code>ViewCard</code> choosen by the player).
     */
    public static void setMyCard(ViewCard card){ if(el!= null) el.setCard(card); }

    /**
     * static setter of player's card.
     * It'll set the card iif the player nickname has already been set.
     *
     * @param card (the name of the choosen by the player).
     */
    public static void setMyCard(String card){ if(el!= null) el.setCard(card); }
}
