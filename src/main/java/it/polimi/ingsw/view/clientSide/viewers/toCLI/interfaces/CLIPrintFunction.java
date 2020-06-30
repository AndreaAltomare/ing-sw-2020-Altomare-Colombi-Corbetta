package it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces;

import com.sun.org.apache.bcel.internal.generic.StackProducer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.UnicodeSymbol;

public interface CLIPrintFunction {

    int MESSAGE_SLEEP_TIME = 2000; // in ns
    int STARTING_SPACE = 7;
    String ERROR_COLOR_AND_SYMBOL = ANSIStyle.RED.getEscape() + UnicodeSymbol.X_MARK.getEscape();
    String CORRECT_COLOR_AND_SYMBOL = ANSIStyle.GREEN.getEscape() + UnicodeSymbol.CHECK_MARK.getEscape();
    String WRITE_MARK = ANSIStyle.UNDERSCORE.getEscape() + UnicodeSymbol.PENCIL.getEscape() + ANSIStyle.RESET;


    /**
     * Prints errorMessage with color and symbol of ERROR_COLOR_AND_SYMBOL, then waits a few second (MESSAGE_SLEEP_TIME / 1000)
     * so the player can read it
     *
     * @param errorMessage
     */
    static void printError(String errorMessage) {
        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.println( ERROR_COLOR_AND_SYMBOL + errorMessage + ANSIStyle.RESET);
        System.out.println();
        try {
            Thread.sleep(MESSAGE_SLEEP_TIME);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Prints checkMessage with color and symbol of CORRECT_COLOR_AND_SYMBOL, then waits a few second (MESSAGE_SLEEP_TIME / 1000)
     * so the player can read it
     *
     * @param checkMessage
     */
    static void printCheck(String checkMessage) {
        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.println( ERROR_COLOR_AND_SYMBOL + checkMessage + ANSIStyle.RESET);
        System.out.println();
        try {
            Thread.sleep(MESSAGE_SLEEP_TIME);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Methods that implements a for cycle to print on standard output
     * a chosen String with the chosen style for a chosen number of times
     *
     * @param style string to resent the style
     * @param string string to print
     * @param repeatsNumber number of times to print the chosen string
     */
    static void printRepeatString(String style, String string, int repeatsNumber) {
        System.out.print( style );
        for ( int i = 0; i < repeatsNumber; i++) {
            System.out.printf("%s", string);
        }
        System.out.print(ANSIStyle.RESET );
    }

    /**
     * Prints on standard output a String with the chosen type at the middle of a chosen length of space using printRepeatString
     *
     * @param style string to represent the style
     * @param middleString string to print
     * @param stringLength length of the middleString
     * @param totalLength length of space where middleString id printed
     */
    static void printAtTheMiddle(String style, String middleString, int stringLength, int totalLength) {
        System.out.print( style );
        if ( ((totalLength - stringLength) % 2 ) == 0) {
            CLIPrintFunction.printRepeatString(""," ", (totalLength - stringLength) / 2);
        } else {
            CLIPrintFunction.printRepeatString(""," ", ((totalLength - stringLength) / 2) + 1);
        }
        System.out.print(middleString);
        CLIPrintFunction.printRepeatString(""," ", (totalLength - stringLength) / 2);
        System.out.print(ANSIStyle.RESET );

    }

    /**
     * Adds spaces to string as long as string's length == final length
     *
     * @param string initial string
     * @param stringLength string's length
     * @param finalLength final length chosen
     * @return string with length == finalLength
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
