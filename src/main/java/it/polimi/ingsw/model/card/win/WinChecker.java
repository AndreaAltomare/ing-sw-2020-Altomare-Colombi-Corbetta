package it.polimi.ingsw.model.card.win;

import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.worker.Worker;

/**
 * Functional interface.
 * Provides an operation to check for a Win condition
 * after a move was executed.
 *
 * @author AndreaAltomare
 */
public interface WinChecker {

    /**
     * Checks for a Win condition.
     *
     * @param move Executed move
     * @param worker Worker which made the move
     * @return True if there is a Win condition
     */
    boolean checkWin(Move move, Worker worker);
}
