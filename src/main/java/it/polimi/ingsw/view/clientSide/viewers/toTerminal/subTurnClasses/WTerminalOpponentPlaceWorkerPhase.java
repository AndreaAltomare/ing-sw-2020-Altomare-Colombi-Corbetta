package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.OpponentPlaceWorkerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.WTerminalGamePreparationViewer;

public class WTerminalOpponentPlaceWorkerPhase extends WTerminalSubTurnViewer {

    private OpponentPlaceWorkerViewer opponentPlaceWorkerViewer;

    private final int STARTING_SPACE = 7;

    public WTerminalOpponentPlaceWorkerPhase(OpponentPlaceWorkerViewer opponentPlaceWorkerViewer ) {
        this.opponentPlaceWorkerViewer = opponentPlaceWorkerViewer;
    }

    /**
     * Prints the board and a waiting message
     */
    @Override
    public void show() {

        System.out.println();
        System.out.println();
        try {
        ViewBoard.getBoard().toWTerminal();

        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println("A player is placing his worker, please waiting");
        System.out.println();
        //todo: maybe to do an little animation like in CLIWaitingStatus

        }catch(NullPointerException e){
            // do anything exit from state if there isn't the board
        }

    }

}
