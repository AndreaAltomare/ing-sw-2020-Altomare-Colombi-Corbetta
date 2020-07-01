package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.BuildBlockExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.NextTurnExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.BuildViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.Symbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class that represents the <code>WTerminalSubTurnViewer</code> SelectMyCart on the Windows Terminal
 * using methods of <code>PrintFunction</code> and <code>Symbols</code>
 *
 * @see WTerminalSubTurnViewer
 * @see PrintFunction
 * @see Symbols
 * @author Marco
 */
public class WTerminalBuildPhase extends WTerminalSubTurnViewer {

    private BuildViewer buildViewer;

    /**
     * Constructor to set the correctly <code>SubTurnViewer</code>
     *
     * @see BuildViewer
     * @param buildViewer <code>SubTurnViewer</code> linked at this class
     */
    public WTerminalBuildPhase(BuildViewer buildViewer) {
        this.buildViewer = buildViewer;
    }


    /**
     * Prints the request to build using a private method to choose block's type, checks the response,
     * notifies the construction to Viewer and returns true if the request is correctly set,
     * or false if it isn't. Notifies to player when the result print a message
     *
     * @return true if executer is correctly set, false if it isn.t
     */
    private boolean showBuildRequest() {
        final String DIRECTION_REQUEST = "Please, select a direction to build:";
        final String CORRECT_MESSAGE = "Your built request is correctly send";
        final String WRONG_DIRECTION_MESSAGE = "The selected direction doesn't exist";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct";
        final String OUT_OF_BOARD_MESSAGE = "The cell in selected direction doesn't exist";
        String wrongSetExecutorMessage = "Build request isn't correct";
        int directionResponse;
        ViewCell selectedCell;
        String pLaceableToBuild;
        boolean correctResponse = false;
        BuildBlockExecuter buildBlockExecuter = (BuildBlockExecuter) buildViewer.getMySubTurn().getExecuter();

        pLaceableToBuild = this.typeBlockRequest();

        if ( pLaceableToBuild != null) {
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
                selectedCell = this.calculateNextCell(directionResponse);
                if (selectedCell != null) {
                    buildBlockExecuter.setWorkerId(ViewWorker.getSelected());
                    buildBlockExecuter.setCell(selectedCell);
                    buildBlockExecuter.setPlaceable(pLaceableToBuild);
                    correctResponse = true;
                    buildBlockExecuter.doIt();
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
                    wrongSetExecutorMessage = e.getMessage();
                }
                PrintFunction.printError(wrongSetExecutorMessage);
            } catch (CannotSendEventException e) {
                PrintFunction.printError(e.getErrorMessage());
            }
        }

        return correctResponse;
    }

    /**
     * Prints the request to choose the block's type, checks the response and returns a <code>String</code> representing block's type
     * if it is correct, or null and an error message to player if it isn't
     *
     * @return <code>String</code> to define the block type if the response is correct, else false
     */
    private String typeBlockRequest() {
        final int PLACEABLE_SPACE = 7;
        final String BLOCK_TYPE_REQUEST = "Please, select what type of block you want to choose:";
        final String WRONG_TYPE_MESSAGE = "The chosen block's type doesn't exist";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct";
        String blockType = null;
        int typeSelected;

        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.println(BLOCK_TYPE_REQUEST);

        //block: first line
        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE + 2);
        PrintFunction.printAtTheMiddle( Symbols.BLOCK.getUpRepresentation() , PLACEABLE_SPACE);
        //block: second line
        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print("1:");
        PrintFunction.printAtTheMiddle( Symbols.BLOCK.getMiddleRepresentation() , PLACEABLE_SPACE);

        //dome: first line
        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE + 2);
        PrintFunction.printAtTheMiddle( Symbols.DOME.getUpRepresentation() , PLACEABLE_SPACE);
        //block: second line
        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print("2:");
        PrintFunction.printAtTheMiddle( Symbols.DOME.getMiddleRepresentation() , PLACEABLE_SPACE);
        System.out.println();

        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print(">>");
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
                    PrintFunction.printError(WRONG_TYPE_MESSAGE);
            }
        } catch (InputMismatchException e) {
            PrintFunction.printError(WRONG_VALUE_MESSAGE);
        }


        return  blockType;
    }

    /**
     * Tries to close player's turn and notifies the result with printed message
     * @return true
     */
    private boolean endTurn() {
        NextTurnExecuter nextTurnExecuter = new NextTurnExecuter();
        boolean endTurn = false;

        try {
            endTurn = true;
            nextTurnExecuter.doIt();
        } catch (CannotSendEventException e) {
            PrintFunction.printError(e.getErrorMessage());
        }
        PrintFunction.printRepeatString("\n", 2);



        return endTurn;
    }

    /**
     * Prints the board and a request of command and uses some private methods to execute them as long as
     * a construction, a change status or a endTurn is correctly chosen
     */
    @Override
    public void show() {
        final String COMMAND_REQUEST = "Please, select a command:";
        final String GODS_DETAILS_COMMAND = "Print all gods' details";
        final String BUILD_COMMAND = "Build";
        final String TO_MOVE_PHASE_COMMAND = "Go to Move Phase";
        final String END_TURN_COMMAND = "End Turn";
        final String EXIT_COMMAND = "Exit from the game";
        final String WRONG_COMMAND_MESSAGE = "The chosen command doesn't exist, please change it";
        boolean endBuild = false;
        int actionSelected;

        while ( !endBuild ) {
            PrintFunction.printRepeatString("\n", 2);

            try {
                ViewBoard.getBoard().toWTerminal();
            }catch(NullPointerException e){
                break;  //exit from state if there isn't the board
            }

            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println(COMMAND_REQUEST);
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println("1: " +  GODS_DETAILS_COMMAND );
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println("2: " + BUILD_COMMAND);
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println("3: " + TO_MOVE_PHASE_COMMAND);
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println("4: " + END_TURN_COMMAND);
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println("5: " + EXIT_COMMAND);

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
                        endBuild = this.showBuildRequest();
                        break;
                    case 3:
                        endBuild = this.changePlayingPhase(ViewSubTurn.MOVE);
                        break;
                    case 4:
                        endBuild = this.endTurn();
                        break;
                    case 5:
                        endBuild = true;
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
