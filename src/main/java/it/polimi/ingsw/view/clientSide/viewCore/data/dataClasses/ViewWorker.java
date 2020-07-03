package it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses;

import it.polimi.ingsw.controller.events.WorkerMovedEvent;
import it.polimi.ingsw.controller.events.WorkerPlacedEvent;
import it.polimi.ingsw.controller.events.WorkerRemovedEvent;
import it.polimi.ingsw.controller.events.WorkerSelectedEvent;
import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewCore.data.ViewObject;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.WTerminalViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.CLISymbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.Symbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.SymbolsLevel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.exceptions.AlreadySetException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * Class intended to represent the Worker in the View Packace.
 *
 * @author giorgio
 */
public class ViewWorker extends ViewObject {

    private int id;
    private ViewPlayer player;
    private ViewCell position;

    protected Color workerColor;
    protected String workerCLIColor;
    protected Symbols workerWTRepresentation;

    private String workerImageFileName;

    private static List<ViewWorker> myList = new ArrayList<ViewWorker>();
    private static ViewWorker selected;

    /**
     * Getter method of the id of the worker
     *
     * @return (int id of the worker)
     */
    @Override
    public String getId(){ return Integer.toString(id); }

    /**
     * Getter method for the player owner of the worker.
     *
     * @return (ViewPlayer owner of this worker)
     */
    public ViewPlayer getPlayer(){ return player; }

    /**
     * Getter for the current position o this
     *
     * @return (the Cell on which this is)
     * @throws NotFoundException (if it is on no cell).
     */
    public ViewCell getPosition() throws NotFoundException {
        if(this.position == null)
            throw new NotFoundException();
        return position;
    }

    /**
     * Setter for position.
     *
     * @param position (the new cell on which it has to be put (null if no cell))
     */
    public void moveOn(ViewCell position){ this.position = position; }

    /**
     * Method returning a unique String for each class.
     * For ViewWorker it's "[Worker]"
     *
     * @return ("[Worker]")
     */
    @Override
    public String getMyClassId() {
        return getClassId();
    }

    /**
     * function that returns a string identifying the Class: "[Worker]".
     *
     * @return ("[Worker]")
     */
    public static String getClassId(){
        return "[Worker]";
    }

    /**
     * Method that will search the ViewWorker with the passed id.
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
     * Method that will search the ViewWorker with the passed id; if it doesn't exists then throws NotFoundException.
     *
     * @param id (String, the toString result of the searched Object)
     * @return (The searched Object)
     * @throws NotFoundException (If it doesn't find the object and cannot build it)
     * @throws WrongViewObjectException (If the object is not of this Class).
     */
    public static ViewObject find( @ NotNull String id) throws NotFoundException, WrongViewObjectException{
        if(!isOfThisClass(id))
            throw new WrongViewObjectException();
        return cSearch(id);
    }

    /**
     * Method that will be called on the arrival of an event on this object.
     * It does nothing.
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
     * Method that will be called on the arrival of a <code>WorkerRemovedEvent</code> event to build a new ViewWorker.
     * It'll create a ViewWorker and associate it to the relative ViewPlayer.
     *
     * @param event (the Event arrived)
     * @return (the new ViewWorker created)
     * @throws WrongEventException (if there are problems creating the ViewWorker)
     */
    public static ViewObject populate( @NotNull WorkerRemovedEvent event) throws WrongEventException{
        ViewWorker worker;
        try {
            worker = (ViewWorker)ViewWorker.search(event.getWorker());
            worker.removeWorker();
        } catch (NotFoundException | WrongViewObjectException e) {
            throw new WrongEventException();
        }
        return worker;
    }

    /**
     * Method called with <code>EventObject</code> event.
     * It'll only throw a WrongEventException (because it shouldn't be called).
     *
     * @param event (the <code>EventObject</code> event).
     * @return (nothinng)
     * @throws WrongEventException (always thrown).
     */
    public static ViewObject populate(EventObject event) throws WrongEventException{
        throw new WrongEventException();
    }


