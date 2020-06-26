package it.polimi.ingsw.model.card.move;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import it.polimi.ingsw.model.exceptions.WinException;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.worker.Worker;

/**
 * Functional interface.
 * Provides an operation for executing a Movement move.
 *
 * @author AndreaAltomare
 */
public interface MoveExecutor {

    /**
     * Executes a Movement move.
     *
     * @param move Move to execute
     * @param worker Worker which made the move
     * @param parentCard Card providing the effect by which execute the move
     * @return True if the move was actually executed
     * @throws OutOfBoardException Exception thrown when trying to move out of the bounds of the Board
     * @throws WinException Exception thrown when the Player wins after the move was made
     */
    boolean executeMove(Move move, Worker worker, Card parentCard) throws OutOfBoardException, WinException;
}
