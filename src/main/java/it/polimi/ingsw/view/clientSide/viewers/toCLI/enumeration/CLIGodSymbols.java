package it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration;

import it.polimi.ingsw.view.exceptions.NotFoundException;

/**
 * Enumeration used to contains the name and the three ASCII <code>String</code> with the same length to represented each god's card
 *
 * @author Marco
 */
public enum CLIGodSymbols {

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

    CLIGodSymbols(String name, String upRepresentation, String middleRepresentation, String downRepresentation) {
        this.name = name;
        this.upRepresentation = upRepresentation;
        this.middleRepresentation = middleRepresentation;
        this.downRepresentation = downRepresentation;
    }

    /**
     * Returns the parameter called name of CLIGodSymbol chosen
     *
     * @return name parameter
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the parameter called upRepresentation of CLIGodSymbol chosen
     *
     * @return upRepresentation parameter
     */
    public String getUpRepresentation() {
        return upRepresentation;
    }

    /**
     * Returns the parameter called middleRepresentation of CLIGodSymbol chosen
     *
     * @return middleRepresentation parameter
     */
    public String getMiddleRepresentation() {
        return middleRepresentation;
    }

    /**
     * Returns the parameter called downRepresentation of CLIGodSymbol chosen
     *
     * @return downRepresentation parameter
     */
    public String getDownRepresentation() {
        return downRepresentation;
    }

    /**
     * Returns upRepresentation's length
     * All representations (up, middle and down) for a single CLIGodSymbol have the same length
     *
     * @return value of upRepresentation's length
     */
    public int getRepresentationLength() {
        return upRepresentation.length();
    }

    /**
     * Compares name's length of all CLIGodSymbol and return the max value
     *
     * @return max value of name's length
     */
    public static int getMaxNameLength() {
        int max = -1;

        for ( CLIGodSymbols cligodSymbols : CLIGodSymbols.values()) {
            if ( cligodSymbols.getName().length() > max) {
                max = cligodSymbols.getName().length();
            }
        }

        return max;
    }


    /**
     * Compares representation's length of all CLIGodSymbol and return the max value.
     * All representations (up, middle and down) for a single CLIGodSymbol have the same length
     *
     * @return max value of upRepresentation's length
     */
    public static int getMaxRepresentationLength() {
        int max = -1;

        for ( CLIGodSymbols cligodSymbols : CLIGodSymbols.values()) {
            if ( cligodSymbols.getUpRepresentation().length() > max) {
                max = cligodSymbols.getUpRepresentation().length();
            }
        }

        return max;
    }

    /**
     * Searches a god's (or goddess') name in the name of CLIGodSymbol's value and if it finds it,
     * it returns the correct GodSymbols but if it doesn't found it, it trows a NotFindException
     *
     * @param godName name of godSymbol to search
     * @return CLIGodSymbols with name == godName
     * @throws NotFoundException exception which notify that there isn't a GodSymbols with name == godName
     */
    public static CLIGodSymbols searchGodSymbol(String godName) throws NotFoundException {
        CLIGodSymbols godFind = null;

        for ( CLIGodSymbols cligodSymbols : CLIGodSymbols.values()) {
            if ( cligodSymbols.getName().equals(godName)) {
                godFind = cligodSymbols;
            }
        }
        if ( godFind == null) {
            throw new NotFoundException();
        }

        return godFind;
    }
}
