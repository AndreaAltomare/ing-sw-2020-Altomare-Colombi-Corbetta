package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCell;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewWorker;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.BuildBlockEvent;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;

import java.util.EventObject;

public class BuildBlockExecuter extends Executer {

    private int x;
    private int y;
    private String workerId;
    private String placeable;

    /**
     * Method that reset the executer with initial values.
     */
    public void clear(){
        x=-1;
        y=-1;
        workerId = null;
        placeable = null;
    }

    /**
     * constructor
     */
    public BuildBlockExecuter(){
        this.clear();
    }

    /**
     * Method that sets the cell on which we want to build the block.
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
     * Method that sets the cell on which we want to build the block.
     *
     * @param cell (Cell on which we want to build)
     * @throws WrongParametersException (if cell is invalid)
     */
    public void setCell(ViewCell cell)throws WrongParametersException{
        if(cell==null) throw new WrongParametersException();
        this.setCell(cell.getX(), cell.getY());
    }

    /**
     * Method that sets the workerId of the worker that is building.
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
     * Method that sets the workerId of the worker that is building.
     *
     * @param worker (ViewWorker that is trying to build).
     * @throws WrongParametersException (if worker is null)
     */
    public void setWorkerId(ViewWorker worker) throws WrongParametersException {
        if(worker==null) throw  new WrongParametersException();
        this.workerId = worker.toString();
    }


    /**
     * Method that sets the placeable that is wanted to bee built.
     *
     * @param placeable (String defining the type of placeable : "BLOCK", "DOME").
     * @throws WrongParametersException (if placeable isn't "DOME" or "BLOCK")
     */
    public void setPlaceable(String placeable) throws WrongParametersException {
        if (!placeable.equals("DOME")&&!placeable.equals("BLOCK")) throw new WrongParametersException();
        this.placeable = placeable;
    }

    /**
     * Static method that returns aunivoque string for each type of Exeuter (in this specific case "[Executer]").
     *
     * @return (Stirng univocally identifying the Executer type).
     */
    public static String myType(){ return Executer.myType() + "\tBuildBlock"; }

    @Override
    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     * @throws CannotSendEventException (if the Executer doesn't have all the information needed  by the Event)
     */
    public EventObject getMyEvent()throws CannotSendEventException {
        if(x<0||y<0) throw new CannotSendEventException("Cannot try to build a block without having selected a cell.");
        if(placeable == null) throw new CannotSendEventException("Cannot try to build a block without knowing which kind of block you want to build");
        if(workerId == null) throw new CannotSendEventException("Cannot build a block without knowing which worker is trying to build it");
        return new BuildBlockEvent(workerId, x, y, (placeable.equals("BLOCK")? PlaceableType.BLOCK: PlaceableType.DOME));
    }

}
