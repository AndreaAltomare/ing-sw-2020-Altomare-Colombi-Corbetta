package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.OpponentPlaceWorkerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;

public class WTerminalOpponentPlaceWorkerPhase extends WTerminalSubTurnViewer {

    private OpponentPlaceWorkerViewer opponentPlaceWorkerViewer;

    public WTerminalOpponentPlaceWorkerPhase(OpponentPlaceWorkerViewer opponentPlaceWorkerViewer ) {
        this.opponentPlaceWorkerViewer = opponentPlaceWorkerViewer;
    }

    /**
     * Prints the board and a waiting message
     */
    @Override
    public void show() {
        final String WAITING_MESSAGE = "A player is placing his worker, please waiting";

        PrintFunction.printRepeatString("\n", 2);
        try {
        ViewBoard.getBoard().toWTerminal();

        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.println(WAITING_MESSAGE);
        System.out.println();

        }catch(NullPointerException e){
            // do anything exit from state if there isn't the board
        }

    }

}
