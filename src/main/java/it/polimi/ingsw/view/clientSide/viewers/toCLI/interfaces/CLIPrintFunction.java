package it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces;

import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.UnicodeSymbol;

/**
 * Interface with methods to help the CLIStatusClasses and CLISubTurnCLasses to print image o message
 *
 * @author Marco
 */
public interface CLIPrintFunction {

    int STARTING_SPACE = 7;
    String ERROR_COLOR_AND_SYMBOL = ANSIStyle.RED.getEscape() + UnicodeSymbol.X_MARK.getEscape();
    String CORRECT_COLOR_AND_SYMBOL = ANSIStyle.GREEN.getEscape() + UnicodeSymbol.CHECK_MARK.getEscape();
    String WRITE_MARK = ANSIStyle.UNDERSCORE.getEscape() + UnicodeSymbol.PENCIL.getEscape() + ANSIStyle.RESET;


    /**
     * Prints errorMessage with color and symbol of ERROR_COLOR_AND_SYMBOL
     *
     * @param errorMessage <code>String</code> to print
     * */
    static void printError(String errorMessage) {
        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.println( ERROR_COLOR_AND_SYMBOL + errorMessage + ANSIStyle.RESET);
        System.out.println();
    }

    /**
     * Prints checkMessage with color and symbol of CORRECT_COLOR_AND_SYMBOL
     *
     * @param checkMessage <code>String</code> to print
     */
    static void printCheck(String checkMessage) {
        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.println( CORRECT_COLOR_AND_SYMBOL + checkMessage + ANSIStyle.RESET);
        System.out.println();
    }

    /**
     * Methods that implements a for cycle to print on standard output
     * a chosen <code>String</code> with the chosen style for a chosen number of times
     *
     * @param style <code>String</code> to resent the style
     * @param string <code>String</code> to print
     * @param repeatsNumber number of times to print the chosen <code>String</code>
     */
    static void printRepeatString(String style, String string, int repeatsNumber) {
        System.out.print( style );
        for ( int i = 0; i < repeatsNumber; i++) {
            System.out.printf("%s", string);
        }
        System.out.print(ANSIStyle.RESET );
    }

    /**
     * Method which prints on standard output a <code>String</code> with the chosen type at the middle of a chosen length
     * of space using printRepeatString.
     * It prints middleString with style if stringLength is more than totalLength
     *
     * @param style <code>String</code> to represent the style
     * @param middleString <code>String</code> to print
     * @param stringLength length of the middleString
     * @param totalLength length of space where middleString is printed
     */
    static void printAtTheMiddle(String style, String middleString, int stringLength, int totalLength) {
        System.out.print( style );
        if ( ((totalLength - stringLength) % 2 ) == 0) {
            for ( int i = 0; i < ((totalLength - stringLength) / 2); i++) {
                System.out.print(" ");
            }
        } else {
            for ( int i = 0; i < (((totalLength - stringLength) / 2) + 1); i++) {
                System.out.print(" ");
            }
        }
        System.out.print(middleString);
        for ( int i = 0; i < ((totalLength - stringLength) / 2); i++) {
            System.out.print(" ");
        }
        System.out.print(ANSIStyle.RESET );

    }

    /**
     * Method which adds spaces to <code>String</code> as long as string's length == final length.
     * If stringLength < finalLength, it returns string
     *
     * @param string initial <code>String</code>
     * @param stringLength string's length
     * @param finalLength final length chosen
     * @return <code>String</code> with length == finalLength, if stringLength < finalLength
     */
    static String increaseLengthWithSpace(String string, int stringLength, int finalLength) {

        for ( int initialSpace = 0; initialSpace < ((finalLength - stringLength) / 2) ; initialSpace++) {
            string = " " + string;
        }
        for ( int finalSpace = 0; finalSpace < ((finalLength - stringLength) / 2) ; finalSpace++) {
            string = string + " ";
        }

        if ( (finalLength - stringLength) % 2 != 0) {
            string = " " + string;
        }

        return string;
    }
}
