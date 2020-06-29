package it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewWorker;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.CLIGodSymbols;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.CLISymbols;
import it.polimi.ingsw.view.exceptions.NotFoundException;

public interface CLIBoardPrintFunction {


    final int DISTANCE_FROM_BOARD = 5;
    final int SPACE_FOR_STRING = 15;

    /**
     * Returns rhe correct String of symbolNumber if cellRow == 2|3|4 ( the middle of the cell ) or an empty String if it isn't
     * @param CLISymbolNumber Symbols which represents the number of Board's row
     * @param cellRow int which represents the number of Cell's row (start from up with number 0)
     * @return a String which represents the part ( up, middle or down ) of symbolNumber if cellRow == 2|3|4,
     *          or empty String if it isn't
     */
    static String getBoardRowSymbol(CLISymbols CLISymbolNumber, int cellRow) {
        String stringNumber = "";

        switch (cellRow ) {
            case 1:
                stringNumber = CLISymbolNumber.getUpRepresentation();
                break;
            case 2:
                stringNumber = CLISymbolNumber.getMiddleRepresentation();
                break;
            case 3:
                stringNumber = CLISymbolNumber.getDownRepresentation();
                break;
            default:
                ;
        }

        return stringNumber;
    }

    /**
     * Prints a part of Players's caption at cell's high if boardRow and cellRow are correct, doesn't do anything it they aren't
     *
     * @param boardRow number or board's row ( start from up with number 0)
     * @param cellRow number or cell's row ( start from up with number 0)
     */
    static void printPlayersCaptionAtCell(int boardRow, int cellRow) {
        CLIGodSymbols cliGodSymbols;
        String playerColor;
        ViewCard viewCard;
        ViewWorker worker;

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", DISTANCE_FROM_BOARD);

        for (ViewPlayer viewPlayer : ViewPlayer.getPlayerList()) {
            switch (boardRow) {
                case 1:
                    try {
                        viewCard = viewPlayer.getCard();
                        cliGodSymbols = CLIGodSymbols.searchGodSymbol( viewCard.getName() );
                        try {
                            worker = viewPlayer.getOneWorker();
                            playerColor = worker.getWorkerCLIColor();
                        } catch ( NullPointerException e) {
                            playerColor = ANSIStyle.RESET;
                        }
                        switch (cellRow) {
                            case 1:
                                CLIPrintFunction.printAtTheMiddle(  playerColor, cliGodSymbols.getUpRepresentation(),
                                                                    cliGodSymbols.getRepresentationLength(), SPACE_FOR_STRING);
                                break;
                            case 2:
                                CLIPrintFunction.printAtTheMiddle(  playerColor, cliGodSymbols.getMiddleRepresentation(),
                                                                    cliGodSymbols.getRepresentationLength(), SPACE_FOR_STRING);
                                break;
                            case 3:
                                CLIPrintFunction.printAtTheMiddle(  playerColor, cliGodSymbols.getDownRepresentation(),
                                                                    cliGodSymbols.getRepresentationLength(), SPACE_FOR_STRING);
                                break;
                            default:
                                ;
                        }
                    } catch (NotFoundException e) {
                        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", SPACE_FOR_STRING);
                    }
                    break;
                default:
                    ;
            }

        }
        System.out.print( ANSIStyle.RESET );

    }

    /**
     * Prints a part of Players's caption at the high of cell'edge if boardRow and cellRow are correct, doesn't do anything it they aren't
     *
     * @param boardRow number or board's row ( start from up with number 0)
     */
    static void printPlayersCaptionAtEdgeCell(int boardRow) {
        String playerColor;
        ViewCard viewCard;
        ViewWorker worker;

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", DISTANCE_FROM_BOARD);

        for (ViewPlayer viewPlayer : ViewPlayer.getPlayerList()) {
            try {
                worker = viewPlayer.getOneWorker();
                playerColor = worker.getWorkerCLIColor();
            } catch ( NullPointerException e) {
                playerColor = ANSIStyle.RESET;
            }

           switch (boardRow) {
               case 1:
                   CLIPrintFunction.printAtTheMiddle(playerColor,viewPlayer.getName(), viewPlayer.getName().length(), SPACE_FOR_STRING );
                   break;
               case 2:
                   try {
                       viewCard = viewPlayer.getCard();
                       CLIPrintFunction.printAtTheMiddle(playerColor, viewCard.getName(), viewCard.getName().length(), SPACE_FOR_STRING);
                   } catch ( NotFoundException e ) {
                       CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", SPACE_FOR_STRING);
                   }
                   break;
               default:
                    ;
            }

        }
        System.out.print( ANSIStyle.RESET );

    }
}
