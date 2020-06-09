package it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces;

public interface PrintFunction {

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
