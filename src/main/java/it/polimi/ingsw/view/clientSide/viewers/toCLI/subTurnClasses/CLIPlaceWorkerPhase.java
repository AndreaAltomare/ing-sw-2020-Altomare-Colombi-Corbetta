package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.PlaceWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.PlaceWorkerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.UnicodeSymbol;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses.CLIGamePreparationViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CLIPlaceWorkerPhase extends CLISubTurnViewer {

    private PlaceWorkerViewer placeWorkerViewer;

    private final int STARTING_SPACE = 7;
    private final String ERROR_COLOR_AND_SYMBOL = ANSIStyle.RED.getEscape() + UnicodeSymbol.X_MARK.getEscape();
    private final String CORRECT_COLOR_AND_SYMBOL = ANSIStyle.GREEN.getEscape() + UnicodeSymbol.CHECK_MARK.getEscape();
    private final String WRITE_MARK = ANSIStyle.UNDERSCORE.getEscape() + UnicodeSymbol.PENCIL.getEscape() + ANSIStyle.RESET;


    public CLIPlaceWorkerPhase(PlaceWorkerViewer placeWorkerViewer ) {
        this.placeWorkerViewer = placeWorkerViewer;
    }

    /**
     * Prints a request to place the worker and checks it, returns true if it is correct or false if it isn't
     * @param workerToPlace
     * @return
     */
    private boolean placeWorkerRequest(int workerToPlace) {
        final String REQUEST = "Please, choose the cell where place your worker:";
        final String OUT_OF_BOARD_MESSAGE = "The cell chosen is out of board, please change it";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct, please change it";
        final String CORRECT_PLACE = "Your worker is correctly set";

        PlaceWorkerExecuter placeWorkerExecuter = (PlaceWorkerExecuter) placeWorkerViewer.getMySubTurn().getExecuter();
        boolean placed = false;
        int row;
        int column;

        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.printf("You have %d worker to place\n", workerToPlace);
        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.print(REQUEST + "\n");

        try {
            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.print( WRITE_MARK + "Row:");
            row = new Scanner( System.in ).nextInt();
            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.print( WRITE_MARK + "Column:");
            column = new Scanner( System.in ).nextInt();
            if ( row < 0 || row >= ViewBoard.getBoard().getXDim() || column < 0 || column >= ViewBoard.getBoard().getYDim() ) {
                System.out.println();
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.println(ERROR_COLOR_AND_SYMBOL + OUT_OF_BOARD_MESSAGE + ANSIStyle.RESET);
            } else {
                placeWorkerExecuter.clear();
                try {
                    placeWorkerExecuter.setCell(row, column);
                    placeWorkerExecuter.doIt();
                    System.out.println();
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                    System.out.println(CORRECT_COLOR_AND_SYMBOL + CORRECT_PLACE + ANSIStyle.RESET);
                    placed = true;
                } catch (WrongParametersException e) {
                    System.out.println();
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                    System.out.println(ERROR_COLOR_AND_SYMBOL + OUT_OF_BOARD_MESSAGE + ANSIStyle.RESET);
                } catch (CannotSendEventException e) {
                    e.printStackTrace();
                }

            }
        } catch (InputMismatchException e) {
            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println(ERROR_COLOR_AND_SYMBOL + WRONG_VALUE_MESSAGE + ANSIStyle.RESET);
        }
        System.out.println();

        return placed;

    }

    /**
     * Prints the board and uses a private method to ask the cell where place worker and check his response as long as
     * the player places all his workers
     */
    @Override
    public void show() {
        int placedNumber = 0;
        int maxWorkers = 1; // 2  //todo: may use Player to know the max number of Worker for each pLayer

        while ( placedNumber < maxWorkers) {
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);

            ViewBoard.getBoard().toCLI();

            if ( this.placeWorkerRequest( maxWorkers - placedNumber) ) {
                placedNumber++;
            }

        }

    }

}
