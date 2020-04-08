package it.polimi.ingsw.model;

public class Card {
    private String name;
    private String description;
    private MyMove myMove;
    private MyConstruction myConstruction;
    private MyVictory myVictory;

    public MyMove getMyMove() {
        return myMove;
    }

    public MyConstruction getMyConstruction() {
        return myConstruction;
    }

    public MyVictory getMyVictory() {
        return myVictory;
    }
}
