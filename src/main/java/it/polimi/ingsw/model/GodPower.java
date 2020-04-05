package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * Bean class to enable serialization/deserialization of Card's power
 * by JSON files
 */
public class GodPower implements Serializable {
    /* General */
    Integer movementsLeft; // some workers can do more than one move at turn
    Integer constructionLeft; // some workers can make more than one construction at turn
    boolean mustObey; // tells if a God's power must be obeyed or not
    boolean sameSpaceDenied; // when a worker make an additional move, it is denied on the same space where last move occurred

    /* Active on my movement */
    LevelDirection hotLastMoveDirection; // tells if the power is active on my worker's last move, and what move it is
    boolean moveIntoOpponentSpace; // tells if a worker can move into an opponent worker's space
    FloorDirection forceOpponentInto; // tells what Cell an opponent's worker should be moved into
    LevelDirection deniedDirection; // tells what direction is denied to move into, if the hotLastMoveDirection occurred

    /* active on opponents movement */
    LevelDirection opponentDeniedDirection; // tells what directions are denied an Opponent's Worker to move into

    /* active on my construction */
    boolean domeAtAnyLevel; // tells if a worker can build a Dome at any level (not just on top of a tower)
    boolean forceConstructionOnSameSpace; // tells if the worker's additional move must occur on the same space of the last one

    /* Active to my Victory */
    boolean newVictoryCondition; // tells if there is a new win condition when an "hotLastMoveDirection" occur

    /* Active to opponents Victory */
    // in case of other power will be added in future

    /* Default Constructor */
    public GodPower() {}

    /* Default Getter and Setter methods */
    public Integer getMovementsLeft() {
        return movementsLeft;
    }

    public void setMovementsLeft(Integer movementsLeft) {
        this.movementsLeft = movementsLeft;
    }

    public Integer getConstructionLeft() {
        return constructionLeft;
    }

    public void setConstructionLeft(Integer constructionLeft) {
        this.constructionLeft = constructionLeft;
    }

    public boolean isMustObey() {
        return mustObey;
    }

    public void setMustObey(boolean mustObey) {
        this.mustObey = mustObey;
    }

    public boolean isSameSpaceDenied() {
        return sameSpaceDenied;
    }

    public void setSameSpaceDenied(boolean sameSpaceDenied) {
        this.sameSpaceDenied = sameSpaceDenied;
    }

    public LevelDirection getHotLastMoveDirection() {
        return hotLastMoveDirection;
    }

    public void setHotLastMoveDirection(LevelDirection hotLastMoveDirection) {
        this.hotLastMoveDirection = hotLastMoveDirection;
    }

    public boolean isMoveIntoOpponentSpace() {
        return moveIntoOpponentSpace;
    }

    public void setMoveIntoOpponentSpace(boolean moveIntoOpponentSpace) {
        this.moveIntoOpponentSpace = moveIntoOpponentSpace;
    }

    public FloorDirection getForceOpponentInto() {
        return forceOpponentInto;
    }

    public void setForceOpponentInto(FloorDirection forceOpponentInto) {
        this.forceOpponentInto = forceOpponentInto;
    }

    public LevelDirection getDeniedDirection() {
        return deniedDirection;
    }

    public void setDeniedDirection(LevelDirection deniedDirection) {
        this.deniedDirection = deniedDirection;
    }

    public LevelDirection isOpponentDeniedDirection() {
        return opponentDeniedDirection;
    }

    public void setOpponentDeniedDirection(LevelDirection opponentDeniedDirection) {
        this.opponentDeniedDirection = opponentDeniedDirection;
    }

    public boolean isDomeAtAnyLevel() {
        return domeAtAnyLevel;
    }

    public void setDomeAtAnyLevel(boolean domeAtAnyLevel) {
        this.domeAtAnyLevel = domeAtAnyLevel;
    }

    public boolean isForceConstructionOnSameSpace() {
        return forceConstructionOnSameSpace;
    }

    public void setForceConstructionOnSameSpace(boolean forceConstructionOnSameSpace) {
        this.forceConstructionOnSameSpace = forceConstructionOnSameSpace;
    }

    public boolean isNewVictoryCondition() {
        return newVictoryCondition;
    }

    public void setNewVictoryCondition(boolean newVictoryCondition) {
        this.newVictoryCondition = newVictoryCondition;
    }
}
