package it.polimi.ingsw.model;

/**
 * Class representing game Cards with their powers
 *
 * @author AndreaAltomare
 */
public class Card {
    private GodPower godPower; // effects of the card
    private String name;
    private String epithet;
    private String description;
    private MyMove myMove;
    private MyConstruction myConstruction;
    private MyVictory myVictory;
    private AdversaryMove adversaryMove;

    public Card(GodPower godPower) {
        this.godPower = godPower;
        this.name = godPower.getName();
        this.epithet = godPower.getEpithet();
        this.description = godPower.getDescription();
        this.myMove = new MyMove(this, godPower);
        this.myConstruction = new MyConstruction(this, godPower);
        this.myVictory = new MyVictory(this, godPower);
        this.adversaryMove = new AdversaryMove(this, godPower);
    }

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
