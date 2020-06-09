package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.OpponentBuildViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses.CLIPlayingViewer;

public class CLIOpponentBuildPhase extends CLISubTurnViewer {

    private CLIPlayingViewer myCLIStatusViewer = null;
    private OpponentBuildViewer opponentBuildViewer;

    private final int STARTING_SPACE = 7;


    public CLIOpponentBuildPhase(OpponentBuildViewer opponentBuildViewer) {
        this.opponentBuildViewer = opponentBuildViewer;
    }

    /**
     * Prints the board, the god's details and a waiting message
     */
    @Override
    public void show() {

        final String WAITING_MESSAGE = "A player is build with his worker, please wait";

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
        ViewBoard.getBoard().toCLI();

        System.out.println();
        this.showCardsDetails(STARTING_SPACE);

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.println(WAITING_MESSAGE);
        //todo: maybe add a little animation like WaitingViewer

    }

    @Override
    public ViewSubTurn getSubTurn() {
        return opponentBuildViewer.getMySubTurn();
    }

    /**
     * Overloading of CLISubTurnViewer's setMyCLIStatusViewer to set the correct CLIStatusViewer
     * @param myCLIStatusViewer
     */
    public void setMyCLIStatusViewer( CLIPlayingViewer myCLIStatusViewer) {
        this.myCLIStatusViewer = myCLIStatusViewer;
    }
}
