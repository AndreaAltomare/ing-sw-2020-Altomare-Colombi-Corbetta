package it.polimi.ingsw.model;

import java.util.List;

/**
 * This class models the concept of a turn flow manager.
 *
 * It implements a State Pattern in which the game flow
 * is controlled by specific subclasses for every particular
 * moments of the game.
 *
 * @author AndreaAltomare
 */
public abstract class TurnManager {
    protected Card card;
    protected List<TurnObserver> observers;
    protected boolean moveAllowed;
    protected StateType state;

    public abstract boolean handle(Move move, Worker worker) throws WinException,LoseException,RunOutMovesException,BuildBeforeMoveException,WrongWorkerException,TurnOverException,TurnSwitchedException;
    public abstract int getMovesLeft();

    /**
     * Notify Observers when a Move is performed.
     *
     * @param move (Move performed)
     * @param worker (Worker who has performed the Move)
     * @throws LoseException (Exception handled by Controller)
     */
    public void notifyObservers(Move move, Worker worker) throws LoseException {
        for(TurnObserver obs : observers) {
            try {
                obs.check(move, worker);
            }
            catch(DeniedMoveException ex) {
                moveAllowed = false;
            }
        }
    }

    /**
     * Register an Observer for a specific Turn state.
     *
     * @param observer (Observer to be registered)
     */
    public void registerObservers(TurnObserver observer) {
        observers.add(observer);
    }

    /**
     * Unregister an Observer for a specific Turn state.
     *
     * @param observer (Observer to be unregistered)
     */
    public void unregisterObservers(TurnObserver observer) {
        observers.remove(observer);
    }

    /**
     *
     * @return The State Type of the actual state for this Turn
     */
    public StateType state() {
        return state;
    }
}
