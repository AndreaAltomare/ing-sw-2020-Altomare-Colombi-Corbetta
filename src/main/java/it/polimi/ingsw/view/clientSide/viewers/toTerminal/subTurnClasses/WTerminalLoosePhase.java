package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;

/**
 * Class that represents the <code>WTerminalSubTurnViewer</code> Move on the Windows Terminal
 * using methods of <code>PrintFunction</code>
 *
 * @see WTerminalSubTurnViewer
 * @see PrintFunction
 * @author Marco
 */
public class WTerminalLoosePhase extends WTerminalSubTurnViewer {

    /**
     * Prints a little image to notify that the player has loosen
     */
    @Override
    public void show() {
        final String WRITE_STRING = "SORRY, YOU HAVE LOOSEN"; //length == 22
        final int TOWER_SPACE = 10;
        final int MAN_SPACE = WRITE_STRING.length() - TOWER_SPACE ;
        final int EDGE_SPACE = 2 ; // >= 1

        PrintFunction.printRepeatString("\n", 2);

        // towers' third block: upper part
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE + EDGE_SPACE);
        System.out.print("  ____    ");
        PrintFunction.printRepeatString(" ", MAN_SPACE);
        System.out.print("    ____  ");
        System.out.println();

        // towers' third block: middle part
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE + EDGE_SPACE);
        System.out.print("\"|    |   ");
        PrintFunction.printRepeatString(" ", MAN_SPACE);
        System.out.print("   |    |\"");
        System.out.println();

        // towers' third and second block: down part and upper part
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE + EDGE_SPACE);
        System.out.print(" |____|_  ");
        PrintFunction.printRepeatString(" ", MAN_SPACE);
        System.out.print("  _|____| ");
        System.out.println();

        // towers' second block: middle part
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE + EDGE_SPACE);
        System.out.print("   |    |\"");
        PrintFunction.printRepeatString(" ", MAN_SPACE);
        System.out.print("\"|    |   ");
        System.out.println();

        // towers' second and first block: down part and upper part. And human's head
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE + EDGE_SPACE);
        System.out.print("  _|____| ");
        PrintFunction.printAtTheMiddle(" O'", MAN_SPACE);
        System.out.print(" |____|_  ");
        System.out.println();

        // towers' first block: middle part. And human's body and arms
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE + EDGE_SPACE);
        System.out.print("\"|    |   ");
        PrintFunction.printAtTheMiddle("/|\\", MAN_SPACE);
        System.out.print("   |    |\"");
        System.out.println();

        // towers' first block: down part. Human's legs and field's un edge
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE + 1);
        PrintFunction.printRepeatString("_", EDGE_SPACE - 1);
        System.out.print("_|____|___");
        if ( ((MAN_SPACE - 3) % 2) == 0 ) {
            PrintFunction.printRepeatString("_", (MAN_SPACE - 3) / 2 );
        } else {
            PrintFunction.printRepeatString("_", (MAN_SPACE - 3) / 2  + 1);
        }
        System.out.print("/_\\");
        PrintFunction.printRepeatString("_", (MAN_SPACE - 3) / 2);
        System.out.print("___|____|_");
        PrintFunction.printRepeatString("_", EDGE_SPACE - 1);
        System.out.println(" ");

        // field's up part
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print("|");
        PrintFunction.printRepeatString(" ", MAN_SPACE + 2*TOWER_SPACE + 2*(EDGE_SPACE - 1) );
        System.out.println("|");

        // field's middle part and WRITE_STRING
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print("|");
        PrintFunction.printAtTheMiddle(WRITE_STRING, MAN_SPACE + 2*TOWER_SPACE + 2*(EDGE_SPACE - 1) );
        System.out.println("|");

        // field's down part
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print("|");
        PrintFunction.printRepeatString("_", MAN_SPACE + 2*TOWER_SPACE + 2*(EDGE_SPACE - 1) );
        System.out.println("|");


        PrintFunction.printRepeatString("\n", 2);

    }

}
