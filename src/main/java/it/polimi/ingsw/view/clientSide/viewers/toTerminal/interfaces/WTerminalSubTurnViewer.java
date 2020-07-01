package it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.TurnStatusChangeExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.Scanner;

/**
 * Abstract class with some methods to menage some command used by some sub classes
 *
 * @author Marco
 */
public abstract class WTerminalSubTurnViewer implements SpecificSubTurnViewer {

    /**
     * Abstract method used by sub classes to menage the their sub status
     */
    public abstract void show();

    /**
     * Method which prints the Name, Epithet and Description of all the player's God, then if waitingPLayer == true
     * prints a string request and waits that the player press enter
     *
     * @param waitingPlayer <code>boolean</code> parameter to wait an input to continue if waitingPLayer == true, or not
     *                      if waitingPlayer == false
     */
    protected void showCardsDetails (boolean waitingPlayer) {
        final String CONTINUE_REQUEST = "Press Enter to continue";
        ViewCard viewCard;

        for (ViewPlayer viewPlayer : ViewPlayer.getPlayerList()) {
            PrintFunction.printRepeatString("\n", PrintFunction.STARTING_SPACE);
            try {
                viewCard = viewPlayer.getCard();
                PrintFunction.printRepeatString(" ", CLIPrintFunction.STARTING_SPACE);
                System.out.printf("Name: %s\n", viewCard.getName());
                PrintFunction.printRepeatString(" ", CLIPrintFunction.STARTING_SPACE);
                System.out.printf("Epithet: %s\n", viewCard.getEpiteth());
                PrintFunction.printRepeatString(" ", CLIPrintFunction.STARTING_SPACE);
                System.out.printf("Description: %s\n", viewCard.getDescription());
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }

        if (waitingPlayer) {
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.print(CONTINUE_REQUEST);
            new Scanner(System.in).nextLine();
        }

    }

    /**
     * Method which calculates the cell at chosen direction and returns it if it is possible,
     * returns null if the direction isn't correct
     * and trow a NotFoundException if the cell isn't on the board ( or the select worker isn't on the board )
     *
     * @see ViewWorker
     * @see ViewCell
     * @param direction <code>int</code> to represent the direction
     * @return <code>ViewCell</code> if there is a <code>ViewWorker</code> selected and a <code>ViewCell</code> at chosen direction
     * @throws NotFoundException if the <code>VIewCell</code> or <code>ViewWorker</code> isn't found
     */
    protected ViewCell calculateNextCell(int direction ) throws NotFoundException {
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
     * Prints the table of direction with the number which represent the direction
     */
    protected void printDirection() {
        final int DIRECTION_SPACE = 15;

        //first line
        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        PrintFunction.printAtTheMiddle("7: North-West", DIRECTION_SPACE);
        PrintFunction.printAtTheMiddle("8: North", DIRECTION_SPACE);
        PrintFunction.printAtTheMiddle("9: North-Est", DIRECTION_SPACE);
        System.out.println();
        //second line
        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        PrintFunction.printAtTheMiddle("4: West", DIRECTION_SPACE);
        PrintFunction.printRepeatString(" ", DIRECTION_SPACE);
        PrintFunction.printAtTheMiddle("6: Est", DIRECTION_SPACE);
        System.out.println();
        //third line
        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        PrintFunction.printAtTheMiddle("1: South-West", DIRECTION_SPACE);
        PrintFunction.printAtTheMiddle("2: South", DIRECTION_SPACE);
        PrintFunction.printAtTheMiddle("3: South-Est", DIRECTION_SPACE);
        System.out.println();
    }

    /**
     * Tries to change subTurn and returns true if it can, or false if it can't
     * @return
     */
    public boolean changePlayingPhase(ViewSubTurn viewSubTurn) {
        final String CORRECT_CHANGE_PHASE_MESSAGE = "Your change turn request is correctly send";
        String wrongSetMessage = "The request isn't correctly set";

        boolean changePhase = false;
        TurnStatusChangeExecuter turnStatusChangeExecuter = new TurnStatusChangeExecuter();

        try {
            turnStatusChangeExecuter.setStatusId( viewSubTurn );
            changePhase = true;
            turnStatusChangeExecuter.doIt();
            PrintFunction.printCheck(CORRECT_CHANGE_PHASE_MESSAGE);
        } catch (WrongParametersException e) {
            if (!e.getMessage().equals("")) {
                wrongSetMessage = e.getMessage();
            }
            PrintFunction.printError(wrongSetMessage);
        } catch (CannotSendEventException e) {
            PrintFunction.printError(e.getErrorMessage());
        }

        return changePhase;

    }

}
