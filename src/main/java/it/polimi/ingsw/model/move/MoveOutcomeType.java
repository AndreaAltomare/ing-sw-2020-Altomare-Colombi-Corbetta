package it.polimi.ingsw.model.move;

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
    TURN_SWITCHED,
    TURN_OVER,
    WIN,
    LOSS,
    OPPONENT_WORKER_MOVED,
    NONE, // undefined [control value]
    ANY // any possible outcome [control value]
}
