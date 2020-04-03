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
    protected int movesLeft;
    protected List<TurnObserver> observers;

    public abstract void handle();
    public abstract int getMovesLeft();

    public void notifyObservers() {
        for(TurnObserver obs : observers)
            obs.check();
    }

    public void registerObservers(TurnObserver observer) {
        observers.add(observer);
    }

    public void unregisterObservers(TurnObserver observer) {
        observers.remove(observer);
    }
}
