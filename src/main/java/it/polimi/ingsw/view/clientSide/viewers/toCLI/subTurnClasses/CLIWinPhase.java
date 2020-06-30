package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewWorker;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.UnicodeSymbol;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.exceptions.NotFoundException;

public class CLIWinPhase extends CLISubTurnViewer {

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
        final String WRITE_STRING = "VICTORY";
        final int DOME_MAX_LENGTH = WRITE_STRING.length() + 4;

        String playerColor = ""; //nothing color
        String myNickname;
        ViewPlayer viewPlayer;
        ViewWorker[] workers;

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
        myNickname = ViewNickname.getMyNickname();
        if ( myNickname != null ) {
            try {
                viewPlayer = ViewPlayer.searchByName(myNickname);
                workers = viewPlayer.getWorkers();
                playerColor = workers[0].getWorkerCLIColor();
            } catch (NotFoundException | NullPointerException ignored) {
            }
        } else {
            myNickname = ""; // nothing nickname
        }

        // dome's up part
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE + 2);
        CLIPrintFunction.printRepeatString(DOME_COLOR, UnicodeSymbol.HIGH_BLOCK_4.getEscape(), DOME_MAX_LENGTH - 4 );
        System.out.println(ANSIStyle.RESET);

        // dome's middle part with WRITE_STRING
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE + 1);
        CLIPrintFunction.printAtTheMiddle(DOME_BACK_COLOR + WRITE_COLOR, WRITE_STRING, WRITE_STRING.length(), DOME_MAX_LENGTH - 2);
        System.out.println(ANSIStyle.RESET);

        // dome's down part
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        CLIPrintFunction.printRepeatString(DOME_BACK_COLOR, " ", DOME_MAX_LENGTH);
        System.out.println(ANSIStyle.RESET);

        // human's head an arms
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        CLIPrintFunction.printAtTheMiddle(playerColor, "\\O/", 3, DOME_MAX_LENGTH);
        System.out.println(ANSIStyle.RESET);

        // human's body
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        CLIPrintFunction.printAtTheMiddle(playerColor, " | ", 3, DOME_MAX_LENGTH);
        System.out.println(ANSIStyle.RESET);

        // human's legs
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        CLIPrintFunction.printAtTheMiddle(playerColor, "/ \\", 3, DOME_MAX_LENGTH);
        System.out.println(ANSIStyle.RESET);

        //winning player's name
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        CLIPrintFunction.printAtTheMiddle(playerColor, myNickname, myNickname.length(), DOME_MAX_LENGTH);
        System.out.println(ANSIStyle.RESET);

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
    }

}
