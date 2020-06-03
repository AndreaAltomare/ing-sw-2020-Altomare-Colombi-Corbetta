package it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses;

import it.polimi.ingsw.controller.events.ServerSendDataEvent;
import it.polimi.ingsw.view.clientSide.viewCore.data.ViewObject;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.CLISymbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.Symbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.BoardPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIBoardPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.BoardGeneralPanel;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;
import org.jetbrains.annotations.NotNull;

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

    private ViewCell selectedCell;

    public void setSelectedCell(int x, int y){
        try {
            selectedCell = getCellAt(x, y);
        } catch (NotFoundException e) {
            selectedCell = null;
        }
    }

    public void setSelectedCell(ViewCell cell){ selectedCell = cell; }

    public ViewCell getSelectedCell(){ return selectedCell; }

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
        if(x<0 || x>=this.getXDim() || y<0 || y>=this.getYDim())
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
     * @param data (the Event arrived)
     * @return (the new object created)
     * @throws WrongEventException (if the Event is not supported by this Class)
     */
    public static ViewObject populate( @NotNull ServerSendDataEvent data) throws WrongEventException{
        if(board!=null)
            return board;

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
     * Method that will print the board and Players' caption
     *
     * @return a String == null
     */
    public String toWTerminal(){
        final int CELL_LENGTH = 17; // must be odd
        final int CELL_HIGH = 7;
        final int X_DIM = this.getXDim();
        final int Y_DIM = this.getYDim();
        final Symbols[] numberArray = { Symbols.NUMBER_0, Symbols.NUMBER_1, Symbols.NUMBER_2, Symbols.NUMBER_3, Symbols.NUMBER_4};

        ViewCell viewCell;

        System.out.println();
        System.out.println();
        // print number of board's columns
        PrintFunction.printRepeatString(" ", CELL_LENGTH);
        for (int column = 0; column < Y_DIM; column++) {
            System.out.print(" ");
            PrintFunction.printAtTheMiddle( numberArray[column].getUpRepresentation(), CELL_LENGTH);
        }
        System.out.println(" ");


        PrintFunction.printRepeatString(" ", CELL_LENGTH);
        for (int column = 0; column < Y_DIM; column++) {
            System.out.print(" ");
            PrintFunction.printAtTheMiddle( numberArray[column].getMiddleRepresentation(), CELL_LENGTH);
        }
        System.out.println(" ");


        PrintFunction.printRepeatString(" ", CELL_LENGTH);
        for (int column = 0; column < Y_DIM; column++) {
            System.out.print(" ");
            PrintFunction.printAtTheMiddle( numberArray[column].getDownRepresentation(), CELL_LENGTH);
        }
        System.out.println(" ");

        System.out.println();


        /* print board with number of board's row and players' caption */
        for (int boardRow = 0; boardRow < X_DIM; boardRow++ ) {
            // print up edge of cells
            PrintFunction.printRepeatString(" ", CELL_LENGTH);
            for ( int boardColumn = 0; boardColumn < Y_DIM; boardColumn++) {
                System.out.print("# ");
                PrintFunction.printRepeatString("# ", CELL_LENGTH / 2);
            }
            System.out.println("#");

            // print a row of cells with number of board's row and players' caption
            for ( int cellRow = 0; cellRow < CELL_HIGH; cellRow++ ) {
                PrintFunction.printAtTheMiddle(BoardPrintFunction.getBoardRowSymbol(numberArray[boardRow], cellRow), CELL_LENGTH);
                for ( int boardColumn = 0; boardColumn < Y_DIM; boardColumn++ ) {
                    System.out.print("#");
                    try {
                        viewCell = this.getCellAt(boardRow, boardColumn);
                        PrintFunction.printAtTheMiddle( viewCell.toWTerminal(cellRow, viewCell.equals(this.selectedCell)), CELL_LENGTH);
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print("#");
                BoardPrintFunction.printPlayersCaption(boardRow, cellRow);
                System.out.println();
            }
        }
        // print down edge of board
        PrintFunction.printRepeatString(" ", CELL_LENGTH);
        for ( int boardColumn = 0; boardColumn < Y_DIM; boardColumn++) {
            System.out.print("# ");
            PrintFunction.printRepeatString("# ", CELL_LENGTH / 2);
        }
        System.out.println("#");


        System.out.println();
        System.out.println();


        return null;
    }

    /**
     * Method that will print the board and Players' caption
     *
     * @return a String == null
     */
    public String toCLI() {
        final int PLACEABLE_LENGTH = 9; // must be odd
        final int MIN_DISTANCE_FROM_EDGE = 1;
        final int CELL_LENGTH = PLACEABLE_LENGTH + 2*MIN_DISTANCE_FROM_EDGE;
        final int CELL_HIGH = 5;
        final int EDGE_THICKNESS = 2;
        final int X_DIM = this.getXDim();
        final int Y_DIM = this.getYDim();
        final CLISymbols[] numberArray = { CLISymbols.NUMBER_0, CLISymbols.NUMBER_1, CLISymbols.NUMBER_2, CLISymbols.NUMBER_3, CLISymbols.NUMBER_4};
        final String SEA_COLOR = ANSIStyle.BACK_DIFFERENT_BLUE.getEscape();
        final String NUMBER_COLOR = ANSIStyle.GREEN.getEscape();
        final String CELL_COLOR = ANSIStyle.BACK_GREEN.getEscape();
        final String EDGE_CELL_COLOR =ANSIStyle.BACK_GREY.getEscape();

        ViewCell viewCell;

        System.out.println();
        System.out.println();
        // print number of board's columns
        CLIPrintFunction.printRepeatString( SEA_COLOR, " ", CELL_LENGTH );
        for (int column = 0; column < Y_DIM; column++) {
            CLIPrintFunction.printRepeatString( SEA_COLOR, " ", EDGE_THICKNESS );
            CLIPrintFunction.printAtTheMiddle( SEA_COLOR + NUMBER_COLOR, numberArray[column].getUpRepresentation(),
                                                numberArray[column].getUpRepresentation().length(), CELL_LENGTH);
        }
        CLIPrintFunction.printRepeatString( SEA_COLOR, " ", EDGE_THICKNESS + CELL_LENGTH);
        System.out.println();


        CLIPrintFunction.printRepeatString( SEA_COLOR, " ", CELL_LENGTH );
        for (int column = 0; column < Y_DIM; column++) {
            CLIPrintFunction.printRepeatString( SEA_COLOR, " ", EDGE_THICKNESS );
            CLIPrintFunction.printAtTheMiddle( SEA_COLOR + NUMBER_COLOR, numberArray[column].getMiddleRepresentation(),
                                                numberArray[column].getMiddleRepresentation().length(), CELL_LENGTH);
        }
        CLIPrintFunction.printRepeatString( SEA_COLOR, " ", EDGE_THICKNESS + CELL_LENGTH);
        System.out.println();


        CLIPrintFunction.printRepeatString( SEA_COLOR, " ", CELL_LENGTH );
        for (int column = 0; column < Y_DIM; column++) {
            CLIPrintFunction.printRepeatString( SEA_COLOR, " ", EDGE_THICKNESS );
            CLIPrintFunction.printAtTheMiddle( SEA_COLOR + NUMBER_COLOR, numberArray[column].getDownRepresentation(),
                                                numberArray[column].getDownRepresentation().length(), CELL_LENGTH);
        }
        CLIPrintFunction.printRepeatString( SEA_COLOR, " ", EDGE_THICKNESS + CELL_LENGTH);
        System.out.println();

        CLIPrintFunction.printRepeatString( SEA_COLOR, " ", CELL_LENGTH );
        for (int column = 0; column < Y_DIM; column++) {
            CLIPrintFunction.printRepeatString( SEA_COLOR, " ", EDGE_THICKNESS + CELL_LENGTH );
        }
        CLIPrintFunction.printRepeatString( SEA_COLOR, " ", EDGE_THICKNESS + CELL_LENGTH);
        System.out.println();


        /* print board with number of board's row and players' caption */
        for (int boardRow = 0; boardRow < X_DIM; boardRow++ ) {
            // print up edge of cells
            CLIPrintFunction.printRepeatString( SEA_COLOR, " ", CELL_LENGTH);
            for ( int boardColumn = 0; boardColumn < Y_DIM; boardColumn++) {
                CLIPrintFunction.printRepeatString(EDGE_CELL_COLOR, " ", EDGE_THICKNESS + CELL_LENGTH);
            }
            CLIPrintFunction.printRepeatString(EDGE_CELL_COLOR, " ", EDGE_THICKNESS );
            CLIPrintFunction.printRepeatString( SEA_COLOR, " ", CELL_LENGTH);
            CLIBoardPrintFunction.printPlayersCaptionAtEdgeCell(boardRow);
            System.out.println();

            // print a row of cells with number of board's row and players' caption
            for ( int cellRow = 0; cellRow < CELL_HIGH; cellRow++ ) {
                CLIPrintFunction.printAtTheMiddle(SEA_COLOR + NUMBER_COLOR, CLIBoardPrintFunction.getBoardRowSymbol(numberArray[boardRow], cellRow),
                                                    CLIBoardPrintFunction.getBoardRowSymbol(numberArray[boardRow], cellRow).length(), CELL_LENGTH);
                for ( int boardColumn = 0; boardColumn < Y_DIM; boardColumn++ ) {
                    CLIPrintFunction.printRepeatString( EDGE_CELL_COLOR, " ", EDGE_THICKNESS );
                    try {
                        viewCell = this.getCellAt(boardRow, boardColumn);
                        CLIPrintFunction.printAtTheMiddle( CELL_COLOR, viewCell.toCLI(cellRow, viewCell.equals(this.selectedCell), PLACEABLE_LENGTH),
                                                            PLACEABLE_LENGTH, CELL_LENGTH);
                    } catch (NotFoundException e) {
                        e.printStackTrace();
                    }
                }
                CLIPrintFunction.printRepeatString( EDGE_CELL_COLOR, " ", EDGE_THICKNESS );
                CLIPrintFunction.printRepeatString( SEA_COLOR, " ", CELL_LENGTH);
                CLIBoardPrintFunction.printPlayersCaptionAtCell(boardRow, cellRow);
                System.out.println();
            }
        }
        // print down edge of board
        CLIPrintFunction.printRepeatString(SEA_COLOR," ", CELL_LENGTH);
        for ( int boardColumn = 0; boardColumn < Y_DIM; boardColumn++) {
            CLIPrintFunction.printRepeatString(EDGE_CELL_COLOR," ", EDGE_THICKNESS + CELL_LENGTH);
        }
        CLIPrintFunction.printRepeatString(EDGE_CELL_COLOR," ", EDGE_THICKNESS);
        CLIPrintFunction.printRepeatString( SEA_COLOR, " ", CELL_LENGTH);
        System.out.println();

        //print the down part of the sea
        for ( int levelDownSea = 0; levelDownSea < 4; levelDownSea++) {
            CLIPrintFunction.printRepeatString(SEA_COLOR, " ", CELL_LENGTH);
            for ( int boardColumn = 0; boardColumn < Y_DIM; boardColumn++) {
                CLIPrintFunction.printRepeatString(SEA_COLOR," ", EDGE_THICKNESS + CELL_LENGTH);
            }
            CLIPrintFunction.printRepeatString(SEA_COLOR," ", EDGE_THICKNESS);
            CLIPrintFunction.printRepeatString( SEA_COLOR, " ", CELL_LENGTH);
            System.out.println();
        }

        System.out.println();
        System.out.println();


        return null;

    }

    /**
     * Method that will return a BoardGeneralPanel that represents the Board on the GUI. There is only one BoardGeneralPanel to represent the Board for all the play.
     *
     * @return (representation of Board for the GUI)
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

        ViewCell workerCell = getSelectedWorkerCell();
        guiPanel.setSelectedWorker(workerCell);
        guiPanel.setSelectCell((workerCell == selectedCell? null: selectedCell));

        return guiPanel;
    }

    public static ViewCell getSelectedWorkerCell(){
        try {
            return ViewWorker.getSelected().getPosition();
        } catch (NotFoundException | NullPointerException e) {
            return null;
        }
    }
}
