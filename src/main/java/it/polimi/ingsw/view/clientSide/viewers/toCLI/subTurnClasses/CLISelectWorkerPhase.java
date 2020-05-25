package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SelectWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.SelectWorkerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.SymbolsLevel;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.PrintFunction;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CLISelectWorkerPhase implements CLISubTurnViewer {

    private SelectWorkerViewer selectWorkerViewer;

    private final int STARTING_SPACE = 7;
    private final int WORKER_SPACE = 5;

    public CLISelectWorkerPhase(SelectWorkerViewer selectWorkerViewer) {
        this.selectWorkerViewer = selectWorkerViewer;
    }

    /**
     * Prints the board and the possible command and uses some private methods to execute them
     */
    @Override
    public void show() {
        boolean selected = false;
        int actionSelected;

        while ( !selected ) {
            System.out.println();
            System.out.println();
            ViewBoard.getBoard().toCLI();

            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println("Please, select a command");
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println("1: Print all gods' details");
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println("1: Select worker");

            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println(">>");
            try {
                actionSelected = new Scanner(System.in).nextInt();
                switch ( actionSelected ) {
                    case 1:
                        this.showCardsDetails();
                        break;
                    case 2:
                        selected = this.showSelectRequest();
                        break;
                    default:
                        System.out.println();
                        PrintFunction.printRepeatString(" ", STARTING_SPACE);
                        System.out.println(">< The chosen command doesn't exist, please change it");
                }
            } catch (InputMismatchException e) {
                System.out.println();
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.println(">< The chosen command doesn't exist, please change it");
            }
        }

    }

    /**
     * Prints the Name, Epithet and Description of all the player's God
     */
    private void showCardsDetails () {
        ViewCard viewCard;

        for ( ViewPlayer viewPlayer : ViewPlayer.getPlayerList() ) {
            System.out.println();
            System.out.println();
            try {
                viewCard = viewPlayer.getCard();
                //todo:maybe add god's symbol
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.printf("Name: %s\n", viewCard.getName());
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.printf("Epithet: %s\n", viewCard.getEpiteth());
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.printf("Description: %s\n", viewCard.getDescription());
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Prints and check the request to select the worker and returns true if the response is correct, or false if it isn't
     * @return
     */
    private boolean showSelectRequest() {
        boolean isSelected = false;
        int worker;
        int workerNumber = 1;
        ViewPlayer viewPlayer;
        List<ViewWorker> workerList = new ArrayList<>();
        SelectWorkerExecuter selectWorkerExecuter = (SelectWorkerExecuter) selectWorkerViewer.getMySubTurn().getExecuter();

        System.out.println();
        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println("Please, choose your worker:");

        try {
            if (ViewNickname.getMyNickname() != null ) {
                viewPlayer = ViewPlayer.searchByName(ViewNickname.getMyNickname());
                for (ViewWorker viewWorker : viewPlayer.getWorkers()) {
                    workerList.add(viewWorker);
                    this.printWorker(viewWorker, workerNumber);
                    workerNumber++;
                }
                System.out.println();
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.print(">>");
                worker = new Scanner( System.in ).nextInt();
                if ( worker > 0 && worker < workerNumber) {
                    selectWorkerExecuter.setWorkerId( workerList.get( worker - 1) );
                    selectWorkerExecuter.doIt();
                    isSelected = true;
                } else {
                    System.out.println();
                    PrintFunction.printRepeatString(" ", STARTING_SPACE);
                    System.out.print(">< The chosen value isn't  a valid worker's number");
                }
            }
        } catch (NotFoundException | WrongParametersException | CannotSendEventException e) {
            e.printStackTrace();
        } catch (InputMismatchException i) {
            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.print(">< The chosen value isn't correct");
        }

        return isSelected;

    }

    /**
     * Prints an image of worker, his number and his position
     * @param viewWorker
     * @param workerNumber
     */
    private void printWorker( ViewWorker viewWorker, int workerNumber ) {

        try {
            System.out.println();
            //first line
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            PrintFunction.printAtTheMiddle(viewWorker.toCLI(SymbolsLevel.UP), WORKER_SPACE);
            System.out.printf("Worker's number: %d\n", workerNumber);
            //second line
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            PrintFunction.printAtTheMiddle(viewWorker.toCLI(SymbolsLevel.MIDDLE), WORKER_SPACE);
            System.out.println();
            //third line
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            PrintFunction.printAtTheMiddle(viewWorker.toCLI(SymbolsLevel.DOWN), WORKER_SPACE);
            System.out.printf("Worker's cell: ( %d ; %d )\n", viewWorker.getPosition().getX(), viewWorker.getPosition().getY());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ViewSubTurn getSubTurn() {
        return selectWorkerViewer.getMySubTurn();
    }
}
