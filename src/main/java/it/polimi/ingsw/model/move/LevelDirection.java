package it.polimi.ingsw.model.move;

/**
 * Enumeration representing possible Level directions
 * when performing a Move.
 *
 * "Level direction" means Up, Down, or neither
 * (stay on the Same level).
 *
 * @author AndreaAltomare
 */
public enum LevelDirection {
    UP,
    DOWN,
    SAME,
    NONE, // tells if no specific level direction is privileged
    ANY // all level directions
}
