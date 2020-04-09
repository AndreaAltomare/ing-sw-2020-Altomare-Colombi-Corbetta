package it.polimi.ingsw.model;

import java.util.ArrayList;

public class ConstructionManager extends TurnManager {

    public ConstructionManager(Card card) {
        this.card = card;
        observers = new ArrayList<>();
        //this.movesLeft = initialMoves; // MOVEMENT moves left todo: maybe to remove
        moveAllowed = true;
    }

    @Override
    public boolean handle(Move move, Worker worker) throws RunOutMovesException,BuildBeforeMoveException {
        return build(move, worker);
    }

    @Override
    public int getMovesLeft() {
        return card.getMyConstruction().getConstructionLeft();
    }

    private boolean build(Move move, Worker worker) throws RunOutMovesException,BuildBeforeMoveException {
        moveAllowed = true;

        if(this.getMovesLeft() < 1)
            throw new RunOutMovesException(StateType.CONSTRUCTION);

        /* Check if the Player has already made a Movement.
        * If not: check if the Player can make a Build before a Movement.
        *     If not: throw an Exception
        */
        if(!card.hasExecutedMovement())
            if(!card.getGodPower().isBuildBeforeMovement())
                throw new BuildBeforeMoveException();

        // TODO: add statements to make a Construction
        /* 1- Check if my Card allow this move */
        moveAllowed = card.getMyConstruction().checkMove((BuildMove)move, worker);

        /* 2- Check if my opponent's Card allow this move */
        if(moveAllowed)
            notifyObservers(move, worker); // if the move is denied by my opponents, moveAllowed is changed to false todo: for Construciton movement, there shouldn't be any observer (just to check, REMOVE THIS COMMENT)

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
        if(moveAllowed)
            card.getMyConstruction().decreaseConstructionLeft();

        return moveAllowed;
    }
}
