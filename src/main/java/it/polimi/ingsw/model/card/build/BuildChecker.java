package it.polimi.ingsw.model.card.build;

import it.polimi.ingsw.model.move.BuildMove;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.player.worker.Worker;

/**
 * Functional interface.
 * Provides an operation for checking a Construction move.
 *
 * @author AndreaAltomare
 */
public interface BuildChecker {

    /**
     * Checks a Construction move.
     *
     * @param move Move to check
     * @param worker Worker which made the move
     * @param lastMove Last Construction move made by the Player
     * @param constructionLeft Number of Construction-type moves left
     * @param parentCard Card providing the effect by which check the move
     * @return True if the move is legal
     */
    boolean checkBuild(BuildMove move, Worker worker, BuildMove lastMove, int constructionLeft, Card parentCard);
}
