package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.OpponentMoveViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.WTerminalPlayingViewer;
import it.polimi.ingsw.view.exceptions.NotFoundException;

public class WTerminalOpponentMovePhase extends WTerminalSubTurnViewer {

    private OpponentMoveViewer opponentMoveViewer;

    private final int STARTING_SPACE = 7;

    public WTerminalOpponentMovePhase(OpponentMoveViewer opponentMoveViewer) {
        this.opponentMoveViewer = opponentMoveViewer;
    }


    /**
     * Prints the Name, Epithet and Description of all the player's God
     */
    private void showCardsDetails () {
        ViewCard viewCard;

        System.out.println("Gods' details:");
        for (ViewPlayer viewPlayer : ViewPlayer.getPlayerList()) {
            System.out.println();
            System.out.println();
            try {
                viewCard = viewPlayer.getCard();
                //todo:maybe add god's symbol
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.printf("Name: %s\n", viewCard.getName());
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.printf("Epithet: %s\n", viewCard.getEpiteth());
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.printf("Description: %s\n", viewCard.getDescription());
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Prints the board, the god's details and a waiting message
     */
    @Override
    public void show() {

        System.out.println();
        System.out.println();
        ViewBoard.getBoard().toWTerminal();

        System.out.println();
        this.showCardsDetails();

        System.out.println();
        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println("A player is moving his worker, please wait");
        //todo: maybe add a little animation like WaitingViewer

    }

}
