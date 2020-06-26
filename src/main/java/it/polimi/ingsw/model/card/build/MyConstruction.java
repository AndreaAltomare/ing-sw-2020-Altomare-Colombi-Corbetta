package it.polimi.ingsw.model.card.build;

import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.GodPower;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import it.polimi.ingsw.model.move.BuildMove;
import it.polimi.ingsw.model.player.worker.Worker;

import java.util.List;

/**
 * Class representing a Player's Construction and providing
 * all the operations it needs to evaluate a Construction move correctness.
 *
 * @author AndreaAltomare
 */
public class MyConstruction {
    //private Cell startingPosition; // once the turn starts, Worker's starting position is saved
    private BuildMove lastMove;
    private GodPower godPower; // state of chosen God's power
    private Card parentCard;
    private int constructionLeft;
    private List<BuildChecker> checkers;
    private BuildExecutor executor;

    /**
     * Constructs a MyConstruction manager.
     *
     * @param parentCard Associated Card
     * @param godPower Associated God's power
     * @param checkers Checkers for moves
     * @param executor Executor for moves
     */
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

        //moveAllowed = checkMove(move, worker);

        /* perform the construction just if it's allowed */
        moveAllowed = executor.executeBuild(move, worker);

        /* Register the executed move */
        if(moveAllowed)
            registerLastMove(move);

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
    }


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
     * Decreases the Moves Left for this kind.
     */
    public void decreaseConstructionLeft() {
        constructionLeft -= 1;
    }

    /**
     * Resets the Constructions Left with the Player's
     * Card provided value.
     */
    public void resetConstructionLeft() {
        constructionLeft = godPower.getConstructionLeft();
    }

    /**
     * Registers last (Build)Move executed
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
