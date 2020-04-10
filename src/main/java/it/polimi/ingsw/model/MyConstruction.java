package it.polimi.ingsw.model;

public class MyConstruction {
    // TODO: Delete unused methods
    //private Cell startingPosition; // once the turn starts, Worker's starting position is saved
    private BuildMove lastMove;
    private GodPower godPower; // state of chosen God's power
    private Card parentCard;
    private int constructionLeft; // todo: needs to be renewed every turn starting

    // TODO: WRITE METHOD TO MAKE THE ACTUAL CONSTRUCTION (NOT JUST TO CHECK IF IT IS POSSIBLE)

    public MyConstruction(Card parentCard, GodPower godPower) {
        this.parentCard = parentCard;
        this.godPower = godPower;
        //this.startingPosition = null;
        this.lastMove = null;
        this.constructionLeft = godPower.getConstructionLeft(); // todo: ensure that every Turn starting, constructionLeft attribute is "renewed"
    }

    public boolean executeMove(BuildMove move, Worker worker) throws OutOfBoardException {
        boolean moveAllowed;

        moveAllowed = checkMove(move, worker);
        // TODO: here, must notify adversary observers in order to check if the actual execution is allowed
        /* perform the construction just if it's allowed */
        if(moveAllowed) {
            if(!godPower.isActiveOnMyConstruction()) {
                /* default construction execution */
                if(move.getBlockType() == PlaceableType.BLOCK)
                    move.getSelectedCell().placeOn(new Block());
                else if(move.getBlockType() == PlaceableType.DOME)
                    move.getSelectedCell().placeOn(new Dome());
                // TODO: notify observers
            }
            else {
                /* special rules when performing a Movement */
                // todo: all construction-based Gods (atlas, demeter, hephaestus, and prometheus) just need to check if the move is possible, befor executing it (just to check, REMOVE THIS COMMENT)
                if(move.getBlockType() == PlaceableType.BLOCK)
                    move.getSelectedCell().placeOn(new Block());
                else if(move.getBlockType() == PlaceableType.DOME)
                    move.getSelectedCell().placeOn(new Dome());
                // TODO: notify observers
            }
        }

        return moveAllowed; // true if the move was executed
    }

    public boolean checkMove(BuildMove move, Worker worker) {
        boolean moveAllowed = false;
        // TODO: add operation to check for the move correctness
        if(!godPower.isActiveOnMyConstruction())
            moveAllowed = checkDefaultRules(move, worker);
        else
            moveAllowed = checkSpecialRules(move, worker);

        return moveAllowed;
    }

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
            if(move.getSelectedCell().equals(lastMove.getSelectedCell()))
                return false;

        /* force build on the same space (for additional-time constructions) */
        // todo: hephaestus (just to check, REMOVE THIS COMMENT)
        if(godPower.isForceConstructionOnSameSpace())
            if(!move.getSelectedCell().equals(lastMove.getSelectedCell()))
                return false;

        // todo: prometheus is implemented by changing the order of State Pattern - Turn Manager (just to check, REMOVE THIS COMMENT)

        return true; // everything ok
    }

    private boolean checkDefaultRules(BuildMove move, Worker worker) {
        // TODO: some statements (check if there is all the code needed)
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

    /**
     * @return godPower (bean class, the only possible way)
     */
    public GodPower getGodPower() {
        return godPower;
    }

    public int getConstructionLeft() {
        return constructionLeft;
    }

    public void decreaseConstructionLeft() {
        constructionLeft -= 1;
    }

    /**
     * This method reset the Constructions Left with the Player's
     * Card provided value
     */
    public void resetConstructionLeft() {
        constructionLeft = godPower.getConstructionLeft();
    }
}
