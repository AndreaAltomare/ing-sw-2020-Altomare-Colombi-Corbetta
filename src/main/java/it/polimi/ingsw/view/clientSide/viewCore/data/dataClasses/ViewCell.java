package it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses;

import it.polimi.ingsw.controller.events.BlockBuiltEvent;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.move.MoveOutcomeType;
import it.polimi.ingsw.model.persistence.board.CellState;
import it.polimi.ingsw.model.persistence.board.PlaceableData;
import it.polimi.ingsw.view.clientSide.viewCore.data.ViewObject;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLICellPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.CellPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Deque;
import java.util.EventObject;
import java.util.List;

/**
 * Class intended to represent the Cell in the View package.
 *
 * @author giorgio
 */
public class ViewCell extends ViewObject {

    private int x;
    private int y;
    private int level;

    private boolean doomed;
    private boolean thereIsWorker;

    private ViewWorker worker;

    private static List<ViewCell> myList = new ArrayList<ViewCell>();

    /**
     * Getter for the x-coordinate.
     *
     * @return (int x-coordinate)
     */
    public int getX(){ return x; }

    /**
     * Getter for the y-coordinate.
     *
     * @return (int y-coordinate)
     */
    public int getY(){ return y; }

    /**
     * Getter for the level.
     *
     * @return (int the level of the construction on this cell)
     */
    public int getLevel(){
        if(level<0)
            level = 0;
        return level;
    }


    /**
     * Getter of doomed status.
     *
     * @return (true iif there is a dome on it)
     */
    public boolean isDoomed(){ return doomed; }

    /**
     * pseudo-getter for the worker
     *
     * @return (true iif there is a worker on this cell)
     */
    public boolean isThereWorker(){ return thereIsWorker; }

    /**
     * Getter of the worker on this cell.
     *
     * @return (ViewWorker on this cell (null if there is none))
     */
    public ViewWorker getWorker(){ return worker; }

    /**
     * Getter of the worker on this cell.
     *
     * @return (String id of the worker on the cell (or "" if none))
     */
    public String getWorkerString(){ return (worker!=null?worker.toString():""); }

    /**
     * Method to build a dome on this cell
     */
    public void buildDome(){ doomed = true; }
    /**
     * Method to remove a dome on this cell
     */
    public void removeDome(){ doomed = false; }

    /**
     * Method to build a level on this cell
     */
    public void buildLevel(){ level++; }
    /**
     * Method to remove a dome on this cell
     */
    public void removeLevel(){ level=(level>0?level-1: 0); }
    /**
     * Method to set the level of this cell.
     *
     * @param level (int level of this cell)
     */
    public void setLevel(int level){ this.level = (level>0?level:0); }

    /**
     * Method to remove the ViewWorker on this cell.
     */
    void removeWorker(){
        thereIsWorker = false;
        worker = null;
    }

    /**
     * Method to place a ViewWorker on this cell.
     *
     * @param worker (the worker to place on it)
     */
    void placeWorker( @NotNull ViewWorker worker){
        thereIsWorker = true;
        this.worker = worker;
    }

    @Override
    /**
     * Method returning a unique string for each object inside the Class.
     *
     * @return (unique String identifying the object)
     */
    public String getId() {
        return "(" + getX() + ", " + getY() + ")";
    }

    @Override
    /**
     * Method returning a unique String for each class.
     *
     * @return (unique string for each class)
     */
    public String getMyClassId() {
        return getClassId();
    }

    /**
     * function that returns for each Class the Base of its objects identificators as "[ClassId]".
     *
     * @return (String the base of Class identificators)
     */
    public static String getClassId(){ return "[Cell]"; }

    /**
     * Method that will search the object with the passed id.
     *
     * @param id (String, the toString result of the searched Object)
     * @return (The searched Object)
     * @throws NotFoundException (If it doesn't find the object)
     * @throws WrongViewObjectException (If the object is not of this Class).
     */
    public static ViewObject search( @NotNull String id) throws NotFoundException, WrongViewObjectException{
        if(!isOfThisClass(id))
            throw new WrongViewObjectException();
        return cSearch(id);
    }

