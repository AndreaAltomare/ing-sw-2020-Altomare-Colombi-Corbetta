package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.SetPlayersNumberEvent;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.EventObject;

public class SetPlayerNumberExecuter extends Executer {

    private Integer playerNumber;

    /**
     * Method that reset the executer with initial values.
     */
    public void clear(){
        playerNumber = null;
    }

    /**
     * constructor
     */
    public SetPlayerNumberExecuter(){
        this.clear();
    }

    /**
     * Method that sets the number of the players.
     *
     * @param num (Integer indicating the number of players).
     * @throws WrongParametersException (if num is not correct).
     */
    public void setNumberOfPlayers(Integer num)throws WrongParametersException {
        //todo check correcntess of num
        if (num==null || num <= 0)throw new WrongParametersException("Invalid number");
        this.playerNumber = num;
    }

    /**
     * Static method that returns a univoque string for each type of Exeuter (in this specific case "[Executer]").
     *
     * @return (Stirng univocally identifying the Executer type).
     */
    public static String myType(){ return Executer.myType() + "\tSetPlayerNumber"; }

    @Override
    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     * @throws CannotSendEventException (if the Executer doesn't have all the information needed  by the Event)
     */
    public EventObject getMyEvent()throws CannotSendEventException {
        if(playerNumber == null) throw new CannotSendEventException("No valid number set");
        SetPlayersNumberEvent setPlayerNumberEvent = new SetPlayersNumberEvent(playerNumber);
        playerNumber = null;
        return setPlayerNumberEvent;
    }

    /*public void send(EventObject event) throws NullPointerException{
        if(event == null) throw new NullPointerException();
        getSender().send((SetPlayersNumberEvent)event);
    }*/
}
