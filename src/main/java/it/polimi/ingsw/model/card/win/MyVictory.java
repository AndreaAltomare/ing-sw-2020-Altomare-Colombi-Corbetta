package it.polimi.ingsw.model.card.win;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.GodPower;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.worker.Worker;

import java.util.List;

/**
 * Class representing a Player's Victory rule set and providing
 * all the operations it needs to evaluate if a Win occurs.
 *
 * @author AndreaAltomare
 */
public class MyVictory {
    private GodPower godPower; // state of chosen God's power
    private Card parentCard;
    private List<WinChecker> checkers;

    /**
     * Constructs a MyVictory manager.
     *
     * @param parentCard Associated Card
     * @param godPower Associated God's power
     * @param checkers Checkers for moves (to determine if there is a win condition)
     */
    public MyVictory(Card parentCard, GodPower godPower, List<WinChecker> checkers) {
        this.parentCard = parentCard;
        this.godPower = godPower;
        this.checkers = checkers;
        //this.lastMove = null;
    }

    /**
     * This method check for a Win condition by the move provided.
     *
     * @param move (Move by which check for a Win)
     * @param worker (Worker who has executed the move)
     * @return (There is a Win condition ? true : false)
     */
    public boolean checkMove(Move move, Worker worker) {
        for(WinChecker checker : checkers) {
            if(checker.checkWin(move, worker))
                return true;
        }
        return false;
    }


    /**
     *
     * @return The bean class which provides the God's power.
     */
    public GodPower getGodPower() {
        /* This is the only possible way to both work with this bean class
         * and to encapsulate the state of the chosen God Cards power
         */
        return godPower;
    }
}
