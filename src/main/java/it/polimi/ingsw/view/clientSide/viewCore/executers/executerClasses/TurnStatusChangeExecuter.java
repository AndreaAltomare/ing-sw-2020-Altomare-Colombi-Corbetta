package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.model.player.turn.StateType;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.events.TurnStatusChangeEvent;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.EventObject;

/**
 * Class to execute the Turn Status Change.
 *
 * @see Executer
 * @author giorgio
 */
public class TurnStatusChangeExecuter extends Executer {
    private ViewSubTurn stato;

    /**
     * Method that reset the executer with initial values.
     */
    @Override
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

    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     * @throws CannotSendEventException (if the Executer doesn't have all the information needed  by the Event)
     */
    @Override
    public EventObject getMyEvent()throws CannotSendEventException {
        if(stato == null) throw new CannotSendEventException("No valid status set");
        if(stato == ViewSubTurn.BUILD_BLOCK || stato == ViewSubTurn.BUILD_DOME){
            Viewer.setAllSubTurnViewer(stato);
            return null;
        }
        return new TurnStatusChangeEvent(stato.toStateType());
    }

    /*public void send(EventObject event) throws NullPointerException{
        if(event == null) return;
        getSender().send((TurnStatusChangeEvent)event);
    }*/

    /**
     * Method returning ture iif this Executer has to wait undo-timeout.
     *
     * @return (ture iif this Executer has to wait undo-timeout).
     */
    protected boolean checkUndo(){
        return true;
    }
}
