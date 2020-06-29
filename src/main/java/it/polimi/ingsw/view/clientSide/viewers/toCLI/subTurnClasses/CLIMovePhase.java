package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.MoveWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.TurnStatusChangeExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.UndoExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.MoveViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.UnicodeSymbol;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.undoUtility.CLICheckWrite;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.undoUtility.StopTimeScanner;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses.CLIPlayingViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CLIMovePhase extends CLISubTurnViewer {

    private MoveViewer moveViewer;

    private final int STARTING_SPACE = 7;
    private final int DIRECTION_SPACE = 15;
    private final String ERROR_COLOR_AND_SYMBOL = ANSIStyle.RED.getEscape() + UnicodeSymbol.X_MARK.getEscape();
    private final String CORRECT_COLOR_AND_SYMBOL = ANSIStyle.GREEN.getEscape() + UnicodeSymbol.CHECK_MARK.getEscape();
    private final String WRITE_MARK = ANSIStyle.UNDERSCORE.getEscape() + UnicodeSymbol.PENCIL.getEscape() + ANSIStyle.RESET;

    public CLIMovePhase(MoveViewer moveViewer) {
        this.moveViewer = moveViewer;
    }

    //todo: add the arrows and use a Array[][] to save the direction
    /**
     * Prints the request to move the worker, checks the response, notifies the movement to Viewer and returns true
     * if it is all correct, or false if it isn't ( notifies to player when the input isn't correct)
     * @return
     */
    private boolean showMoveRequest() {
        final String DIRECTION_REQUEST = "Please, select a direction to move:";
        final String CORRECT_MESSAGE = "Your worker is correctly moved";
        final String WRONG_DIRECTION_MESSAGE = "The selected direction doesn't exist";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct";
        final String OUT_OF_BOARD_MESSAGE = "The cell in selected direction doesn't exist";

        int directionResponse;
        ViewCell selectedCell;
        boolean correctResponse = false;
        MoveWorkerExecuter moveWorkerExecuter = (MoveWorkerExecuter) moveViewer.getMySubTurn().getExecuter();

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
            selectedCell = this.calculateNextCell( directionResponse );
            if ( selectedCell != null ) {
                moveWorkerExecuter.setWorkerId( ViewWorker.getSelected() );
                moveWorkerExecuter.setCell( selectedCell );
                moveWorkerExecuter.doIt();
                System.out.println();
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.print(CORRECT_COLOR_AND_SYMBOL + CORRECT_MESSAGE + ANSIStyle.RESET);
                correctResponse = true;
            } else {
                System.out.println();
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.print(ERROR_COLOR_AND_SYMBOL + WRONG_DIRECTION_MESSAGE + ANSIStyle.RESET);
            }
        } catch (InputMismatchException e) {
            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.print(ERROR_COLOR_AND_SYMBOL + WRONG_VALUE_MESSAGE +ANSIStyle.RESET);
        } catch (NotFoundException e) {
            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.print(ERROR_COLOR_AND_SYMBOL + OUT_OF_BOARD_MESSAGE + ANSIStyle.RESET);
        } catch (WrongParametersException | CannotSendEventException e) {
            e.printStackTrace(); //todo: remove after testing
        }
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);


        return correctResponse;
    }

    /**
     * Calculates the cell at chosen direction and returns it if it is possible, returns null if the direction isn't correct
     * and trow a NotFoundException if the cell isn't on the board ( or the select worker isn't on the board )
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
     * Tries to change subTurn in BUILD and returns true if it can, or false if it can't
     * @return
     */
    private boolean toBuildPhase() {
        final String CORRECT_CHANGE_PHASE_MESSAGE = "You are in the Build Phase";

        boolean changePhase = false;
        TurnStatusChangeExecuter turnStatusChangeExecuter = new TurnStatusChangeExecuter();

        try {
            turnStatusChangeExecuter.setStatusId( ViewSubTurn.BUILD );
            turnStatusChangeExecuter.doIt();
            changePhase = true;
            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println(CORRECT_COLOR_AND_SYMBOL + CORRECT_CHANGE_PHASE_MESSAGE + ANSIStyle.RESET);
        } catch (WrongParametersException | CannotSendEventException e) {
            e.printStackTrace();
        }

        return changePhase;

    }

    /**
     * Prints the board and a request of command and uses some private methods to execute them as long as
     * a movement or a change status is correctly executed
     */
    @Override
    public void show() {
        final String COMMAND_REQUEST = "Please, select a command:";
        final String GODS_DETAILS_COMMAND = "Print all gods' details";
        final String MOVE_COMMAND = "Move worker";
        final String TO_BUILD_PHASE_COMMAND = "Go to Build Phase";
        final String WRONG_COMMAND_MESSAGE = "The chosen command doesn't exist, please change it";

        boolean endMove = false;
        boolean moved = false;
        int actionSelected;

        while ( !endMove ) {
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);

            try {
                ViewBoard.getBoard().toCLI();
            }catch(NullPointerException e){
                endMove = true;
                break;
            }

            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println(COMMAND_REQUEST);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println("1: " + GODS_DETAILS_COMMAND);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println("2: " + MOVE_COMMAND);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println("3: " + TO_BUILD_PHASE_COMMAND);

            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.print(WRITE_MARK);
            try {
                actionSelected = new Scanner(System.in).nextInt();
                switch ( actionSelected ) {
                    case 1:
                        this.showCardsDetails(STARTING_SPACE);
                        break;
                    case 2:
                        endMove = this.showMoveRequest();
                        moved = endMove;
                        break;
                    case 3:
                        endMove = this.toBuildPhase();
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
