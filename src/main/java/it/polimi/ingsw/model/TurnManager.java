package it.polimi.ingsw.model;

import java.util.List;

/**
 * This class models the concept of a turn flow manager.
 *
 * It implements a State Pattern in which the game flow
 * is controlled by specific subclasses for every particular
 * moments of the game.
 */
public abstract class TurnManager {
    protected Card card;
    //protected int movesLeft; // todo: check if implementation by calculation is possible
    protected List<TurnObserver> observers;
    protected boolean moveAllowed;

    public abstract boolean handle(Move move, Worker worker) throws WinException,LoseException,RunOutMovesException,BuildBeforeMoveException;
    public abstract int getMovesLeft();

    public void notifyObservers(Move move, Worker worker) {
        for(TurnObserver obs : observers) {
            try {
                obs.check(move, worker);
            }
            catch(DeniedMoveException ex) {
                moveAllowed = false; // todo: to renew with "true" value every Turn starting
            }
        }
    }

    public void registerObservers(TurnObserver observer) {
        observers.add(observer);
    }

    public void unregisterObservers(TurnObserver observer) {
        observers.remove(observer);
    }
}
