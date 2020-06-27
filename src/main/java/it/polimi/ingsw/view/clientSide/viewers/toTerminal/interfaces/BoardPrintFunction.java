package it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewWorker;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.WTerminalViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.GodSymbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.Symbols;
import it.polimi.ingsw.view.exceptions.NotFoundException;

public interface BoardPrintFunction {

    /**
     * Returns rhe correct String of symbolNumber if cellRow == 2|3|4 ( the middle of the cell ) or an empty String if it isn't
     * @param symbolNumber Symbols which represents the number of Board's row
     * @param cellRow int Which represents the number of Cell's row (start from up with number 0)
     * @return a String which represents the part ( up, middle or down ) of symbolNumber if cellRow == 2|3|4,
     *          or empty String if it isn't
     */
    static String getBoardRowSymbol(Symbols symbolNumber, int cellRow) {
        String stringNumber = "";

        switch (cellRow ) {
            case 2:
                stringNumber = symbolNumber.getUpRepresentation();
                break;
            case 3:
                stringNumber = symbolNumber.getMiddleRepresentation();
                break;
            case 4:
                stringNumber = symbolNumber.getDownRepresentation();
                break;
            default:
                ;
        }

        return stringNumber;
    }

    /**
     * Prints a part of Players's caption if boardRow and cellRow are correct, doesn't do anything it they aren't
     * @param boardRow number or board's row ( start from up with number 0)
     * @param cellRow number or cell's row ( start from up with number 0)
     */
    static void printPlayersCaption(int boardRow, int cellRow) {
        final int DISTANCE_FROM_BOARD = 10;
        final int SPACE_FOR_STRING = 15;
        String stringToPrint;
        ViewCard viewCard;
        ViewWorker[] workers;

        PrintFunction.printRepeatString(" ", DISTANCE_FROM_BOARD);

        for (ViewPlayer viewPlayer : ViewPlayer.getPlayerList()) {
            switch (boardRow) {
                case 1:
                    switch (cellRow) {
                        case 2:
                            try {
                                workers = viewPlayer.getWorkers();
                                stringToPrint = workers[0].getWorkerWTRepresentation().getUpRepresentation();
                            } catch (NullPointerException | NotFoundException e) {
                                stringToPrint = ""; // nothing representation to print
                            }
                            PrintFunction.printAtTheMiddle(stringToPrint, SPACE_FOR_STRING);
                            break;
                        case 3:
                            try {
                                workers = viewPlayer.getWorkers();
                                stringToPrint = workers[0].getWorkerWTRepresentation().getMiddleRepresentation();
                            } catch (NullPointerException | NotFoundException e) {
                                stringToPrint = ""; // nothing representation to print
                            }
                            PrintFunction.printAtTheMiddle(stringToPrint, SPACE_FOR_STRING);
                            break;
                        case 4:
                            try {
                                workers = viewPlayer.getWorkers();
                                stringToPrint = workers[0].getWorkerWTRepresentation().getDownRepresentation();
                            } catch (NullPointerException | NotFoundException e) {
                                stringToPrint = ""; // nothing representation to print
                            }
                            PrintFunction.printAtTheMiddle(stringToPrint, SPACE_FOR_STRING);
                            break;
                       case 5:
                            break;
                       case 6:
                           PrintFunction.printAtTheMiddle(viewPlayer.getName(), SPACE_FOR_STRING);
                           break;
                       default:
                           ;
                    }
                    break;
                case 2:
                    try {
                        viewCard = viewPlayer.getCard();
                        switch (cellRow) {
                            case 0:
                                stringToPrint = GodSymbols.searchGodSymbol( viewCard.getName() ).getUpRepresentation();
                                PrintFunction.printAtTheMiddle(stringToPrint, SPACE_FOR_STRING);
                                break;
                            case 1:
                                stringToPrint = GodSymbols.searchGodSymbol( viewCard.getName() ).getMiddleRepresentation();
                                PrintFunction.printAtTheMiddle(stringToPrint, SPACE_FOR_STRING);
                                break;
                            case 2:
                                stringToPrint = GodSymbols.searchGodSymbol( viewCard.getName() ).getDownRepresentation();
                                PrintFunction.printAtTheMiddle(stringToPrint, SPACE_FOR_STRING);
                                break;
                            case 3:
                                break;
                            case 4:
                                PrintFunction.printAtTheMiddle( viewCard.getName(), SPACE_FOR_STRING);
                                break;
                            default:
                                ;
                        }
                    } catch (NotFoundException | NullPointerException e) {
                        PrintFunction.printRepeatString("", SPACE_FOR_STRING);
                    }
                    break;
                default:
                    ;
            }

        }

    }
}
