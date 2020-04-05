package it.polimi.ingsw.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * This Class is ued to represent the Cells on the board.
 *
 * @author giorgio r
 */
public class Cell {

    private final int maxBlockHeighth = 3;

    private Deque<Placeable> building = new ArrayDeque<Placeable>();

    private final int x;
    private final int y;

    /**
     * Enum used to catch the status of the actual Cell.
     */
    //TODO commentare questa parte di codice
    public enum PossibleStatus {
        BLOCKED(true, true),
        REACHABLE(true, false),
        UNREACHABLE(false, true),
        UNREACHBLOCKED(false, false);

        private final boolean reachable;
        private final boolean blocked;

        PossibleStatus(boolean reachable, boolean blocked) {
            this.reachable = reachable;
            this.blocked = blocked;
        }

        public boolean isReachable() {
            return reachable;
        }

        public boolean isBloked() {
            return blocked;
        }

        public static PossibleStatus clear() {
            return UNREACHABLE;
        }

        public PossibleStatus setReachable(){
            if(blocked)
                return BLOCKED;
            else
                return REACHABLE;
        }

        public PossibleStatus setBlocked(){
            if(reachable)
                return BLOCKED;
            else
                return UNREACHBLOCKED;
        }
    }

    private PossibleStatus status;

    /**
     * Class constructor.
     *
     * @param x (x coordinate of the Cell)
     * @param y (y coordinate of the cell)
     */
    public Cell(int x, int y){
        this.x = x;
        this.y = y;
        status = PossibleStatus.clear();
    }


    /**
     * Method that returns the x-coordinate of this
     *
     * @return (the x-coordinate of the cell)
     */
    public int getX(){
        return x;
    }

    /**
     * Method that returns the y-coordinate of this
     *
     * @return (the y-coordinate of the cell)
     */
    public int getY(){
        return y;
    }

    /**
     * Overriden method that checks weather an object is equal to this.
     *
     * @param obj (The Cell to be compared to this)
     * @return  (this==other?true:false)
     */
    @Override
    public boolean equals(Object obj){
        if(obj == null) return false;
        if(obj instanceof Cell)
            return ((((Cell)obj).getX() == this.x) && (((Cell)obj).getY() == this.y));
        return false;
    }

    /**
     * Method that returns the top element on the Cell
     *
     * @return (The Placeable on the top of the Cell)
     */
    public Placeable getTop(){
        return building.peekLast();
    }

    /**
     * Method that does the same things of the getTop() but doesn't return null.
     * If it doesn't exist it returns a new Placeable
     *
     * @return (The Placeable on top of the building on the Cell, else a new placeable)
     */
    private Placeable getTopNotNull(){
        Placeable ret = this.getTop();
        if(ret == null){
            ret = new Placeable() {
                @Override
                public boolean place(Cell destination) {
                    return false;
                }
            };
        }
        return ret;
    }

    /**
     * Method that returns the height of the buildings on the Cell.
     *
     * @return (the heigth of the building on this Cell)
     */
    public int getHeigth(){
        return building.size();
    }

    /**
     * Method that returns the height of the Blocks of the building on the Cell.
     *
     * @return (the heigth of the Blocks of the building on this Cell)
     */
    public int getLevel(){
        int h = getHeigth();
        if(isFree())
            return h;
        return h-1;
    }

