package it.polimi.ingsw.model.persistence.players;

import java.io.Serializable;

/**
 * Bean class to enable serialization/deserialization of a Card's information
 * by JSON files, and to encapsulate the actual state of a Card at a certain point.
 *
 * @author AndreaAltomare
 */
public class CardData implements Serializable {
    /* Relevant information for a Player's turn */
    private String name;
    private String epithet;
    private String description;
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

    public String getEpithet() {
        return epithet;
    }

    public void setEpithet(String epithet) {
        this.epithet = epithet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
