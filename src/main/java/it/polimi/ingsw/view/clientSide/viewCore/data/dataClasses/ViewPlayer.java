package it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses;

import it.polimi.ingsw.controller.events.CardSelectedEvent;
import it.polimi.ingsw.controller.events.ServerSendDataEvent;
import it.polimi.ingsw.view.clientSide.viewCore.data.ViewObject;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
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
 * Class intended to represent the players in the view package.
 *
 * @author giorgio
 */
public class ViewPlayer extends ViewObject {

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
     * Method returning one ViewWorker of the Player or null if not existing.
     *
     * @return (one ViewWorker of the Player or null if not existing).
     */
    // CLI prloblem of color
    public ViewWorker getOneWorker() {
        return workers[0];
    }

    /**
     * Method to set the card for this player.
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

    /**
     * Method returning a unique string for each object inside the Class.
     * returns the name of the player.
     *
     * @return (getName())
     */
    @Override
    public String getId() {
        return getName();
    }

    /**
     * Method returning a unique String for each class.
     * ViewPlayer returns "[Player]"
     *
     * @return ("[Player]")
     */
    @Override
    public String getMyClassId() {
        return getClassId();
    }

    /**
     * function that returns a unique class identifier: "[Player]".
     *
     * @return ("[Player]")
     */
    public static String getClassId(){ return "[Player]"; }

    /**
     * Method that will search the ViewPlayer with the passed id.
     *
     * @param id (String, the toString result of the searched ViewPlayer)
     * @return (The searched ViewPlayer)
     * @throws NotFoundException (If it doesn't find the object)
     * @throws WrongViewObjectException (If the object is not of this Class).
     */
    public static ViewObject search( @NotNull String id) throws NotFoundException, WrongViewObjectException{
        if(!isOfThisClass(id))
            throw new WrongViewObjectException();
        return cSearch(id);
    }

    /**
     * Method that will search the ViewPlayer with the passed id; if it doesn't exists then try to create it.
     *
     * @param id (String, the toString result of the searched ViewPlayer)
     * @return (The searched ViewPlayer)
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
     * Method that will be called on the arrival of an event to build the new ViewPlayers.
     * When called it'll create a new ViewPlayer for each player into the <code>ServerSendDataEvent</code>
     * and adds the relative Workers to them.
     *
     * @param data (the Event arrived)
     * @return (the new object created)
     * @throws WrongEventException (if the Event is not supported by this Class)
     */
    public static ViewObject populate( @NotNull ServerSendDataEvent data) throws WrongEventException{


        for (String player : data.getPlayers()) {
            //CHECK IF IT WORKS
            try {
                searchByName(player);
            } catch (NotFoundException e) {
                new ViewPlayer(player);
            }
            //PREVIOUSLY: new ViewPlayer(player);

            List<String> workers = data.getWorkersToPlayer().get(player);
            if(workers!=null) {
                for (String worker : workers) {
                    try {
                        new ViewWorker(worker, player);
                    } catch (NotFoundException | WrongViewObjectException e) {
                        throw new WrongEventException();
                    }
                }
            }
        }
        return null;
        //throw new WrongEventException();
    }

    /**
     * This method is called with <code>CardSelectedEvent</code> assigning at a Player its Card.
     *
     * @param event (the <code>CardSelectedEvent</code> to be notified).
     * @return  (the ViewPlayer to which has been set the Card).
     * @throws WrongEventException (iif the Player doesn't exists).
     */
    public static ViewObject populate(CardSelectedEvent event) throws WrongEventException{
        ViewPlayer ret;
        try {
            ret = ViewPlayer.searchByName(event.getPlayerNickname());
        } catch (NotFoundException e) {
            throw new WrongEventException();
        }

        ret.setCard(event.getCardName());
/*
        try {
            System.out.println("Player:\t" + ret.getName() + " \tCard:\t" + ret.getCard().getName());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }*/

        return ret;
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
    @Override
    public String toTerminal(){ return this.toString() + "\n\t" + card.toString() + "\n\t" + (workers[0]!=null?workers[0].toString():"null") + "\n\t" + (workers[1]!=null?workers[1].toString():"null") + "\n"; }


    /**
     * Method which returns <code>ViewPLayer</code>'s name like representation of ViewPLayer on the WTerminal
     *
     * @return <code>ViewPLayer</code>'s name
     */
    @Override
    public String toWTerminal(){ return this.getName(); }

    /**
     * Method which returns <code>ViewPLayer</code>'s name with the same color of his workers, if there are.
     * The returned <code>String</code> is used on CLI to represented the player
     *
     * @see ViewWorker
     * @return <code>ViewPLayer</code>'s name with his workers' color
     */
    @Override
    public String toCLI(){
        String playerString = this.getName();
        String playerColor = ""; // nothing color
        ViewWorker[] workerArray;

        try {
            workerArray = this.getWorkers();
            playerColor = workerArray[0].getWorkerCLIColor();
        } catch (NotFoundException | NullPointerException e) {
            playerColor = "";
        }

        playerString = playerColor + playerString + ANSIStyle.RESET;

        return playerString;

    }

    /**
     * Method that will return a (Object) that will represent the ViewObject on the GUI.
     *
     * @return (representation of Object for the GI)
     */
    @Override
    public JPanel toGUI(){ return null; }

    /**
     * Method that will search the ViewPlayer with the passed id.
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
     * Method to search for the player with a certain name.
     *
     * @param name (the name of the searched player)
     * @return (the player with the searched name)
     * @throws NotFoundException (iif the searched name is of no player)
     */
    public static ViewPlayer searchByName( @NotNull String name) throws NotFoundException {
        for (ViewPlayer i: myList) {
            if (i.getName().equals(name))
                return i;
        }
        throw new NotFoundException();
    }

    /**
     * Method to set the Player's card.
     *
     * @param cardName (the Card's name).
     */
    public void setCard(String cardName){
        try {
            setCard((ViewCard) ViewCard.search(cardName));
        } catch (NotFoundException | WrongViewObjectException | AlreadySetException ignored) {
        }
    }

    /**
     * constructor.
     *
     * @param name (player's name).
     */
    public ViewPlayer(String name){

        this.name = name;
        this.card = null;
        this.workers[0] = null;
        this.workers[1] = null;

        //CHANGED FOR THE CLI
        try {
            myList.remove(searchByName(name));
        } catch (NotFoundException e) {
            //e.printStackTrace();
        }

        myList.add(this);
    }

    /**
     * Method returning the number of Players.
     *
     * @return (the number of Players).
     */
    public static int getNumberOfPlayers(){
        return myList.size();
    }

    /**
     * Method returning a list of all Players.
     *
     * @return (list of all Players).
     */
    public static List<ViewPlayer> getPlayerList() {
        return new ArrayList<ViewPlayer>(myList);
    }

}
