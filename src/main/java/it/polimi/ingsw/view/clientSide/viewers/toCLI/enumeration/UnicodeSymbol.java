package it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration;

public enum UnicodeSymbol {

    //Signal
    CHECK_MARK("\u2714"),
    X_MARK("\u2716"),

    PENCIL("\u270E"),
    HAMMER("\u2692"),

    //blocks
    HIGH_BLOCK_1("\u2581"),
    HIGH_BLOCK_2("\u2582"),
    HIGH_BLOCK_3("\u2583"),
    HIGH_BLOCK_4("\u2584"),
    HIGH_BLOCK_5("\u2585"),
    HIGH_BLOCK_6("\u2586"),
    HIGH_BLOCK_7("\u2587"),
    HIGH_BLOCK_8("\u2588"),
    SCALE_RIGHT("\u2599"),
    SCALE_LEFT("\u259F");

    private String escape;

    UnicodeSymbol( String escape) {
        this.escape = escape;
    }

    public String getEscape() {
        return escape;
    }
}
