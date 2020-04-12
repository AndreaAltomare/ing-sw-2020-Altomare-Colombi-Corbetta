package it.polimi.ingsw.model;

import org.graalvm.compiler.asm.aarch64.AArch64MacroAssembler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Cell class, aimed to verify it works properly
 *
 * @author Marco
 */
class CellTest {

    private Cell cell;

    /**
     * Initialization before method's test
     */
    @BeforeEach
    void setUp() {

        cell = new Cell(1,2, null);
    }

    /**
     * Reset attribute after method's test
     */
    @AfterEach
    void tearDown() {

        cell = null;

    }

    /**
     * Check if getX() reads the correctly value after initialization
     *
     * Black Box and White Box
     */
    @Test
    void getX() {

        assertTrue( cell.getX() == 1 );

    }

    /**
     * Check if getY() reads the correctly value after initialization
     *
     * Black Box and White Box
     */
    @Test
    void getY() {

        assertTrue(cell.getY() == 2);
    }

    /**
     * Check if equals( Cell ) can recognize Cell and null and if it can value X and Y
     *
     * Black Box and White Box
     */
    @Test
    void testEquals() {
        Cell cell1 = new Cell(1,1,null);
        Cell cell2 = new Cell(2,2, null);
        Cell cell3 = new Cell(1,2, null);
        Block fakeCell = new Block();

        assertTrue( cell.equals(cell) );
        assertTrue( !(cell.equals(null)) );
        assertTrue( !(cell.equals(fakeCell)) );
        assertTrue( !(cell.equals(cell1)) );
        assertTrue( !(cell.equals(cell2)) );
        assertTrue( cell.equals(cell3) );
        assertTrue( cell.equals( (Object) cell3 ) );
    }

