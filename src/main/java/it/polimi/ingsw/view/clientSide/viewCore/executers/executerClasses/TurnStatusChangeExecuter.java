package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.model.StateType;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.events.TurnStatusChangeEvent;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.EventObject;

public class TurnStatusChangeExecuter extends Executer {
    private ViewSubTurn stato;

    /**
     * Method that reset the executer with initial values.
     */
    public void clear(){
        stato = null;
    }

    /**
     * constructor
     */
    public TurnStatusChangeExecuter(){
        this.clear();
    }

    /**
     * Method that sets the status.
     *
     * @param status (ViewSubTurn identifying the status).
     * @throws WrongParametersException (if status is null).
     */
    public void setStatusId(ViewSubTurn status)throws WrongParametersException {
        if(status == null) throw new WrongParametersException();
        if(status.toStateType()== StateType.NONE) throw new WrongParametersException();
        this.stato = status;
    }

    /**
     * Method that sets the status.
     *
     * @param status (String identifying the status).
     * @throws WrongParametersException (if status doesn't represent a valid status).
     */
    public void setStatusId(String status)throws WrongParametersException {
        try {
            setStatusId(ViewSubTurn.search(status));
        } catch (NotFoundException e) {
            throw new WrongParametersException();
        }
    }

    /**
     * Static method that returns aunivoque string for each type of Exeuter (in this specific case "[Executer]").
     *
     * @return (Stirng univocally identifying the Executer type).
     */
    public static String myType(){ return Executer.myType() + "\tTurnStatusChange"; }

    @Override
    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     * @throws CannotSendEventException (if the Executer doesn't have all the information needed  by the Event)
     */
    public EventObject getMyEvent()throws CannotSendEventException {
        if(stato == null) throw new CannotSendEventException("No valid status set");
        return new TurnStatusChangeEvent(stato.toStateType());
    }
}
