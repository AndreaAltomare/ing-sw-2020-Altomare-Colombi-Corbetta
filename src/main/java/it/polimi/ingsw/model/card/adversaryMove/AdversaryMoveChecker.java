package it.polimi.ingsw.model.card.adversaryMove;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.exceptions.LoseException;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.worker.Worker;

/**
 * Functional interface.
 * Provides an operation for checking an Adversary's Worker Movement.
 *
 * @author AndreaAltomare
 */
public interface AdversaryMoveChecker {

    /**
     * Checks the Movement of an Adversary's Worker.
     *
     * @param move Move to check
     * @param worker Worker which made the move
     * @param parentCard Card providing the effect by which check the move
     * @return True if the move is legal
     * @throws LoseException Exception thrown when a Player loses
     */
    boolean checkMove(Move move, Worker worker, Card parentCard) throws LoseException;
}
