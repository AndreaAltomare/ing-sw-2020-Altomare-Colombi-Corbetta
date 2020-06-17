package it.polimi.ingsw.model.card.move;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.GodPower;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import it.polimi.ingsw.model.exceptions.WinException;
import it.polimi.ingsw.model.move.FloorDirection;
import it.polimi.ingsw.model.move.LevelDirection;
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

    public MyMove(Card parentCard, GodPower godPower, List<MoveChecker> checkers, MoveExecutor executor) {
        this.parentCard = parentCard;
        this.godPower = godPower; // TODO: maybe refactor this to be only on Card class, so to remove duplicated code
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
        boolean moveAllowed = true;

        //moveAllowed = checkMove(move, worker); // todo forse è inutile [debug]

        /* perform the movement just if it's allowed */
        if(moveAllowed) {
            executor.executeMove(move, worker, parentCard); // todo debug
//            if(!godPower.isActiveOnMyMovement()) {
//                /* default movement execution */
//                worker.place(move.getSelectedCell());
//            }
//            else {
//                /* special rules when performing a Movement */
//                // if the Cell is occupied, force the opponent's Worker into another Cell otherwise perform the Movement
//                if(occupiedCell(move.getSelectedCell())) {
//                    // todo: apollo (just to check, REMOVE THIS COMMENT)
//                    if (godPower.getForceOpponentInto() == FloorDirection.ANY) {
//                        Worker opponentWorker = move.getSelectedCell().removeWorker(); // get opponent's Worker from its Cell
//                        Cell myWorkerCurrentPosition = worker.position();
//                        worker.place(move.getSelectedCell()); // place my Worker into the selected Cell
//                        forceMove(opponentWorker,myWorkerCurrentPosition); // force movement for opponent's worker
//                    }
//                    else if(godPower.getForceOpponentInto() == FloorDirection.SAME) { // todo: minotaur (just to check, REMOVE THIS COMMENT)
//                        Worker opponentWorker = move.getSelectedCell().getWorker(); // get opponent's Worker
//                        forceMove(opponentWorker,calculateNextCell(move)); // force movement for opponent's worker
//                        worker.place(move.getSelectedCell()); // place my Worker into the selected Cell
//                    }
//                }
//                else {
//                    // todo: artemis (just to check, REMOVE THIS COMMENT)
//                    worker.place(move.getSelectedCell());
//                }
//            }

            /* Register the executed move */
            registerLastMove(move);

            // check if there is a win condition
            if(parentCard.getMyVictory().checkMove(move, worker))
                throw new WinException(worker.getOwner());
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
    public boolean checkMove(Move move, Worker worker) {
        for(MoveChecker checker : checkers) {
            if(!checker.checkMove(move,worker,startingPosition,movesLeft,parentCard))
                return false;
        }
        return true;
//        boolean moveAllowed = false;
//
//        if(!godPower.isActiveOnMyMovement())
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
    private boolean checkSpecialRules(Move move, Worker worker) {
        boolean checkResult = true;

        /* check if the Player can make a move in the first place */
        if(this.movesLeft <= 0)
            return false;

        /* cannot move into the same position */
        if(isSameCell(move.getSelectedCell(), worker.position()))
            return false;

        /* cannot move beyond adjoining cells */
        if(beyondAdjacentCells(move.getSelectedCell(), worker.position()))
            return false;

        /* cannot go more than one level up*/
        if(tooHighCell(move.getSelectedCell(), worker.position()))
            return false;

        /* cannot go if there is another Worker */
        // todo: apollo and minotaur (just to check, REMOVE THIS COMMENT)
        if(!godPower.isMoveIntoOpponentSpace()) {
            if (occupiedCell(move.getSelectedCell()))
                return false;
        }
        else {
            // perform additional controls only in cas the selectedCell is already occupied
            // todo: add a control to check that the worker is opponent's worker
            if(occupiedCell(move.getSelectedCell())) {
                // check that the worker is opponent's worker
                if(move.getSelectedCell().getWorker().getOwner().equals(worker.getOwner()))
                    return false;

                // just apollo can have ANY as a FloorDirection (it means to move the worker into the vacant Cell)
                // todo: apollo (just to check, REMOVE THIS COMMENT)
                if (godPower.getForceOpponentInto() != FloorDirection.ANY) {
                    // todo: minotaur (just to check, REMOVE THIS COMMENT)
                    // check if the opponent's Worker can be moved into the right cell (as with Minotaur Card's power)
                    Cell nextOpponentCell;
                    try {
                        nextOpponentCell = calculateNextCell(move);
                    }
                    catch(OutOfBoardException ex) {
                        return false;
                    }
                    checkResult = checkNextCell(nextOpponentCell); // check if opponent's Worker can be forced into the next calculated Cell
                    if (checkResult == false)
                        return false;
                }
            }
        }

        /* cannot go if there is a Dome */
        if(domedCell(move.getSelectedCell()))
            return false;

        /* cannot move back into the initial space */
        // todo: artemis (just to check, REMOVE THIS COMMENT)
        if(godPower.isStartingSpaceDenied()) {
            /* Check if a movement has already occurred with the selected Worker */
            if(parentCard.hasExecutedMovement()) {
                if (isSameCell(move.getSelectedCell(), startingPosition))
                    return false;
            }
            else { /* Otherwise, just register the starting position */
                this.startingPosition = worker.position(); // TODO: Maybe make a new method to encapsulate this operation
            }
        }

        /* check for denied move Direction when performing Construction before Movement */
        // todo: prometheus (just to check, REMOVE THIS COMMENT)
        if(godPower.getHotLastMoveDirection() != LevelDirection.NONE)
            if(parentCard.getMyConstruction().getConstructionLeft() <= 1)
                if(move.getLevelDirection() == godPower.getHotLastMoveDirection())
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
    private boolean checkDefaultRules(Move move, Worker worker) {
        /* check if the Player can make a move in the first place */
        if(this.movesLeft <= 0)
            return false;

        /* cannot move into the same position */
        if(isSameCell(move.getSelectedCell(), worker.position()))
            return false;

        /* cannot move beyond adjoining cells */
        if(beyondAdjacentCells(move.getSelectedCell(), worker.position()))
            return false;

        /* cannot go more than one level up*/
        if(tooHighCell(move.getSelectedCell(), worker.position()))
            return false;

        /* cannot go if there is another Worker */
        if(occupiedCell(move.getSelectedCell()))
            return false;

        /* cannot go if there is a Dome */
        if(domedCell(move.getSelectedCell()))
            return false;

        return true; // everything ok
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
        String oppWorkerId = opponentWorker.getWorkerId(); // todo: cancellare questi attributi (sono già nella lambda expression)
        int initialX = opponentWorker.position().getX(); // todo: check if this works correctly
        int initialY = opponentWorker.position().getY(); // todo: check if this works correctly
        opponentWorker.place(destinationCell);
        int finalX = opponentWorker.position().getX();
        int finalY = opponentWorker.position().getY();
        //opponentForcedMove = new WorkerMoved(oppWorkerId, initialX, initialY, finalX, finalY); // todo modifica carte
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

    public static class WorkerMoved {
        private String workerId;
        private int initialX;
        private int initialY;
        private int finalX;
        private int finalY;

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
