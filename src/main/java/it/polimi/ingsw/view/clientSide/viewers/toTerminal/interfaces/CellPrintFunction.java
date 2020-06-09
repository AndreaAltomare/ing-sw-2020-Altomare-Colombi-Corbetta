package it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewWorker;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.Symbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.SymbolsLevel;

public interface CellPrintFunction {

    /**
     * Returns string with selection Symbols at the level chosen if isSelected == true,
     * if it isn't the methods returns the same string in input
     * @param string original string to select
     * @param selectionLevel level of the selection Symbols
     * @param isSelected boolean parameter that set or not the seletion
     * @return string if isSelected == false, string between selection's level iof isSelected == false
     */
    static String tryAddSelectionSymbols(String string, SymbolsLevel selectionLevel, boolean isSelected) {
        String newString = string;

        if ( isSelected ) {
            switch (selectionLevel) {
                case UP:
                    newString = Symbols.SELECTION_LEFT.getUpRepresentation() + newString + Symbols.SELECTION_RIGHT.getUpRepresentation();
                    break;
                case MIDDLE:
                    newString = Symbols.SELECTION_LEFT.getMiddleRepresentation() + newString + Symbols.SELECTION_RIGHT.getMiddleRepresentation();
                    break;
                case DOWN:
                    newString = Symbols.SELECTION_LEFT.getDownRepresentation() + newString + Symbols.SELECTION_RIGHT.getDownRepresentation();
                    break;
                default:
                    ;
            }
        }

        return newString;
    }

