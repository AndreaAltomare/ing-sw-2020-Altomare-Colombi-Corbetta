package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.PlaceWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.PlaceWorkerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.PrintFunction;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CLIPlaceWorkerPhase implements CLISubTurnViewer {

    private PlaceWorkerViewer placeWorkerViewer;

    private final int STARTING_SPACE = 7;

    public CLIPlaceWorkerPhase( PlaceWorkerViewer placeWorkerViewer ) {
        this.placeWorkerViewer = placeWorkerViewer;
    }

    /**
     * Prints a request to place the worker and checks it, returns true if it is correct or false if it isn't
     * @param workerToPlace
     * @return
     */
    private boolean placeWorkerRequest(int workerToPlace) {
        PlaceWorkerExecuter placeWorkerExecuter = (PlaceWorkerExecuter) placeWorkerViewer.getMySubTurn().getExecuter();
        boolean placed = false;
        int row;
        int column;

        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.printf("You have %d worker to place\n", workerToPlace);
        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.print("Please, choose the cell where place your worker\n");

        try {
            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.print(">>Row:");
            row = new Scanner( System.in ).nextInt();
            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.print(">>Column:");
            column = new Scanner( System.in ).nextInt();
            if ( row < 0 || row > ViewBoard.getBoard().getXDim() || column < 0 || column > ViewBoard.getBoard().getYDim() ) {
                System.out.println();
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.print(">< The cell chosen is out of board, please change it\n");
            } else {
                placeWorkerExecuter.clear();
                try {
                    placeWorkerExecuter.setCell(row, column);
                    placeWorkerExecuter.doIt();
                    placed = true;
                } catch (WrongParametersException e) {
                    System.out.println();
                    PrintFunction.printRepeatString(" ", STARTING_SPACE);
                    System.out.print(">< The cell chosen is out of board, please change it\n");
                } catch (CannotSendEventException e) {
                    e.printStackTrace();
                }

            }
        } catch (InputMismatchException e) {
            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.print(">< The chosen value isn't correct, please change it\n");
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
        int maxWorkers = 2; //todo: may use Player to know the max number of Worker for each pLayer

        while ( placedNumber < maxWorkers) {
            System.out.println();
            System.out.println();
            ViewBoard.getBoard().toCLI();

            if ( this.placeWorkerRequest( maxWorkers - placedNumber) ) {
                placedNumber++;
            }

        }

    }

    @Override
    public ViewSubTurn getSubTurn() {
        return placeWorkerViewer.getMySubTurn();
    }
}
