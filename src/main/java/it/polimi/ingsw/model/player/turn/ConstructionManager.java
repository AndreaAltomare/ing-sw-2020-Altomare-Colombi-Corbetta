package it.polimi.ingsw.model.player.turn;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.move.BuildMove;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.worker.Worker;

import java.util.ArrayList;

/**
 * Concrete class for the Construction state of the FSM
 * modelling the Turn flow.
 *
 * @author AndreaAltomare
 */
public class ConstructionManager extends TurnManager {

    public ConstructionManager(Card card) {
        this.card = card;
        observers = new ArrayList<>();
        moveAllowed = true;
        state = StateType.CONSTRUCTION;
    }

    /**
     * Overridden method to handle a Construction move by a Player's Worker.
     *
     * @param move (Move to handle)
     * @param worker (Worker who made the move)
     * @return (Move was actually executed ? true : false)
     * @throws LoseException (Exception handled by Controller)
     * @throws RunOutMovesException (Exception handled by Controller)
     * @throws BuildBeforeMoveException (Exception handled by Controller)
     * @throws WrongWorkerException (Exception handled by Controller)
     * @throws TurnOverException (Exception handled by Controller)
     */
    @Override
    public boolean handle(Move move, Worker worker) throws LoseException,RunOutMovesException,BuildBeforeMoveException,WrongWorkerException,TurnOverException {
        if(!worker.isChosen())
            throw new WrongWorkerException();

        return build(move, worker);
    }

    /**
     *
     * @return Construction moves left
     */
    @Override
    public int getMovesLeft() {
        return card.getMyConstruction().getConstructionLeft();
    }

    /**
     * Private method called when handling a Move provided by a Player
     * through the Turn's interface.
     *
     * @param move (Move to be executed)
     * @param worker (Worker by which execute the move)
     * @return (Move was actually executed ? true : false)
     * @throws LoseException (Exception handled by Controller)
     * @throws RunOutMovesException (Exception handled by Controller)
     * @throws BuildBeforeMoveException (Exception handled by Controller)
     * @throws TurnOverException (Exception handled by Controller)
     */
    private boolean build(Move move, Worker worker) throws LoseException,RunOutMovesException,BuildBeforeMoveException,TurnOverException {
        moveAllowed = true;

        if(this.getMovesLeft() < 1)
            throw new RunOutMovesException(StateType.CONSTRUCTION);

        /* Check if the Player has already made a Movement.
        * If not: check if the Player can make a Build before a Movement.
        *     If not: throw an Exception
        *
        * If the Player can make a Build before a movement, but he/she have already
        * made one, the Player must now perform a Movement.
        */
        if(!card.hasExecutedMovement()) {
            if (!card.getGodPower().isBuildBeforeMovement())
                throw new BuildBeforeMoveException();
            else if(card.getGodPower().isBuildBeforeMovement() && card.hasExecutedConstruction())
                throw new BuildBeforeMoveException("Player has already made a Construction! Need to perform a Movement now.");
        }

        /* 1- Check if my Card allow this move */ // TODO: qui andrebbe messo un try-catch per gestire la seguente situazione: un player che ha già fatto un movimento, switcha al turno CONSTRUCTION ma fa comunque una MOVEMENT. Il cast dovrebbe non essere consentito e quindi dovrebbe lanciare eccezione a runtime.
        moveAllowed = card.getMyConstruction().checkMove((BuildMove)move, worker);

        /* 2- Check if my opponent's Card allow this move */
        if(moveAllowed)
            notifyObservers(move, worker); // if the move is denied by my opponents, moveAllowed is changed to false todo: for Construction movement, there shouldn't be any observer (maybe for the Advenced Gods, let's wait) (just to check, REMOVE THIS COMMENT)

        /* 3- Execute this move */
        if(moveAllowed) {
            try {
                moveAllowed = card.getMyConstruction().executeMove((BuildMove)move, worker);
            }
            catch(OutOfBoardException ex) {
                moveAllowed = false;
            }
        }

        /* Once the move is executed, decrease the number of Constructions left */
        if(moveAllowed) {
            card.setConstructionExecuted(true);
            card.getMyConstruction().decreaseConstructionLeft();

            if(card.hasExecutedMovement()) {
                card.setTurnCompleted(true);

                if(card.getGodPower().isBuildBeforeMovement())
                    card.getMyConstruction().decreaseConstructionLeft();
            }
        }

        /* If Construction Moves are over, trigger an Exception to switch the Player */
        if(card.getMyConstruction().getConstructionLeft() < 1)
            throw new TurnOverException(); // TODO: check if the Exception to switch the Player works fine (just to check, REMOVE THIS COMMENT)

        return moveAllowed;
    }
}
