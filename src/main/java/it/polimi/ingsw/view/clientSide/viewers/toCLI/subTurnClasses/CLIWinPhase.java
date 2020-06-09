package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.UnicodeSymbol;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.exceptions.NotFoundException;

public class CLIWinPhase extends CLISubTurnViewer {

    final int STARTING_SPACE = 7;

    public CLIWinPhase() {}

    /**
     * Prints a little image to notify the victory to the player
     * example //todo: ad image
     */
    @Override
    public void show() {
        final String DOME_BACK_COLOR = ANSIStyle.BACK_BLUE.getEscape();
        final String DOME_COLOR = ANSIStyle.BLUE.getEscape();
        final String WRITE_COLOR = ANSIStyle.YELLOW.getEscape();
        final String PLAYER_COLOR;
        final String WRITE_STRING = "VICTORY";
        final int DOME_MAX_LENGTH = WRITE_STRING.length() + 4;

        ViewPlayer viewPlayer;

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
        try {
            viewPlayer = ViewPlayer.searchByName(ViewNickname.getMyNickname());
            PLAYER_COLOR = CLIViewer.getPlayerColor(viewPlayer);

            // dome's up part
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE + 2);
            CLIPrintFunction.printRepeatString(DOME_COLOR, UnicodeSymbol.HIGH_BLOCK_4.getEscape(), DOME_MAX_LENGTH - 4 );
            System.out.println(ANSIStyle.RESET);

            // dome's middle part with WRITE_STRING
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE + 1);
            CLIPrintFunction.printAtTheMiddle(DOME_BACK_COLOR + WRITE_COLOR, WRITE_STRING, WRITE_STRING.length(), DOME_MAX_LENGTH - 2);
            System.out.println(ANSIStyle.RESET);

            // dome's down part
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            CLIPrintFunction.printRepeatString(DOME_BACK_COLOR, " ", DOME_MAX_LENGTH);
            System.out.println(ANSIStyle.RESET);

            // human's head an arms
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            CLIPrintFunction.printAtTheMiddle(PLAYER_COLOR, "\\O/", 3, DOME_MAX_LENGTH);
            System.out.println(ANSIStyle.RESET);

            // human's body
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            CLIPrintFunction.printAtTheMiddle(PLAYER_COLOR, " | ", 3, DOME_MAX_LENGTH);
            System.out.println(ANSIStyle.RESET);

            // human's legs
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            CLIPrintFunction.printAtTheMiddle(PLAYER_COLOR, "/ \\", 3, DOME_MAX_LENGTH);
            System.out.println(ANSIStyle.RESET);

        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
    }

    @Override
    public ViewSubTurn getSubTurn() {
        return null;
    }
}
