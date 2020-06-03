package it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration;

public enum CLISymbols {
    // for each symbol each representations have the same length

    WORKER( " o ",
                UnicodeSymbol.PIECE_OF_BLOCK_HIGH_RIGHT.getEscape() +
                                    UnicodeSymbol.HIGH_BLOCK_8.getEscape() +
                                    UnicodeSymbol.PIECE_OF_BLOCK_HIGH_LEFT.getEscape(),
                "   ")    {

                                            @Override
                                            public int getLength() { return 3;}

                                            },

    DOME(       "   ",
            UnicodeSymbol.SCALE_LEFT.getEscape() +
                                UnicodeSymbol.HIGH_BLOCK_8.getEscape() +
                                UnicodeSymbol.SCALE_RIGHT.getEscape(),
                "   ")    {

                                            @Override
                                            public int getLength() { return 3;}

                                            },

    BLOCK(      "  ",
            UnicodeSymbol.HIGH_BLOCK_8.getEscape() +
                                UnicodeSymbol.HIGH_BLOCK_8.getEscape(),
            "  ")      {

                                            @Override
                                            public int getLength() { return 2;}

                                            },

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

    CLISymbols(String upRepresentation, String middleRepresentation, String downRepresentation) {
        this.upRepresentation = upRepresentation;
        this.middleRepresentation = middleRepresentation;
        this.downRepresentation = downRepresentation;
    }

    /**
     * Returns the parameter called upRepresentation of GodSymbol chosen
     * @return upRepresentation parameter
     */
    public String getUpRepresentation() {
        return upRepresentation;
    }

    /**
     * Returns the parameter called middleRepresentation of GodSymbol chosen
     * @return middleRepresentation parameter
     */
    public String getMiddleRepresentation() {
        return middleRepresentation;
    }

    /**
     * Returns the parameter called downRepresentation of GodSymbol chosen
     * @return downRepresentation parameter
     */
    public String getDownRepresentation() {
        return downRepresentation;
    }

    /**
     * Returns the length of UpRepresentation ( all representations have the same length for construction
     * @return
     */
    public int getLength() {
        return upRepresentation.length();
    }
}
