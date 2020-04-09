package it.polimi.ingsw.model;

import java.util.ArrayList;

public class MovementManager extends TurnManager {

    public MovementManager(Card card) {
        this.card = card;
        observers = new ArrayList<>();
        //this.movesLeft = initialMoves; // MOVEMENT moves left todo: maybe to remove
        moveAllowed = true;
    }

    @Override
    public boolean handle(Move move, Worker worker) throws WinException,LoseException {
        return moveWorker(move, worker);
    }

    @Override
    public int getMovesLeft() {
        return card.getMyMove().getMovesLeft();
    }

    private boolean moveWorker(Move move, Worker worker) throws WinException,LoseException {
        moveAllowed = true;
        // TODO: add statements to make a Worker move
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
        if(moveAllowed)
            card.getMyMove().decreaseMovesLeft();

        return moveAllowed;
    }
}
