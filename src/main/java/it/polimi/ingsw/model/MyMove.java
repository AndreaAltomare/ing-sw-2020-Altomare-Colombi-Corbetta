package it.polimi.ingsw.model;

public class MyMove {
    // TODO: [MAYBE USELESS] Add attribute "Cell" for startingPosition (or just check it up by reference in methods)
    private Move lastMove;
    private int movesLeft;
    private boolean specialRules; // tell if a power change the basic rules

    public boolean checkMove(Move move, Worker worker) {
        boolean moveAllowed = false;
        // TODO: add operation to check for the move correctness
        if(!specialRules) {
            moveAllowed = checkDefaultRules(move, worker);
        }

        return moveAllowed;
    }

    private boolean checkDefaultRules(Move move, Worker worker) {
        // TODO: some statements (check if there is all the code needed)
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

        /* cannot go if there is another Worker */
        if(domedCell(move.getSelectedCell()))
            return false;

        return true; // everything ok
    }





    private boolean isSameCell(Cell a, Cell b) {
        return a.equals(b);
    }

    private boolean beyondAdjacentCells(Cell a, Cell b) {
        // x axis check
        if(!((move.getSelectedCell().getX() <= (worker.position().getX() + 1)) && (move.getSelectedCell().getX() >= (worker.position().getX() - 1))))
            return true;
        // y axis check
        if(!((move.getSelectedCell().getY() <= (worker.position().getY() + 1)) && (move.getSelectedCell().getY() >= (worker.position().getY() - 1))))
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
}
