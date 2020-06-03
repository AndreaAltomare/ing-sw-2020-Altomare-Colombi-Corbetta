package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.MoveWorkerExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.TurnStatusChangeExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.MoveViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.WTerminalPlayingViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class WTerminalMovePhase extends WTerminalSubTurnViewer {

    private WTerminalPlayingViewer myWTerminalStatusViewer = null;
    private MoveViewer moveViewer;

    private final int STARTING_SPACE = 7;
    private final int DIRECTION_SPACE = 15;

    public WTerminalMovePhase(MoveViewer moveViewer) {
        this.moveViewer = moveViewer;
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
     * Prints the request to move the worker, checks the response, notifies the movement to Viewer and returns true
     * if it is all correct, or false if it isn't ( notifies to player when the input isn't correct)
     * @return
     */
    private boolean showMoveRequest() {
        int directionResponse;
        ViewCell selectedCell;
        boolean correctResponse = false;
        MoveWorkerExecuter moveWorkerExecuter = (MoveWorkerExecuter) moveViewer.getMySubTurn().getExecuter();

        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println("Please, select a direction to move:");

        System.out.println();
        //first line
        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        PrintFunction.printAtTheMiddle("7: North-West", DIRECTION_SPACE);
        PrintFunction.printAtTheMiddle("8: North", DIRECTION_SPACE);
        PrintFunction.printAtTheMiddle("9: North-Est", DIRECTION_SPACE);
        System.out.println();
        //second line
        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        PrintFunction.printAtTheMiddle("4: West", DIRECTION_SPACE);
        PrintFunction.printRepeatString(" ", DIRECTION_SPACE);
        PrintFunction.printAtTheMiddle("6: Est", DIRECTION_SPACE);
        System.out.println();
        //third line
        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        PrintFunction.printAtTheMiddle("1: South-West", DIRECTION_SPACE);
        PrintFunction.printAtTheMiddle("2: South", DIRECTION_SPACE);
        PrintFunction.printAtTheMiddle("3: South-Est", DIRECTION_SPACE);
        System.out.println();

        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.print(">>");
        try {
            directionResponse = new Scanner(System.in).nextInt();
            selectedCell = this.calculateNextCell( directionResponse );
            if ( selectedCell != null ) {
                moveWorkerExecuter.setWorkerId( ViewWorker.getSelected() );
                moveWorkerExecuter.setCell( selectedCell );
                moveWorkerExecuter.doIt();
                correctResponse = true;
                //todo: a little CLI control if it isn't necessary cancel it and all its helper methods
                if ( this.myWTerminalStatusViewer != null ) {
                    myWTerminalStatusViewer.setMoveTrue();
                }
            } else {
                System.out.println();
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.print(">< The selected direction doesn't exist\n");
            }
        } catch (InputMismatchException e) {
            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.print(">< The chosen value isn't correct\n");
        } catch (NotFoundException e) {
            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.print(">< The cell in selected direction doesn't exist\n");
        } catch (WrongParametersException | CannotSendEventException e) {
            e.printStackTrace();
        }

        return correctResponse;
    }

    /**
     * Calculates the cell at chosen direction and returns it if it is possible, returns null if the direction isn't correct
     * and trow a NotFoundException if the cell isn't on the board ( or the select worker isn't on the board )
     * @param direction
     * @return
     * @throws NotFoundException
     */
    private ViewCell calculateNextCell(int direction ) throws NotFoundException {
        ViewCell selectedCell = null;
        int workerRow;
        int workerColumn;

        workerRow = ViewWorker.getSelected().getPosition().getX();
        workerColumn = ViewWorker.getSelected().getPosition().getY();

        switch (direction) {
            case 7:
                selectedCell = ViewBoard.getBoard().getCellAt(workerRow - 1, workerColumn - 1);
                break;
            case 8:
                selectedCell = ViewBoard.getBoard().getCellAt(workerRow - 1, workerColumn );
                break;
            case 9:
                selectedCell = ViewBoard.getBoard().getCellAt(workerRow - 1, workerColumn + 1);
                break;
            case 4:
                selectedCell = ViewBoard.getBoard().getCellAt( workerRow , workerColumn - 1);
                break;
            case 6:
                selectedCell = ViewBoard.getBoard().getCellAt( workerRow , workerColumn + 1);
                break;
            case 1:
                selectedCell = ViewBoard.getBoard().getCellAt( workerRow + 1, workerColumn - 1);
                break;
            case 2:
                selectedCell = ViewBoard.getBoard().getCellAt( workerRow + 1, workerColumn );
                break;
            case 3:
                selectedCell = ViewBoard.getBoard().getCellAt( workerRow + 1, workerColumn + 1);
                break;
            case 5:
            default:
                ;
        }

        return selectedCell;

    }

    /**
     * Tries to change subTurn in BUILD and returns true if it can, or false if it can't
     * @return
     */
    private boolean toBuildPhase() {
        boolean changePhase = false;
        TurnStatusChangeExecuter turnStatusChangeExecuter = new TurnStatusChangeExecuter();

        try {
            turnStatusChangeExecuter.setStatusId( ViewSubTurn.BUILD );
            turnStatusChangeExecuter.doIt();
            changePhase = true;
        } catch (WrongParametersException | CannotSendEventException e) {
            e.printStackTrace();
        }

        return changePhase;

    }

    /**
     * Prints the board and a request of command and uses some private methods to execute them as long as
     * a movement or a change status is correctly executed
     */
    @Override
    public void show() {
        boolean endMove = false;
        int actionSelected;

        while ( !endMove ) {
            System.out.println();
            System.out.println();

/*            //todo: valutarlo
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/
            ViewBoard.getBoard().toWTerminal();

            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println("Please, select a command:");
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println("1: Print all gods' details");
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println("2: Move worker");
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println("3: Go to Build Phase");

            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.print(">>");
            try {
                actionSelected = new Scanner(System.in).nextInt();
                switch ( actionSelected ) {
                    case 1:
                        this.showCardsDetails();
                        break;
                    case 2:
                        endMove = this.showMoveRequest();
                        break;
                    case 3:
                        endMove = this.toBuildPhase();
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

    @Override
    public ViewSubTurn getSubTurn() {
        return moveViewer.getMySubTurn();
    }

    /**
     * Overloading of WTerminalSubTurnViewer's setMyWTerminalStatusViewer to set the correct WTerminalStatusViewer
     * @param myWTerminalStatusViewer
     */
    public void setMyWTerminalStatusViewer( WTerminalPlayingViewer myWTerminalStatusViewer) {
        this.myWTerminalStatusViewer = myWTerminalStatusViewer;
    }
}
