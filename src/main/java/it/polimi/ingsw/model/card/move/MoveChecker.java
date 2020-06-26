package it.polimi.ingsw.model.card.move;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.worker.Worker;

/**
 * Functional interface.
 * Provides an operation for checking a Movement move.
 *
 * @author AndreaAltomare
 */
public interface MoveChecker {

    /**
     * Checks a Movement move.
     *
     * @param move Move to check
     * @param worker Worker which made the move
     * @param startingPosition Initial position (Cell) of the Worker which made the move
     * @param movesLeft Number of Movement-type moves left
     * @param parentCard Card providing the effect by which check the move
     * @return True if the move is legal
     */
    boolean checkMove(Move move, Worker worker, Cell startingPosition, int movesLeft, Card parentCard);
}
