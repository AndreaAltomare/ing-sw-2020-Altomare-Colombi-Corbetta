package it.polimi.ingsw.model;

public enum FloorDirection {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    NORTH_EAST,
    SOUTH_EAST,
    NORTH_WEST,
    SOUTH_WEST,
    SAME, // tels when a Worker is supposed to be forced into another Player's worker space gone vacated
    NONE, // tells if no specific floor direction is privileged
    ANY // all floor directions
}
