package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.PlaceWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.PlaceWorkerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class that represents the <code>WTerminalSubTurnViewer</code> PLaceWorker on the Windows Terminal
 * using methods of <code>PrintFunction</code>
 *
 * @see WTerminalSubTurnViewer
 * @see PrintFunction
 * @author Marco
 */
public class WTerminalPlaceWorkerPhase extends WTerminalSubTurnViewer {

    private PlaceWorkerViewer placeWorkerViewer;

    /**
     * Constructor to set the correctly <code>SubTurnViewer</code>
     *
     * @see PlaceWorkerViewer
     * @param placeWorkerViewer <code>SubTurnViewer</code> linked at this class
     */
    public WTerminalPlaceWorkerPhase(PlaceWorkerViewer placeWorkerViewer ) {
        this.placeWorkerViewer = placeWorkerViewer;
    }

    /**
     * Prints a request to place the worker and checks it, returns true if the response is correct and correctly set or false if it isn't
     *
     * @param workerToPlace number of <code>ViewWorker</code> chosen
     * @return true if the <code>ViewCell</code> is correctly chosen and set, else false
     */
    private boolean placeWorkerRequest(int workerToPlace) {
        final String REQUEST = "Please, choose the cell where place your worker:";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct, please change it";
        final String CORRECT_PLACE = "Your place request is correctly sent";
        String outOfBoardMessage = "The cell chosen is out of board, please change it";

        PlaceWorkerExecuter placeWorkerExecuter = (PlaceWorkerExecuter) placeWorkerViewer.getMySubTurn().getExecuter();
        boolean placed = false;
        int row;
        int column;

        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.printf("You have %d worker to place\n", workerToPlace);
        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.println(REQUEST);

        try {
            System.out.println();
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.print(">>Row:");
            row = new Scanner( System.in ).nextInt();
            System.out.println();
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.print(">>Column:");
            column = new Scanner( System.in ).nextInt();
            if ( row < 0 || row > ViewBoard.getBoard().getXDim() || column < 0 || column > ViewBoard.getBoard().getYDim() ) {
                PrintFunction.printError(outOfBoardMessage);
            } else {
                placeWorkerExecuter.clear();
                try {
                    placeWorkerExecuter.setCell(row, column);
                    placed = true;
                    placeWorkerExecuter.doIt();
                    PrintFunction.printCheck(CORRECT_PLACE);
                } catch (WrongParametersException e) {
                    if (!e.getMessage().equals("")) {
                        outOfBoardMessage = e.getMessage();
                    }
                    PrintFunction.printError(outOfBoardMessage);
                } catch (CannotSendEventException e) {
                    PrintFunction.printError(e.getErrorMessage());
                }

            }
        } catch (InputMismatchException e) {
            PrintFunction.printError(WRONG_VALUE_MESSAGE);
        }
        System.out.println();

        return placed;

    }

    /**
     * Prints the board and uses a private method to ask the cell where place worker and check his response as long as
     * the place response is correctly set
     */
    @Override
    public void show() {
        int placedNumber = 0;
        int maxWorkers = 1;

        while ( placedNumber < maxWorkers) {
            PrintFunction.printRepeatString("\n", PrintFunction.STARTING_SPACE);

            try {
                ViewBoard.getBoard().toWTerminal();
            }catch(NullPointerException e){
                break;  //exit from state if there isn't the board
            }

            if ( this.placeWorkerRequest( maxWorkers - placedNumber) ) {
                placedNumber++;
            }

        }

    }

}
