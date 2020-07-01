package it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewWorker;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.*;

/**
 * Interface with methods to create the correct <code>String</code> with the correct <code>CLISymbols</code> for each cell's row
 *
 * @see CLISymbols
 * @author Marco
 */
public interface CLICellPrintFunction {

    String DOME_COLOR = ANSIStyle.BLUE.getEscape();
    String BLOCK_COLOR = ANSIStyle.GREY.getEscape();
    int SELECTION_SPACE  = 4; // see and change tryAddSelectionSymbols to know and change the value

    /**
     * Method which returns <code>String</code> with selection Symbols if isSelected == true,
     * if it isn't the methods returns the same string with 2 initial space and 2 final space in input
     *
     * @param string original string to select
     * @param isSelected boolean parameter that set or not the selection
     * @return string if isSelected == false, string between selection's level iof isSelected == false
     */
    static String tryAddSelectionSymbols(String string, boolean isSelected) {
        final String DEFAULT_SELECTED_COLOR = "";
        ViewWorker selectedWorker = ViewWorker.getSelected();
        String SelectionColor;
        String newString;

        if ( selectedWorker != null ) {
            SelectionColor = selectedWorker.getWorkerCLIColor();
        } else {
            SelectionColor = DEFAULT_SELECTED_COLOR;
        }

        if ( isSelected ) {
            newString = SelectionColor + UnicodeSymbol.SELECTION_LEFT.getEscape() + " " + string + " " + SelectionColor + UnicodeSymbol.SELECTION_RIGHT.getEscape();
        } else {
            newString = "  " + string + "  ";
        }

        return newString;
    }

    /**
     * Method which returns the correct <code>String</code> of length == symbolSpace symbol in first cell's row from up using some parameter
     *
     * @param blockLevel level of block's build
     * @param doomed boolean parameter to know if there is a dome
     * @param thereIsWorker boolean parameter to know if there is a worker
     * @param viewWorker viewWorker of worker on Cell (using only if thereIsWorker == true)
     * @param isSelected boolean parameter to know if the cell is selected
     * @param symbolSpace length of the returned string
     * @return string to string at the middle of cell's row
     */
    static String cellRow0(Integer blockLevel, boolean doomed, boolean thereIsWorker, ViewWorker viewWorker, boolean isSelected, int symbolSpace) {
        String symbolString = "";

        symbolString = CLIPrintFunction.increaseLengthWithSpace( symbolString, 0, symbolSpace);

        if ( blockLevel != 0) {
            if ( thereIsWorker ) {
                symbolString = viewWorker.toCLI( true );
                symbolString = CLIPrintFunction.increaseLengthWithSpace(symbolString, CLISymbols.WORKER.getLength(), symbolSpace);
            }

        }

        return symbolString;
    }

    /**
     * Method which returns the correct <code>String</code> of length == symbolSpace symbol in second cell's row from up using some parameter
     *
     * @param blockLevel level of block's build
     * @param doomed boolean parameter to know if there is a dome
     * @param thereIsWorker boolean parameter to know if there is a worker
     * @param viewWorker viewWorker of worker on Cell (using only if thereIsWorker == true)
     * @param isSelected boolean parameter to know if the cell is selected
     * @param symbolSpace length of the returned string
     * @return string to string at the middle of cell's row
     */
    static String cellRow1(Integer blockLevel, boolean doomed, boolean thereIsWorker, ViewWorker viewWorker, boolean isSelected , int symbolSpace) {
        String symbolString = "";
        int placeableLength = 0;

        if ( blockLevel != 0) {
            if ( doomed ) {
                symbolString = DOME_COLOR + CLISymbols.DOME.getMiddleRepresentation();
                symbolString = CLICellPrintFunction.tryAddSelectionSymbols( symbolString, isSelected );
                placeableLength = CLISymbols.DOME.getLength() + SELECTION_SPACE;
            } else {
                if ( thereIsWorker ) {
                    symbolString = viewWorker.toCLI( false );
                    symbolString = CLICellPrintFunction.tryAddSelectionSymbols( symbolString, isSelected );
                    placeableLength = CLISymbols.WORKER.getLength() + SELECTION_SPACE;
                }
            }
        } else {
            if ( thereIsWorker ) {
                placeableLength = CLISymbols.WORKER.getLength();
                symbolString = viewWorker.toCLI( true );
            }
        }

        symbolString = CLIPrintFunction.increaseLengthWithSpace( symbolString, placeableLength, symbolSpace);

        return symbolString;
    }

