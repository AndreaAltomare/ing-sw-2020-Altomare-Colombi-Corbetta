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
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct, please change it";
        final String CORRECT_PLACE = "Your place request is correctly sent";
        String outOfBoardMessage = "The cell chosen is out of board, please change it";

        PlaceWorkerExecuter placeWorkerExecuter = (PlaceWorkerExecuter) placeWorkerViewer.getMySubTurn().getExecuter();
        boolean placed = false;
        int row;
        int column;

        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.printf("You have %d worker to place\n", workerToPlace);
        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.print(REQUEST + "\n");

        try {
            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
            System.out.print( CLIPrintFunction.WRITE_MARK + "Row:");
            row = new Scanner( System.in ).nextInt();
            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
            System.out.print( CLIPrintFunction.WRITE_MARK + "Column:");
            column = new Scanner( System.in ).nextInt();
            if ( row < 0 || row >= ViewBoard.getBoard().getXDim() || column < 0 || column >= ViewBoard.getBoard().getYDim() ) {
                CLIPrintFunction.printError(outOfBoardMessage);
            } else {
                placeWorkerExecuter.clear();
                try {
                    placeWorkerExecuter.setCell(row, column);
                    placed = true;
                    placeWorkerExecuter.doIt();
                    CLIPrintFunction.printCheck(CORRECT_PLACE);
                } catch (WrongParametersException e) {
                    if (!e.getMessage().equals("")) {
                        outOfBoardMessage = e.getMessage();
                    }
                    CLIPrintFunction.printError(outOfBoardMessage);
                } catch (CannotSendEventException e) {
                    CLIPrintFunction.printError(e.getErrorMessage());
                }

            }
        } catch (InputMismatchException e) {
            CLIPrintFunction.printError(WRONG_VALUE_MESSAGE);
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
        int workersToPlace = 1;

        while ( placedNumber < workersToPlace) {
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);

            try {
                ViewBoard.getBoard().toCLI();
            }catch(NullPointerException e){
                break;  //exit from state if there isn't the board
            }

            if ( this.placeWorkerRequest( workersToPlace - placedNumber) ) {
                placedNumber++;
            }

        }

    }

}
