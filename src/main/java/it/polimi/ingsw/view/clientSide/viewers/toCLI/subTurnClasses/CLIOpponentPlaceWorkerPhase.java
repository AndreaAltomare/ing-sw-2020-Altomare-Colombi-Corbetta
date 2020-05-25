package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.OpponentPlaceWorkerViewer;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.PlaceWorkerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.PrintFunction;

public class CLIOpponentPlaceWorkerPhase implements CLISubTurnViewer {
    private OpponentPlaceWorkerViewer opponentPlaceWorkerViewer;

    private final int STARTING_SPACE = 7;

    public CLIOpponentPlaceWorkerPhase( OpponentPlaceWorkerViewer opponentPlaceWorkerViewer ) {
        this.opponentPlaceWorkerViewer = opponentPlaceWorkerViewer;
    }

    /**
     * Prints the board and a waiting message
     */
    @Override
    public void show() {

        System.out.println();
        System.out.println();
        ViewBoard.getBoard().toCLI();

        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println("A player is placing his worker, please waiting");
        System.out.println();
        //todo: maybe to do an little animation like in CLIWaitingStatus
    }

    @Override
    public ViewSubTurn getSubTurn() {
        return opponentPlaceWorkerViewer.getMySubTurn();
    }
}
