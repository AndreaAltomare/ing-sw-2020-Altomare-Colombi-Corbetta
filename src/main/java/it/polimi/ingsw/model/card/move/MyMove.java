package it.polimi.ingsw.model.card.move;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.GodPower;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import it.polimi.ingsw.model.exceptions.WinException;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.worker.Worker;

import java.util.List;

/**
 * Class representing a Player's Movement and providing
 * all the operations it needs to evaluate a Movement move correctness.
 *
 * @author AndreaAltomare
 */
public class MyMove {
    private Cell startingPosition; // once the turn starts, Worker's starting position is saved
    private Move lastMove;
    private GodPower godPower; // state of chosen God's power
    private Card parentCard;
    private int movesLeft;
    private WorkerMoved opponentForcedMove; // tells data of an Opponent's Worker forced move
    private List<MoveChecker> checkers;
    private MoveExecutor executor;

    /**
     * Constructs a MyMove manager.
     *
     * @param parentCard Associated Card
     * @param godPower Associated God's power
     * @param checkers Checkers for moves
     * @param executor Executor for moves
     */
    public MyMove(Card parentCard, GodPower godPower, List<MoveChecker> checkers, MoveExecutor executor) {
        this.parentCard = parentCard;
        this.godPower = godPower;
        this.checkers = checkers;
        this.executor = executor;
        this.startingPosition = null;
        this.lastMove = null;
        this.movesLeft = godPower.getMovementsLeft();
    }

    /**
     * This method check for the correctness of the move provided,
     * then actually executes it.
     *
     * @param move (Move to execute)
     * @param worker (Worker who has executed the move)
     * @return (Move was successfully executed ? true : false)
     * @throws OutOfBoardException (Exception handled by Controller)
     * @throws WinException (Exception handled by Controller)
     */
    public boolean executeMove(Move move, Worker worker) throws OutOfBoardException, WinException {
        //moveAllowed = checkMove(move, worker);

        /* perform the movement just if it's allowed */
        executor.executeMove(move, worker, parentCard);

        /* Register the executed move */
        registerLastMove(move);

        // check if there is a win condition
        if(parentCard.getMyVictory().checkMove(move, worker))
            throw new WinException(worker.getOwner());

        /* returns true if the move was executed
         * (since the move was previously checked, this method always returns true).
         */
        return true;
    }

    /**
     * This method check for the correctness of the move provided.
     *
     * @param move (Move to check)
     * @param worker (Worker who has executed the move)
     * @return (Move is allowed ? true : false)
     */
    public boolean checkMove(Move move, Worker worker) {
        for(MoveChecker checker : checkers) {
            if(!checker.checkMove(move,worker,startingPosition,movesLeft,parentCard))
                return false;
        }
        return true;
    }

    /**
     * Given a Move performed by a worker, this method calculate
     * the next Cell on the same Cardinal direction on which
     * the Move occurs.
     *
     * @param move (Move on which perform the calculation)
     * @return Next Cell on the same Cardinal direction on which Move occurs. (Calculated)
     * @throws OutOfBoardException (Exception handled by Controller)
     */
    public static Cell calculateNextCell(Move move) throws OutOfBoardException {
        Board board;
        Cell nextCell = null;
        int currentX = 0;
        int currentY = 0;

        board = move.getSelectedCell().getBoard();
        currentX = move.getSelectedCell().getX();
        currentY = move.getSelectedCell().getY();
        switch(move.getFloorDirection()) {
            case NORTH:
                nextCell = board.getCellAt(currentX - 1, currentY);
                break;
            case SOUTH:
                nextCell = board.getCellAt(currentX + 1, currentY);
                break;
            case EAST:
                nextCell = board.getCellAt(currentX, currentY + 1);
                break;
            case WEST:
                nextCell = board.getCellAt(currentX, currentY - 1);
                break;
            case NORTH_EAST:
                nextCell = board.getCellAt(currentX - 1, currentY + 1);
                break;
            case NORTH_WEST:
                nextCell = board.getCellAt(currentX - 1, currentY - 1);
                break;
            case SOUTH_EAST:
                nextCell = board.getCellAt(currentX + 1, currentY + 1);
                break;
            case SOUTH_WEST:
                nextCell = board.getCellAt(currentX + 1, currentY - 1);
                break;
            default:
                throw new OutOfBoardException("ERROR: Cannot calculate next Cell.");
                //break;
        }

        return nextCell;
    }

