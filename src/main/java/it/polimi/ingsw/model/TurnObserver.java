package it.polimi.ingsw.model;

/**
 * This class models the observer for a movement made by
 * a Player during its turn.
 *
 * It implements an Observer Pattern in which the subclasses
 * are required to notify other players when a specific
 * movement through the turn flow is made.
 *
 * @author AndreaAltomare
 */
public abstract class TurnObserver {

    public abstract void check(Move move, Worker worker) throws DeniedMoveException;
}
