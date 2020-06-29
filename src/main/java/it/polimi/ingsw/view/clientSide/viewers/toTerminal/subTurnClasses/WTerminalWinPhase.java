package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;

public class WTerminalWinPhase extends WTerminalSubTurnViewer {

    final int STARTING_SPACE = 7;

    /**
     * Prints a little image to notify the victory to the player
     * example //todo: ad image
     */
    @Override
    public void show() {
        final String WRITE_STRING = "VICTORY";
        final int BLOCK_LENGTH = WRITE_STRING.length() + 6;

        PrintFunction.printRepeatString("\n", 2);

        // block's upper edge
        PrintFunction.printRepeatString(" ", STARTING_SPACE + 1);
        PrintFunction.printRepeatString("_", BLOCK_LENGTH - 2);
        System.out.println(" ");

        // block's up part
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.print("|");
        PrintFunction.printRepeatString(" ", BLOCK_LENGTH - 2);
        System.out.println("|");

        // block's middle part and WRITE_STRING
        PrintFunction.printRepeatString(" ", STARTING_SPACE );
        System.out.print("|");
        PrintFunction.printAtTheMiddle(WRITE_STRING, BLOCK_LENGTH - 2);
        System.out.println("|");

        // block's down part
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.print("|");
        PrintFunction.printRepeatString("_", BLOCK_LENGTH - 2);
        System.out.println("|");

        // human's head and arms
        PrintFunction.printRepeatString(" ", STARTING_SPACE );
        PrintFunction.printAtTheMiddle("\\O/", BLOCK_LENGTH);
        System.out.println();

        // human's head and arms
        PrintFunction.printRepeatString(" ", STARTING_SPACE );
        PrintFunction.printAtTheMiddle(" | ", BLOCK_LENGTH);
        System.out.println();

        // human's head and arms
        PrintFunction.printRepeatString(" ", STARTING_SPACE );
        PrintFunction.printAtTheMiddle("/ \\", BLOCK_LENGTH);
        System.out.println();

        // winning player name
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        if ( ViewNickname.getMyNickname() != null) {
            PrintFunction.printAtTheMiddle(ViewNickname.getMyNickname(), BLOCK_LENGTH);
        }
        System.out.println();

        PrintFunction.printRepeatString("\n", 2);
    }

}
