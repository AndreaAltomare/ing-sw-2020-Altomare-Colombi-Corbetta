package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.BuildBlockExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.NextTurnExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.TurnStatusChangeExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.BuildViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.CLISymbols;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.UnicodeSymbol;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CLIBuildPhase extends CLISubTurnViewer {

    private BuildViewer buildViewer;

    private final int STARTING_SPACE = 7;
    private final int DIRECTION_SPACE = 15;
    private final int PLACEABLE_SPACE = 7;
    private final String ERROR_COLOR_AND_SYMBOL = ANSIStyle.RED.getEscape() + UnicodeSymbol.X_MARK.getEscape();
    private final String CORRECT_COLOR_AND_SYMBOL = ANSIStyle.GREEN.getEscape() + UnicodeSymbol.CHECK_MARK.getEscape();
    private final String WRITE_MARK = ANSIStyle.UNDERSCORE.getEscape() + UnicodeSymbol.PENCIL.getEscape() + ANSIStyle.RESET;


    public CLIBuildPhase(BuildViewer buildViewer) {
        this.buildViewer = buildViewer;
    }


    /**
     * Prints the request to build using a private method to choose block's type, checks the response,
     * notifies the construction to Viewer and returns true if it is all correct,
     * or false if it isn't ( notifies to player when the input isn't correct)
     *
     * @return
     */
    private boolean showBuildRequest() {
        final String DIRECTION_REQUEST = "Please, select a direction to build:";
        final String CORRECT_MESSAGE = "Your worker has correctly built";
        final String WRONG_DIRECTION_MESSAGE = "The selected direction doesn't exist";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct";
        final String OUT_OF_BOARD_MESSAGE = "The cell in selected direction doesn't exist";

        int directionResponse;
        ViewCell selectedCell;
        String pLaceableToBuild;
        boolean correctResponse = false;
        BuildBlockExecuter buildBlockExecuter = (BuildBlockExecuter) buildViewer.getMySubTurn().getExecuter();

        pLaceableToBuild = this.typeBlockRequest();

        if ( pLaceableToBuild != null) {

            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println(DIRECTION_REQUEST);

            System.out.println();
            //first line
            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "7: North-West", "7: North-West".length(), DIRECTION_SPACE);
            CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "8: North", "8: North".length(), DIRECTION_SPACE);
            CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "9: North-Est", "9: North-Est".length(), DIRECTION_SPACE);
            System.out.println();
            //second line
            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "4: West", "4: West".length(), DIRECTION_SPACE);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", DIRECTION_SPACE);
            CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "6: Est", "6: Est".length(), DIRECTION_SPACE);
            System.out.println();
            //third line
            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "1: South-West", "1: South-West".length(), DIRECTION_SPACE);
            CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "2: South", "2: South".length(), DIRECTION_SPACE);
            CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "3: South-Est", "3: South-Est".length(), DIRECTION_SPACE);
            System.out.println();

            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.print(WRITE_MARK);
            try {
                directionResponse = new Scanner(System.in).nextInt();
                selectedCell = this.calculateNextCell(directionResponse);
                if (selectedCell != null) {
                    buildBlockExecuter.setWorkerId(ViewWorker.getSelected());
                    buildBlockExecuter.setCell(selectedCell);
                    buildBlockExecuter.setPlaceable(pLaceableToBuild);
                    buildBlockExecuter.doIt();
                    correctResponse = true;
                    System.out.println();
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                    System.out.print(CORRECT_COLOR_AND_SYMBOL + CORRECT_MESSAGE + ANSIStyle.RESET);
                } else {
                    System.out.println();
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                    System.out.print(ERROR_COLOR_AND_SYMBOL + WRONG_DIRECTION_MESSAGE + ANSIStyle.RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println();
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.print(ERROR_COLOR_AND_SYMBOL + WRONG_VALUE_MESSAGE + ANSIStyle.RESET);
            } catch (NotFoundException e) {
                System.out.println();
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.print(ERROR_COLOR_AND_SYMBOL + OUT_OF_BOARD_MESSAGE + ANSIStyle.RESET);
            } catch (WrongParametersException | CannotSendEventException e) {
                e.printStackTrace(); //todo: remove after testing
            }
        }
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);

        return correctResponse;
    }

    /**
     * Prints the request to choose the block's type, checks the response and returns a String representing th block's type
     * if it is correct, or null and an error message to player if it isn't
     *
     * @return
     */
    private String typeBlockRequest() {
        final String BLOCK_TYPE_REQUEST = "Please, select what type of block you want to choose:";
        final String WRONG_TYPE_MESSAGE = "The chosen block's type doesn't exist";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct";
        final String BLOCK_COLOR = ANSIStyle.GREY.getEscape();
        final String DOME_COLOR = ANSIStyle.BLUE.getEscape();

        String blockType = null;
        int typeSelected;

        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.println(BLOCK_TYPE_REQUEST);

        System.out.println();
        //block
        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.print("1: ");
        CLIPrintFunction.printAtTheMiddle(BLOCK_COLOR, CLISymbols.BLOCK.getMiddleRepresentation(), CLISymbols.BLOCK.getLength(), PLACEABLE_SPACE);

        System.out.println();

        // dome
        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.print("2: ");
        CLIPrintFunction.printAtTheMiddle(DOME_COLOR, CLISymbols.DOME.getMiddleRepresentation(), CLISymbols.DOME.getLength(), PLACEABLE_SPACE);

        System.out.println();

        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.print(WRITE_MARK);
        try {
            typeSelected = new Scanner(System.in).nextInt();
            switch ( typeSelected ) {
                case 1:
                    blockType = "BLOCK";
                    break;
                case 2:
                    blockType = "DOME";
                    break;
                default:
                    System.out.println();
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                    System.out.println(ERROR_COLOR_AND_SYMBOL + WRONG_TYPE_MESSAGE + ANSIStyle.RESET);
            }
        } catch (InputMismatchException e) {
            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println(ERROR_COLOR_AND_SYMBOL + WRONG_VALUE_MESSAGE + ANSIStyle.RESET);
        }

        return  blockType;
    }

    /**
     * Calculates the cell at chosen direction and returns it if it is possible, returns null if the direction isn't correct
     * and trow a NotFoundException if the cell isn't on the board ( or the select worker isn't on the board )
     *
     * @param direction
     * @return
     * @throws NotFoundException
     */
    private ViewCell calculateNextCell(int direction ) throws NotFoundException {
        ViewCell selectedCell = null;
        int workerRow;
        int workerColumn;

        workerRow = ViewWorker.getSelected().getPosition().getX();
        workerColumn = ViewWorker.getSelected().getPosition().getY();

        switch (direction) {
            case 7:
                selectedCell = ViewBoard.getBoard().getCellAt(workerRow - 1, workerColumn - 1);
                break;
            case 8:
                selectedCell = ViewBoard.getBoard().getCellAt(workerRow - 1, workerColumn );
                break;
            case 9:
                selectedCell = ViewBoard.getBoard().getCellAt(workerRow - 1, workerColumn + 1);
                break;
            case 4:
                selectedCell = ViewBoard.getBoard().getCellAt( workerRow , workerColumn - 1);
                break;
            case 6:
                selectedCell = ViewBoard.getBoard().getCellAt( workerRow , workerColumn + 1);
                break;
            case 1:
                selectedCell = ViewBoard.getBoard().getCellAt( workerRow + 1, workerColumn - 1);
                break;
            case 2:
                selectedCell = ViewBoard.getBoard().getCellAt( workerRow + 1, workerColumn );
                break;
            case 3:
                selectedCell = ViewBoard.getBoard().getCellAt( workerRow + 1, workerColumn + 1);
                break;
            case 5:
            default:
                ;
        }

        return selectedCell;

    }

    /**
     * Tries to change subTurn in MOVE and returns true if it can, or false if it can't
     * @return
     */
    private boolean toMovePhase() {
        final String CORRECT_CHANGE_PHASE_MESSAGE = "You are in the Move Phase";

        boolean changePhase = false;
        TurnStatusChangeExecuter turnStatusChangeExecuter = new TurnStatusChangeExecuter();

        try {
            turnStatusChangeExecuter.setStatusId( ViewSubTurn.MOVE );
            turnStatusChangeExecuter.doIt();
            changePhase = true;
            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println(CORRECT_COLOR_AND_SYMBOL + CORRECT_CHANGE_PHASE_MESSAGE + ANSIStyle.RESET);
        } catch (WrongParametersException | CannotSendEventException e) {
            e.printStackTrace();        //todo: cancel after testing
        }

        return changePhase;

    }

    /**
     * Tries to close player's turn and returns true if it can, or false if it can't
     * @return
     */
    private boolean endTurn() {

        NextTurnExecuter nextTurnExecuter = new NextTurnExecuter();
        boolean endTurn = false;

        try {
            nextTurnExecuter.doIt();
            endTurn = true;
        } catch (CannotSendEventException e) {
            e.printStackTrace(); //todo: delete after testing
        }
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);

        return endTurn;
    }

    /**
     * Prints the board and a request of command and uses some private methods to execute them as long as
     * a construction, a change status or a endTurn is correctly executed
     */
    @Override
    public void show() {
        final String COMMAND_REQUEST = "Please, select a command:";
        final String GODS_DETAILS_COMMAND = "Print all gods' details";
        final String BUILD_COMMAND = "Build";
        final String TO_MOVE_PHASE_COMMAND = "Go to Move Phase";
        final String END_TURN_COMMAND = "End Turn";
        final String WRONG_COMMAND_MESSAGE = "The chosen command doesn't exist, please change it";

        boolean endBuild = false;
        int actionSelected;

        while ( !endBuild ) {
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);

            try {
                ViewBoard.getBoard().toCLI();
            }catch(NullPointerException e){
                break;  //exit from state if there isn't the board
            }

            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println(COMMAND_REQUEST);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println("1: " +  GODS_DETAILS_COMMAND );
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println("2: " + BUILD_COMMAND);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println("3: " + TO_MOVE_PHASE_COMMAND);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println("4: " + END_TURN_COMMAND);

            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.print(WRITE_MARK);
            try {
                actionSelected = new Scanner(System.in).nextInt();
                switch (actionSelected) {
                    case 1:
                        this.showCardsDetails(STARTING_SPACE);
                        break;
                    case 2:
                        endBuild = this.showBuildRequest();
                        break;
                    case 3:
                        endBuild = this.toMovePhase();
                        break;
                    case 4:
                        endBuild = this.endTurn();
                        break;
                    default:
                        System.out.println();
                        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                        System.out.println(ERROR_COLOR_AND_SYMBOL + WRONG_COMMAND_MESSAGE + ANSIStyle.RESET);
                }
            } catch (InputMismatchException e) {
                System.out.println();
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.println(ERROR_COLOR_AND_SYMBOL + WRONG_COMMAND_MESSAGE + ANSIStyle.RESET);
            }
        }

     }

}
