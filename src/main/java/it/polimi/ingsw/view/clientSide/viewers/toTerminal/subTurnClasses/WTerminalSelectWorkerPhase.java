package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SelectWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.SelectWorkerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.SymbolsLevel;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Class that represents the <code>WTerminalSubTurnViewer</code> SelectWorker on the Windows Terminal
 * using methods of <code>PrintFunction</code> and <code>SymbolsLevel</code>
 *
 * @see WTerminalSubTurnViewer
 * @see PrintFunction
 * @see SymbolsLevel
 * @author Marco
 */
public class WTerminalSelectWorkerPhase extends WTerminalSubTurnViewer {

    private SelectWorkerViewer selectWorkerViewer;

    /**
     * Constructor to set the correctly <code>SubTurnViewer</code>
     *
     * @see SelectWorkerViewer
     * @param selectWorkerViewer <code>SubTurnViewer</code> linked at this class
     */
    public WTerminalSelectWorkerPhase(SelectWorkerViewer selectWorkerViewer) {
        this.selectWorkerViewer = selectWorkerViewer;
    }

    /**
     * Prints the board and the possible command and uses some private methods to execute them as long as
     * a response of <code>ViewWorker</code> is correctly set
     */
    @Override
    public void show() {
        final String COMMAND_REQUEST = "Please, select a command:";
        final String DETAILS_GODS_COMMAND = "Print all gods' details";
        final String SELECT_WORKER_COMMAND = "Select worker";
        final String WRONG_COMMAND_MESSAGE = "The chosen command doesn't exist, please change it";

        boolean selected = false;
        int actionSelected;

        while ( !selected ) {
            PrintFunction.printRepeatString("\n", 2);

            try {
                ViewBoard.getBoard().toWTerminal();
            }catch(NullPointerException e){
                break;  //exit from state if there isn't the board
            }

            System.out.println();
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println(COMMAND_REQUEST);
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println("1: " + DETAILS_GODS_COMMAND);
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println("2: " + SELECT_WORKER_COMMAND);

            System.out.println();
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.print(">>");
            try {
                actionSelected = new Scanner(System.in).nextInt();
                switch ( actionSelected ) {
                    case 1:
                        this.showCardsDetails(true);
                        break;
                    case 2:
                        selected = this.showSelectRequest();
                        break;
                    default:
                        PrintFunction.printError(WRONG_COMMAND_MESSAGE);
                }
            } catch (InputMismatchException e) {
                PrintFunction.printError(WRONG_COMMAND_MESSAGE);
            }
        }

    }

    /**
     * Prints and check the request to select the <code>ViewWorker</code> and returns true if it is correctly set,
     * or false if it isn't notifying the result with a printed message
     *
     * @return true if s <code>ViewWorker</code> is correctly set, else false
     */
    private boolean showSelectRequest() {
        final String REQUEST = "Please, choose your worker:";
        final String CORRECT_MESSAGE = "The select request is correctly sent";
        final String WRONG_WORKER_MESSAGE = "The chosen value isn't a valid worker's number";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct";
        String wrongSetMessage = "Your worker isn't correctly set";

        boolean isSelected = false;
        int worker;
        int workerNumber = 1;
        ViewPlayer viewPlayer;
        List<ViewWorker> workerList = new ArrayList<>();
        SelectWorkerExecuter selectWorkerExecuter = (SelectWorkerExecuter) selectWorkerViewer.getMySubTurn().getExecuter();

        System.out.println();
        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.println(REQUEST);

        try {
            if (ViewNickname.getMyNickname() != null ) {
                viewPlayer = ViewPlayer.searchByName(ViewNickname.getMyNickname());
                for (ViewWorker viewWorker : viewPlayer.getWorkers()) {
                    workerList.add(viewWorker);
                    this.printWorker(viewWorker, workerNumber);
                    workerNumber++;
                }
                System.out.println();
                PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
                System.out.print(">>");
                worker = new Scanner( System.in ).nextInt();
                if ( worker > 0 && worker < workerNumber) {
                    selectWorkerExecuter.setWorkerId( workerList.get( worker - 1) );
                    isSelected = true;
                    selectWorkerExecuter.doIt();
                    PrintFunction.printCheck(CORRECT_MESSAGE);
                } else {
                    PrintFunction.printError(WRONG_WORKER_MESSAGE);
                }
            }
        } catch (NotFoundException ignored) {
        } catch (InputMismatchException i) {
            PrintFunction.printError(WRONG_VALUE_MESSAGE);
        } catch (WrongParametersException e) {
            if (!e.getMessage().equals("")) {
                wrongSetMessage = e.getMessage();
            }
            PrintFunction.printError(wrongSetMessage);
        }catch (CannotSendEventException e) {
            PrintFunction.printError(e.getErrorMessage());
        }
        PrintFunction.printRepeatString("\n", 2);

        return isSelected;

    }

    /**
     * Prints an image of worker, his number and his position
     *
     * @param viewWorker <code>ViewWorker</code> to print
     * @param workerNumber number of the viewWorker
     */
    private void printWorker( ViewWorker viewWorker, int workerNumber ) {
        final int WORKER_SPACE = 5;

        try {
            System.out.println();
            //first line
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            PrintFunction.printAtTheMiddle(viewWorker.toWTerminal(SymbolsLevel.UP), WORKER_SPACE);
            System.out.printf("Worker's number: %d\n", workerNumber);
            //second line
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            PrintFunction.printAtTheMiddle(viewWorker.toWTerminal(SymbolsLevel.MIDDLE), WORKER_SPACE);
            System.out.println();
            //third line
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            PrintFunction.printAtTheMiddle(viewWorker.toWTerminal(SymbolsLevel.DOWN), WORKER_SPACE);
            System.out.printf("Worker's cell: ( Row: %d ; Columns: %d )\n", viewWorker.getPosition().getX(), viewWorker.getPosition().getY());
        } catch (NotFoundException ignored) {
        }

    }

}
