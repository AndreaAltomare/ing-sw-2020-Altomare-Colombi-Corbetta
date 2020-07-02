package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.statusViewers.ReadyViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.UnicodeSymbol;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;

/**
 * Class that represents the <code>CLIStatusViewer</code> Ready on the CLI
 * using methods of <code>CLIPrintFunction</code> and <code>ANSIStyle</code>
 *
 * @see CLIStatusViewer
 * @see CLIPrintFunction
 * @see ANSIStyle
 * @author Marco
 */
public class CLIReadyViewer extends CLIStatusViewer {

    private ReadyViewer readyViewer;

    final int COLUMNS_THICKNESS = 2;
    final int COLUMNS_HIGH = 2;
    final String TITLE = "SANTORINI";
    final int COLUMNS_NUMBER = 4;
    final int PEDIMENT_LENGTH = 2 * COLUMNS_THICKNESS * COLUMNS_NUMBER;
    final String TITLE_COLOR = ANSIStyle.GREEN.getEscape();
    final String TEMPLE_COLOR = ANSIStyle.GREY.getEscape();
    final String TEMPLE_BACK_COLOR = ANSIStyle.BACK_GREY.getEscape();

    /**
     * Constructor to set correct <code>StatusViewer</code>
     *
     * @see ReadyViewer
     * @param readyViewer  <code>StatusViewer</code> linked at this class
     */
    public CLIReadyViewer(ReadyViewer readyViewer) {
        this.readyViewer = readyViewer;
    }

    /**
     * Method that use the private methods of this class to print the Title's image
     */
    @Override
    public void show() {

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
        this.showPediment();
        this.showTitle();
        this.showColumns();
        this.showStaircase();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
    }

    /**
     * Prints the Pediment of the temple
     */
    private void showPediment() {

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        CLIPrintFunction.printAtTheMiddle(TEMPLE_COLOR,UnicodeSymbol.HIGH_BLOCK_1.getEscape() +
                                                                    UnicodeSymbol.HIGH_BLOCK_2.getEscape() +
                                                                    UnicodeSymbol.HIGH_BLOCK_3.getEscape() +
                                                                    UnicodeSymbol.HIGH_BLOCK_4.getEscape() +
                                                                    UnicodeSymbol.HIGH_BLOCK_5.getEscape() +
                                                                    UnicodeSymbol.HIGH_BLOCK_6.getEscape() +
                                                                    UnicodeSymbol.HIGH_BLOCK_7.getEscape() +

                                                                    UnicodeSymbol.HIGH_BLOCK_7.getEscape() +
                                                                    UnicodeSymbol.HIGH_BLOCK_6.getEscape() +
                                                                    UnicodeSymbol.HIGH_BLOCK_5.getEscape() +
                                                                    UnicodeSymbol.HIGH_BLOCK_4.getEscape() +
                                                                    UnicodeSymbol.HIGH_BLOCK_3.getEscape() +
                                                                    UnicodeSymbol.HIGH_BLOCK_2.getEscape() +
                                                                    UnicodeSymbol.HIGH_BLOCK_1.getEscape(),
                                        14, PEDIMENT_LENGTH);
        System.out.println();

    }

    /**
     * Prints the title under the pediment
     */
    private void showTitle() {

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        CLIPrintFunction.printAtTheMiddle(TEMPLE_BACK_COLOR + TITLE_COLOR, TITLE, TITLE.length(), PEDIMENT_LENGTH);
        System.out.println();

    }

    /**
     * Prints the columns under the pedestal using the constants COLUMNS_HIGH and COLUMNS_THICKNESS
     * to print the correct numbers and the correct thickness of columns.
     */
    private void showColumns() {

        for (int columnLevel = 0; columnLevel < COLUMNS_HIGH; columnLevel++) {
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE + 1);
            for (int columnNumber = 0; columnNumber < COLUMNS_NUMBER; columnNumber++) {
                CLIPrintFunction.printRepeatString(TEMPLE_BACK_COLOR, " ", COLUMNS_THICKNESS);
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", COLUMNS_THICKNESS);
            }
            System.out.println();
        }

    }


    /**
     * Prints the Staircase under the columns using the constant STARTING_SPACE and
     * PEDESTAL_LENGTH to center the StairCase.
     */
    private void showStaircase() {

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.print( TEMPLE_COLOR + UnicodeSymbol.SCALE_LEFT.getEscape() );
        CLIPrintFunction.printRepeatString( TEMPLE_COLOR, UnicodeSymbol.HIGH_BLOCK_8.getEscape(), PEDIMENT_LENGTH - 2);
        System.out.print( TEMPLE_COLOR + UnicodeSymbol.SCALE_RIGHT.getEscape() );
        System.out.println();

    }

}
