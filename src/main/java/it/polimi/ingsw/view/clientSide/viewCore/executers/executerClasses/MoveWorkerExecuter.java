package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCell;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewWorker;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.events.MoveWorkerEvent;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;

import java.util.EventObject;

/**
 * Class to execute the Move Worker.
 *
 * @see Executer
 * @author giorgio
 */
public class MoveWorkerExecuter extends Executer {
    private String workerId; // univocal Worker identifier (who made this move)
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
    public MoveWorkerExecuter(){
        this.clear();
    }

    /**
     * Method that sets the cell on which the worker wants to go.
     *
     * @param x (x_position of the cell).
     * @param y (y_position of the cell).
     * @throws WrongParametersException (if x<0 || y<0)
     */
    public void setCell(int x, int y) throws WrongParametersException {
        if(x<0||y<0) throw new WrongParametersException();
        this.x = x;
        this.y = y;
    }

    /**
     * Method that sets the cell on which the worker wants to go.
     *
     * @param cell (Cell on which the worker wants to go)
     * @throws WrongParametersException (if cell is invalid)
     */
    public void setCell(ViewCell cell)throws WrongParametersException{
        if(cell==null) throw new WrongParametersException();
        this.setCell(cell.getX(), cell.getY());
    }

    /**
     * Method that sets the workerId of the worker that is moving.
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
     * Method that sets the workerId of the worker that is moving.
     *
     * @param worker (ViewWorker that is trying to move).
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
    public static String myType(){ return Executer.myType() + "\tMoveWorker"; }

    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     * @throws CannotSendEventException (if the Executer doesn't have all the information needed  by the Event)
     */
    @Override
    public EventObject getMyEvent()throws CannotSendEventException {
        if(ViewWorker.getSelected() == null){
            ViewSubTurn.setSubTurn(ViewSubTurn.SELECTWORKER);
            throw new CannotSendEventException("You have to choose a worker before");
        }
        if(x<0||y<0) throw new CannotSendEventException("Cannot try to move without having selected the destination");
        if (workerId == null) {
            workerId = ViewWorker.getSelected().toString();
            if(workerId == null) throw new CannotSendEventException("Cannot move without knowing which worker is trying to move");
        }
        return new MoveWorkerEvent(workerId, x, y);
    }

    /*public void send(EventObject event) throws NullPointerException{
        if(event == null) throw new NullPointerException();
        getSender().send((MoveWorkerEvent)event);
    }*/

    /**
     * Method returning ture iif this Executer has to wait undo-timeout.
     *
     * @return (ture).
     */
    @Override
    protected boolean checkUndo(){
        return true;
    }
}
