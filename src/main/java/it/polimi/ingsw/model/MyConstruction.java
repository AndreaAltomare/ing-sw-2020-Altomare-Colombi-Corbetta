package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.build.BuildChecker;
import it.polimi.ingsw.model.card.build.BuildExecutor;
import it.polimi.ingsw.model.card.move.MoveChecker;
import it.polimi.ingsw.model.card.move.MoveExecutor;

import java.util.List;

/**
 * Class representing a Player's Construction and providing
 * all the operations it needs to evaluate a Construction move correctness.
 *
 * @author AndreaAltomare
 */
public class MyConstruction {
    // TODO: Delete unused methods
    //private Cell startingPosition; // once the turn starts, Worker's starting position is saved TODO: maybe it's useful for Advanced Gods. Let's wait...
    private BuildMove lastMove;
    private GodPower godPower; // state of chosen God's power
    private Card parentCard;
    private int constructionLeft;
    private List<BuildChecker> checkers;
    private BuildExecutor executor;

    public MyConstruction(Card parentCard, GodPower godPower, List<BuildChecker> checkers, BuildExecutor executor) {
        this.parentCard = parentCard;
        this.godPower = godPower;
        this.lastMove = null;
        this.constructionLeft = godPower.getConstructionLeft();
        this.checkers = checkers;
        this.executor = executor;
    }

    /**
     * This method check for the correctness of the move provided,
     * then actually executes it.
     *
     * @param move (Move to execute)
     * @param worker (Worker who has executed the move)
     * @return (Move was successfully executed ? true : false)
     * @throws OutOfBoardException (Exception handled by Controller)
     */
    public boolean executeMove(BuildMove move, Worker worker) throws OutOfBoardException {
        boolean moveAllowed = true;

        //moveAllowed = checkMove(move, worker); // todo forse è inutile [debug]

        /* perform the construction just if it's allowed */
        if(moveAllowed) {
            moveAllowed = executor.executeBuild(move, worker); // todo debug
//            if(!godPower.isActiveOnMyConstruction()) {
//                /* default construction execution */
//                if(move.getBlockType() == PlaceableType.BLOCK)
//                    moveAllowed = move.getSelectedCell().buildBlock();
//                else if(move.getBlockType() == PlaceableType.DOME)
//                    moveAllowed = move.getSelectedCell().buildDome();
//                else
//                    moveAllowed = false;
//            }
//            else {
//                /* special rules when performing a Movement */
//                // todo: all construction-based Gods (atlas, demeter, hephaestus, and prometheus) just need to check if the move is possible, before executing it (just to check, REMOVE THIS COMMENT)
//                if(move.getBlockType() == PlaceableType.BLOCK)
//                    moveAllowed = move.getSelectedCell().buildBlock();
//                else if(move.getBlockType() == PlaceableType.DOME)
//                    moveAllowed = move.getSelectedCell().buildDome();
//                else
//                    moveAllowed = false;
//            }

            /* Register the executed move */
            if(moveAllowed)
                registerLastMove(move);
        }

        return moveAllowed; // true if the move was executed
    }

    /**
     * This method check for the correctness of the move provided.
     *
     * @param move (Move to check)
     * @param worker (Worker who has executed the move)
     * @return (Move is allowed ? true : false)
     */
    public boolean checkMove(BuildMove move, Worker worker) {
        for(BuildChecker checker : checkers) {
            if(!checker.checkBuild(move, worker, lastMove, constructionLeft, parentCard))
                return false;
        }
        return true;
//        boolean moveAllowed = false;
//
//        if(!godPower.isActiveOnMyConstruction())
//            moveAllowed = checkDefaultRules(move, worker);
//        else
//            moveAllowed = checkSpecialRules(move, worker);
//
//        return moveAllowed;
    }

