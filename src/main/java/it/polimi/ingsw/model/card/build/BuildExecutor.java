package it.polimi.ingsw.model.card.build;

import it.polimi.ingsw.model.move.BuildMove;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import it.polimi.ingsw.model.player.worker.Worker;

/**
 * Functional interface.
 * Provides an operation for executing a Construction move.
 *
 * @author AndreaAltomare
 */
public interface BuildExecutor {

    /**
     * Executes a Construction move.
     *
     * @param move Move to execute
     * @param worker Worker which made the move
     * @return True if the move was actually executed
     * @throws OutOfBoardException Exception thrown when trying to build out of the bounds of the Board
     */
    boolean executeBuild(BuildMove move, Worker worker) throws OutOfBoardException;
}
