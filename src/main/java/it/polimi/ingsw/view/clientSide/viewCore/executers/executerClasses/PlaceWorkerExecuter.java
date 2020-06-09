package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCell;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.PlaceWorkerEvent;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.EventObject;

public class PlaceWorkerExecuter extends Executer {
    private int x;
    private int y;

    /**
     * Method that reset the executer with initial values.
     */
    public void clear(){
        x=-1;
        y=-1;
    }

    /**
     * constructor
     */
    public PlaceWorkerExecuter(){
        this.clear();
    }

    /**
     * Method that sets the cell on which the worker wants to be placed.
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
     * Method that sets the cell on which the worker wants to be positionated.
     *
     * @param cell (Cell on which the worker wants to go)
     * @throws WrongParametersException (if cell is invalid)
     */
    public void setCell(ViewCell cell)throws WrongParametersException{
        if(cell==null) throw new WrongParametersException();
        this.setCell(cell.getX(), cell.getY());
    }

    /**
     * Static method that returns aunivoque string for each type of Exeuter (in this specific case "[Executer]").
     *
     * @return (Stirng univocally identifying the Executer type).
     */
    public static String myType(){ return Executer.myType() + "\tPlaceWorker"; }

    @Override
    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     * @throws CannotSendEventException (if the Executer doesn't have all the information needed  by the Event)
     */
    public EventObject getMyEvent()throws CannotSendEventException {
        if(x<0||y<0) throw new CannotSendEventException("Cannot try to place the worker without having selected the destination");
        return new PlaceWorkerEvent(x, y);
    }


    public void send(EventObject event) throws NullPointerException{
        if(event == null) throw new NullPointerException();
        getSender().send((PlaceWorkerEvent)event);
    }
}