    /**
     * Check if getTop() can read the correct block when the there aren't, when there is one and when there are some
     * Methods used:    placeOn( Placeable )        of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void getTop() {
        Placeable block = new Block();
        Placeable dome = new Dome();
        Placeable placeable;

        placeable = cell.getTop();
        assertTrue( placeable == null );

        cell.placeOn( block );
        placeable = cell.getTop();
        assertTrue( placeable.equals( block ) );

        cell.placeOn( dome );
        placeable = cell.getTop();
        assertTrue( placeable.equals(dome) );

    }

    /**
     * Check if getTopNotNull() can read the correct block when the there aren't, when there is one and when there are some
     * Methods used:    placeOn( Placeable )        of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void getTopNotNull() {
        Placeable block = new Block();
        Placeable dome = new Dome();
        Placeable placeable;

        placeable = cell.getTop();
        assertTrue( placeable != null );

        cell.placeOn( block );
        placeable = cell.getTop();
        assertTrue( placeable.equals( block ) );

        cell.placeOn( dome );
        placeable = cell.getTop();
        assertTrue( placeable.equals(dome) );

    }

    /**
     * Check if getHeigth() can return the correct height when there aren't Placeables and when there are some
     * Methods used:    placeOn( Placeable)     of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void getHeigth() {
        Placeable block = new Block();
        Placeable dome = new Dome();
        int height;

        height = cell.getHeigth();
        assertTrue( height == 0);

        cell.placeOn( block );
        cell.placeOn( dome );
        height = cell.getHeigth();
        assertTrue( height == 2);

    }

    /**
     * Check if getLevel cen return the correct number of Blocks when there aren't, when there are some and when there is a Dome
     * Methods used: placeOn( PLaceable)        of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void getLevel() {
        Placeable block1 = new Block();
        Placeable block2 = new Block();
        Placeable block3 = new Block();
        Placeable dome = new Dome();
        int level;

        level = cell.getLevel();
        assertTrue( level == 0 );

        cell.placeOn( block1 );
        cell.placeOn( block2 );
        level = cell.getLevel();
        assertTrue( level == 2 );

        cell.placeOn( block3 );
        cell.placeOn( dome );
        level = cell.getLevel();
        assertTrue( level == 3 );

    }

    /**
     * Check if getPlaceableAt(int) can return null when there aren't Placeables or when level is too high and
     * if it can return the correct Placeable when there are some
     * Methods used:    placeOn( Placeable )        of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void getPlaceableAt() {
        Placeable block0 = new Block();
        Placeable block1 = new Block();
        Placeable block2 = new Block();
        Placeable placeable;
        int level = 0;

        placeable = cell.getPlaceableAt( level );
        assertTrue( placeable == null );

        level = 3;
        cell.placeOn( block0 );
        cell.placeOn( block1 );
        cell.placeOn( block2 );
        placeable = cell.getPlaceableAt( level );
        assertTrue( placeable == null );

        level = 1;
        placeable = cell.getPlaceableAt( level );
        assertTrue( placeable.equals( block1 ) );

    }

    /**
     * Check if isDomed() can return the correct value when there aren't Placeables and when there is or not a Dome
     * Methods used:    placeOn( Placeable )    of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void isDomed() {
        Placeable block1 = new Block();
        Placeable block2 = new Block();
        Placeable dome = new Dome();
        boolean check;

        check = cell.isDomed();
        assertTrue( !check );

        cell.placeOn( block1 );
        cell.placeOn( block2);
        check = cell.isDomed();
        assertTrue( !check );

        cell.placeOn( dome );
        check = cell.isDomed();
        assertTrue( check );

    }

    /**
     * Check if isOccupied() can return the correct value when there aren't Placeables and when there is or not a Worker
     * Methods used:    placeOn( Placeable )    of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void isOccupied() {
        Placeable block1 = new Block();
        Placeable block2 = new Block();
        Placeable worker = new Worker( null );
        boolean check;

        check = cell.isOccupied();
        assertTrue( !check );

        cell.placeOn( block1 );
        cell.placeOn( block2);
        check = cell.isOccupied();
        assertTrue( !check );

        cell.placeOn( worker );
        check = cell.isOccupied();
        assertTrue( check );

    }

    /**
     * Check if isFree() can return the correct value when there aren't Placeables and when there is or not a Worker or a Dome
     * Methods used:    placeOn( Placeable )    of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void isFree() {
        Cell cell1 = new Cell(0,0, null);
        Placeable block = new Block();
        Placeable dome = new Dome();
        Placeable worker = new Worker( null );
        boolean check;

        check = cell.isFree();
        assertTrue( check );

        cell.placeOn( block );
        check = cell.isFree();
        assertTrue( check );

        cell.placeOn( dome );
        check = cell.isFree();
        assertTrue( !check );

        cell1.placeOn( worker );
        check = cell1.isFree();
        assertTrue( !check );

    }

    /**
     * Check if removePlaceable() can return the correct value when there aren't Placeable and when there are some, then
     * if it remove the last Placeable check that it doesn't change the other Placeables of Building
     * Methods used:    placeOn( Placeable)         of  Cell
     *                  getHeigth()                 of  Cell
     *                  getPlaceableAt( int )       of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void removePlaceable() {
        Placeable block0 = new Block();
        Placeable block1 = new Block();
        Placeable dome = new Dome();
        int height;
        boolean check;

        height = cell.getHeigth();
        check = cell.removePlaceable();
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0 ) == null );

        cell.placeOn( block0 );
        cell.placeOn( block1);
        cell.placeOn( dome );
        height = cell.getHeigth();
        check = cell.removePlaceable();
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height - 1) );
        assertTrue( cell.getPlaceableAt(0).equals( block0 ));
        assertTrue( cell.getPlaceableAt(1).equals( block1 ));
        assertTrue( cell.getPlaceableAt(2) == null );

    }

    /**
     * Check if removePlaceable( int ) can return the correct value when there aren't Placeable and when there are some, then
     * if there are Placeables check that it doesn't change the other Placeables of Building
     * Methods used:    placeOn( Placeable)         of  Cell
     *                  getHeigth()                 of  Cell
     *                  getPlaceableAt( int )       of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void testRemovePlaceable() {
        Placeable block0 = new Block();
        Placeable block1 = new Block();
        Placeable dome = new Dome();
        int level = 0;
        int height;
        boolean check;

        height = cell.getHeigth();
        check = cell.removePlaceable( level );
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0 ) == null );

        level = 3;
        cell.placeOn( block0 );
        cell.placeOn( block1);
        cell.placeOn( dome );
        height = cell.getHeigth();
        check = cell.removePlaceable( level );
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).equals( block0 ));
        assertTrue( cell.getPlaceableAt(1).equals( block1 ));
        assertTrue( cell.getPlaceableAt(2).equals( dome ));
        assertTrue( cell.getPlaceableAt(3) == null );

        level = 1;
        height = cell.getHeigth();
        check = cell.removePlaceable( level );
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height - 1));
        assertTrue( cell.getPlaceableAt(0).equals( block0 ));
        assertTrue( cell.getPlaceableAt(1).equals( dome ));
        assertTrue( cell.getPlaceableAt(2) == null );

    }

    /**
     * Check if the returned value, the height and the Placeables of building are correct while a full tower is built
     * Methods used:    getHeigth()             of  Cell
     *                  getPlaceableAt( int )   of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void placeOn() {
        Cell cellWorker = new Cell(0,0,null);
        Placeable block0 = new Block();
        Placeable block1 = new Block();
        Placeable block2 = new Block();
        Placeable block3 = new Block();
        Placeable block = new Block();
        Placeable dome3 = new Dome();
        Placeable dome4 = new Dome();
        Placeable worker = new Worker( null );
        int height;
        boolean check;

        height = cell.getHeigth();
        check = cell.placeOn( block0 );
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).equals( block0 ) );
        assertTrue( cell.getPlaceableAt(1) == null );

        height = cell.getHeigth();
        check = cell.placeOn( block1 );
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).equals( block0 ) );
        assertTrue( cell.getPlaceableAt(1).equals( block1 ) );
        assertTrue( cell.getPlaceableAt(2) == null );

        height = cell.getHeigth();
        check = cell.placeOn( block2 );
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).equals( block0 ) );
        assertTrue( cell.getPlaceableAt(1).equals( block1 ) );
        assertTrue( cell.getPlaceableAt(2).equals( block2 ) );
        assertTrue( cell.getPlaceableAt(3) == null );

        height = cell.getHeigth();
        check = cell.placeOn( block3 );
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).equals( block0 ) );
        assertTrue( cell.getPlaceableAt(1).equals( block1 ) );
        assertTrue( cell.getPlaceableAt(2).equals( block2 ) );
        assertTrue( cell.getPlaceableAt(3) == null );

        height = cell.getHeigth();
        check = cell.placeOn( dome3 );
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).equals( block0 ) );
        assertTrue( cell.getPlaceableAt(1).equals( block1 ) );
        assertTrue( cell.getPlaceableAt(2).equals( block2 ) );
        assertTrue( cell.getPlaceableAt(3).equals( dome3) );
        assertTrue( cell.getPlaceableAt( 4) == null );

        height = cell.getHeigth();
        check = cell.placeOn( dome4 );
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height  );
        assertTrue( cell.getPlaceableAt(0).equals( block0 ) );
        assertTrue( cell.getPlaceableAt(1).equals( block1 ) );
        assertTrue( cell.getPlaceableAt(2).equals( block2 ) );
        assertTrue( cell.getPlaceableAt(3).equals( dome3) );
        assertTrue( cell.getPlaceableAt( 4) == null );

        height = cellWorker.getHeigth();
        check = cellWorker.placeOn( worker );
        assertTrue( check );
        assertTrue( cellWorker.getHeigth() == (height + 1) );
        assertTrue( cellWorker.getPlaceableAt(0).equals( worker ) );
        assertTrue( cellWorker.getPlaceableAt(1) == null );

        height = cellWorker.getHeigth();
        check = cellWorker.placeOn( block );
        assertTrue( !check );
        assertTrue( cellWorker.getHeigth() == height );
        assertTrue( cellWorker.getPlaceableAt(0).equals( worker ) );
        assertTrue( cellWorker.getPlaceableAt(1) == null );

    }

    /**
     * Check if buildBlock() can build under Worker or on free Cell but not under Dome
     * Methods used:    getHeigth()             of  Cell
     *                  getPlaceableAt( int )   of  Cell
     *                  placeOn( Placeable )    of  Cell
     *                  isBlock()               of  Block
     *
     * Black Box and White Box
     */
    @Test
    void buildBlock() {
        Cell cellDome = new Cell(0,0,null );
        Placeable worker = new Worker( null );
        Placeable dome = new Dome();
        int height;
        boolean check;

        height = cell.getHeigth();
        check = cell.buildBlock();
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.getPlaceableAt(1) == null );

