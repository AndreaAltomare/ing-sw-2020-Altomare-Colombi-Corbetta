package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCell;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewWorker;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.PlaceWorkerEvent;
import it.polimi.ingsw.view.events.SelectWorkerEvent;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;

import java.util.EventObject;

/**
 * Class to execute the Select Worker.
 *
 * @see Executer
 * @author giorgio
 */
public class SelectWorkerExecuter extends Executer {
    private String workerId;

    /**
     * Method that reset the executer with initial values.
     */
    @Override
    public void clear(){
        workerId = null;
    }

    /**
     * constructor
     */
    public SelectWorkerExecuter(){
        this.clear();
    }

    /**
     * Method that sets the workerId of the worker that is selected.
     *
     * @param workerId (Sting identifying the worker).
     * @throws WrongParametersException (if workerId is of no worker).
     */
    public void setWorkerId(String workerId)throws WrongParametersException {
        try {
            this.workerId = ViewWorker.search(workerId).toString();
        } catch (NotFoundException | WrongViewObjectException e) {
            throw new WrongParametersException();
        }
    }

    /**
     * Method that sets the workerId of the worker that is selected.
     *
     * @param worker (ViewWorker that is selected).
     * @throws WrongParametersException (if worker is null)
     */
    public void setWorkerId(ViewWorker worker) throws WrongParametersException {
        if(worker==null) throw  new WrongParametersException();
        this.workerId = worker.toString();
    }

    /**
     * Static method that returns aunivoque string for each type of Exeuter (in this specific case "[Executer]").
     *
     * @return (Stirng univocally identifying the Executer type).
     */
    public static String myType(){ return Executer.myType() + "\tSelectWorker"; }

    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     * @throws CannotSendEventException (if the Executer doesn't have all the information needed  by the Event)
     */
    @Override
    public EventObject getMyEvent()throws CannotSendEventException {
        if(workerId == null) throw new CannotSendEventException("You have to select a worker");
        return new SelectWorkerEvent(workerId);
    }

    /**
     * Method that selects the Worker on the Cell.
     *
     * @param cell (the Cell on which there is the selected Worker).
     * @throws WrongParametersException (if there is no Worker on the Cell).
     */
    public void setCell(ViewCell cell) throws WrongParametersException{
        if(cell.isThereWorker())
            setWorkerId(cell.getWorker());
        else
            throw new WrongParametersException("No worker on the selected Cell");
    }

    /**
     * Method that selects the Worker on the Cell on the given position.
     *
     * @param x (the x-position of the Cell).
     * @param y (the y-position of the Cell).
     * @throws WrongParametersException (if there is no Worker on the Cell).
     */
    public void setCell(int x, int y) throws WrongParametersException{
        try {
            setCell(ViewBoard.getBoard().getCellAt(x, y));
        } catch (NotFoundException e) {
            throw new WrongParametersException("No valid cell selected");
        }
    }

    /*public void send(EventObject event) throws NullPointerException{
        if(event == null) throw new NullPointerException();
        getSender().send((SelectWorkerEvent)event);
    }*/

    /**
     * Method returning ture iif this Executer has to wait undo-timeout.
     *
     * @return (ture iif this Executer has to wait undo-timeout).
     */
    @Override
    protected boolean checkUndo(){
        return true;
    }

}
