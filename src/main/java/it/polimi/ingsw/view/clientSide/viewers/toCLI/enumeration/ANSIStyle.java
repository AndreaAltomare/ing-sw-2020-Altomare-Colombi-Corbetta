package it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration;

/**
 * An Enumeration which has elements with a <code>String</code> to represent the ANSI escape
 *
 * @author Marco
 */
public enum ANSIStyle {


    //Char Style
    UNDERSCORE("\u001B[4m"),
    REVERSE("\u001B[7m"),

    //Back Colors
    BACK_RED("\u001B[41m"),
    BACK_GREEN("\u001B[42m"),
    BACK_YELLOW("\u001B[43m"),
    BACK_BLUE("\u001B[44m"),
    BACK_PURPLE("\u001B[45m"),
    BACK_DIFFERENT_BLUE("\u001B[46m"),
    BACK_GREY("\u001B[47m"),

    //Colors
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    DIFFERENT_BLUE("\u001B[36m"),
    GREY("\u001B[37m");

    public static final String RESET = "\u001B[0m";

    private String escape;

    ANSIStyle( String escape) {
        this.escape = escape;
    }

    /**
     * Returns the <code>String</code> of enumeration
     * @return <code>String</code> of ANSI escape
     */
    public String getEscape() {
        return escape;
    }
}