    /**
     * Method that will be called on the arrival of a <code>WorkerPlacedEvent</code> event to place a Worker.
     * It'll create a new ViewWorker, associate it to the player and place it on the Board.
     *
     * @param workerPlaced (the Event arrived)
     * @return (the ViewWorker placed)
     * @throws WrongEventException (if there has been some errors)
     */
    public static ViewObject populate( @NotNull WorkerPlacedEvent workerPlaced) throws WrongEventException{
        ViewWorker myWorker;
        try {
            myWorker = new ViewWorker(workerPlaced.getWorker(), ViewSubTurn.getActual().getPlayer());
            myWorker.placeOn(workerPlaced.getX(), workerPlaced.getY());
            myWorker.setWorkerColor(workerPlaced.getColor());
            /*switch(workerPlaced.getColor()){
                case BLUE:
                    myWorker.workerColor = java.awt.Color.BLUE;
                    myWorker.workerCLIColor = ANSIStyle.RED.getEscape();
                    myWorker.workerWTRepresentation = Symbols.WORKER_1;

                    break;
                case BROWN:
                    myWorker.workerColor = new java.awt.Color(153, 102,  0);
                    myWorker.workerCLIColor = ANSIStyle.YELLOW.getEscape();
                    myWorker.workerWTRepresentation = Symbols.WORKER_2;
                    break;
                case GREY:
                    myWorker.workerColor = java.awt.Color.GRAY;
                    myWorker.workerCLIColor = ANSIStyle.PURPLE.getEscape();
                    myWorker.workerWTRepresentation = Symbols.WORKER_3;
                    break;
            }*/
            if(View.debugging)
                System.out.println(workerPlaced.getWorker() + "(" + workerPlaced.getX()+":"+workerPlaced.getY()+")");
        } catch (NotFoundException | WrongViewObjectException e) {
            throw new WrongEventException();
        }
        return myWorker;
    }

    /**
     * Method that will be called on the arrival of a <code>WorkerSelectedEvent</code> event .
     * It'll search the Worker, select it and return it.
     *
     * @param selectedEvent (the Event arrived)
     * @return (the ViewWorker selected)
     * @throws WrongEventException (if there has been some errors)
     */
    public static ViewObject populate( @NotNull WorkerSelectedEvent selectedEvent) throws WrongEventException{
        ViewWorker worker;
        try {
             worker = (ViewWorker) search(selectedEvent.getWorker());
        } catch (NotFoundException | WrongViewObjectException e) {
            worker = null;
        }
        selectWorker(worker);
        return worker;
    }

