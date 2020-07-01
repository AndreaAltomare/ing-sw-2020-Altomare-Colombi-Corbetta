package it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration;

/**
 * Enumeration with the three <code>String</code> of the three level of the representation for the most important
 * element of the play like workers, block and dome, selection symbols and the number from 0 to 4 build with ASCII's chars
 *
 * @author Marco
 */
public enum Symbols {

    WORKER_1(   " O ",
                "/|\\",
                "/ \\"),

    WORKER_2(   "\\O ",
                " |\\",
                "/ \\"),

    WORKER_3(   "\\O/",
                " | ",
                "/ \\"),

    DOME(       " ___ ",
            "/___\\",
                "     "),

    BLOCK(      " __ ",
            "|__|",
                "    "),

    SELECTION_LEFT( "   /",
                    ">>( ",
                    "   \\"),

    SELECTION_RIGHT("\\   ",
                    " )<<",
                    "/   "),

    NUMBER_0(   " _ ",
                "| |",
                "|_|"),

    NUMBER_1(   " . ",
                "/| ",
                "_|_"),

    NUMBER_2(   " _ ",
                " _|",
                "|_ "),

    NUMBER_3(   " _ ",
                " _|",
                " _|"),

    NUMBER_4(   "   ",
                "|_|",
                "  |");

    private String upRepresentation;
    private String middleRepresentation;
    private String downRepresentation;

    Symbols(String upRepresentation, String middleRepresentation, String downRepresentation) {
        this.upRepresentation = upRepresentation;
        this.middleRepresentation = middleRepresentation;
        this.downRepresentation = downRepresentation;
    }

    /**
     * Returns the parameter called upRepresentation of GodSymbol chosen
     *
     * @return upRepresentation parameter
     */
    public String getUpRepresentation() {
        return upRepresentation;
    }

    /**
     * Returns the parameter called middleRepresentation of GodSymbol chosen
     *
     * @return middleRepresentation parameter
     */
    public String getMiddleRepresentation() {
        return middleRepresentation;
    }

    /**
     * Returns the parameter called downRepresentation of GodSymbol chosen
     *
     * @return downRepresentation parameter
     */
    public String getDownRepresentation() {
        return downRepresentation;
    }
}
