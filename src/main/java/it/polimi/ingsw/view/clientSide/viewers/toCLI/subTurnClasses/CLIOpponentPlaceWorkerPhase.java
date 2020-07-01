package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.OpponentPlaceWorkerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;

/**
 * Class that represents the <code>CLISubTurnViewer</code> OpponentPLaceWorker on the CLI
 * using methods of <code>CLIPrintFunction</code> and <code>ANSIStyle</code>
 *
 * @see CLISubTurnViewer
 * @see CLIPrintFunction
 * @see ANSIStyle
 * @author Marco
 */
public class CLIOpponentPlaceWorkerPhase extends CLISubTurnViewer {

    private OpponentPlaceWorkerViewer opponentPlaceWorkerViewer;

    /**
     * Constructor to set the correctly <code>SubTurnViewer</code>
     *
     * @see OpponentPlaceWorkerViewer
     * @param opponentPlaceWorkerViewer <code>SubTurnViewer</code> linked at this class
     */
    public CLIOpponentPlaceWorkerPhase(OpponentPlaceWorkerViewer opponentPlaceWorkerViewer ) {
        this.opponentPlaceWorkerViewer = opponentPlaceWorkerViewer;
    }

    /**
     * Prints the board and a waiting message
     */
    @Override
    public void show() {
        final String WAITING_MESSAGE = "A player is placing his worker, please waiting";

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
        try {
            ViewBoard.getBoard().toCLI();

            System.out.println();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
            System.out.println(WAITING_MESSAGE);
            System.out.println();

        }catch(NullPointerException e){
            // do anything and exit from state if there isn't the board
        }

    }

}