    /**
     * Method that will search the object with the passed id; if it doesn't exists then try to create it.
     *
     * @param id (String, the toString result of the searched Object)
     * @return (The searched Object)
     * @throws NotFoundException (If it doesn't find the object and cannot build it)
     * @throws WrongViewObjectException (If the object is not of this Class).
     */
    public static ViewObject find( @NotNull String id) throws NotFoundException, WrongViewObjectException{
        if(!isOfThisClass(id))
            throw new WrongViewObjectException();
        return cSearch(id);
    }

    /**
     * Method that will be called on the arrival of an event on this object.
     *
     * @param event (The Event to be notified)
     * @return (true iif the event is notified in the right way)
     * @throws WrongEventException (if the Event is not used for this object)
     */
    public boolean notifyEvent( @NotNull EventObject event) throws WrongEventException{
        //todo: implement it
        return true;
    }

    public boolean notifyEvent(CellState state){
        Deque<PlaceableData> x = state.getBuilding();

        this.doomed = false;
        this.level = 0;
        this.thereIsWorker = false;
        this.worker = null;

        for (PlaceableData i: x) {
            if(i.getPlaceableType().equals(PlaceableType.DOME)){
                this.doomed = true;
            }else if(i.getPlaceableType().equals(PlaceableType.BLOCK)){
                this.level++;
            }else if(i.getPlaceableType().equals(PlaceableType.WORKER)){
                this.thereIsWorker = true;
                try {
                    this.placeWorker((ViewWorker)ViewWorker.search(i.getWorkerId()));
                } catch (NotFoundException | WrongViewObjectException e) {
                    e.printStackTrace();
                }
            }

        }
        return true;
    }

    /**
     * Method that will be called on the arrival of an event to build a new Object.
     *
     * @param event (the Event arrived)
     * @return (the new object created)
     * @throws WrongEventException (if the Event is not supported by this Class)
     */
    public static ViewObject populate(@NotNull EventObject event) throws WrongEventException{
        //todo: implement it
        throw new WrongEventException();
    }

