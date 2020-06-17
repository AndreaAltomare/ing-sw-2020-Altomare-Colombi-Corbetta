package it.polimi.ingsw.model.persistence.players;

import java.io.Serializable;

public class CardData implements Serializable {
    /* Relevant information for a Player's turn */
    private String name;
    private MovementData movement;
    private ConstructionData construction;
    private boolean movementExecuted;
    private boolean constructionExecuted;
    private boolean turnCompleted;

    /* Default Constructor */
    public CardData() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MovementData getMovement() {
        return movement;
    }

    public void setMovement(MovementData movement) {
        this.movement = movement;
    }

    public ConstructionData getConstruction() {
        return construction;
    }

    public void setConstruction(ConstructionData construction) {
        this.construction = construction;
    }

    public boolean isMovementExecuted() {
        return movementExecuted;
    }

    public void setMovementExecuted(boolean movementExecuted) {
        this.movementExecuted = movementExecuted;
    }

    public boolean isConstructionExecuted() {
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
}
