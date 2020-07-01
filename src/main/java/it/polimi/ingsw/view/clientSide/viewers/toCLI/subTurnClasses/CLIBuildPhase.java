package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.BuildBlockExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.NextTurnExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.BuildViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.CLISymbols;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CLIBuildPhase extends CLISubTurnViewer {

    private BuildViewer buildViewer;

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
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
            System.out.println(DIRECTION_REQUEST);
            System.out.println();

            this.printDirection();

            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
            System.out.print(CLIPrintFunction.WRITE_MARK);
            try {
                directionResponse = new Scanner(System.in).nextInt();
                selectedCell = this.calculateNextCell(directionResponse);
                if (selectedCell != null) {
                    buildBlockExecuter.setWorkerId(ViewWorker.getSelected());
                    buildBlockExecuter.setCell(selectedCell);
                    buildBlockExecuter.setPlaceable(pLaceableToBuild);
                    correctResponse = true;
                    buildBlockExecuter.doIt();
                    CLIPrintFunction.printCheck(CORRECT_MESSAGE);
                } else {
                    CLIPrintFunction.printError(WRONG_DIRECTION_MESSAGE);
                }
            } catch (InputMismatchException e) {
                CLIPrintFunction.printError(WRONG_VALUE_MESSAGE);
            } catch (NotFoundException e) {
                CLIPrintFunction.printError(OUT_OF_BOARD_MESSAGE);
            } catch (WrongParametersException e) {
                if (!e.getMessage().equals("")) {
                    wrongSetExecutorMessage = e.getMessage();
                }
                CLIPrintFunction.printError(wrongSetExecutorMessage);
            } catch (CannotSendEventException e) {
                CLIPrintFunction.printError(e.getErrorMessage());
            }
        }

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
        final int PLACEABLE_SPACE = 7;
        final String BLOCK_COLOR = ANSIStyle.GREY.getEscape();
        final String DOME_COLOR = ANSIStyle.BLUE.getEscape();

        String blockType = null;
        int typeSelected;

        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.println(BLOCK_TYPE_REQUEST);

        //block
        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.print("1: ");
        CLIPrintFunction.printAtTheMiddle(BLOCK_COLOR, CLISymbols.BLOCK.getMiddleRepresentation(), CLISymbols.BLOCK.getLength(), PLACEABLE_SPACE);

        System.out.println();

        // dome
        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.print("2: ");
        CLIPrintFunction.printAtTheMiddle(DOME_COLOR, CLISymbols.DOME.getMiddleRepresentation(), CLISymbols.DOME.getLength(), PLACEABLE_SPACE);

        System.out.println();

        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.print(CLIPrintFunction.WRITE_MARK);
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
                    CLIPrintFunction.printError(WRONG_TYPE_MESSAGE);
            }
        } catch (InputMismatchException e) {
            CLIPrintFunction.printError(WRONG_VALUE_MESSAGE);
        }

        return  blockType;
    }

    /**
     * Tries to close player's turn and returns true if it can, or false if it can't
     * @return
     */
    private boolean endTurn() {

        NextTurnExecuter nextTurnExecuter = new NextTurnExecuter();
        boolean endTurn = false;

        try {
            endTurn = true;
            nextTurnExecuter.doIt();
        } catch (CannotSendEventException e) {
            CLIPrintFunction.printError(e.getErrorMessage());
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
        final String EXIT_COMMAND = "Exit from the game";
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

            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
            System.out.println(COMMAND_REQUEST);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
            System.out.println("1: " +  GODS_DETAILS_COMMAND );
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
            System.out.println("2: " + BUILD_COMMAND);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
            System.out.println("3: " + TO_MOVE_PHASE_COMMAND);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
            System.out.println("4: " + END_TURN_COMMAND);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
            System.out.println("5: " + EXIT_COMMAND);

            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
            System.out.print(CLIPrintFunction.WRITE_MARK);
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
                        CLIPrintFunction.printError(WRONG_COMMAND_MESSAGE);
                }
            } catch (InputMismatchException e) {
                CLIPrintFunction.printError(WRONG_COMMAND_MESSAGE);
            }
        }

     }

}
