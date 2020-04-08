package it.polimi.ingsw.model;

public class Card {
    private String name;
    private String description;
    private MyMove myMove;
    private MyConstruction myConstruction;

    public MyConstruction getMyConstruction() {
        return myConstruction;
    }
}
