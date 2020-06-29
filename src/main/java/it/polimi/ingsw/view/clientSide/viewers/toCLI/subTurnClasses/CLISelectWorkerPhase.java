package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SelectWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.SelectWorkerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.CLISymbols;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.UnicodeSymbol;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses.CLIPlayingViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CLISelectWorkerPhase extends CLISubTurnViewer {

    private SelectWorkerViewer selectWorkerViewer;

    private final int STARTING_SPACE = 7;
    private final int WORKER_SPACE = 5;
    private final String ERROR_COLOR_AND_SYMBOL = ANSIStyle.RED.getEscape() + UnicodeSymbol.X_MARK.getEscape();
    private final String CORRECT_COLOR_AND_SYMBOL = ANSIStyle.GREEN.getEscape() + UnicodeSymbol.CHECK_MARK.getEscape();
    private final String WRITE_MARK = ANSIStyle.UNDERSCORE.getEscape() + UnicodeSymbol.PENCIL.getEscape() + ANSIStyle.RESET;

    public CLISelectWorkerPhase(SelectWorkerViewer selectWorkerViewer) {
        this.selectWorkerViewer = selectWorkerViewer;
    }

    /**
     * Prints the board and the possible command and uses some private methods to execute them
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
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
            try {
                ViewBoard.getBoard().toCLI();
            }catch(NullPointerException e){
                break;  //exit from state if there isn't the board
            }

            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println(COMMAND_REQUEST);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println("1:" + DETAILS_GODS_COMMAND);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println("2:" + SELECT_WORKER_COMMAND);

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
                        selected = this.showSelectRequest();
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


    /**
     * Prints and check the request to select the worker and returns true if the response is correct, or false if it isn't
     * @return
     */
    private boolean showSelectRequest() {
        final String REQUEST = "Please, choose your worker:";
        final String CORRECT_MESSAGE = "The worker is correctly selected";
        final String WRONG_WORKER_MESSAGE = "The chosen value isn't a valid worker's number";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct";

        boolean isSelected = false;
        int worker;
        int workerNumber = 1;
        String playerColor;
        ViewPlayer viewPlayer;
        List<ViewWorker> workerList = new ArrayList<>();
        SelectWorkerExecuter selectWorkerExecuter = (SelectWorkerExecuter) selectWorkerViewer.getMySubTurn().getExecuter();

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
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
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.print(WRITE_MARK);
                worker = new Scanner( System.in ).nextInt();
                if ( worker > 0 && worker < workerNumber) {
                    selectWorkerExecuter.setWorkerId( workerList.get( worker - 1) );
                    selectWorkerExecuter.doIt();
                    System.out.println();
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                    System.out.print(CORRECT_COLOR_AND_SYMBOL + CORRECT_MESSAGE +ANSIStyle.RESET);
                    isSelected = true;
                } else {
                    System.out.println();
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                    System.out.print(ERROR_COLOR_AND_SYMBOL + WRONG_WORKER_MESSAGE + ANSIStyle.RESET);
                }
            }
        } catch (NotFoundException | WrongParametersException | CannotSendEventException e) {
            e.printStackTrace();
        } catch (InputMismatchException i) {
            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.print(ERROR_COLOR_AND_SYMBOL + WRONG_VALUE_MESSAGE + ANSIStyle.RESET);
        }
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);

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
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE + 2);
            CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, viewWorker.toCLI(true), CLISymbols.WORKER.getLength(), WORKER_SPACE);
            System.out.println();
            //second line
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE );
            System.out.printf("%d.", workerNumber);
            CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, viewWorker.toCLI(false), CLISymbols.WORKER.getLength(), WORKER_SPACE);
            System.out.printf("Worker's cell: ( Row: %d ; Columns: %d )", viewWorker.getPosition().getX(), viewWorker.getPosition().getY());
            System.out.println();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

    }

}
