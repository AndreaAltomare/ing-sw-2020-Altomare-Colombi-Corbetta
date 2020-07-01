package it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration;

/**
 * Enumeration with the representation of board's element like worker, blocks and dome, and the representation
 * of the numbers from 0 to 4 using <code>UnicodeSymbol</code>
 *
 * @see UnicodeSymbol
 * @author Marco
 */
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


    NUMBER_0(UnicodeSymbol.HIGH_BLOCK_4.getEscape() + UnicodeSymbol.HIGH_BLOCK_4.getEscape() + UnicodeSymbol.HIGH_BLOCK_4.getEscape(),
            UnicodeSymbol.HIGH_BLOCK_8.getEscape() + " " + UnicodeSymbol.HIGH_BLOCK_8.getEscape(),
            UnicodeSymbol.HIGH_BLOCK_8.getEscape() + UnicodeSymbol.HIGH_BLOCK_4.getEscape() + UnicodeSymbol.HIGH_BLOCK_8.getEscape())
            {

                @Override
                public int getLength() { return 3;}

            },

    NUMBER_1( " " + UnicodeSymbol.HIGH_BLOCK_4.getEscape() + " ",
            " " + UnicodeSymbol.HIGH_BLOCK_8.getEscape() + " ",
            " " + UnicodeSymbol.HIGH_BLOCK_8.getEscape() + " ")
            {

                @Override
                public int getLength() { return 3;}

            },

    NUMBER_2(UnicodeSymbol.HIGH_BLOCK_4.getEscape() + UnicodeSymbol.HIGH_BLOCK_4.getEscape() + UnicodeSymbol.HIGH_BLOCK_4.getEscape(),
            UnicodeSymbol.HIGH_BLOCK_4.getEscape() + UnicodeSymbol.HIGH_BLOCK_4.getEscape() + UnicodeSymbol.HIGH_BLOCK_8.getEscape(),
            UnicodeSymbol.HIGH_BLOCK_8.getEscape() + UnicodeSymbol.HIGH_BLOCK_4.getEscape() + UnicodeSymbol.HIGH_BLOCK_4.getEscape())
            {

                @Override
                public int getLength() { return 3;}

            },

    NUMBER_3(UnicodeSymbol.HIGH_BLOCK_4.getEscape() + UnicodeSymbol.HIGH_BLOCK_4.getEscape() + UnicodeSymbol.HIGH_BLOCK_4.getEscape(),
            UnicodeSymbol.HIGH_BLOCK_4.getEscape() + UnicodeSymbol.HIGH_BLOCK_4.getEscape() + UnicodeSymbol.HIGH_BLOCK_8.getEscape(),
            UnicodeSymbol.HIGH_BLOCK_4.getEscape() + UnicodeSymbol.HIGH_BLOCK_4.getEscape() + UnicodeSymbol.HIGH_BLOCK_8.getEscape())
            {

                @Override
                public int getLength() { return 3;}

            },

    NUMBER_4(UnicodeSymbol.HIGH_BLOCK_4.getEscape() + " " + UnicodeSymbol.HIGH_BLOCK_4.getEscape(),
            UnicodeSymbol.HIGH_BLOCK_8.getEscape() + UnicodeSymbol.HIGH_BLOCK_4.getEscape() + UnicodeSymbol.HIGH_BLOCK_8.getEscape(),
            " " + " " + UnicodeSymbol.HIGH_BLOCK_8.getEscape())
            {

                @Override
                public int getLength() { return 3;}

            };

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
