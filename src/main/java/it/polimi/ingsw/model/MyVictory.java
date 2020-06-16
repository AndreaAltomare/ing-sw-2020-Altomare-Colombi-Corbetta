package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.win.WinChecker;

import java.util.List;

/**
 * Class representing a Player's Victory rule set and providing
 * all the operations it needs to evaluate if a Win occurs.
 *
 * @author AndreaAltomare
 */
public class MyVictory {
    // TODO: Delete unused methods
    private GodPower godPower; // state of chosen God's power
    private Card parentCard;
    private final int panLevelDepth = -2;
    private List<WinChecker> checkers;

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

//        boolean isVictory = false;
//
//        if(!godPower.isNewVictoryCondition())
//            isVictory = checkDefaultRules(move, worker);
//        else
//            isVictory = checkSpecialRules(move, worker);
//
//        return isVictory;
    }

    /**
     * This method is called when a Card's power modifies basic game rules.
     *
     * @param move (Move by which check for a Win)
     * @param worker (Worker who has executed the move)
     * @return (There is a Win condition ? true : false)
     */
    private boolean checkSpecialRules(Move move, Worker worker) {
        boolean isVictory = false;

        /* check for new Victory condition first */
        if(move.getLevelDirection() == godPower.getHotLastMoveDirection())
            // todo: pan (just to check, REMOVE THIS COMMENT)
            if(move.levelDepth <= godPower.getHotLevelDepth())
                return true;

        /* check for a default Victory otherwise */
        isVictory = checkDefaultRules(move, worker);

        return isVictory;
    }

    /**
     * This method is called when only basic rules apply.
     *
     * @param move (Move by which check for a Win)
     * @param worker (Worker who has executed the move)
     * @return (There is a Win condition ? true : false)
     */
    private boolean checkDefaultRules(Move move, Worker worker) {

        /* Default rules: a player win if and only if its Worker moves up on top of level 3 */
        /* check if the Worker was on a lower level before the move (by checking its last move was on UP Direction) */
        if(move.getLevelDirection() != LevelDirection.UP)
            return false;

        /* check if it is now onto a level 3 position */
        if(worker.position().getLevel() != 3)
            return false;

        return true; // it's a win
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
