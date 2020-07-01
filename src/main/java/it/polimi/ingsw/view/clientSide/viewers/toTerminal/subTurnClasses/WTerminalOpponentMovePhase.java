package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.OpponentMoveViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.WTerminalPlayingViewer;
import it.polimi.ingsw.view.exceptions.NotFoundException;

public class WTerminalOpponentMovePhase extends WTerminalSubTurnViewer {

    private OpponentMoveViewer opponentMoveViewer;

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
