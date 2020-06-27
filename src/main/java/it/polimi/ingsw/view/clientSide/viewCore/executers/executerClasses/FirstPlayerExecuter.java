package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.controller.events.RequireStartPlayerEvent;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.SetStartPlayerEvent;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import java.util.ArrayList;
import java.util.EventObject;

/**
 * Class to execute a First Player.
 *
 * @see Executer
 */
public class FirstPlayerExecuter extends Executer {

    private ArrayList<String> playerList;
    private String myChoice;

    /**
     * Method that reset the executer with initial values.
     */
    @Override
    public void clear() {
        playerList = null;
        myChoice = null;
    }

    /**
     * Method populating this (with needed info).
     *
     * @param event (the <code>RequireStartPlayerEvent</code> containing the needed informations).
     */
    public void populate(RequireStartPlayerEvent event){
        clear();
        playerList = new ArrayList<String>(event.getPlayers());
    }


    /**
     * Method returning the list of payers available to be set as first.
     *
     * @return (the list of available first player)
     */
    public ArrayList<String> getPlayerList(){
        return new ArrayList<>(playerList);
    }


    /**
     * Method that sets the first player  to be the one represented by the given parameter.
     *
     * @param choosed (the <code>String</code> representing the player).
     */
    public void set(String choosed){
        myChoice = choosed;
    }

    /**
     * Method setting the first player in a random way.
     * Intended to be used if no selecction method available.
     */
    public void setRandom(){
        try {
            myChoice = playerList.get(0);
        }catch (Exception e){
            myChoice = null;
        }
    }

    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     * @throws CannotSendEventException (if the Executer doesn't have all the information needed  by the Event)
     */
    @Override
    public EventObject getMyEvent()throws CannotSendEventException {
        if(myChoice == null) throw new CannotSendEventException("You have to choose a player!");
        return new SetStartPlayerEvent(myChoice);
    }
}
