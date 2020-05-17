package it.polimi.ingsw.model;

/**
 * Enumeration type for possible outcome
 * of a Player's move.
 *
 * @author AndreaAltomare
 */
public enum MoveOutcomeType {
    EXECUTED,
    NOT_EXECUTED,
    OUT_OF_BOARD,
    RUN_OUT_OF_MOVES,
    BUILD_BEFORE_MOVE,
    WRONG_WORKER,
    TURN_OVER,
    WIN,
    LOSS,
    NONE, // undefined [control value]
    ANY // any possible outcome [control value]
}
