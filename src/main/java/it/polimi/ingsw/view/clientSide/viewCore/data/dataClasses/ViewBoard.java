package it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses;

import it.polimi.ingsw.controller.events.ServerSendDataEvent;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.view.clientSide.viewCore.data.ViewObject;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BoardGeneralPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;
import it.polimi.ingsw.view.interfaces.Addressable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.EventObject;

/**
 * Class to rapresent the board.
 * !! there will be no more than one instance in each client!!
 *
 * @author giorgio
 */
public class ViewBoard extends ViewObject {

    private BoardGeneralPanel guiPanel;

    private int xDim;
    private int yDim;

    private ViewCell[][] realBoard;

    private static ViewBoard board;

    public static ViewBoard getBoard(){ return board; }

    /**
     * Getter of the x-dimension of the board.
     *
     * @return (the x-dimension of the board)
     */
    public int getXDim(){ return xDim; }
    /**
     * Getter of the y-dimension of the board.
     *
     * @return (the y-dimension of the board)
     */
    public int getYDim(){ return yDim; }

    /**
     * Method that returns the Cell at the selected position.
     *
     * @param x (x-position)
     * @param y (y-position)
     * @return (the cell on the selected position)
     * @throws NotFoundException (iif it's accessing outside the borders)
     */
    public ViewCell getCellAt(int x, int y) throws NotFoundException {
        if(x<0 || x>this.getXDim() || y<0 || y>this.getYDim())
            throw new NotFoundException();
        return realBoard[x][y];
    }

    @Override
    /**
     * Method returning a unique string for each object inside the Class.
     *
     * @return (unique String identifying the object)
     */
    public String getId() {
        return "";
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
    public static String getClassId(){ return "[Board]"; }

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
        if(board == null)
            throw new NotFoundException();
        return board;
    }

    /**
     * Method that will search the object with the passed id; if it doesn't exists then try to create it.
     *
     * @param id (String, the toString result of the searched Object)
     * @return (The searched Object)
     * @throws NotFoundException (If it doesn't find the object and cannot build it)
     * @throws WrongViewObjectException (If the object is not of this Class).
     */
    public static ViewObject find( @ NotNull String id) throws NotFoundException, WrongViewObjectException{
        if(!isOfThisClass(id))
            throw new WrongViewObjectException();
        if(board == null)
            throw new NotFoundException();
        return board;
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
        return false;
    }

    /**
     * Method that will be called on the arrival of an event to build a new Object.
     *
     * @param event (the Event arrived)
     * @return (the new object created)
     * @throws WrongEventException (if the Event is not supported by this Class)
     */
    public static ViewObject populate( @NotNull EventObject event) throws WrongEventException{
        ServerSendDataEvent data;
        try{
            data = (ServerSendDataEvent) event;
        }catch (Exception e){
            throw new WrongEventException();
        }

        board = new ViewBoard();
        board.xDim = data.getBoardXsize();
        board.yDim = data.getBoardYsize();
        board.realBoard = new ViewCell[board.xDim][board.yDim];
        for(int i=0; i<board.xDim; i++) {
            for (int j = 0; j < board.yDim; j++){
                board.realBoard[i][j] = new ViewCell(i, j);
            }
        }
        return board;
    }

    /**
     * Method to discard all the objects of the Class.
     */
    public static void clear(){
        board = null;
    }

    /**
     * Method that will return a String to write the ViewObject on the Terminal.
     *
     * @return (String representing the object and its status)
     */
    public String toTerminal(){
        String ret = this.toString() + "\n";
        for(int x = 0; x< this.getXDim(); x++) {
            for (int y = 0; y < this.getYDim(); y++) {
                try {
                    ret += this.getCellAt(x, y).toTerminal();
                } catch (NotFoundException ignore) {   }
            }
            ret += "\n";
        }
        return ret;
    }


    /**
     * Method that will return a (Object) that will represent the ViewObject on the CLI.
     *
     * @return (representation of Object for the CLI)
     */
    public Object toCLI(){ return null; }

    /**
     * Method that will return a (Object) that will represent the ViewObject on the GUI.
     *
     * @return (representation of Object for the GI)
     */
    public BoardGeneralPanel toGUI(){
        if(guiPanel == null){
            try {
                guiPanel = BoardGeneralPanel.buildBoard(xDim, yDim);
            } catch (WrongParametersException ignore) {
                return null;
            }
        }

        for(int i=0; i<xDim; i++) {
            for (int j = 0; j < yDim; j++) {
                try {
                    guiPanel.updateCell(getCellAt(i, j));
                } catch (NotFoundException ignore) {  }
            }
        }
        return guiPanel;

        /*JPanel ret = new ImagePanel(1,1,0,0, "/img/board/boardScalata.png");
        JPanel tmp;
        JPanel cellPanel;

        double xLen = ((double)1/xDim);
        double yLen = ((double)1/yDim);

        for(int i=0; i<xDim; i++){
            for(int j =0; j<yDim; j++){
                tmp = new SubPanel(xLen, yLen, i*xLen, j*yLen);
                tmp.setOpaque(false);
                try {
                    cellPanel = getCellAt(i, j).toGUI();
                    System.out.println(getCellAt(i, j).toTerminal());
                    if(cellPanel!=null){
                        tmp.add(cellPanel);
                        ret.add(tmp);
                    }
                } catch (NotFoundException ignore) {  }
            }
        }
        return ret;*/
    }
}
