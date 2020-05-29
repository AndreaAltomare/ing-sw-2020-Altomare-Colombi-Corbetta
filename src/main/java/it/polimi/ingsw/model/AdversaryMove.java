package it.polimi.ingsw.model;

/**
 * Class representing a Player's Adversary Movement Observer and providing
 * all the operations it needs to evaluate an Opponent's Movement move correctness.
 *
 * @author AndreaAltomare
 */
public class AdversaryMove {
    private Cell startingPosition; // once the turn starts, Worker's starting position is saved
    //private Move lastMove; // opponent's last move
    private GodPower godPower; // state of chosen God's power
    private Card parentCard;

    public AdversaryMove(Card parentCard, GodPower godPower) {
        this.parentCard = parentCard;
        this.godPower = godPower; // TODO: maybe refactor this to be only on Card class, so to remove duplicated code
        this.startingPosition = null;
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
     */
    public boolean checkMove(Move move, Worker worker) throws LoseException {
        boolean moveAllowed = true;

        /* move can be denied only if the God's power has to be applied to opponent's move */
        if(godPower.isActiveOnOpponentMovement())
            moveAllowed = checkSpecialRules(move, worker);

        return moveAllowed;
    }

    /**
     * This method is called when a Card's power modifies basic game rules.
     *
     * @param move (Opponent's move to check)
     * @param worker (Opponent's Worker who has executed the move)
     * @return (Opponent move is allowed ? true : false)
     * @throws LoseException (Exception handled by Controller)
     */
    private boolean checkSpecialRules(Move move, Worker worker) throws LoseException {
        Move myLastMove = parentCard.getMyMove().getLastMove();

        /* check if my last move was one of the Hot Last Moves checked by my God's power:
        * in this case, check the opponent's move
        */
        // todo: athena (just to check, REMOVE THIS COMMENT)
        if(myLastMove != null && myLastMove.getLevelDirection() == godPower.getHotLastMoveDirection())
            if(move.getLevelDirection() == godPower.getOpponentDeniedDirection()) {
                if(godPower.isMustObey()) {
                    // if the God's power must be obeyed, and it's not, trigger a Lose Condition
                    throw new LoseException(worker.getOwner(),"Player " + worker.getOwner().getNickname() + "has lost! (By not respecting Opponent Card's power)");
                }
                else {
                    return false;
                }
            }

        return true; // everything ok
    }

    public Card getParentCard() {
        return parentCard;
    }
}
