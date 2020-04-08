package it.polimi.ingsw.model;

public class AdversaryMove {
    private Cell startingPosition; // once the turn starts, Worker's starting position is saved
    //private Move lastMove; // opponent's last move
    private GodPower godPower; // state of chosen God's power
    private Card parentCard;

    public AdversaryMove(Card parentCard, GodPower godPower) {
        this.parentCard = parentCard;
        this.godPower = godPower; // TODO: maybe refactor this to be only on Card class, so to remuve duplicated code
        this.startingPosition = null;
        //this.lastMove = null;
    }

    /**
     *
     * @param move (Opponent's move)
     * @param worker (Opponent's Worker)
     * @return moveAllowed (Whether the opponent's move is allowed or not)
     */
    public boolean checkMove(Move move, Worker worker) {
        boolean moveAllowed = true;
        // TODO: add operation to check for the move correctness
        /* move can be denied only if the God's power has to be applied to opponent's move */
        if(godPower.isActiveOnOpponentMovement())
            moveAllowed = checkSpecialRules(move, worker);

        return moveAllowed;
    }

    private boolean checkSpecialRules(Move move, Worker worker) {
        Move myLastMove = parentCard.getMyMove().getLastMove();

        /* check if my last move was one of the Hot Last Moves checked by my God's power:
        * in this case, check the opponent's move
        */
        // todo: athena (just to check, REMOVE THIS COMMENT)
        if(myLastMove.getLevelDirection() == godPower.getHotLastMoveDirection())
            if(move.getLevelDirection() == godPower.getHotLastMoveDirection())
                return false;

        return true; // everything ok
    }
}
