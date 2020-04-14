package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * Bean class to enable serialization/deserialization of Card's power
 * by JSON files, and to encapsulate the Card's power properties.
 *
 * @author AndreaAltomare
 */
public class GodPower implements Serializable {
    /* General */
    private String name; // God's name
    private String epithet; // God's epithet
    private String description; // God's power description

    private Integer movementsLeft; // some workers can do more than one move at turn
    private Integer constructionLeft; // some workers can make more than one construction at turn
    private boolean mustObey; // tells if a God's power must be obeyed or not
    private boolean startingSpaceDenied; // when a worker make an additional move, it is denied on the same space where the Worker started its turn
    private boolean sameSpaceDenied; // when a worker make an additional move, it is denied on the same space where last move occurred

    /* Active on my movement */
    private boolean activeOnMyMovement; // tells if this power is active on my movement
    private LevelDirection hotLastMoveDirection; // tells if the power is active on my worker's last move, and what move it is
    private boolean moveIntoOpponentSpace; // tells if a worker can move into an opponent worker's space
    private FloorDirection forceOpponentInto; // tells what Cell an opponent's worker should be moved into
    private LevelDirection deniedDirection; // tells what direction is denied to move into, if the hotLastMoveDirection occurred

    /* active on opponents movement */
    private boolean activeOnOpponentMovement; // tells if this power is active on opponent's movement
    private LevelDirection opponentDeniedDirection; // tells what directions are denied an Opponent's Worker to move into

    /* active on my construction */
    private boolean activeOnMyConstruction; // tells if this power is active on my construction
    private boolean buildBeforeMovement; // tells if the Player can make a construction before a movement of its Workers
    private boolean domeAtAnyLevel; // tells if a worker can build a Dome at any level (not just on top of a tower)
    private boolean forceConstructionOnSameSpace; // tells if the worker's additional move must occur on the same space of the last one

    /* active on opponents construction */
    // in case of other power will be added in future

    /* Active to my Victory */
    private boolean newVictoryCondition; // tells if there is a new win condition when an "hotLastMoveDirection" occur

    /* Active to opponents Victory */
    // in case of other power will be added in future

    /* Default Constructor */
    public GodPower() {}

    /* Default Getter and Setter methods */
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

    public boolean isStartingSpaceDenied() {
        return startingSpaceDenied;
    }

    public void setStartingSpaceDenied(boolean startingSpaceDenied) {
        this.startingSpaceDenied = startingSpaceDenied;
    }

    public boolean isSameSpaceDenied() {
        return sameSpaceDenied;
    }

    public void setSameSpaceDenied(boolean sameSpaceDenied) {
        this.sameSpaceDenied = sameSpaceDenied;
    }

    public boolean isActiveOnMyMovement() {
        return activeOnMyMovement;
    }

    public void setActiveOnMyMovement(boolean activeOnMyMovement) {
        this.activeOnMyMovement = activeOnMyMovement;
    }

    public boolean isActiveOnOpponentMovement() {
        return activeOnOpponentMovement;
    }

    public void setActiveOnOpponentMovement(boolean activeOnOpponentMovement) {
        this.activeOnOpponentMovement = activeOnOpponentMovement;
    }

    public boolean isActiveOnMyConstruction() {
        return activeOnMyConstruction;
    }

    public void setActiveOnMyConstruction(boolean activeOnMyConstruction) {
        this.activeOnMyConstruction = activeOnMyConstruction;
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

    public LevelDirection getOpponentDeniedDirection() {
        return opponentDeniedDirection;
    }

    public void setOpponentDeniedDirection(LevelDirection opponentDeniedDirection) {
        this.opponentDeniedDirection = opponentDeniedDirection;
    }

    public boolean isBuildBeforeMovement() {
        return buildBeforeMovement;
    }

    public void setBuildBeforeMovement(boolean buildBeforeMovement) {
        this.buildBeforeMovement = buildBeforeMovement;
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
