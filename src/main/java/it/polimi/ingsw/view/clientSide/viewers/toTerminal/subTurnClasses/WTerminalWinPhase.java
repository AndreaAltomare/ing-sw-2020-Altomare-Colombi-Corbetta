package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;

/**
 * Class that represents the <code>WTerminalSubTurnViewer</code> Win on the Windows Terminal
 * using methods of <code>PrintFunction</code>
 *
 * @see WTerminalSubTurnViewer
 * @see PrintFunction
 * @author Marco
 */
public class WTerminalWinPhase extends WTerminalSubTurnViewer {

    /**
     * Prints a little image to notify the victory to the player
     */
    @Override
    public void show() {
        final String WRITE_STRING = "VICTORY";
        final int BLOCK_LENGTH = WRITE_STRING.length() + 6;

        PrintFunction.printRepeatString("\n", 2);

        // block's upper edge
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE + 1);
        PrintFunction.printRepeatString("_", BLOCK_LENGTH - 2);
        System.out.println(" ");

        // block's up part
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print("|");
        PrintFunction.printRepeatString(" ", BLOCK_LENGTH - 2);
        System.out.println("|");

        // block's middle part and WRITE_STRING
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE );
        System.out.print("|");
        PrintFunction.printAtTheMiddle(WRITE_STRING, BLOCK_LENGTH - 2);
        System.out.println("|");

        // block's down part
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print("|");
        PrintFunction.printRepeatString("_", BLOCK_LENGTH - 2);
        System.out.println("|");

        // human's head and arms
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE );
        PrintFunction.printAtTheMiddle("\\O/", BLOCK_LENGTH);
        System.out.println();

        // human's head and arms
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE );
        PrintFunction.printAtTheMiddle(" | ", BLOCK_LENGTH);
        System.out.println();

        // human's head and arms
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE );
        PrintFunction.printAtTheMiddle("/ \\", BLOCK_LENGTH);
        System.out.println();

        // winning player name
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        if ( ViewNickname.getMyNickname() != null) {
            PrintFunction.printAtTheMiddle(ViewNickname.getMyNickname(), BLOCK_LENGTH);
        }
        System.out.println();

        PrintFunction.printRepeatString("\n", 2);
    }

}