    /**
     * Method that will be called on the arrival of an event to build a new Object.
     *
     * @param blockBuiltEvent (the Event arrived)
     * @return (the new object created)
     * @throws WrongEventException (if the Event is not supported by this Class)
     */
    public static ViewObject populate(@NotNull BlockBuiltEvent blockBuiltEvent) throws WrongEventException{
        if(ViewObject.outcome(blockBuiltEvent.getMoveOutcome())) {
            ViewCell cell;
            try {
                cell = ViewBoard.getBoard().getCellAt(blockBuiltEvent.getX(), blockBuiltEvent.getY());
            } catch (NotFoundException e) {
                ViewMessage.populateAndSend("Wrong cell on which build", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
                return null;
            }
            if(blockBuiltEvent.getBlockType() == PlaceableType.DOME){
                cell.buildDome();
            }else{
                cell.buildLevel();
            }
            return cell;
        }else{
            if(ViewSubTurn.getActual().getPlayer().equals(ViewNickname.getMyNickname())){
                ViewMessage.populateAndSend("Built unsuccesful, retry", ViewMessage.MessageType.FROM_SERVER_ERROR);
            }
            return null;
        }
    }

    /**
     * Method to discard all the objects of the Class.
     */
    public static void clear(){
        myList.clear();
    }

    /**
     * Method that will return a String to write the ViewObject on the Terminal.
     *
     * @return (String representing the object and its status)
     */
    public String toTerminal(){
        return this.toString() + " " + this.getLevel() + " D: " + (this.isDoomed()?'t': 'f') + " W: " + (this.isThereWorker()?worker.getId():"no") + "| ";

    }


    /**
     * Method that will return a String that will represent the different symbol in the different cell's rows
     * @param level number of cell's row, starts from the up of cell with number 0
     * @param isSelected boolean parameter to know if the cell is selected
     * @return the correct String for each cell's row
     */
    public String toWTerminal(int level, boolean isSelected){
        String symbolInRow = "";

        if ( this.thereIsWorker ) {
            if ( worker.equals( ViewWorker.getSelected() )) {
                isSelected = true;
            }
        }

        switch (level) {
            case 0:
                break;
            case 1:
                symbolInRow = CellPrintFunction.cellRow1(this.level, this.doomed, this.thereIsWorker, this.worker, isSelected);
                break;
            case 2:
                symbolInRow = CellPrintFunction.cellRow2(this.level, this.doomed, this.thereIsWorker, this.worker, isSelected);
                break;
            case 3:
                symbolInRow = CellPrintFunction.cellRow3(this.level, this.doomed, this.thereIsWorker, this.worker, isSelected);
                break;
            case 4:
                symbolInRow = CellPrintFunction.cellRow4(this.level, this.doomed, this.thereIsWorker, this.worker, isSelected);
                break;
            case 5:
                symbolInRow = CellPrintFunction.cellRow5(this.level, this.doomed, this.thereIsWorker, this.worker, isSelected);
                break;
            case 6:
            default:
                ;
        }

        return symbolInRow;
    }


    /**
     * Method that will return a String of length == symbolSpace that will represent the different symbol in the different cell's rows
     *
     * @param level number of cell's row, starts from the up of cell with number 0
     * @param isSelected boolean parameter to know if the cell is selected
     * @param symbolSpace length of returned string
     * @return the correct String for each cell's row
     */
    public String toCLI(int level, boolean isSelected, int symbolSpace){
        String symbolInRow = "";

        if ( this.thereIsWorker ) {
            if ( worker.equals( ViewWorker.getSelected() )) {
                isSelected = true;
            }
        }

        switch (level) {
            case 0:
                symbolInRow = CLICellPrintFunction.cellRow0(this.level, this.doomed, this.thereIsWorker, this.worker, isSelected, symbolSpace);
                break;
            case 1:
                symbolInRow = CLICellPrintFunction.cellRow1(this.level, this.doomed, this.thereIsWorker, this.worker, isSelected, symbolSpace);
                break;
            case 2:
                symbolInRow = CLICellPrintFunction.cellRow2(this.level, this.doomed, this.thereIsWorker, this.worker, isSelected, symbolSpace);
                break;
            case 3:
                symbolInRow = CLICellPrintFunction.cellRow3(this.level, this.doomed, this.thereIsWorker, this.worker, isSelected, symbolSpace);
                break;
            case 4:
                symbolInRow = CLICellPrintFunction.cellRow4(this.level, this.doomed, this.thereIsWorker, this.worker, isSelected, symbolSpace);
                break;
            default:
                symbolInRow = CLIPrintFunction.increaseLengthWithSpace( symbolInRow, 0, symbolSpace);
        }

        return symbolInRow;
    }


    /**
     * Method that will return a (Object) that will represent the ViewObject on the GUI.
     *
     * @return (representation of Object for the GI)
     */
    public ImagePanel toGUI(){
        ImagePanel ret = null;
        String fileName = "";

        if(isDoomed())
            fileName += "doomed_";
        if(level==1)
            fileName += "single_block";
        if(level == 2)
            fileName += "double_block";
        if(level == 3)
            fileName += "triple_block";

        if(!fileName.equals("")) {
            System.out.println(fileName);
            ret = new ImagePanel(1, 1, 0, 0, "/img/board/cells/" + fileName + ".png");
        }
        if (isThereWorker()){
            ImagePanel workerToken = getWorker().toGUI();
            if(ret == null){
                ret = workerToken;
            }else{
                ret.add(workerToken);
            }
        }
        return ret;
    }

    /**
     * Method that will search the object with the passed id.
     *
     * @param id (String, the toString result of the searched Object)
     * @return (The searched Object)
     * @throws NotFoundException (If it doesn't find the object)
     */
    private static ViewCell cSearch( @NotNull String id) throws NotFoundException {
        for (ViewCell i: myList)
            if(i.toString().equals(id))
                return i;
        throw new NotFoundException();
    }

    /**
     * Constructor
     *
     * @param x (x coordinate)
     * @param y (y coordinate)
     */
    public ViewCell(int x, int y){
        this.x = x;
        this.y = y;
        this.level = 0;
        this.doomed = false;
        this.thereIsWorker = false;
        this.worker = null;
    }

    public static int getMaxLevel(){
        return 3;
    }


}
