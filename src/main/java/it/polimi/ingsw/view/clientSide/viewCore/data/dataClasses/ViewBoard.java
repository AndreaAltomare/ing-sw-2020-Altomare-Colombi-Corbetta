package it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses;

import it.polimi.ingsw.controller.events.ServerSendDataEvent;
import it.polimi.ingsw.model.persistence.board.BoardState;
import it.polimi.ingsw.model.persistence.board.CellState;
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
 * Class to represent the board.
 * There will be no more than one instance in each client.
 * It extends <code>ViewObject</code>.
 *
 * This class contains the Board with all its <code>ViewCell</code> and gives also the possibility to mark a Cell as "selected".
 * No more than one Cell should be selected at the same time.
 *
 * @see ViewCell
 * @see ViewObject
 * @author giorgio
 */
public class ViewBoard extends ViewObject {

    private BoardGeneralPanel guiPanel;

    private int xDim;
    private int yDim;

    private ViewCell[][] realBoard;

    private static ViewBoard board;

    private ViewCell selectedCell;

    /**
     * Method to set the selected Cell to be the Cell in the given position.
     * If there is no Cell in the given position it'll set no Cell as selected.
     *
     * @param x (x position of the Cell to be selected).
     * @param y (y position of the Cell to be selected).
     */
    public void setSelectedCell(int x, int y){
        try {
            selectedCell = getCellAt(x, y);
        } catch (NotFoundException e) {
            selectedCell = null;
        }
    }

    /**
     * Method to set the selected Cell to be the given Cell.
     * If called with <code>null</code> than it'll set no Cell selected.
     *
     * @param cell (Cell to be set as selected. It must belong to this Board).
     */
    public void setSelectedCell(ViewCell cell){ selectedCell = cell; }

    /**
     * Method to retrieve the Cell selected or null if there is no Cell selected.
     *
     * @return (the <code>ViewCell</code> selected. <code>null</code> if no Cell is selected).
     */
    public ViewCell getSelectedCell(){ return selectedCell; }

    /**
     * Static method to retrieve the last instantiated <code>ViewBoard</code>, null if no <code>ViewBoard</code> has been instantiated.
     *
     * @return (the last <code>ViewBoard</code> instantiated).
     */
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
     * Method that returns the Cell at the given position.
     *
     * @param x (x-position)
     * @param y (y-position)
     * @return (the cell relative to the selected position)
     * @throws NotFoundException (iif it's accessing outside the borders)
     */
    public ViewCell getCellAt(int x, int y) throws NotFoundException {
        if(x<0 || x>=this.getXDim() || y<0 || y>=this.getYDim())
            throw new NotFoundException();
        return realBoard[x][y];
    }

    /**
     * Method that returns the id of the instance inside the class.
     * It'll always return "" because there should be only one instantiated <code>ViewBoard</code>
     * (and so there is no needed to set a unique id to identify the different instances).
     *
     * @see ViewObject
     * @return (String the id of the object or "" if no id is needed).
     */
    @Override
    public String getId() {
        return "";
    }


    /**
     * Method returning the unique String for this class: "[Board]".
     *
     * @return ("[Board]").
     */
    @Override
    public String getMyClassId() {
        return getClassId();
    }

    /**
     * function that returns "[Board]".
     *
     * @return ("[Board]").
     */
    public static String getClassId(){ return "[Board]"; }

    /**
     * Method that will search the ViewBoard with the passed id.
     * If it is searched any instance of the <code>ViewBoard</code>, it returns the only one that is available.
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
     * If the searched id is referring to a <code>ViewBoard</code> then:
     * if exists an instance of the Board, then it'll return it;
     * else it'll instantiate a new <code>ViewBoard</code> and will return it.
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
     * It does nothing on the Board.
     *
     * @param event (The Event to be notified)
     * @return (true iif the event is notified in the right way)
     * @throws WrongEventException (if the Event is not used for this object)
     */
    @Override
    public boolean notifyEvent( @NotNull EventObject event) throws WrongEventException{
        return false;
    }

    /**
     * Method called when arriving a ServerSendDataEvent containing the information to build a new ViewBoard.
     * If there already is an instance of the ViewBoard, it'll do nothing but returning it,
     * else it'll construct a new ViewBoard -with the given information- and returns it.
     *
     * @param data (the Event arrived)
     * @return (the new object created)
     */
    public static ViewObject populate( @NotNull ServerSendDataEvent data){
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
     * Method called when arriving a BoardState containing the information to build a new ViewBoard
     * or containing a representation of the state of the Board.
     * If doesn't already exist an instance of the ViewBoard, it'll construct it.
     * It set the instance of the ViewBoard to represent the same state given by the state parameter.
     *
     * @param state (the <code>BoardStatus</code> to be represented)
     * @return (the ViewBoard correctly updated)
     */
    public static ViewObject populate(BoardState state){
        if(board == null){
            board = new ViewBoard();
        }

        if(board.xDim!=state.getXSize()){
            board.realBoard = null;
            board.xDim = state.getXSize();
        }


        if(board.yDim!=state.getYSize()){
            board.realBoard = null;
            board.yDim = state.getYSize();
        }

        if(board.realBoard==null){
            board.realBoard = new ViewCell[board.xDim][board.yDim];
            for(int i=0; i<board.xDim; i++) {
                for (int j = 0; j < board.yDim; j++){
                    board.realBoard[i][j] = new ViewCell(i, j);
                }
            }
        }

        CellState[][] cellStates = state.getBoard();

        for(int i=0; i<board.xDim; i++) {
            for (int j = 0; j < board.yDim; j++) {
                board.realBoard[i][j].notifyEvent(cellStates[i][j]);
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
    @Override
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
     * Method which prints the board and Players' caption using methods of <code>BoardPrintFunction</code> and
     * the toWTerminal(int level, boolean isSelected) of <code>ViewCell</code> to print
     * parts of Board and symbol of <code>Symbols</code>.
     *
     * Constant are using to define the most important parameter to print the Board
     *
     * @see BoardPrintFunction
     * @see Symbols
     * @return <code>null</code>
     */
    @Override
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


        return null;
    }

    /**
     * Method which prints the board and Players' caption using methods of <code>CLIBoardPrintFunction</code> and
     * the toCLI(int level, boolean isSelected, int symbolSpace) of <code>ViewCell</code> to print
     * parts of Board and the colors and symbol of <code>ANSIStyle</code> and <code>CLISymbol</code>.
     *
     * Constant are using to define the most important parameter to print the Board
     *
     * @see CLIPrintFunction
     * @see ANSIStyle
     * @see CLISymbols
     * @return <code>null</code>
     */
    @Override
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
        final String NUMBER_COLOR = ANSIStyle.GREY.getEscape();
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


        return null;

    }

    /**
     * Method that will return a BoardGeneralPanel that represents the Board on the GUI. There is only one BoardGeneralPanel to represent the Board for all the play.
     *
     * @return (representation of Board for the GUI)
     */
    @Override
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

    /**
     * Method returning the currently selected Cell.
     *
     * @return (the currently selected Cell).
     */
    private static ViewCell getSelectedWorkerCell(){
        try {
            return ViewWorker.getSelected().getPosition();
        } catch (NotFoundException | NullPointerException e) {
            return null;
        }
    }
}
