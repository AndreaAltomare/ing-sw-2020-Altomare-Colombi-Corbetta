package it.polimi.ingsw.model;

/**
 * Class representing game Cards with their powers.
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
    private boolean movementExecuted; // tells if at least one movement was executed
    private boolean constructionExecuted; // tells if at least one construction was executed
    private boolean turnCompleted; // tells if at least a regular turn cycle is completed

    public Card(GodPower godPower) {
        this.godPower = godPower;
        this.name = godPower.getName();
        this.epithet = godPower.getEpithet();
        this.description = godPower.getDescription();
        this.myMove = new MyMove(this, godPower);
        this.myConstruction = new MyConstruction(this, godPower);
        this.myVictory = new MyVictory(this, godPower);
        this.adversaryMove = new AdversaryMove(this, godPower);
        this.movementExecuted = false;
        this.constructionExecuted = false;
        this.turnCompleted = false;
    }

    /**
     * This method reset conditions when a new Turn starts
     */
    public void resetForStart() {
        /* Reset the number of Moves Left */
        myMove.resetMovesLeft();
        myConstruction.resetConstructionLeft();

        /* Reset Movement and Construction executed */
        movementExecuted = false;
        constructionExecuted = false;
        turnCompleted = false;
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

    public AdversaryMove getAdversaryMove() {
        return adversaryMove;
    }

    public boolean hasExecutedMovement() {
        return movementExecuted;
    }

    public void setMovementExecuted(boolean movementExecuted) {
        this.movementExecuted = movementExecuted;
    }

    public boolean hasExecutedConstruction() {
        return constructionExecuted;
    }

    public void setConstructionExecuted(boolean constructionExecuted) {
        this.constructionExecuted = constructionExecuted;
    }

    public boolean isTurnCompleted() {
        return turnCompleted;
    }

    public void setTurnCompleted(boolean turnCompleted) {
        this.turnCompleted = turnCompleted;
    }

    public GodPower getGodPower() {
        return godPower;
    }

    public String getName() {
        return name;
    }
}