    /**
     * Returns the correct String symbol in this cell's row using some parameter
     * @param blockLevel level of block's build
     * @param doomed boolean parameter to know if there is a dome
     * @param thereIsWorker boolean parameter to know if there is a worker
     * @param viewWorker viewWorker of worker on Cell (using only if thereIsWorker == true)
     * @param isSelected boolean parameter to know if the cell is selected
     * @return string to string at the middle of cell's row
     */
    static String cellRow1(Integer blockLevel, boolean doomed, boolean thereIsWorker, ViewWorker viewWorker, boolean isSelected) {
        String symbolString = "";

        if ( blockLevel != 0) {
            if ( doomed ) {
                symbolString = Symbols.DOME.getUpRepresentation();
                symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.UP, isSelected);
            } else {
                if ( thereIsWorker ) {
                    symbolString = viewWorker.toWTerminal(SymbolsLevel.UP);
                    symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.UP, isSelected);
                }
            }
        }

        return symbolString;
    }

    /**
     * Returns the correct String symbol in this cell's row using some parameter
     * @param blockLevel level of block's build
     * @param doomed boolean parameter to know if there is a dome
     * @param thereIsWorker boolean parameter to know if there is a worker
     * @param viewWorker viewWorker of worker on Cell (using only if thereIsWorker == true)
     * @param isSelected boolean parameter to know if the cell is selected
     * @return string to string at the middle of cell's row
     */
    static String cellRow2(Integer blockLevel, boolean doomed, boolean thereIsWorker, ViewWorker viewWorker, boolean isSelected) {
        String symbolString = "";

        if ( blockLevel != 0) {
            if ( doomed ) {
                symbolString = Symbols.DOME.getMiddleRepresentation();
                symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.MIDDLE, isSelected);
            } else {
                if ( thereIsWorker ) {
                    symbolString = viewWorker.toWTerminal(SymbolsLevel.MIDDLE);
                    symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.MIDDLE, isSelected);
                } else {
                    symbolString = Symbols.BLOCK.getUpRepresentation() + "   ";
                    symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.UP, isSelected);
                }
            }
        } else {
            if ( doomed ) {
                symbolString = Symbols.DOME.getUpRepresentation();
                symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.UP, isSelected);
            } else {
                if ( thereIsWorker ) {
                    symbolString = viewWorker.toWTerminal(SymbolsLevel.UP);
                    symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.UP, isSelected);
                } else {
                    symbolString = "       ";
                    symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.UP, isSelected);
                }
            }
        }

        return symbolString;
    }

    /**
     * Returns the correct String symbol in this cell's row using some parameter
     * @param blockLevel level of block's build
     * @param doomed boolean parameter to know if there is a dome
     * @param thereIsWorker boolean parameter to know if there is a worker
     * @param viewWorker viewWorker of worker on Cell (using only if thereIsWorker == true)
     * @param isSelected boolean parameter to know if the cell is selected
     * @return string to string at the middle of cell's row
     */
    static String cellRow3(Integer blockLevel, boolean doomed, boolean thereIsWorker, ViewWorker viewWorker, boolean isSelected) {
        String symbolString = "";

        if ( blockLevel != 0) {
            if ( doomed ) {
                symbolString = Symbols.DOME.getDownRepresentation();
                symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.DOWN, isSelected);
            } else {
                if ( thereIsWorker ) {
                    symbolString = viewWorker.toWTerminal(SymbolsLevel.DOWN);
                    symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.DOWN, isSelected);
                } else {
                    symbolString = Symbols.BLOCK.getMiddleRepresentation() + "  " + blockLevel.toString();
                    symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.MIDDLE, isSelected);
                }
            }
        } else {
            if ( doomed ) {
                symbolString = Symbols.DOME.getMiddleRepresentation();
                symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.MIDDLE, isSelected);
            } else {
                if ( thereIsWorker ) {
                    symbolString = viewWorker.toWTerminal(SymbolsLevel.MIDDLE);
                    symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.MIDDLE, isSelected);
                } else {
                    symbolString = "       ";
                    symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.MIDDLE, isSelected);
                }
            }
        }

        return symbolString;
    }

    /**
     * Returns the correct String symbol in this cell's row using some parameter
     * @param blockLevel level of block's build
     * @param doomed boolean parameter to know if there is a dome
     * @param thereIsWorker boolean parameter to know if there is a worker
     * @param viewWorker viewWorker of worker on Cell (using only if thereIsWorker == true)
     * @param isSelected boolean parameter to know if the cell is selected
     * @return string to string at the middle of cell's row
     */
    static String cellRow4(Integer blockLevel, boolean doomed, boolean thereIsWorker, ViewWorker viewWorker, boolean isSelected) {
        String symbolString = "";

        if ( blockLevel != 0) {
            if ( doomed || thereIsWorker ) {
                symbolString = Symbols.BLOCK.getUpRepresentation() + "   ";
            } else {
                symbolString = Symbols.BLOCK.getDownRepresentation() + "   " ;
                symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.DOWN, isSelected);
            }
        } else {
            if ( doomed ) {
                symbolString = Symbols.DOME.getDownRepresentation();
                symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.DOWN, isSelected);
            } else {
                if ( thereIsWorker ) {
                    symbolString = viewWorker.toWTerminal(SymbolsLevel.DOWN);
                    symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.DOWN, isSelected);
                } else {
                    symbolString = "       ";
                    symbolString = CellPrintFunction.tryAddSelectionSymbols(symbolString, SymbolsLevel.DOWN, isSelected);
                }
            }
        }

        return symbolString;
    }

    /**
     * Returns the correct String symbol in this cell's row using some parameter
     * @param blockLevel level of block's build
     * @param doomed boolean parameter to know if there is a dome
     * @param thereIsWorker boolean parameter to know if there is a worker
     * @param viewWorker viewWorker of worker on Cell (using only if thereIsWorker == true)
     * @param isSelected boolean parameter to know if the cell is selected
     * @return string to string at the middle of cell's row
     */
    static String cellRow5(Integer blockLevel, boolean doomed, boolean thereIsWorker, ViewWorker viewWorker, boolean isSelected) {
        String symbolString = "";

        if ( blockLevel != 0) {
            if ( doomed || thereIsWorker ) {
                symbolString = Symbols.BLOCK.getMiddleRepresentation() + "  " + blockLevel.toString();
            }
        }

        return symbolString;
    }
}
