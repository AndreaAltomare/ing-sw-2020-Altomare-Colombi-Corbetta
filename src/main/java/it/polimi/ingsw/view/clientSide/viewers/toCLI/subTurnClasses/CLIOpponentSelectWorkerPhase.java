package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.OpponentSelectWorkerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;

/**
 * Class that represents the <code>CLISubTurnViewer</code> SelectWorker on the CLI
 * using methods of <code>CLIPrintFunction</code> and <code>ANSIStyle</code>
 *
 * @see CLISubTurnViewer
 * @see CLIPrintFunction
 * @see ANSIStyle
 * @author Marco
 */
public class CLIOpponentSelectWorkerPhase extends CLISubTurnViewer {

    private OpponentSelectWorkerViewer opponentSelectWorkerViewer;

    /**
     * Constructor to set the correctly <code>SubTurnViewer</code>
     *
     * @see OpponentSelectWorkerViewer
     * @param opponentSelectWorkerViewer <code>SubTurnViewer</code> linked at this class
     */
    public CLIOpponentSelectWorkerPhase(OpponentSelectWorkerViewer opponentSelectWorkerViewer) {
        this.opponentSelectWorkerViewer = opponentSelectWorkerViewer;
    }


    /**
     * Prints the board, the god's details and a waiting message
     */
    @Override
    public void show() {
        final String WAITING_MESSAGE = "A player is selecting his worker, please wait";

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
        try {
        ViewBoard.getBoard().toCLI();

        System.out.println();
        this.showCardsDetails(false);

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.println(WAITING_MESSAGE);

        }catch(NullPointerException e){
            // do anything and exit from state if there isn't the board
        }

    }

}
