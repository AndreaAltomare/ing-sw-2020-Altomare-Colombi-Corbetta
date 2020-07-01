package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.OpponentBuildViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
public class CLIOpponentBuildPhase extends CLISubTurnViewer {

    private OpponentBuildViewer opponentBuildViewer;


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
        try {
            ViewBoard.getBoard().toCLI();

            System.out.println();
            this.showCardsDetails(false);

            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
            System.out.println(WAITING_MESSAGE);
        }catch(NullPointerException ignored){
            // do nothing and exit from state if there isn't the board
        }

    }

}