        cell.placeOn( worker );
        height = cell.getHeigth();
        check = cell.buildBlock();
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(2).equals( worker ) );
        assertTrue( cell.getPlaceableAt(3) == null );

        cellDome.placeOn( dome );
        height = cell.getHeigth();
        check = cell.buildBlock();
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0 ).equals( dome ) );
        assertTrue( cell.getPlaceableAt(1 ) == null );

    }

    /**
     * Check if canBuildBlock() can correctly evaluate strong situations like Cell with Dome and full building
     *Methods used:            placeOn( Placeable )    of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void canBuildBlock() {
        Cell cellWorker = new Cell(0,0,null);
        Placeable block0 = new Block();
        Placeable block1 = new Block();
        Placeable block2 = new Block();
        Placeable blockW0 = new Block();
        Placeable blockW1 = new Block();
        Placeable worker = new Worker(null);
        Placeable dome = new Dome();
        boolean check;

        check = cell.canBuildBlock();
        assertTrue( check );

        cell.placeOn( block0 );
        cell.placeOn( block1 );
        cell.placeOn( block2 );
        check = cell.canBuildBlock();
        assertTrue( !check );

        cellWorker.placeOn( blockW0 );
        cellWorker.placeOn( blockW1 );
        cellWorker.placeOn( worker );
        check = cell.canBuildBlock();
        assertTrue( check );

        cell.placeOn( dome );
        check = cell.canBuildBlock();
        assertTrue( !check );

    }

    /**
     * Check if canBuildDome() can return the correct value when there aren't Placeables and when there is or not a Worker or a Dome
     * Methods used:    placeOn( Placeable )    of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void canBuildDome() {
        Cell cell1 = new Cell(0,0, null);
        Placeable block0 = new Block();
        Placeable block1 = new Block();
        Placeable block2 = new Block();
        Placeable dome = new Dome();
        Placeable worker = new Worker( null );
        boolean check;

        check = cell.canBuildDome();
        assertTrue( check );

        cell.placeOn( block0 );
        cell.placeOn( block1 );
        cell.placeOn( block2 );
        check = cell.canBuildDome();
        assertTrue( check );

        cell.placeOn( dome );
        check = cell.canBuildDome();
        assertTrue( !check );

        cell1.placeOn( worker );
        check = cell1.canBuildDome();
        assertTrue( !check );

    }

    /**
     * Check if buildDome() can return the correct value and if it can build a Dome or not
     * when there aren't Placeables, when there is a Dome and when there is a full block building
     * Methods used:    placeOn( Placeable )    of  Cell
     *                  getHeigth()             of  Cell
     *                  getPlaceableAte( int )  of  Cell
     *                  isDome()                of  Dome
     *
     * Black Box and White Box
     */
    @Test
    void buildDome() {
        Cell cellDome = new Cell(0,0,null);
        Placeable block0 = new Block();
        Placeable block1 = new Block();
        Placeable block2 = new Block();
        int height;
        boolean check;

        cell.placeOn( block0 );
        cell.placeOn( block1 );
        cell.placeOn( block2 );
        height = cell.getHeigth();
        check = cell.buildDome();
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1));
        assertTrue( cell.getPlaceableAt(0).equals(block0) );
        assertTrue( cell.getPlaceableAt(1).equals(block1) );
        assertTrue( cell.getPlaceableAt(2).equals(block2) );
        assertTrue( cell.getPlaceableAt(3).isDome() );
        assertTrue( cell.getPlaceableAt(4) == null );

        height = cellDome.getHeigth();
        check = cellDome.buildDome();
        assertTrue( check );
        assertTrue( cellDome.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).isDome() );
        assertTrue( cell.getPlaceableAt(1) == null );


        height = cellDome.getHeigth();
        check = cellDome.buildDome();
        assertTrue( !check );
        assertTrue( cellDome.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).isDome() );
        assertTrue( cell.getPlaceableAt(1) == null );

    }

    /**
     * Check if getStatus() can correctly read the status after Cell initialization
     *
     * Black Box and White Box
     */
    @Test
    void getStatus() {
        boolean check;

        check = (cell.getStatus() == Cell.PossibleStatus.UNCHECKED);
        assertTrue( check );
    }

    /**
     * Check if setStatus() can correctly set the status
     * Methods used:    getStatus()         of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void setStatus() {
        boolean check;

        cell.setStatus( Cell.PossibleStatus.BLOCKED );
        check = (cell.getStatus() == Cell.PossibleStatus.BLOCKED);
        assertTrue( check );
    }

    /**
     * Check if clearStatus() can correctly set the status after a change
     * Methods used:    getStatus()                     of  Cell
     *                  setStatus( PossibleStatus )     of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void clearStatus() {
        boolean check;

        cell.setStatus( Cell.PossibleStatus.BLOCKED );
        cell.clearStatus();
        check = (cell.getStatus() == Cell.PossibleStatus.UNCHECKED);
        assertTrue( check );

    }

    /**
     * Check if setReachableStatus() can correctly set the status when the Cell is unreachable blocked or not and reachable
     * Methods used:    getStatus()                     of  Cell
     *                  setStatus( PossibleStatus )     of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void setReachableStatus() {
        boolean check;

        cell.setReachableStatus();
        check = (cell.getStatus() == Cell.PossibleStatus.REACHABLE);
        assertTrue( check );

        cell.setReachableStatus();
        check = (cell.getStatus() == Cell.PossibleStatus.REACHABLE);
        assertTrue( check );

        cell.setStatus( Cell.PossibleStatus.UNREACHBLOCKED );
        cell.setReachableStatus();
        check = (cell.getStatus() == Cell.PossibleStatus.BLOCKED);
        assertTrue( check );

    }

    /**
     * Check if setBlockedStatus() can correctly set the status when the Cell is unblocked reachable or not and blocked
     * Methods used:    getStatus()                     of  Cell
     *                  setStatus( PossibleStatus )     of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void setBlockedStatus() {
        boolean check;

        cell.setBlockedStatus();
        check = (cell.getStatus() == Cell.PossibleStatus.UNREACHBLOCKED);
        assertTrue( check );

        cell.setBlockedStatus();
        check = (cell.getStatus() == Cell.PossibleStatus.UNREACHBLOCKED);
        assertTrue( check );

        cell.setStatus( Cell.PossibleStatus.REACHABLE );
        cell.setBlockedStatus();
        check = (cell.getStatus() == Cell.PossibleStatus.BLOCKED);
        assertTrue( check );

    }

    /**
     * Check if a full tower, a full block tower with worker and a free Cell are correctly evalueted
     * Methods used:        placeOn( Placeable )            of  Cell
     *
     * Blck Box and White Box
     */
    @Test
    void repOk() {
        Cell cellWorker = new Cell(0,0,null);
        Placeable blockD0 = new Block();
        Placeable blockD1 = new Block();
        Placeable blockD2 = new Block();
        Placeable blockW0 = new Block();
        Placeable blockW1 = new Block();
        Placeable blockW2 = new Block();
        Placeable dome = new Dome();
        Placeable worker = new Worker(null);
        boolean check;

        check = cell.repOk();
        assertTrue( check );

        cell.placeOn( blockD0 );
        cell.placeOn( blockD1 );
        cell.placeOn( blockD2 );
        cell.placeOn( dome );
        check = cell.repOk();
        assertTrue( check );

        cellWorker.placeOn( blockW0 );
        cellWorker.placeOn( blockW1 );
        cellWorker.placeOn( blockW2 );
        cellWorker.placeOn( worker );
        check = cellWorker.repOk();
        assertTrue( check );

    }

    /**
     * Check if getBoard() can read the correct Board of Cell after initialization
     *
     * Black Box and White Box
     */
    @Test
    void getBoard() {
        Board board = new IslandBoard();
        cell = new Cell(0,0, board );

        assertTrue( cell.getBoard().equals( board ) );
    }

    /**
     * Check if getWorker() return the correct value when there aren't Placeables and when there is or not Worker
     * Methods used:    placeOn( Placeable )        of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void getWorker() {
        Placeable block = new Block();
        Worker worker = new Worker(null);
        Worker workerReturn;

        workerReturn = cell.getWorker();
        assertTrue( workerReturn == null );

        cell.placeOn( block );
        workerReturn = cell.getWorker();
        assertTrue( workerReturn == null );

        cell.placeOn( worker );
        workerReturn = cell.getWorker();
        assertTrue( workerReturn.equals( worker ) );

    }

    /**
     * Check if removeWorker() return the correct value when there aren't Placeables and when there is or not Worker
     * and if it removes only the Worker
     * Methods used:    placeOn( Placeable )        of  Cell
     *                  getHeigth()                 of  Cell
     *                  getPlaeableAt( int )        of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void removeWorker() {
        Placeable block = new Block();
        Worker worker = new Worker(null);
        int height;
        Worker workerReturn;

        height = cell.getHeigth();
        workerReturn = cell.removeWorker();
        assertTrue( workerReturn == null );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0) == null );

        cell.placeOn( block );
        height = cell.getHeigth();
        workerReturn = cell.removeWorker();
        assertTrue( workerReturn == null );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).equals( block ) );
        assertTrue( cell.getPlaceableAt(1) == null );

        cell.placeOn( worker );
        height = cell.getHeigth();
        workerReturn = cell.removeWorker();
        assertTrue( workerReturn.equals( worker ) );
        assertTrue( cell.getHeigth() == (height - 1) );
        assertTrue( cell.getPlaceableAt(0).equals( block ) );
        assertTrue( cell.getPlaceableAt(1) == null );

    }

    /**
     * Check if removeThisWorker( Worker ) removes only the correct Worker when there aren't Placeables
     * and when there is or not the correct Worker
     * Methods used:    placeOn( Placeable )        of  Cell
     *                  getHeigth()                 of  Cell
     *                  getPlaeableAt( int )        of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void removeThisWorker() {
        Placeable block = new Block();
        Worker worker1 = new Worker(null);
        Worker worker2 = new Worker( null);
        int height;

        height = cell.getHeigth();
        cell.removeThisWorker( worker1 );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0) == null );

        cell.placeOn( block );
        height = cell.getHeigth();
        cell.removeThisWorker( worker1 );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).equals( block ) );
        assertTrue( cell.getPlaceableAt(1) == null );

        cell.placeOn( worker1 );
        height = cell.getHeigth();
        cell.removeThisWorker( worker1 );
        assertTrue( cell.getHeigth() == (height - 1) );
        assertTrue( cell.getPlaceableAt(0).equals( block ) );
        assertTrue( cell.getPlaceableAt(1) == null );

        cell.placeOn( worker2 );
        height = cell.getHeigth();
        cell.removeThisWorker( worker1 );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).equals( block ) );
        assertTrue( cell.getPlaceableAt(1).equals( worker2) );
        assertTrue( cell.getPlaceableAt(2) == null );
    }
}