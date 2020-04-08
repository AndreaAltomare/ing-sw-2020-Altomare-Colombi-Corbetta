package it.polimi.ingsw.model;

public class MyMove {
    private Cell startingPosition; // once the turn starts, Worker's starting position is saved
    private Move lastMove;
    private GodPower godPower; // state of chosen God's power
    private Card parentCard;
    private int movesLeft; // todo: needs to be renewed every turn starting

    // TODO: WRITE METHOD TO MAKE THE ACTUAL MOVE (NOT JUST TO CHECK IF IT IS POSSIBLE)

    public MyMove(Card parentCard, GodPower godPower) {
        this.parentCard = parentCard;
        this.godPower = godPower; // TODO: maybe refactor this to be only on Card class, so to remuve duplicated code
        this.startingPosition = null;
        this.lastMove = null;
        this.movesLeft = godPower.getMovementsLeft(); // todo: ensure that every Turn starting, movementsLeft attribute is "renewed"
    }

    public boolean checkMove(Move move, Worker worker) {
        boolean moveAllowed = false;
        // TODO: add operation to check for the move correctness
        if(!godPower.isActiveOnMyMovement())
            moveAllowed = checkDefaultRules(move, worker);
        else
            moveAllowed = checkSpecialRules(move, worker);

        return moveAllowed;
    }

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
            if(occupiedCell(move.getSelectedCell())) {
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
        if(godPower.isStartingSpaceDenied())
            if(isSameCell(move.getSelectedCell(), startingPosition))
                return false;

        /* check for denied move Direction when performing Construction before Movement */
        // todo: prometheus (just to check, REMOVE THIS COMMENT)
        if(godPower.getHotLastMoveDirection() != LevelDirection.NONE)
            if(parentCard.getMyConstruction().getConstructionLeft() <= 1)
                if(move.getLevelDirection() == godPower.getHotLastMoveDirection())
                    return false;

        return true; // everything ok
    }

    private Cell calculateNextCell(Move move) throws OutOfBoardException {
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
                //todo: maybe throw an exception here (or maybe delete this default section)
                break;
        }

        return nextCell;
    }

    private boolean checkNextCell(Cell nextOpponentCell) {
        /* cannot go if there is another Worker */
        if(occupiedCell(nextOpponentCell))
            return false;

        /* cannot go if there is another Worker */
        if(domedCell(nextOpponentCell))
            return false;

        return true; // everything ok
    }

    private boolean checkDefaultRules(Move move, Worker worker) {
        // TODO: some statements (check if there is all the code needed)
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

    private boolean tooHighCell(Cell a, Cell b) {
        return a.getLevel() > (b.getLevel() + 1);
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
