package it.polimi.ingsw.model;

public enum Color {
    BLUE(java.awt.Color.BLUE),
    GREY(java.awt.Color.GRAY),
    BROWN(new java.awt.Color(153, 102,  0));
    public final java.awt.Color color;

    Color(java.awt.Color color1){
        color = color1;
    }
}
