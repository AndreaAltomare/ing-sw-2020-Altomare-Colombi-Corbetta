package it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.TurnStatusChangeExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.Scanner;

/**
 * Abstract class with some methods to menage some command used by some sub classes
 *
 * @author Marco
 */
public abstract class CLISubTurnViewer implements SpecificSubTurnViewer {

    /**
     * Abstract method used by sub classes to menage the their sub status
     */
    public abstract void show();
    
    /**
     * Method which prints the Name, Epithet and Description of all the player's God with the color of player, then if waitingPLayer == true
     * prints a string request and waits that the player press enter
     *
     * @param waitingPlayer <code>boolean</code> parameter to wait an input to continue if waitingPLayer == true, or not
     *                      if waitingPlayer == false
     */
    protected void showCardsDetails (boolean waitingPlayer) {
        final String CONTINUE_REQUEST = "Press Enter to continue:";
        String detailsStyle;
        ViewCard viewCard;
        ViewWorker[] workers;

        for ( ViewPlayer viewPlayer : ViewPlayer.getPlayerList() ) {
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
            try {
                viewCard = viewPlayer.getCard();
                try {
                    workers = viewPlayer.getWorkers();
                    detailsStyle = workers[0].getWorkerCLIColor();
                } catch ( NotFoundException | NullPointerException e ) {
                    detailsStyle = "";  // doesn't set color
                }
                detailsStyle = ANSIStyle.REVERSE.getEscape() + detailsStyle;

                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                System.out.printf(detailsStyle + "Name:" + ANSIStyle.RESET + " %s\n\n", viewCard.getName());
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                System.out.printf(detailsStyle + "Epithet:"  + ANSIStyle.RESET + " %s\n\n", viewCard.getEpiteth());
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                System.out.printf(detailsStyle + "Description:" + ANSIStyle.RESET + " %s\n\n", viewCard.getDescription());
            } catch (NotFoundException ignored) {
            }
        }

        if (waitingPlayer) {
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
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
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "7: North-West", "7: North-West".length(), DIRECTION_SPACE);
        CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "8: North", "8: North".length(), DIRECTION_SPACE);
        CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "9: North-Est", "9: North-Est".length(), DIRECTION_SPACE);
        System.out.println();
        //second line
        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "4: West", "4: West".length(), DIRECTION_SPACE);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", DIRECTION_SPACE);
        CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "6: Est", "6: Est".length(), DIRECTION_SPACE);
        System.out.println();
        //third line
        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "1: South-West", "1: South-West".length(), DIRECTION_SPACE);
        CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "2: South", "2: South".length(), DIRECTION_SPACE);
        CLIPrintFunction.printAtTheMiddle(ANSIStyle.RESET, "3: South-Est", "3: South-Est".length(), DIRECTION_SPACE);
        System.out.println();

    }

    /**
     * Tries to set subTurn change and returns true if it can, or false if it can't, and print a message to show the result on CLI
     *
     * @return (change subTurnRequest is set ? true : false)
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
            CLIPrintFunction.printCheck(CORRECT_CHANGE_PHASE_MESSAGE);
        } catch (WrongParametersException e) {
            if (!e.getMessage().equals("")) {
                wrongSetMessage = e.getMessage();
            }
            CLIPrintFunction.printError(wrongSetMessage);
        } catch (CannotSendEventException e) {
            CLIPrintFunction.printError(e.getErrorMessage());
        }

        return changePhase;

    }
}
