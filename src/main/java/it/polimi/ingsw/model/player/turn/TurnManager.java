package it.polimi.ingsw.model.player.turn;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.turn.observers.TurnObserver;
import it.polimi.ingsw.model.player.worker.Worker;

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

    /**
     * Handles a move made within the actual Turn
     * (represented by a {@code TurnManager} subclass).
     *
     * @param move Move to handle
     * @param worker Worker which made the move
     * @return True if the move was actually executed and thus correctly handled
     * @throws WinException Exception thrown when a Player wins after executing the move
     * @throws LoseException Exception thrown when a Player loses after executing the move
     * @throws RunOutMovesException Exception thrown when the move is not executed because the Player run out of moves
     * @throws BuildBeforeMoveException Exception thrown when the move is not executed because the Player tried to illegally make a Construction before a Movement
     * @throws WrongWorkerException Exception thrown when the move is not executed because the Player made the move with a different Worker than the last one chosen
     * @throws TurnOverException Exception thrown when a Player's Turn is over after the move is executed
     * @throws TurnSwitchedException Exception thrown when a Player's Sub-Turn (Movement / Construction) is over after the move is executed
     */
    public abstract boolean handle(Move move, Worker worker) throws WinException, LoseException, RunOutMovesException, BuildBeforeMoveException,WrongWorkerException, TurnOverException,TurnSwitchedException;

    /**
     * Gets the number of moves left for the actual Turn
     * (represented by a {@code TurnManager} subclass).
     *
     * @return Actual Turn moves left
     */
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
