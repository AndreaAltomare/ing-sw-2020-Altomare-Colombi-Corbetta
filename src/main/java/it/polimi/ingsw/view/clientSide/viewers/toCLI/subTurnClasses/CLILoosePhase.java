package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewWorker;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.CLISymbols;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.exceptions.NotFoundException;

public class CLILoosePhase extends CLISubTurnViewer {

    final int STARTING_SPACE = 7;

    public CLILoosePhase() {}

    /**
     * Prints a little image to notify that the player has loosen
     * example //todo: add it
     */
    @Override
    public void show() {
        final String DOME_COLOR = ANSIStyle.BLUE.getEscape();
        final String BLOCK_COLOR = ANSIStyle.GREY.getEscape();
        final String DROP_COLOR = ANSIStyle.DIFFERENT_BLUE.getEscape();
        final String FIELD_BACK_COLOR = ANSIStyle.BACK_GREEN.getEscape();
        final String WRITE_COLOR = ANSIStyle.GREY.getEscape();
        final String WRITE_STRING = "SORRY, YOU HAVE LOOSEN";
        final int TOWER_SPACE = 5;
        final int MAN_SPACE = WRITE_STRING.length() - 2*TOWER_SPACE;
        final int EDGE_SPACE = 1;

        String playerColor = "";
        ViewPlayer viewPlayer;
        ViewWorker[] workers;

        try {
            if ( ViewNickname.getMyNickname() != null ) {
                viewPlayer = ViewPlayer.searchByName(ViewNickname.getMyNickname());
                workers = viewPlayer.getWorkers();
                playerColor = CLIViewer.getWorkerCLIColor(workers[0].getColor());
            }
        } catch (NotFoundException ignored) {
        }
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET,"\n", 2);
        // towers' domes
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE + EDGE_SPACE);
        System.out.print( "\"" + DOME_COLOR + CLISymbols.DOME.getMiddleRepresentation() + ANSIStyle.RESET + "\"");
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", MAN_SPACE);
        System.out.print( "\"" + DOME_COLOR + CLISymbols.DOME.getMiddleRepresentation() + ANSIStyle.RESET + "\"");
        System.out.println(ANSIStyle.RESET);

        // towers' third block and human's head
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE + EDGE_SPACE);
        System.out.print( "  " + BLOCK_COLOR + CLISymbols.BLOCK.getMiddleRepresentation() + ANSIStyle.RESET + "\"");
        CLIPrintFunction.printAtTheMiddle(playerColor, " O" + DROP_COLOR + "'", 3, MAN_SPACE);
        System.out.print( "\"" + BLOCK_COLOR + CLISymbols.BLOCK.getMiddleRepresentation() + ANSIStyle.RESET + "  ");
        System.out.println(ANSIStyle.RESET);

        // towers' second block and human's body and arm
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE + EDGE_SPACE);
        System.out.print( "\"" + BLOCK_COLOR + CLISymbols.BLOCK.getMiddleRepresentation() + ANSIStyle.RESET + "  ");
        CLIPrintFunction.printAtTheMiddle(playerColor, "/|\\" , 3, MAN_SPACE);
        System.out.print( "  " + BLOCK_COLOR + CLISymbols.BLOCK.getMiddleRepresentation() + ANSIStyle.RESET + "\"");
        System.out.println(ANSIStyle.RESET);

        // towers' first block and human's legs
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE + EDGE_SPACE);
        System.out.print( "  " + BLOCK_COLOR + CLISymbols.BLOCK.getMiddleRepresentation() + ANSIStyle.RESET + "\"");
        CLIPrintFunction.printAtTheMiddle(playerColor, "/ \\", 3, MAN_SPACE);
        System.out.print( "\"" + BLOCK_COLOR + CLISymbols.BLOCK.getMiddleRepresentation() + ANSIStyle.RESET + "  ");
        System.out.println(ANSIStyle.RESET);

        // field and WRITE_STRING
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        CLIPrintFunction.printAtTheMiddle(FIELD_BACK_COLOR + WRITE_COLOR, WRITE_STRING, WRITE_STRING.length(), MAN_SPACE + 2*TOWER_SPACE + 2*EDGE_SPACE);
        System.out.println(ANSIStyle.RESET);

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET,"\n", 2);

    }

    @Override
    public ViewSubTurn getSubTurn() {
        return null;
    }
}
