package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCell;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewWorker;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.RemoveWorkerEvent;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;

import java.util.EventObject;

/**
 * Class to execute the Remove Worker.
 *
 * @see Executer
 * @author giorgio
 */
public class RemoveWorkerExecuter extends Executer {

    private String workerId; // univocal Worker identifier (who EVENTUALLY made this move)
    private int x;
    private int y;

    /**
     * Method that reset the executer with initial values.
     */
    @Override
    public void clear(){
        x=-1;
        y=-1;
        workerId = null;
    }

    /**
     * constructor
     */
    public RemoveWorkerExecuter(){
        this.clear();
    }

    /**
     * Method that sets the cell on which is the Worker we want to remove.
     *
     * @param x (x_position of the cell).
     * @param y (y_position of the cell).
     * @throws WrongParametersException (if x less than 0 || y less than 0)
     */
    public void setCell(int x, int y) throws WrongParametersException {
        if(x<0||y<0) throw new WrongParametersException();
        this.x = x;
        this.y = y;
    }

    /**
     * Method that sets the cell on which is the worker we want to remove.
     *
     * @param cell (Cell on which we want to remove)
     * @throws WrongParametersException (if cell is invalid)
     */
    public void setCell(ViewCell cell)throws WrongParametersException{
        if(cell==null) throw new WrongParametersException();
        this.setCell(cell.getX(), cell.getY());
    }

    /**
     * Method that sets the workerId of the worker that is going to be removed.
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
     * Method that sets the workerId of the worker that is going to be removed.
     *
     * @param worker (ViewWorker is going to be removed).
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
    public static String myType(){ return Executer.myType() + "\tRemoveWorker"; }

    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     * @throws CannotSendEventException (if the Executer doesn't have all the information needed  by the Event)
     */
    @Override
    public EventObject getMyEvent()throws CannotSendEventException {
        if(x<0||y<0) throw new CannotSendEventException("Cannot try to remove a worker without having selected a cell.");
        if(workerId == null) throw new CannotSendEventException("Cannot remove a worker without knowing which worker is going to be removed");
        return new RemoveWorkerEvent(workerId, x, y);
    }

    /**
     * Class to execute the Remove Block.
     *
     * @see Executer
     */
    protected boolean checkUndo(){
        return true;
    }

}
