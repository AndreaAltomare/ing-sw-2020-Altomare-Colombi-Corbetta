package it.polimi.ingsw.model.move;

/**
 * Enumeration representing possible Cardinal directions
 * when performing a Move.
 *
 * @author AndreaAltomare
 */
public enum FloorDirection {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    NORTH_EAST,
    SOUTH_EAST,
    NORTH_WEST,
    SOUTH_WEST,
    SAME, // tells when a Worker is supposed to be forced into another Player's worker space gone vacated
    NONE, // tells if no specific floor direction is privileged
    ANY // all floor directions
}
