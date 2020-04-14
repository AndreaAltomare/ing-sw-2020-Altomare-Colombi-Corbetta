package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * Concrete class for the Movement state of the FSM
 * modelling the Turn flow.
 *
 * @author AndreaAltomare
 */
public class MovementManager extends TurnManager {

    public MovementManager(Card card) {
        this.card = card;
        observers = new ArrayList<>();
        moveAllowed = true;
        state = StateType.MOVEMENT;
    }

    /**
     * Overridden method to handle a Movement move by a Player's Worker.
     *
     * @param move (Move to handle)
     * @param worker (Worker who made the move)
     * @return (Move was actually executed ? true : false)
     * @throws WinException (Exception handled by Controller)
     * @throws LoseException (Exception handled by Controller)
     * @throws RunOutMovesException (Exception handled by Controller)
     * @throws WrongWorkerException (Exception handled by Controller)
     */
    @Override
    public boolean handle(Move move, Worker worker) throws WinException,LoseException,RunOutMovesException,WrongWorkerException {
        if(!worker.isChosen())
            throw new WrongWorkerException();

        return moveWorker(move, worker);
    }

    /**
     *
     * @return Movement moves left
     */
    @Override
    public int getMovesLeft() {
        return card.getMyMove().getMovesLeft();
    }

    /**
     * Private method called when handling a Move provided by a Player
     * through the Turn's interface.
     *
     * @param move (Move to be executed)
     * @param worker (Worker by which execute the move)
     * @return (Move was actually executed ? true : false)
     * @throws WinException (Exception handled by Controller)
     * @throws LoseException (Exception handled by Controller)
     * @throws RunOutMovesException (Exception handled by Controller)
     */
    private boolean moveWorker(Move move, Worker worker) throws WinException,LoseException,RunOutMovesException {
        moveAllowed = true;

        if(this.getMovesLeft() < 1)
            throw new RunOutMovesException(StateType.MOVEMENT);

        /* 1- Check if my Card allow this move */
        moveAllowed = card.getMyMove().checkMove(move,worker);

        /* 2- Check if my opponent's Card allow this move */
        if(moveAllowed)
            notifyObservers(move, worker); // if the move is denied by my opponents, moveAllowed is changed to false


        /* 3- Execute this move */
        if(moveAllowed) {
            try {
                card.getMyMove().executeMove(move, worker);
            }
            catch(OutOfBoardException ex) {
                moveAllowed = false;
            }
        }

        /* Once the move is executed, decrease the number of Moves left */
        if(moveAllowed) {
            card.setMovementExecuted(true);
            card.getMyMove().decreaseMovesLeft();
        }

        return moveAllowed;
    }
}
