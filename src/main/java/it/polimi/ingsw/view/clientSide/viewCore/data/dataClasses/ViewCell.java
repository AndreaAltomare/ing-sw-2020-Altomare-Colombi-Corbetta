package it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses;

import it.polimi.ingsw.controller.events.BlockBuiltEvent;
import it.polimi.ingsw.controller.events.BlockRemovedEvent;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.persistence.board.CellState;
import it.polimi.ingsw.model.persistence.board.PlaceableData;
import it.polimi.ingsw.view.clientSide.View;
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
     * Method that removes the passed WiewWorker from the cell.
     * Not to remove wrong Workers.
     *
     * @param worker (the ViewWorker to be removed).
     */
    void removeWorker(ViewWorker worker){
        if(thereIsWorker && worker.equals(this.worker)){
            removeWorker();
        }else{
            if(View.debugging)
                System.out.println("Non tolgo");
        }
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

    /**
     * Method returning a unique string for each object inside the Class.
     * It returns a string formatted as:
     * "(" + x-pos + ", " + y-pos + ")"
     *
     * @return ( "(" + x-pos + ", " + y-pos + ")" )
     */
    @Override
    public String getId() {
        return "(" + getX() + ", " + getY() + ")";
    }

    /**
     * Method returning a unique String for each class.
     * For VieCell it's: "[Cell]"
     *
     * @return ("[Cell]")
     */
    @Override
    public String getMyClassId() {
        return getClassId();
    }

    /**
     * function that returns a string identifying the Class: "[Cell]".
     *
     * @return ("[Cell]")
     */
    public static String getClassId(){ return "[Cell]"; }

    /**
     * Method that will search the ViewCell with the passed toString.
     *
     * @param id (String, the toString result of the searched ViewCell)
     * @return (The searched ViewCell)
     * @throws NotFoundException (If it doesn't find the object)
     * @throws WrongViewObjectException (If the object is not of this Class).
     */
    public static ViewObject search( @NotNull String id) throws NotFoundException, WrongViewObjectException{
        if(!isOfThisClass(id))
            throw new WrongViewObjectException();
        return cSearch(id);
    }

    /**
     ** Method that will search the ViewCell with the passed toString.
     * If the searched ViewCell doesn't exists, it'll not instanciate a new one but will throw a NotFoundException.
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
     * Method that will be called on the arrival of an event on this.
     * It'll do nothing.
     *
     * @param event (The Event to be notified)
     * @return (true iif the event is notified in the right way)
     * @throws WrongEventException (if the Event is not used for this object)
     */
    @Override
    public boolean notifyEvent( @NotNull EventObject event) throws WrongEventException{
        return true;
    }

    /**
     * Method that will be called on the arrival of a <code>CellState</code> that sets the representation of this.
     *
     * @param state (The <code>CellState</code> representing the state to be updated)
     * @return (true iif the event is notified in the right way)
     */
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
                    ViewWorker worker = (ViewWorker)ViewWorker.search(i.getWorkerId());
                    //this.placeWorker(worker);
                    worker.placeOn( getX(), getY());
                } catch (NotFoundException | WrongViewObjectException e) {
                    e.printStackTrace();
                }
            }

        }
        return true;
    }

    /**
     * Method that will be called on the arrival of an event to build a new ViewCell.
     * It'll throw WrongEventException.
     *
     * @param event (the Event arrived)
     * @return (the new object created)
     * @throws WrongEventException (if the Event is not supported by this Class)
     */
    public static ViewObject populate(@NotNull EventObject event) throws WrongEventException{
        throw new WrongEventException();
    }

    /**
     * * Method that will be called on the arrival of an event to update a ViewCell to remove from it a Block or Dome.
     *
     * @param event (the <code>BlockRemovedEvent</code> that has to be executed).
     * @return  (the ViewCell referring to the Cell on which there has been removed the Block).
     * @throws WrongEventException (if the Cell selected doesn't exists).
     */
    public static ViewObject populate(@NotNull BlockRemovedEvent event) throws WrongEventException{
        ViewCell cella;
        try {
            cella = ViewBoard.getBoard().getCellAt(event.getX(), event.getY());
        } catch (NotFoundException e) {
            throw new WrongEventException();
        }

        if(event.getBlockType().equals(PlaceableType.DOME)){
            cella.removeDome();
        }else if(event.getBlockType().equals(PlaceableType.BLOCK)){
            cella.removeLevel();
        }
        return cella;
    }

    /**
     * Method that will be called on the arrival of an event to build a new Block on a Cell.
     *
     * @param blockBuiltEvent (the <code>BlockBuiltEvent</code> arrived)
     * @return (the Cell on which has been built the Block or null)
     * @throws WrongEventException (in no case)
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
    @Override
    public String toTerminal(){
        return this.toString() + " " + this.getLevel() + " D: " + (this.isDoomed()?'t': 'f') + " W: " + (this.isThereWorker()?worker.getId():"no") + "| ";

    }


    /**
     * Methods which returns a <code>String</code> to represent the different symbol in the different cell's rows.
     * Uses <code>CellPrintFunction</code> to create the correct <code>String</code> of Symbol for each cell's row
     *
     * @see CellPrintFunction
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
     * Method which returns a <code>String</code> of length == symbolSpace to represent represent the different symbol
     * in the different cell's rows using <code>CLICellPrintFunction</code> to create the correct <code>String</code>
     * for each cell's row
     *
     * @see CLICellPrintFunction
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
    @Override
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
            if(View.debugging)
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
     * Method that will search the ViewCell with the passed id.
     *
     * @param id (String, the toString result of the searched Object)
     * @return (The searched ViewCell)
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

    /**
     * Method to return the maximum height of the building of the ViewCell.
     *
     * @return (the maximum height of the building of the ViewCell).
     */
    public static int getMaxLevel(){
        return 3;
    }


}
