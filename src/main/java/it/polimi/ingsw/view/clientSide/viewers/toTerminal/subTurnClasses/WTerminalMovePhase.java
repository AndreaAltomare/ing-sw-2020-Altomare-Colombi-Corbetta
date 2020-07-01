package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.MoveWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.TurnStatusChangeExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.MoveViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.WTerminalPlayingViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class WTerminalMovePhase extends WTerminalSubTurnViewer {

    private MoveViewer moveViewer;

    public WTerminalMovePhase(MoveViewer moveViewer) {
        this.moveViewer = moveViewer;
    }

    /**
     * Prints the request to move the worker, checks the response, notifies the movement to Viewer and returns true
     * if it is all correct, or false if it isn't ( notifies to player when the input isn't correct)
     * @return
     */
    private boolean showMoveRequest() {
        final String DIRECTION_REQUEST = "Please, select a direction to move:";
        final String CORRECT_MESSAGE = "Your move request is correctly send";
        final String WRONG_DIRECTION_MESSAGE = "The selected direction doesn't exist";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct";
        final String OUT_OF_BOARD_MESSAGE = "The cell in selected direction doesn't exist";
        String wrongExecutorSet = "The move isn't correctly set";
        int directionResponse;
        ViewCell selectedCell;
        boolean correctResponse = false;
        MoveWorkerExecuter moveWorkerExecuter = (MoveWorkerExecuter) moveViewer.getMySubTurn().getExecuter();

        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.println(DIRECTION_REQUEST);

        System.out.println();

        this.printDirection();

        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print(">>");
        try {
            directionResponse = new Scanner(System.in).nextInt();
            selectedCell = this.calculateNextCell( directionResponse );
            if ( selectedCell != null ) {
                moveWorkerExecuter.setWorkerId( ViewWorker.getSelected() );
                moveWorkerExecuter.setCell( selectedCell );
                correctResponse = true;
                moveWorkerExecuter.doIt();
                PrintFunction.printCheck(CORRECT_MESSAGE);
            } else {
                PrintFunction.printError(WRONG_DIRECTION_MESSAGE);
            }
        } catch (InputMismatchException e) {
            PrintFunction.printError(WRONG_VALUE_MESSAGE);
        } catch (NotFoundException e) {
            PrintFunction.printError(OUT_OF_BOARD_MESSAGE);
        } catch (WrongParametersException e) {
            if (!e.getMessage().equals("")) {
                wrongExecutorSet = e.getMessage();
            }
            PrintFunction.printError(wrongExecutorSet);
        } catch (CannotSendEventException e) {
            PrintFunction.printError(e.getErrorMessage());
        }

        return correctResponse;
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
        final String EXIT_COMMAND = "Exit from the game";
        final String WRONG_COMMAND_MESSAGE = "The chosen command doesn't exist, please change it";

        boolean endMove = false;
        int actionSelected;

        while (!endMove) {
            PrintFunction.printRepeatString("\n", 2);

            try {
                ViewBoard.getBoard().toWTerminal();
            } catch (NullPointerException e) {
                break;  //exit from state if there isn't the board
            }

            System.out.println();
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println(COMMAND_REQUEST);
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println("1: " + GODS_DETAILS_COMMAND);
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println("2: " + MOVE_COMMAND);
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println("3: " + TO_BUILD_PHASE_COMMAND);
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println("4: " + EXIT_COMMAND);

            System.out.println();
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.print(">>");
            try {
                actionSelected = new Scanner(System.in).nextInt();
                switch (actionSelected) {
                    case 1:
                        this.showCardsDetails(true);
                        break;
                    case 2:
                        endMove = this.showMoveRequest();
                        break;
                    case 3:
                        endMove = this.changePlayingPhase(ViewSubTurn.MOVE);
                        break;
                    case 4:
                        endMove = true;
                        Viewer.exitAll();
                        break;
                    default:
                        PrintFunction.printError(WRONG_COMMAND_MESSAGE);
                }
            } catch (InputMismatchException e) {
                PrintFunction.printError(WRONG_COMMAND_MESSAGE);
            }
        }
    }

}
