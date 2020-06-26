package it.polimi.ingsw.model.card.adversaryMove;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.GodPower;
import it.polimi.ingsw.model.exceptions.LoseException;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.worker.Worker;

import java.util.List;

/**
 * Class representing a Player's Adversary Movement Observer and providing
 * all the operations it needs to evaluate an Opponent's Movement move correctness.
 *
 * @author AndreaAltomare
 */
public class AdversaryMove {
    private GodPower godPower; // state of chosen God's power
    private Card parentCard;
    private List<AdversaryMoveChecker> checkers;
    //private Cell startingPosition; // once the turn starts, Worker's starting position is saved
    //private Move lastMove; // opponent's last move

    /**
     * Constructs an AdversaryMove manager.
     *
     * @param parentCard Associated Card
     * @param godPower Associated God's power
     * @param checkers Checkers for moves
     */
    public AdversaryMove(Card parentCard, GodPower godPower, List<AdversaryMoveChecker> checkers) {
        this.parentCard = parentCard;
        this.checkers = checkers;
        this.godPower = godPower;
        //this.startingPosition = null;
        //this.lastMove = null;
    }

    /**
     * This method check for the correctness of the Opponent's move provided.
     * If the Card's power does not modify the rule set, any Opponent's move
     * is allowed by default (by others Player point of view).
     *
     * @param move (Opponent's move to check)
     * @param worker (Opponent's Worker who has executed the move)
     * @return (Opponent move is allowed ? true : false)
     * @throws LoseException Exception thrown when a Player loses
     */
    public boolean checkMove(Move move, Worker worker) throws LoseException {
        for(AdversaryMoveChecker checker : checkers) {
            if(!checker.checkMove(move, worker, parentCard))
                return false;
        }
        return true;
    }

    public Card getParentCard() {
        return parentCard;
    }
}
