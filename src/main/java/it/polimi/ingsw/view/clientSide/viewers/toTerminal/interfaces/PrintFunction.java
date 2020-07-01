package it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces;

import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;

public interface PrintFunction {

    int MESSAGE_SLEEP_TIME = 2000; // in ns
    int STARTING_SPACE = 7;

    /**
     * Prints errorMessage with error symbol, then waits a few second (MESSAGE_SLEEP_TIME / 1000)
     * so the player can read it
     *
     * @param errorMessage
     */
    static void printError(String errorMessage) {
        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println( "><" +  errorMessage);
        System.out.println();
        try {
            Thread.sleep(MESSAGE_SLEEP_TIME);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Prints checkMessage with check symbol, then waits a few second (MESSAGE_SLEEP_TIME / 1000)
     * so the player can read it
     *
     * @param checkMessage
     */
    static void printCheck(String checkMessage) {
        System.out.println();
        PrintFunction.printRepeatString("", STARTING_SPACE + 2);
        System.out.println("/");
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println( "\\/ " + checkMessage );
        System.out.println();
        try {
            Thread.sleep(MESSAGE_SLEEP_TIME);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Methods that implements a for cycle to print on standard output
     * a chosen String for a chosen number of times
     *
     * @param string string to print
     * @param repeatsNumber number of times to print the chosen string
     */
    static void printRepeatString(String string, int repeatsNumber) {
        for ( int i = 0; i < repeatsNumber; i++) {
            System.out.printf("%s", string);
        }
    }

    /**
     * Prints on standard output a String at the middle of a chosen length of space using printRepeatString
     * @param middleString string to print
     * @param totalLength length of space where middleString id printed
     */
    static void printAtTheMiddle(String middleString, int totalLength) {
        if ( ((totalLength - middleString.length()) % 2 ) == 0) {
            PrintFunction.printRepeatString(" ", (totalLength - middleString.length()) / 2);
        } else {
            PrintFunction.printRepeatString(" ", ((totalLength - middleString.length()) / 2) + 1);
        }
        System.out.print(middleString);
        PrintFunction.printRepeatString(" ", (totalLength - middleString.length()) / 2);

    }
}