    /**
     * Method that will be called on the arrival of a <code>WorkerMovedEvent</code> event.
     * It'll search the Worker, move it and return it.
     *
     * @param workerMovedEvent (the Event arrived)
     * @return (the ViewWorker moved)
     * @throws WrongEventException (if there has been some errors)
     */
    public static ViewObject populate( @NotNull WorkerMovedEvent workerMovedEvent) throws WrongEventException{
        if (ViewObject.outcome(workerMovedEvent.getMoveOutcome())) {
            ViewWorker worker = null;
            try {
                worker = (ViewWorker) search(workerMovedEvent.getWorker());
            } catch (NotFoundException | WrongViewObjectException e) {
                ViewMessage.populateAndSend("Wrong worker moved", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
                return null;
            }
            worker.placeOn(workerMovedEvent.getFinalX(), workerMovedEvent.getFinalY());
            ViewBoard.getBoard().setSelectedCell(workerMovedEvent.getInitialX(), workerMovedEvent.getInitialY());
            return worker;
        }else{
            if(ViewSubTurn.getActual().getPlayer().equals(ViewNickname.getMyNickname())){
                ViewMessage.populateAndSend("Move unsuccesful, retry", ViewMessage.MessageType.FROM_SERVER_ERROR);
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
    public String toTerminal(){ return this.toString(); }


    /**
     * Method which returns a <code>String</code> that will represent worker's representation in <code>Symbols</code>
     * of level chosen or a <code>String</code> of space if worker'representation isn't found
     *
     * @see SymbolsLevel
     * @param representationLevel part of worker's representation Symbols
     * @return String at the correct level of worker's Symbols
     */
    public String toWTerminal(SymbolsLevel representationLevel){
        String workerString;

        if (workerWTRepresentation != null) {
            switch (representationLevel) {
                case UP:
                    workerString = workerWTRepresentation.getUpRepresentation();
                    break;
                case MIDDLE:
                    workerString = workerWTRepresentation.getMiddleRepresentation();
                    break;
                case DOWN:
                    workerString = workerWTRepresentation.getDownRepresentation();
                    break;
                default:
                    workerString = "   ";
            }
        } else {
            workerString = "   ";
        }

        return workerString;
    }

    /**
     * Method which returns a <code>String</code> which represents worker's representation chosen with the workers' CLIColor
     * found or with default color ( nothing color ) if workers' color isn't found
     *
     * @see CLISymbols
     * @param head A boolean
     * @return String of worker's representation chosen with workers' color if it is found
     */
    public String toCLI(boolean head){
        String workerString;

        if (head) {
            workerString = CLISymbols.WORKER.getUpRepresentation();
        } else {
            workerString = CLISymbols.WORKER.getMiddleRepresentation();
        }

        workerString = this.getWorkerCLIColor() + workerString;

        return workerString;
    }

    /**
     * Method that will return a (Object) that will represent the ViewObject on the GUI.
     *
     * @return (representation of Object for the GI)
     */
    @Override
    public ImagePanel toGUI(){
        ImagePanel background = new ImagePanel(1, 1, 0, 0, "/img/board/cells/void_space.png");
        try {
            //background.add(new ImagePanel(0.8, 0.8, 0.1 ,0.1 , "/img/godPodium/" + getPlayer().getCard().getName() + ".png"));
            background.add(new ImagePanel(0.8, 0.8, 0.1 ,0.1 , workerImageFileName));
        } catch (Exception e) {
            background.add( new ImagePanel(0.8, 0.8, 0.1 ,0.1 ,  "/img/godPodium/Default.png"));
        }
        return background;
    }

    /**
     * Method that will search the ViewWorker with the passed id.
     *
     * @param id (String, the toString result of the searched Object)
     * @return (The searched ViewWorker)
     * @throws NotFoundException (If it doesn't find the object)
     */
    private static ViewWorker cSearch( @NotNull String id) throws NotFoundException {
        for (ViewWorker i: myList)
            if(i.toString().equals(id))
                return i;
        throw new NotFoundException();
    }

    /**
     * Constructor
     *
     * @param id (int id of the Worker)
     * @param player (ViewPlayer the player owning the worker)
     * @throws NotFoundException (iif player = null)
     */
    public ViewWorker(int id, ViewPlayer player) throws NotFoundException{
        if(player == null)
            throw new NotFoundException();
        this.id = id;
        try {
            player.addWorker(this);
        } catch (AlreadySetException e) {
            throw new NotFoundException();
        }
        this.player = player;
        this.position = null;

        myList.add(this);
    }

    /**
     * Constructor
     *
     * @param id (int id of the Worker)
     * @param player (id of the player owning the worker)
     * @throws NotFoundException (iif player doesn't exists)
     * @throws WrongViewObjectException Wrong view object
     */
    public ViewWorker(int id, @NotNull String player) throws NotFoundException, WrongViewObjectException {
        this(id, (ViewPlayer)ViewPlayer.find(player));
    }

    /**
     * Constructor
     *
     * @param id (String id of the Worker)
     * @param player (ViewPlayer the player owning the worker)
     * @throws NotFoundException (iif player = null)
     */
    public ViewWorker(String id, ViewPlayer player) throws NotFoundException{
        if((player == null)||(!isOfThisClass(id)))
            throw new NotFoundException();
        this.id = Integer.parseInt(id.substring(getClassId().length()+1));
        this.player = player;
        try {
            player.addWorker(this);
        } catch (AlreadySetException e) {
            throw new NotFoundException();
        }
        this.position = null;

        myList.add(this);
    }

    /**
     * Method checking weather the given id will correspond to an id of an other ViewwWorker.
     *
     * @param id (String to be analysed)
     * @return   (true iif there should be a ViewWorker with such an id).
     */
    public static boolean isOfThisClass(String id){
        return id.startsWith(getClassId());
    }

    /**
     * Constructor
     *
     * @param id (String id of the Worker)
     * @param player (id of the player owning the worker)
     * @throws NotFoundException (iif player doesn't exists)
     * @throws WrongViewObjectException Wrong view object
     */
    public ViewWorker(String id, @NotNull String player) throws NotFoundException, WrongViewObjectException {
        this(id, findOrSearch(player));
    }

    /**
     * Method to place the ViewWorker on to the cell located at the given position.
     *
     * @param x (cell's x-position).
     * @param y (cell's y-position).
     */
    public void placeOn(int x, int y){
        if(position != null) position.removeWorker(this);
        try {
            position = ViewBoard.getBoard().getCellAt(x, y);
        } catch (NotFoundException e) {
            position = null;
            return;
        }
        position.placeWorker(this);
    }

    /**
     * Method to remove the ViewWorker from the Cell on which it actually is.
     */
    public void removeWorker(){
        if(position!=null) position.removeWorker();
    }

    /**
     * Mehod that search a ViewEorker firstly by its toString, and then with its id.
     *
     * @param name (toString() or id of the searched ViewWorker).
     * @return     (the searched ViewWorker).
     * @throws NotFoundException (iif the searched ViewWorker doesn't exists).
     */
    protected static ViewPlayer findOrSearch(String name) throws NotFoundException{
        try {
            return (ViewPlayer)ViewPlayer.find(name);
        }  catch (WrongViewObjectException e) {
            return ViewPlayer.searchByName(name);
        }
    }

    /**
     * Method to set the selected Worker.
     *
     * @param worker (the selected ViewWorker).
     */
    public static void selectWorker(ViewWorker worker){
        selected = worker;
    }

    /**
     * Method to set the selected Worker.
     *
     * @param worker (the toString() or id of the selected ViewWorker).
     */
    public static void selectWorker(String worker){
        try {
            selectWorker((ViewWorker) search(worker));
        } catch (NotFoundException | WrongViewObjectException e) {
            selectWorker((ViewWorker)null);
        }
    }

    /**
     * getter for the selected ViewWorker.
     *
     * @return (the ViewWorker selected).
     */
    public static ViewWorker getSelected(){
        return selected;
    }

    /**
     * getter for the color associated to the View Worker.
     *
     * @return (the color associated to the View Worker).
     */
    public Color getColor(){
        return workerColor;
    }

    /**
     * getter for the color associated to the View Worker for colored CLI.
     * If workerCLIColor == null, returns a empty string ( == nothing color )
     *
     * @return a String which represents the ANSI color of the View Worker
     */
     public String getWorkerCLIColor() {
        return  workerCLIColor;
    }

    /**
     * getter for the symbol associated to the View Worker for WTerminal
     *
     * @return a Symbol which represents the representation with ASCII's characters of the View Worker
     */
    public Symbols getWorkerWTRepresentation() {
        return  workerWTRepresentation;
    }

    /**
     * Method setting the color of this Worker.
     *
     * @param color (the Color of this Worker).
     */
    public void setWorkerColor(it.polimi.ingsw.model.player.worker.Color color){
        switch(color){
            case BLUE:
                this.workerColor = java.awt.Color.BLUE;
                this.workerCLIColor = ANSIStyle.RED.getEscape();
                this.workerWTRepresentation = Symbols.WORKER_1;
                this.workerImageFileName = "/img/board/workers/worker_blue.png";
                break;
            case BROWN:
                this.workerColor = new java.awt.Color(153, 102,  0);
                this.workerCLIColor = ANSIStyle.YELLOW.getEscape();
                this.workerWTRepresentation = Symbols.WORKER_2;
                this.workerImageFileName = "/img/board/workers/worker_brown.png";
                break;
            case GREY:
                this.workerColor = java.awt.Color.GRAY;
                this.workerCLIColor = ANSIStyle.PURPLE.getEscape();
                this.workerWTRepresentation = Symbols.WORKER_3;
                this.workerImageFileName = "/img/board/workers/worker_grey.png";
                break;
        }

    }
}
