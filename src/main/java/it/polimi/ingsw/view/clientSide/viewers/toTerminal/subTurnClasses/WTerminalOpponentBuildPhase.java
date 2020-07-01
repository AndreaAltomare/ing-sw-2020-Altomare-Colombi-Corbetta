package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.OpponentBuildViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;

/**
 * Class that represents the <code>WTerminalSubTurnViewer</code> Build on the Windows Terminal
 * using methods of <code>PrintFunction</code>
 *
 * @see WTerminalSubTurnViewer
 * @see PrintFunction
 * @author Marco
 */
public class WTerminalOpponentBuildPhase extends WTerminalSubTurnViewer {

    private OpponentBuildViewer opponentBuildViewer;

    /**
     * Constructor to set the correctly <code>SubTurnViewer</code>
     *
     * @see OpponentBuildViewer
     * @param opponentBuildViewer <code>SubTurnViewer</code> linked at this class
     */
    public WTerminalOpponentBuildPhase(OpponentBuildViewer opponentBuildViewer) {
        this.opponentBuildViewer = opponentBuildViewer;
    }

    /**
     * Prints the board, the god's details and a waiting message
     */
    @Override
    public void show() {

        final String WAITING_MESSAGE = "A player is build with his worker, please wait";

        PrintFunction.printRepeatString("\n", PrintFunction.STARTING_SPACE);
        try {
            ViewBoard.getBoard().toWTerminal();

            System.out.println();
            this.showCardsDetails(false);

            PrintFunction.printRepeatString("\n", PrintFunction.STARTING_SPACE);
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println(WAITING_MESSAGE);
        }catch(NullPointerException e){
            // do anything exit from state if there isn't the board
        }

    }

}
