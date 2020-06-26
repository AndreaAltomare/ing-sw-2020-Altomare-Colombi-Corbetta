package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.board.placeables.Block;
import it.polimi.ingsw.model.board.placeables.Dome;
import it.polimi.ingsw.model.board.placeables.Placeable;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.worker.Worker;
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
     * Check if equals( Cell ) can recognize Cell, Cell with same or different x, y or board values and null
     * Methods used:        IslandBoard()       of  IslandBoard
     *
     * Black Box and White Box
     */
    @Test
    void testEquals() {
        IslandBoard islandBoard1 = new IslandBoard();
        Cell cell1 = new Cell(1,1,null);
        Cell cell2 = new Cell(2,2, null);
        Cell cell3 = new Cell(1,2, null);
        Cell cell4 = new Cell(1, 2, islandBoard1);
        Cell cell5 = new Cell(1,2, islandBoard1);
        Block fakeCell = new Block();

        // cell is equal to itself
        assertTrue( cell.equals(cell) );
        // cell is different to null object
        assertTrue( !(cell.equals(null)) );
        // cell is different to an Object which isn't a cell
        assertTrue( !(cell.equals(fakeCell)) );
        // cells with different y value are different
        assertTrue( !(cell.equals(cell1)) );
        // cells with different x value are different
        assertTrue( !(cell.equals(cell2)) );
        // cells with same x and y value are equals
        assertTrue( cell.equals(cell3) );
        // cells with same x and y value but different board are equals  //todo: speak to friends
        assertTrue( cell.equals( cell4 ) );
        // cells with same x and y value and board are equals  //todo: speak to friends
        assertTrue( cell5.equals( cell4 ) );
    }

    /**
     * Check if getTop() can read the correct block when the there aren't, when there is one and when there are some
     * Methods used:    placeOn( Placeable )        of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void getTop() {
        Placeable block1 = new Block();
        Placeable block2 = new Block();
        Placeable worker = new Worker(new Player("pippo"));
        Placeable dome = new Dome();
        Placeable placeable;

        // returns null with free cell
        placeable = cell.getTop();
        assertTrue( placeable == null );

        // returns the only placeable
        cell.placeOn( block1 );
        placeable = cell.getTop();
        assertTrue( placeable.equals( block1 ) );

        // returns the top placeable with Block
        cell.placeOn( block2 );
        placeable = cell.getTop();
        assertTrue( placeable.equals( block2 ) );

        // return the top placeable with Dome
        cell = new Cell(1,2, null);
        cell.placeOn( block1 );
        cell.placeOn( dome );
        placeable = cell.getTop();
        assertTrue( placeable.equals(dome) );

        // return the top placeable with Worker
        cell = new Cell(1,2, null);
        cell.placeOn( block1 );
        cell.placeOn( worker );
        placeable = cell.getTop();
        assertTrue( placeable.equals(worker) );
    }

    /**
     * Check if getHeigth() can return the correct height when there aren't Placeables and when there are some
     * Methods used:    placeOn( Placeable)     of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void getHeigth() {
        Placeable block1 = new Block();
        Placeable block2 = new Block();
        Placeable worker = new Worker(new Player("pippo"));
        Placeable dome = new Dome();
        int height;

        // returns 0 with free cell
        height = cell.getHeigth();
        assertTrue( height == 0);

        // return correct value with blocks
        cell.placeOn( block1 );
        cell.placeOn( block2 );
        height = cell.getHeigth();
        assertTrue( height == 2);

        // return correct value with block and dome
        cell = new Cell(1,2, null);
        cell.placeOn( block1 );
        cell.placeOn( dome );
        height = cell.getHeigth();
        assertTrue( height == 2);

        // return correct value with blocks and worker
        cell = new Cell(1,2, null);
        cell.placeOn( block1 );
        cell.placeOn( block2 );
        cell.placeOn( worker );
        height = cell.getHeigth();
        assertTrue( height == 3);

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
        Placeable worker = new Worker(new Player("pippo"));
        int level;

        // free Cell
        level = cell.getLevel();
        assertTrue( level == 0 );

        // cell with only blocks
        cell.placeOn( block1 );
        cell.placeOn( block2 );
        level = cell.getLevel();
        assertTrue( level == 2 );

        // cell with blocks and dome
        cell.placeOn( block3 );
        cell.placeOn( dome );
        level = cell.getLevel();
        assertTrue( level == 3 );

        // cell with blocks and worker
        cell = new Cell(1,2, null);
        cell.placeOn(block1);
        cell.placeOn(block2);
        cell.placeOn(worker);
        level = cell.getLevel();
        assertTrue( level == 2);

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
        Placeable dome = new Dome();
        Placeable worker = new Worker(new Player("pippo"));
        Placeable placeable;
        int level = 0;

        // free Cell
        placeable = cell.getPlaceableAt( level );
        assertTrue( placeable == null );

        // level too high
        level = 3;
        cell.placeOn( block0 );
        cell.placeOn( block1 );
        cell.placeOn( block2 );
        placeable = cell.getPlaceableAt( level );
        assertTrue( placeable == null );

        // level <= maxLevel with block
        level = 1;
        placeable = cell.getPlaceableAt( level );
        assertTrue( placeable.equals( block1 ) );

        // level <= maxLevel with dome
        level = 3;
        cell.placeOn(dome);
        placeable = cell.getPlaceableAt(level);
        assertTrue( placeable.equals(dome) );

        // level <= maxLevel with worker
        level = 0;
        cell = new Cell(1,2, null);
        cell.placeOn(worker);
        placeable = cell.getPlaceableAt(level);
        assertTrue( placeable.equals(worker) );

    }

    /**
     * Check if isDomed() can return the correct value when there aren't Placeables and when there is or not a Dome
     * ( try to check also getTopNotNull() )
     * Methods used:    placeOn( Placeable )    of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void isDomed() {
        Placeable block1 = new Block();
        Placeable block2 = new Block();
        Placeable dome = new Dome();
        Placeable worker = new Worker(new Player("pippo"));
        boolean check;

        // free cell
        check = cell.isDomed();
        assertTrue( !check );

        // cell with only block
        cell.placeOn( block1 );
        cell.placeOn( block2);
        check = cell.isDomed();
        assertTrue( !check );

        // cell with dome and blocks
        cell.placeOn( dome );
        check = cell.isDomed();
        assertTrue( check );

        // cell with worker
        cell = new Cell(1,2, null);
        cell.placeOn( worker );
        check = cell.isDomed();
        assertTrue( !check );

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
        Placeable dome = new Dome();
        Placeable worker = new Worker( null );
        boolean check;

        // free Cell
        check = cell.isOccupied();
        assertTrue( !check );

        // cell with blocks
        cell.placeOn( block1 );
        cell.placeOn( block2);
        check = cell.isOccupied();
        assertTrue( !check );

        // cell with blocks and worker
        cell.placeOn( worker );
        check = cell.isOccupied();
        assertTrue( check );

        // cell with dome
        cell = new Cell( 1,2,null);
        cell.placeOn( dome );
        check = cell.isOccupied();
        assertTrue( !check );

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

        // free cell
        check = cell.isFree();
        assertTrue( check );

        //cell with block
        cell.placeOn( block );
        check = cell.isFree();
        assertTrue( check );

        // cell with block and dome
        cell.placeOn( dome );
        check = cell.isFree();
        assertTrue( !check );

        // cell with worker
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
        Placeable worker = new Worker( null );
        int height;
        boolean check;

        // free cell
        height = cell.getHeigth();
        check = cell.removePlaceable();
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height );
        assertTrue(height==0);
        assertTrue( cell.getPlaceableAt(0 ) == null );

        // cell with a block as top
        cell.placeOn( block0 );
        cell.placeOn( block1);
        height = cell.getHeigth();
        check = cell.removePlaceable();
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height - 1) );
        assertTrue( cell.getPlaceableAt(0).equals( block0 ));
        assertTrue( cell.getPlaceableAt(1) == null );

        // cell with a dome as top
        cell = new Cell(1,2, null);
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

        // cell with a worker as top
        cell = new Cell(1,2, null);
        cell.placeOn( block0 );
        cell.placeOn( block1);
        cell.placeOn( worker );
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
        Placeable worker = new Worker(null);
        int level = 0;
        int height;
        boolean check;

        // free cell
        height = cell.getHeigth();
        check = cell.removePlaceable( level );
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0 ) == null );

        // level > tower's level
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

        // level <= tower's level with block
        level = 1;
        height = cell.getHeigth();
        check = cell.removePlaceable( level );
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height - 1));
        assertTrue( cell.getPlaceableAt(0).equals( block0 ));
        assertTrue( cell.getPlaceableAt(1).equals( dome ));
        assertTrue( cell.getPlaceableAt(2) == null );

        // level <= tower's level with dome
        level = 1;
        height = cell.getHeigth();
        check = cell.removePlaceable( level );
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height - 1));
        assertTrue( cell.getPlaceableAt(0).equals( block0 ));
        assertTrue( cell.getPlaceableAt(1) == null );

        // level <= tower's level with worker
        level = 1;
        cell.placeOn(worker);
        height = cell.getHeigth();
        check = cell.removePlaceable( level );
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height - 1));
        assertTrue( cell.getPlaceableAt(0).equals( block0 ));
        assertTrue( cell.getPlaceableAt(1) == null );

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
        Cell cellDome = new Cell(1,1, null);
        Placeable block0 = new Block();
        Placeable block1 = new Block();
        Placeable block2 = new Block();
        Placeable block3 = new Block();
        Placeable block = new Block();
        Placeable workerBlock = new Block();
        Placeable dome3 = new Dome();
        Placeable dome4 = new Dome();
        Placeable worker = new Worker( null );
        int height;
        boolean check;

        // free cell, place block
        height = cell.getHeigth();
        check = cell.placeOn( block0 );
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).equals( block0 ) );
        assertTrue( cell.getPlaceableAt(1) == null );

        // cell with block, place block
        height = cell.getHeigth();
        check = cell.placeOn( block1 );
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).equals( block0 ) );
        assertTrue( cell.getPlaceableAt(1).equals( block1 ) );
        assertTrue( cell.getPlaceableAt(2) == null );

        // cell with blocks, place block
        height = cell.getHeigth();
        check = cell.placeOn( block2 );
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).equals( block0 ) );
        assertTrue( cell.getPlaceableAt(1).equals( block1 ) );
        assertTrue( cell.getPlaceableAt(2).equals( block2 ) );
        assertTrue( cell.getPlaceableAt(3) == null );

        // cell with blocks, place block at level 3
        height = cell.getHeigth();
        check = cell.placeOn( block3 );
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).equals( block0 ) );
        assertTrue( cell.getPlaceableAt(1).equals( block1 ) );
        assertTrue( cell.getPlaceableAt(2).equals( block2 ) );
        assertTrue( cell.getPlaceableAt(3) == null );

        // cell with blocks, place dome at level 3
        height = cell.getHeigth();
        check = cell.placeOn( dome3 );
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).equals( block0 ) );
        assertTrue( cell.getPlaceableAt(1).equals( block1 ) );
        assertTrue( cell.getPlaceableAt(2).equals( block2 ) );
        assertTrue( cell.getPlaceableAt(3).equals( dome3) );
        assertTrue( cell.getPlaceableAt( 4) == null );

        // cell with three blocks and dome, place dome at level 4 on another dome
        height = cell.getHeigth();
        check = cell.placeOn( dome4 );
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height  );
        assertTrue( cell.getPlaceableAt(0).equals( block0 ) );
        assertTrue( cell.getPlaceableAt(1).equals( block1 ) );
        assertTrue( cell.getPlaceableAt(2).equals( block2 ) );
        assertTrue( cell.getPlaceableAt(3).equals( dome3) );
        assertTrue( cell.getPlaceableAt( 4) == null );

        // free cell, place a worker
        height = cellWorker.getHeigth();
        check = cellWorker.placeOn( worker );
        assertTrue( check );
        assertTrue( cellWorker.getHeigth() == (height + 1) );
        assertTrue( cellWorker.getPlaceableAt(0).equals( worker ) );
        assertTrue( cellWorker.getPlaceableAt(1) == null );

        // cell with a worker, place a block
        height = cellWorker.getHeigth();
        check = cellWorker.placeOn( block );
        assertTrue( !check );
        assertTrue( cellWorker.getHeigth() == height );
        assertTrue( cellWorker.getPlaceableAt(0).equals( worker ) );
        assertTrue( cellWorker.getPlaceableAt(1) == null );

        // cell with a block, place a worker
        cellWorker = new Cell(0,0, null);
        cellWorker.placeOn( workerBlock );
        height = cellWorker.getHeigth();
        check = cellWorker.placeOn( worker );
        assertTrue( !check );
        assertTrue( cellWorker.getHeigth() == ( + 1) );
        assertTrue( cellWorker.getPlaceableAt(0).equals( workerBlock ) );
        assertTrue( cellWorker.getPlaceableAt(1).equals( worker ) );
        assertTrue( cellWorker.getPlaceableAt(2) == null );

        // free cell, place a dome
        height = cellWorker.getHeigth();
        check = cellWorker.placeOn( dome4 );
        assertTrue( check );
        assertTrue( cellWorker.getHeigth() == (height + 1) );
        assertTrue( cellWorker.getPlaceableAt(0).equals( dome4 ) );
        assertTrue( cellWorker.getPlaceableAt(1) == null );

        // free cell, place a dome
        height = cellWorker.getHeigth();
        check = cellWorker.placeOn( block );
        assertTrue( check );
        assertTrue( cellWorker.getHeigth() == height );
        assertTrue( cellWorker.getPlaceableAt(0).equals( dome4 ) );
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
        Cell blockCell = new Cell(1,1,null);
        Placeable worker = new Worker( null );
        Placeable dome = new Dome();
        int height;
        boolean check;

        // free cell
        height = cell.getHeigth();
        check = cell.buildBlock();
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.getPlaceableAt(1) == null );

        // on a cell with worker
        cell.placeOn( worker );
        height = cell.getHeigth();
        check = cell.buildBlock();
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(2).equals( worker ) );
        assertTrue( cell.getPlaceableAt(3) == null );

        // on a cell with worker and three blocks
        cell.buildBlock();
        height = cell.getHeigth();
        check = cell.buildBlock();
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(2).isBlock() );
        assertTrue( cell.getPlaceableAt(3).equals( worker ) );
        assertTrue( cell.getPlaceableAt(4) == null );

        // on a cell with dome
        cellDome.placeOn( dome );
        height = cellDome.getHeigth();
        check = cellDome.buildBlock();
        assertTrue( !check );
        assertTrue( cellDome.getHeigth() == height );
        assertTrue( cellDome.getPlaceableAt(0 ).equals( dome ) );
        assertTrue( cellDome.getPlaceableAt(1 ) == null );

        // on a cell with three blocks
        blockCell.placeOn(new Block());
        blockCell.placeOn(new Block());
        blockCell.placeOn(new Block());
        height = blockCell.getHeigth();
        check = cell.buildBlock();
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(2).isBlock() );
        assertTrue( cell.getPlaceableAt(3) == null );


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
        Placeable blockW2 = new Block();
        Placeable worker = new Worker(null);
        Placeable dome = new Dome();
        boolean check;

        // free cell
        check = cell.canBuildBlock();
        assertTrue( check );

        // cell with three blocks
        cell.placeOn( block0 );
        cell.placeOn( block1 );
        cell.placeOn( block2 );
        check = cell.canBuildBlock();
        assertTrue( !check );

        // cell with dome and three blocks
        cell.placeOn( dome );
        check = cell.canBuildBlock();
        assertTrue( !check );

        //cell with worker
        cellWorker.placeOn( blockW0 );
        cellWorker.placeOn( blockW1 );
        cellWorker.placeOn( worker );
        check = cellWorker.canBuildBlock();
        assertTrue( check );

        //cell with worker and three blocks
        cellWorker = new Cell(0,0,null);
        cellWorker.placeOn( blockW0 );
        cellWorker.placeOn( blockW1 );
        cellWorker.placeOn( blockW2 );
        cellWorker.placeOn( worker );
        check = cellWorker.canBuildBlock();
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
        Placeable block0 = new Block();
        Placeable block1 = new Block();
        Placeable block2 = new Block();
        Placeable dome = new Dome();
        Placeable worker = new Worker( null );
        boolean check;

        // free cell
        check = cell.canBuildDome();
        assertTrue( check );

        // cell with some blocks
        cell.placeOn( block0 );
        cell.placeOn( block1 );
        check = cell.canBuildDome();
        assertTrue( check );

        // cell with three blocks
        cell.placeOn( block2 );
        check = cell.canBuildDome();
        assertTrue( check );

        // cell with dome
        cell = new Cell (1,2, null);
        cell.placeOn( dome );
        check = cell.canBuildDome();
        assertTrue( !check );

        // cell with worker
        cell = new Cell(1,2, null);
        cell.placeOn( worker );
        check = cell.canBuildDome();
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

        // cell with three blocks
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

        // free cell
        height = cellDome.getHeigth();
        check = cellDome.buildDome();
        assertTrue( check );
        assertTrue( cellDome.getHeigth() == (height + 1) );
        assertTrue( cellDome.getPlaceableAt(0).isDome() );
        assertTrue( cellDome.getPlaceableAt(1) == null );

        // cell with dome
        height = cellDome.getHeigth();
        check = cellDome.buildDome();
        assertTrue( !check );
        assertTrue( cellDome.getHeigth() == height );
        assertTrue( cellDome.getPlaceableAt(0).isDome() );
        assertTrue( cellDome.getPlaceableAt(1) == null );

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

        // free cell
        check = cell.repOk();
        assertTrue( check );

        // cell with full tower
        cell.placeOn( blockD0 );
        cell.placeOn( blockD1 );
        cell.placeOn( blockD2 );
        cell.placeOn( dome );
        check = cell.repOk();
        assertTrue( check );

        // cell with three blocks and a worker
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
        Placeable dome = new Dome();
        Worker worker = new Worker(null);
        Worker workerReturn;

        // free cell
        workerReturn = cell.getWorker();
        assertTrue( workerReturn == null );

        // cell with block
        cell.placeOn( block );
        workerReturn = cell.getWorker();
        assertTrue( workerReturn == null );

        // cell with block and worker
        cell.placeOn( worker );
        workerReturn = cell.getWorker();
        assertTrue( workerReturn.equals( worker ) );

        // cell with block and dome
        cell = new Cell(1, 2, null);
        cell.placeOn( block );
        cell.placeOn( dome );
        workerReturn = cell.getWorker();
        assertTrue( workerReturn == null );

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
        Placeable dome = new Dome();
        Worker worker = new Worker(null);
        int height;
        Worker workerReturn;

        // free cell
        height = cell.getHeigth();
        workerReturn = cell.removeWorker();
        assertTrue( workerReturn == null );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0) == null );

        // cell with a block
        cell.placeOn( block );
        height = cell.getHeigth();
        workerReturn = cell.removeWorker();
        assertTrue( workerReturn == null );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).equals( block ) );
        assertTrue( cell.getPlaceableAt(1) == null );

        // cell with block and worker
        cell.placeOn( worker );
        height = cell.getHeigth();
        workerReturn = cell.removeWorker();
        assertTrue( workerReturn.equals( worker ) );
        assertTrue( cell.getHeigth() == (height - 1) );
        assertTrue( cell.getPlaceableAt(0).equals( block ) );
        assertTrue( cell.getPlaceableAt(1) == null );

        // cell with block and dome
        cell = new Cell(1,2, null);
        cell.placeOn( block );
        cell.placeOn( dome );
        height = cell.getHeigth();
        workerReturn = cell.removeWorker();
        assertTrue( workerReturn == null );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).equals( block ) );
        assertTrue( cell.getPlaceableAt(1).equals( dome ) );
        assertTrue( cell.getPlaceableAt(2) == null );

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
        Placeable dome = new Dome();
        Worker worker1 = new Worker(null);
        Worker worker2 = new Worker( null);
        int height;

        // free cell
        height = cell.getHeigth();
        cell.removeThisWorker( worker1 );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0) == null );

        // cell with a block
        cell.placeOn( block );
        height = cell.getHeigth();
        cell.removeThisWorker( worker1 );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).equals( block ) );
        assertTrue( cell.getPlaceableAt(1) == null );

        // cell with a block and the worker to remove
        cell.placeOn( worker1 );
        height = cell.getHeigth();
        cell.removeThisWorker( worker1 );
        assertTrue( cell.getHeigth() == (height - 1) );
        assertTrue( cell.getPlaceableAt(0).equals( block ) );
        assertTrue( cell.getPlaceableAt(1) == null );

        // cell with  block and the wrong worker
        cell = new Cell( 1,2, null );
        cell.placeOn( block );
        cell.placeOn( worker2 );
        height = cell.getHeigth();
        cell.removeThisWorker( worker1 );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).equals( block ) );
        assertTrue( cell.getPlaceableAt(1).equals( worker2) );
        assertTrue( cell.getPlaceableAt(2) == null );

        // cell with  block and a dome
        cell = new Cell( 1,2, null );
        cell.placeOn( block );
        cell.placeOn( dome );
        height = cell.getHeigth();
        cell.removeThisWorker( worker1 );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).equals( block ) );
        assertTrue( cell.getPlaceableAt(1).equals( dome ) );
        assertTrue( cell.getPlaceableAt(2) == null );

    }

    //todo: do test to place(Placeable Type)

    /**
     * Check if place() can build only block or dome and also if it is possible
     * (for better test of buildDome() and buildBlock() see their test)
     * Methods used:    placeOn( PLaceable )        of  Cell
     *                  getHeight()                 of  Cell
     *                  getPlaceableAt( int )       of  Cell
     *                  isBlock()                   of  Placeable
     *                  isDome()                    of  Placeable
     *
     * Black box and White Box
     */
    @Test
    void place() {
        int height;
        boolean check;

        // free cell, build block
        height = cell.getHeigth();
        check = cell.place( PlaceableType.BLOCK );
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.getPlaceableAt(1) == null);

        // cell with three block, try to build block
        cell.placeOn( new Block() );
        cell.placeOn( new Block() );
        height = cell.getHeigth();
        check = cell.place( PlaceableType.BLOCK );
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(2).isBlock() );
        assertTrue( cell.getPlaceableAt(3) == null );

        // free cell, build dome
        cell = new Cell(1,2, null);
        height = cell.getHeigth();
        check = cell.place( PlaceableType.DOME );
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).isDome() );
        assertTrue( cell.getPlaceableAt(1) == null );

        // cell with dome, try to build dome
        height = cell.getHeigth();
        check = cell.place( PlaceableType.DOME );
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).isDome() );
        assertTrue( cell.getPlaceableAt(1) == null );

        // cell with three blocks, build dome
        cell = new Cell(1,2, null);
        cell.placeOn( new Block());
        cell.placeOn( new Block());
        cell.placeOn( new Block());
        height = cell.getHeigth();
        check = cell.place( PlaceableType.DOME );
        assertTrue( check );
        assertTrue( cell.getHeigth() == (height + 1) );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(2).isBlock() );
        assertTrue( cell.getPlaceableAt(3).isDome() );
        assertTrue( cell.getPlaceableAt(4) == null );

        // cell with some blocks, try to build a worker
        cell = new Cell(1,2, null);
        cell.placeOn( new Block());
        cell.placeOn( new Block());
        height = cell.getHeigth();
        check = cell.place( PlaceableType.WORKER );
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(3) == null );

        // free cell, try to build an unknown placeable
        cell = new Cell(1,2, null);
        height = cell.getHeigth();
        check = cell.place( PlaceableType.ANY );
        assertTrue( !check );
        assertTrue( cell.getHeigth() == height );
        assertTrue( cell.getPlaceableAt(0) == null );

    }

}