    /**
     * Method that returns the Placeable on the level of the building on this Cell.
     *
     * @param level (The level of the building to be returned)
     * @return (The Placeable on the level of the building on this cell)
     */
    public Placeable getPlaceableAt(int level){
        try {
            return (Placeable) (building.toArray()[level]);
        }catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    /**
     * Method that returns true iif there is a Dome on this Cell
     *
     * @return (Is there a Dome on this Cell? true:false)
     */
    public boolean isDomed(){
        if(this.getTopNotNull().isDome())
            return true;
        return false;
    }

    /**
     * Method that checks if there is a worker on this Cell
     *
     * @return (is there a Worker on this? true:false)
     */
    public boolean isOccupied(){
        Placeable r = this.getTopNotNull();
        return (r.isWorker());
    }

    /**
     * Method that checks if there isn't a worker or a Dome on this Cell
     *
     * @return (Is there a Dome or a Worker on it? false: true)
     */
    public boolean isFree(){
        Placeable r = this.getTopNotNull();
        return !(r.isDome()||r.isWorker());
    }

    /**
     * Method that eliminate the Placeable on the top  of the building
     *
     * @return (did it remove the top Placeable? true: false)
     */
    public boolean removePlaceable(){
        try{
            building.pop();
            return true;
        }catch(NoSuchElementException e){
            return false;
        }
    }

    /**
     * Method that removes the level of the building on this Cell.
     *
     * @param level (the level of the building to be removed)
     * @return (true iif it has succesfully removed the Placeable on level)
     */
    public boolean removePlaceable(int level){
        return building.remove(this.getPlaceableAt(level));
    }

    /**
     * Method that puts on the top of the building of this Cell the passed Placeable.
     * It also checks weather it's possible or not doing so.
     *
     * @param placeable (the placeable to be put on the top of the building)
     * @return (true iif placeable can be put on the top of this Cell)
     */
    public boolean placeOn(Placeable placeable){
        //check if it can build on it
        if (this.isFree()) {
            //if placeable is a Block
            if (placeable.isBlock())
                //If this.building has reached the max height of Blocks
                if (this.getLevel() > maxBlockHeighth)
                    return false;
        }else   //If this is not free
            return false;

        building.addLast(placeable);
        return true;
    }

    /**
     * Method intended to be used to build blocks on the Cell.
     * This is useful because it adds Blocks from the bottom  of the constructionn, so it can be usefut, for example, for Zeus.
     *
     * @return true iif it has built the block on the Cell.
     */
    public boolean buildBlock(){
        if(canBuildBlock()) {
            building.addFirst(new Block(this));
            return true;
        }
        return false;
    }

    /**
     * Method intended to check weather the building of a block on this Cell brings this Cell to an unconsistent state.
     *
     * @return true iif it can build the block on the Cell.
     */
    public boolean canBuildBlock(){
        //Check if this has a Dome
        if(this.isDomed())
            return false;
        //If this.building has reached the max height of Blocks
        if (this.getLevel() > maxBlockHeighth)
            return false;
        building.addFirst(new Block(this));
        return true;
    }

    /**
     * Method intended to check weather the building of a dome on this Cell brings this Cell to an unconsistent state.
     *
     * @return true iif it can build the dome on the Cell.
     */
    public boolean canBuildDome(){
        return this.isFree();
    }

    /**
     * Method intended to build the Dome on this.builidng
     *
     * @return true iif it has bilt the Dome on this.building
     */
    public boolean buildDome(){
        if(canBuildDome()){
            building.addLast(new Dome(this));
            return true;
        }
        return false;
    }

    /**
     * Getter method for the status.
     *
     * @return (the actual status)
     */
    public PossibleStatus getStatus() {
        return status;
    }

    /**
     * Setter method for the status
     *
     * @param status (the status to which we want to set the status)
     */
    public void setStatus(PossibleStatus status){
        this.status = status;
    }

    /**
     * Method to impost the default status
     */
    public void clearStatus(){
        this.status = this.status.clear();
    }

    /**
     * Method to make the status reachable
     */
    public void setReachableStatus(){
        this.status = this.status.setReachable();
    }

    /**
     * Method to make the status blocked
     */
    public void setBlockedStatus(){
        this.status = this.status.setBlocked();
    }

    /**
     * Method that checks whether the representation is still consistent.
     *
     * @return (true iif the representation is consistent).
     */
    public boolean repOk(){
        int len = getLevel();
        //Each level is a Block (at least the last one can be a Dome or a worker)
        for(int i = 0; i < len; i++)
            if ((!getPlaceableAt(i).isBlock() && i != len - 1)) {
                assert true : "Error in rapresentation";
                return false;
            }
        return true;
    }

}
