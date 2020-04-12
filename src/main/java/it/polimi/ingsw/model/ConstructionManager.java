package it.polimi.ingsw.model;

import java.util.ArrayList;

public class ConstructionManager extends TurnManager {

    public ConstructionManager(Card card) {
        this.card = card;
        observers = new ArrayList<>();
        moveAllowed = true;
        state = StateType.CONSTRUCTION;
    }

    @Override
    public boolean handle(Move move, Worker worker) throws LoseException,RunOutMovesException,BuildBeforeMoveException,WrongWorkerException,TurnOverException {
        if(!worker.isChosen())
            throw new WrongWorkerException();

        return build(move, worker);
    }

    @Override
    public int getMovesLeft() {
        return card.getMyConstruction().getConstructionLeft();
    }

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

        /* 1- Check if my Card allow this move */
        moveAllowed = card.getMyConstruction().checkMove((BuildMove)move, worker);

        /* 2- Check if my opponent's Card allow this move */
        if(moveAllowed)
            notifyObservers(move, worker); // if the move is denied by my opponents, moveAllowed is changed to false todo: for Construction movement, there shouldn't be any observer (maybe for the Advenced Gods, let's wait) (just to check, REMOVE THIS COMMENT)

        /* 3- Execute this move */
        if(moveAllowed) {
            try {
                card.getMyConstruction().executeMove((BuildMove)move, worker);
            }
            catch(OutOfBoardException ex) {
                moveAllowed = false;
            }
        }

        /* Once the move is executed, decrease the number of Constructions left */
        if(moveAllowed) {
            card.setConstructionExecuted(true);
            card.getMyConstruction().decreaseConstructionLeft();
        }

        /* If Construction Moves are over, trigger an Exception to switch the Player */
        if(card.getMyConstruction().getConstructionLeft() < 1)
            throw new TurnOverException(); // TODO: check if the Exception to switch the Player works fine (just to check, REMOVE THIS COMMENT)

        return moveAllowed;
    }
}
