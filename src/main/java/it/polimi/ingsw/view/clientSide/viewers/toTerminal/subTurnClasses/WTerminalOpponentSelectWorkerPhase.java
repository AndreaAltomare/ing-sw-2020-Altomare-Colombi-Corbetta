package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.OpponentSelectWorkerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.WTerminalPlayingViewer;
import it.polimi.ingsw.view.exceptions.NotFoundException;

public class WTerminalOpponentSelectWorkerPhase extends WTerminalSubTurnViewer {

    private OpponentSelectWorkerViewer opponentSelectWorkerViewer;

    private final int STARTING_SPACE = 7;

    public WTerminalOpponentSelectWorkerPhase(OpponentSelectWorkerViewer opponentSelectWorkerViewer) {
        this.opponentSelectWorkerViewer = opponentSelectWorkerViewer;
    }

    /**
     * Prints the board, the god's details and a waiting message
     */
    @Override
    public void show() {

        System.out.println();
        System.out.println();
        try {
        ViewBoard.getBoard().toWTerminal();

        System.out.println();
        this.showCardsDetails(STARTING_SPACE);

        System.out.println();
        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println("A player is selecting his worker, please wait");
        //todo: maybe add a little animation like WaitingViewer

        }catch(NullPointerException e){
            // do anything exit from state if there isn't the board
        }

    }

}