    /**
     * Method used when playing with Minotaur Card.
     *
     * Tells if the (calculated) next Cell in which
     * the Opponent's Worker would be forced into is
     * allowed.
     *
     * @param nextOpponentCell (Cell to check)
     * @return (Cell is free and allowed to make the Opponent's Worker to move into ? true : false)
     */
    public static boolean checkNextCell(Cell nextOpponentCell) {
        /* cannot go if there is another Worker */
        if(occupiedCell(nextOpponentCell))
            return false;

        /* cannot go if there is another Worker */
        if(domedCell(nextOpponentCell))
            return false;

        return true; // everything ok
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
     * Given two Cells, tells if the first one is more than one Level
     * higher than the second one.
     *
     * @param a (First provided Cell)
     * @param b (Second provided Cell)
     * @return (Cell a is more than one Level higher than Cell b ? true : false)
     */
    public static boolean tooHighCell(Cell a, Cell b) {
        return a.getLevel() > (b.getLevel() + 1);
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
     * Force an Opponent's Worker into another Cell.
     *
     * @param opponentWorker (Opponent's Worker)
     * @param destinationCell (Cell into which force the Opponent's Worker)
     */
    public static void forceMove(Worker opponentWorker, Cell destinationCell) {
        opponentWorker.place(destinationCell);
        //opponentForcedMove = new WorkerMoved(oppWorkerId, initialX, initialY, finalX, finalY);
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

    public Move getLastMove() {
        return lastMove;
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    /**
     * Decrease the Moves Left for this kind.
     */
    public void decreaseMovesLeft() {
        movesLeft -= 1;
    }

    /**
     * Reset the Moves Left with the Player's
     * Card provided value.
     */
    public void resetMovesLeft() {
        movesLeft = godPower.getMovementsLeft();
    }

    /**
     * Register last Move executed.
     *
     * @param move (Executed Move, to be registered)
     */
    private void registerLastMove(Move move) {
        this.lastMove = move;
    }

    /**
     * Registers the movement forced upon an opponent's Worker.
     *
     * @param opponentForcedMove Move to register
     */
    public void setOpponentForcedMove(WorkerMoved opponentForcedMove) {
        this.opponentForcedMove = opponentForcedMove;
    }





    /* ##### METHOD USED WHEN RESTORING DATA ##### */
    public void setStartingPosition(Cell startingPosition) {
        this.startingPosition = startingPosition;
    }

    public Cell getStartingPosition() {
        return this.startingPosition;
    }

    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }

    public void setMovesLeft(int movesLeft) {
        this.movesLeft = movesLeft;
    }

    /**
     * This class let to create objects which
     * encapsulate the information about
     * a movement forced upon an opponent's Worker.
     *
     * @author AndreaAltomare
     */
    public static class WorkerMoved {
        private String workerId;
        private int initialX;
        private int initialY;
        private int finalX;
        private int finalY;

        /**
         * Constructs a WorkerMoved object.
         *
         * @param workerId Worker forced to move
         * @param initialX Worker's initial X position
         * @param initialY Worker's initial Y position
         * @param finalX Worker's arrival X position
         * @param finalY Worker's arrival Y position
         */
        public WorkerMoved(String workerId, int initialX, int initialY, int finalX, int finalY) {
            this.workerId = workerId;
            this.initialX = initialX;
            this.initialY = initialY;
            this.finalX = finalX;
            this.finalY = finalY;
        }

        public String getWorkerId() {
            return workerId;
        }

        public int getInitialX() {
            return initialX;
        }

        public int getInitialY() {
            return initialY;
        }

        public int getFinalX() {
            return finalX;
        }

        public int getFinalY() {
            return finalY;
        }
    }


    public WorkerMoved getOpponentForcedMove() {
        return opponentForcedMove;
    }

    public void resetOpponentForcedMove() {
        opponentForcedMove = null;
    }
}
