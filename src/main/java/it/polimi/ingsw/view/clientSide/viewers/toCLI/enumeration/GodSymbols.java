package it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration;

import it.polimi.ingsw.view.exceptions.NotFoundException;

public enum GodSymbols {

    // Each godSymbol have and must have its up, middle and down representation at the same length

    APOLLO(     "Apollo",
                " )_______( ",
                "(   |||   )",
                " \\  |||  / "),

    ARTEMIS(    "Artemis",
                "   |\\   ",
                ">>-|-|->",
                "   |/   "),

    ATHENA(     "Athena",
                " /      \\ ",
                "|(o)/\\(o)|",
                " \\  \\/  / "),

    ATLAS(      "Atlas",
                " _ ",
                "(_)",
                "\\o/"),

    DEMETER(    "Demeter",
                "V",
                "V",
                "|"),

    HEPHAESTUS( "Hephaestus",
                " ___ ",
                "|___|",
                "  |  "),

    MINOTAUR(   "Minotaur",
                "     ",
                "|___|",
                " (_) "),

    PAN(        "Pan",
                "     ",
                "@___@",
                " (_) "),

    PROMETHEUS( "Prometheus",
                "    ",
                " )\\ ",
                "(())");


    private String name;
    private String upRepresentation;
    private String middleRepresentation;
    private String downRepresentation;

    GodSymbols(String name, String upRepresentation, String middleRepresentation, String downRepresentation) {
        this.name = name;
        this.upRepresentation = upRepresentation;
        this.middleRepresentation = middleRepresentation;
        this.downRepresentation = downRepresentation;
    }

    /**
     * Returns the parameter called name of GodSymbol chosen
     * @return name parameter
     */
    public String getName() {
        return name;
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
     * Compares name's length of all GodSymbol and return the max value
     * @return max value of name's length
     */
    public static int getMaxNameLength() {
        int max = -1;

        for ( GodSymbols godSymbols : GodSymbols.values()) {
            if ( godSymbols.getName().length() > max) {
                max = godSymbols.getName().length();
            }
        }

        return max;
    }


    /**
     * Compares representation's length of all GodSymbol and return the max value.
     * All representations (up, middle and down) for a single GodSymbol have the same length
     * @return max value of upRepresentation's length
     */
    public static int getMaxRepresentationLength() {
        int max = -1;

        for ( GodSymbols godSymbols : GodSymbols.values()) {
            if ( godSymbols.getUpRepresentation().length() > max) {
                max = godSymbols.getUpRepresentation().length();
            }
        }

        return max;
    }

    /**
     * Searches a god's (or goddess') name in the name of GodSymbol's value and if it finds it,
     * it returns the correct GodSymbols but if it doesn't found it, it trows a NotFindException
     * @param godName name of godSymbol to search
     * @return GodSymbols with name == godName
     * @throws NotFoundException exception which notify that there isn't a GodSymbols with name == godName
     */
    public static GodSymbols searchGodSymbol(String godName) throws NotFoundException {
        GodSymbols godFind = null;

        for ( GodSymbols godSymbols : GodSymbols.values()) {
            if ( godSymbols.getName().equals(godName)) {
                godFind = godSymbols;
            }
        }
        if ( godFind == null) {
            throw new NotFoundException();
        }

        return godFind;
    }
}
