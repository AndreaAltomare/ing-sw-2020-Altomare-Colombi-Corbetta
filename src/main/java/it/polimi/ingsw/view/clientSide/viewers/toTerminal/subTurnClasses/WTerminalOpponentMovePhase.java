package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.OpponentMoveViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;

/**
 * Class that represents the <code>WTerminalSubTurnViewer</code> Move on the Windows Terminal
 * using methods of <code>PrintFunction</code>
 *
 * @see WTerminalSubTurnViewer
 * @see PrintFunction
 * @author Marco
 */
public class WTerminalOpponentMovePhase extends WTerminalSubTurnViewer {

    private OpponentMoveViewer opponentMoveViewer;

    /**
     * Constructor to set the correctly <code>SubTurnViewer</code>
     *
     * @see OpponentMoveViewer
     * @param opponentMoveViewer <code>SubTurnViewer</code> linked at this class
     */
    public WTerminalOpponentMovePhase(OpponentMoveViewer opponentMoveViewer) {
        this.opponentMoveViewer = opponentMoveViewer;
    }

    /**
     * Prints the board, the god's details and a waiting message
     */
    @Override
    public void show() {

        final String WAITING_MESSAGE = "A player is moving his worker, please wait";

        PrintFunction.printRepeatString("\n", 2);
        try {
            ViewBoard.getBoard().toWTerminal();

            System.out.println();
            this.showCardsDetails(false);

            PrintFunction.printRepeatString("\n", 2);
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println(WAITING_MESSAGE);

        }catch(NullPointerException e){
            // do anything exit from state if there isn't the board
        }

    }

}