    /**
     * Method which returns the correct <code>String</code> of length == symbolSpace symbol in third cell's row from up using some parameter
     *
     * @param blockLevel level of block's build
     * @param doomed boolean parameter to know if there is a dome
     * @param thereIsWorker boolean parameter to know if there is a worker
     * @param viewWorker viewWorker of worker on Cell (using only if thereIsWorker == true)
     * @param isSelected boolean parameter to know if the cell is selected
     * @param symbolSpace length of the returned string
     * @return string to string at the middle of cell's row
     */
    static String cellRow2(Integer blockLevel, boolean doomed, boolean thereIsWorker, ViewWorker viewWorker, boolean isSelected, int symbolSpace) {
        String symbolString = "";
        int placeableLength = 0;

        if ( blockLevel != 0) {
            if ( !doomed && !thereIsWorker ) {
                symbolString = BLOCK_COLOR + CLISymbols.BLOCK.getMiddleRepresentation() + "  " + blockLevel.toString();
                symbolString = CLICellPrintFunction.tryAddSelectionSymbols( symbolString, isSelected );
                placeableLength = CLISymbols.BLOCK.getLength() + 3 + SELECTION_SPACE;
            }
        } else {
            if ( doomed ) {
                symbolString = DOME_COLOR + CLISymbols.DOME.getMiddleRepresentation();
                symbolString = CLICellPrintFunction.tryAddSelectionSymbols( symbolString, isSelected );
                placeableLength = CLISymbols.DOME.getLength() + SELECTION_SPACE;
            } else {
                if ( thereIsWorker ) {
                    symbolString = viewWorker.toCLI( false );
                    symbolString = CLICellPrintFunction.tryAddSelectionSymbols( symbolString, isSelected );
                    placeableLength = CLISymbols.WORKER.getLength() + SELECTION_SPACE;
                } else {
                    symbolString = "     ";
                    symbolString = CLICellPrintFunction.tryAddSelectionSymbols(symbolString, isSelected );
                    placeableLength = 5 + SELECTION_SPACE;
                }
            }
        }

        symbolString = CLIPrintFunction.increaseLengthWithSpace( symbolString, placeableLength, symbolSpace);

        return symbolString;
    }

    /**
     * Method which returns the correct <code>String</code> of length == symbolSpace symbol in fourth cell's row from up using some parameter
     *
     * @param blockLevel level of block's build
     * @param doomed boolean parameter to know if there is a dome
     * @param thereIsWorker boolean parameter to know if there is a worker
     * @param viewWorker viewWorker of worker on Cell (using only if thereIsWorker == true)
     * @param isSelected boolean parameter to know if the cell is selected
     * @param symbolSpace length of the returned string
     * @return string to string at the middle of cell's row
     */
    static String cellRow3(Integer blockLevel, boolean doomed, boolean thereIsWorker, ViewWorker viewWorker, boolean isSelected, int symbolSpace) {
        String symbolString = "";
        int placeableLength = 0;

        if ( blockLevel != 0) {
            if ( doomed || thereIsWorker ) {
                symbolString = BLOCK_COLOR + CLISymbols.BLOCK.getMiddleRepresentation() + "  " + blockLevel.toString();
                placeableLength = CLISymbols.BLOCK.getLength() + 3;
            }
        }

        symbolString = CLIPrintFunction.increaseLengthWithSpace( symbolString, placeableLength, symbolSpace);

        return symbolString;
    }

    /**
     * Method which returns the correct <code>String</code> of length == symbolSpace symbol in fifth cell's row from up using some parameter
     *
     * @param blockLevel level of block's build
     * @param doomed boolean parameter to know if there is a dome
     * @param thereIsWorker boolean parameter to know if there is a worker
     * @param viewWorker viewWorker of worker on Cell (using only if thereIsWorker == true)
     * @param isSelected boolean parameter to know if the cell is selected
     * @param symbolSpace length of the returned string
     * @return string to string at the middle of cell's row
     */
    static String cellRow4(Integer blockLevel, boolean doomed, boolean thereIsWorker, ViewWorker viewWorker, boolean isSelected, int symbolSpace) {

        return CLIPrintFunction.increaseLengthWithSpace( "", 0, symbolSpace);

    }

}