    /**
     * This method is called when a Card's power modifies basic game rules.
     *
     * @param move (Move to check)
     * @param worker (Worker who has executed the move)
     * @return (Move is allowed ? true : false)
     */
    private boolean checkSpecialRules(BuildMove move, Worker worker) {
        boolean checkResult = true;

        /* check if the Player can make a construction in the first place */
        if(this.constructionLeft <= 0)
            return false;

        /* cannot build into the same position */
        if(isSameCell(move.getSelectedCell(), worker.position()))
            return false;

        /* cannot build beyond adjoining cells */
        if(beyondAdjacentCells(move.getSelectedCell(), worker.position()))
            return false;

        /* cannot build if there is another Worker */
        if(occupiedCell(move.getSelectedCell()))
            return false;

        /* cannot build if there is a Dome */
        if(domedCell(move.getSelectedCell()))
            return false;

        /* cannot build a Dome at any level */
        // todo: atlas (just to check, REMOVE THIS COMMENT)
        if(!godPower.isDomeAtAnyLevel())
            if(move.getBlockType() == PlaceableType.DOME && move.getSelectedCell().getLevel() < 3)
                return false;

        /* cannot build on the same space (for additional-time constructions) */
        // todo: demeter (just to check, REMOVE THIS COMMENT)
        if(godPower.isSameSpaceDenied())
            if(parentCard.hasExecutedConstruction() && move.getSelectedCell().equals(lastMove.getSelectedCell())) // if(lastMove != null && constructionLeft == 1 && move.getSelectedCell().equals(lastMove.getSelectedCell()))
                return false;

        /* force build on the same space (for additional-time constructions) */
        // todo: hephaestus (just to check, REMOVE THIS COMMENT)
        if(godPower.isForceConstructionOnSameSpace())
            if(parentCard.hasExecutedConstruction() && (!move.getSelectedCell().equals(lastMove.getSelectedCell()) || move.getBlockType() == PlaceableType.DOME))
                return false;

        return true; // everything ok
    }

    /**
     * This method is called when only basic rules apply.
     *
     * @param move (Move to check)
     * @param worker (Worker who has executed the move)
     * @return (Move is allowed ? true : false)
     */
    private boolean checkDefaultRules(BuildMove move, Worker worker) {
        /* check if the Player can make a construction in the first place */
        if(this.constructionLeft <= 0)
            return false;

        /* cannot build into the same position */
        if(isSameCell(move.getSelectedCell(), worker.position()))
            return false;

        /* cannot build beyond adjoining cells */
        if(beyondAdjacentCells(move.getSelectedCell(), worker.position()))
            return false;

        /* Workers can build at any level height, by default rules */

        /* cannot build if there is another Worker */
        if(occupiedCell(move.getSelectedCell()))
            return false;

        /* cannot build if there is a Dome */
        if(domedCell(move.getSelectedCell()))
            return false;

        /* cannot build a Dome at any level */
        if(move.getBlockType() == PlaceableType.DOME && move.getSelectedCell().getLevel() < 3)
            return false;

        return true; // everything ok
    }


    // TODO: duplicated code from this point, can be implemented either as a Class which encapsulates Default Game rules set, or with a Parent Class by which extends functionalities (for both MyMove and MyConstruction classes)

    /**
     * Given two Cells, tells if they are the same
     * (same position on the Board).
     *
     * @param a (First provided Cell)
     * @param b (Second provided Cell)
     * @return (Cell a and Cell b are equal ? true : false)
     */
    public static boolean isSameCell(Cell a, Cell b) {
        return a.equals(b);
    }

    /**
     * Given two Cells, tells if they are adjacent.
     *
     * @param a (First provided Cell)
     * @param b (Second provided Cell)
     * @return (Cell a and Cell b are adjacent ? true : false)
     */
    public static boolean beyondAdjacentCells(Cell a, Cell b) {
        // x axis check
        if(!((a.getX() <= (b.getX() + 1)) && (a.getX() >= (b.getX() - 1))))
            return true;
        // y axis check
        if(!((a.getY() <= (b.getY() + 1)) && (a.getY() >= (b.getY() - 1))))
            return true;

        return false; // not beyond adjacent cells
    }

    /**
     * Given a Cell, tells if it is occupied.
     *
     * @param cell (Provided Cell)
     * @return (Cell is occupied ? true : false)
     */
    public static boolean occupiedCell(Cell cell) {
        return cell.isOccupied();
    }

    /**
     * Given a Cell, tells if there is a Dome on it.
     *
     * @param cell (Provided Cell)
     * @return (There is a Dome on the Cell ? true : false)
     */
    public static boolean domedCell(Cell cell) {
        return cell.isDomed();
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

    public BuildMove getLastMove() {
        return lastMove;
    }

    public int getConstructionLeft() {
        return constructionLeft;
    }

    /**
     * Decrease the Moves Left for this kind.
     */
    public void decreaseConstructionLeft() {
        constructionLeft -= 1;
    }

    /**
     * Reset the Constructions Left with the Player's
     * Card provided value.
     */
    public void resetConstructionLeft() {
        constructionLeft = godPower.getConstructionLeft();
    }

    /**
     * Register last (Build)Move executed
     *
     * @param move (Executed (Build)Move, to be registered)
     */
    private void registerLastMove(BuildMove move) {
        this.lastMove = move;
    }





    /* ##### METHOD USED WHEN RESTORING DATA ##### */
    public void setLastMove(BuildMove lastMove) {
        this.lastMove = lastMove;
    }

    public void setConstructionLeft(int constructionLeft) {
        this.constructionLeft = constructionLeft;
    }
}
