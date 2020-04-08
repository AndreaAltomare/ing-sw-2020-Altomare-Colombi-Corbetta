package it.polimi.ingsw.model;

import java.util.logging.Level;

public class MyVictory {
    // TODO: Delete unused methods
    private Move lastMove; // TODO: maybe useless, to remove
    private GodPower godPower; // state of chosen God's power
    private Card parentCard;
    private final int panLevelDepth = -2;

    // TODO: WRITE METHOD TO MAKE THE ACTUAL MOVE (NOT JUST TO CHECK IF IT IS POSSIBLE)

    public MyVictory(Card parentCard, GodPower godPower) {
        this.parentCard = parentCard;
        this.godPower = godPower;
        this.lastMove = null;
    }

    public boolean checkMove(Move move, Worker worker) {
        boolean isVictory = false;
        // TODO: add operation to check for the move correctness
        if(!godPower.isNewVictoryCondition())
            isVictory = checkDefaultRules(move, worker);
        else
            isVictory = checkSpecialRules(move, worker);

        return isVictory;
    }

    private boolean checkSpecialRules(Move move, Worker worker) {
        boolean isVictory = false;

        // check for new Victory condition first
        if(move.getLevelDirection() == godPower.getHotLastMoveDirection())
            // todo: pan (just to check, REMOVE THIS COMMENT)
            if(move.levelDepth <= panLevelDepth)
                return true;

        // check for a default Vicotry otherwise
        isVictory = checkDefaultRules(move, worker);

        return isVictory;
    }

    private boolean checkDefaultRules(Move move, Worker worker) {
        // TODO: some statements (check if there is all the code needed)
        /* Default rules: a player win if and only if its Worker moves up on top of level 3 */
        // check if the Worker was on a lower level before the move (by checking its last move was on UP Direction)
        if(move.getLevelDirection() != LevelDirection.UP)
            return false;

        // check if it is now onto a level 3 position
        if(worker.position().getLevel() != 3)
            return false;

        return true; // it's a win
    }




    private boolean isSameCell(Cell a, Cell b) {
        return a.equals(b);
    }

    private boolean beyondAdjacentCells(Cell a, Cell b) {
        // x axis check
        if(!((a.getX() <= (b.getX() + 1)) && (a.getX() >= (b.getX() - 1))))
            return true;
        // y axis check
        if(!((a.getY() <= (b.getY() + 1)) && (a.getY() >= (b.getY() - 1))))
            return true;

        return false; // not beyond adjacent cells
    }

    private boolean occupiedCell(Cell cell) {
        return cell.isOccupied();
    }

    private boolean domedCell(Cell cell) {
        return cell.isDomed();
    }

    private void forceMove() {

    }

    /**
     * @return godPower (bean class, the only possible way)
     */
    public GodPower getGodPower() {
        return godPower;
    }
}
