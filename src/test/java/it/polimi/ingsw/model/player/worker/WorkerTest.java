package it.polimi.ingsw.model.player.worker;

import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.placeables.Dome;
import it.polimi.ingsw.model.player.worker.ChooseType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Worker class, aimed to verify it works properly
 *
 * @author Marco
 */

class WorkerTest {

    private Worker worker;
    private Player player = new Player("cpu1");
    /**
     * Initialization before method's test
     */
    @BeforeEach
    void setUp() {

        worker = new Worker(player);

    }

    /**
     * Reset attribute after method's test
     */
    @AfterEach
    void tearDown() {

        worker = null;

    }

    /**
     * Check if the position is correctly set or not on the Cell
     * Methods used:    position()              of  Placeable
     *                  equals( Cell )          of  Cell
     *                  buildBlock()            of  Cell
     *                  buildDome()             of  Cell
     *                  getHeight()             of  Cell
     *                  getPlaceabeAt( int )    of  Cell
     *                  repOk()                 of  Cell
     *                  isBLock()               of  Placeable
     *                  isDome()                of  Placeable
     *
     * Black Box and White Box
     */
    @Test
    void place() {
        Cell cell = new Cell(0,0,null);
        Cell blockCell = new Cell(1,1, null);
        blockCell.buildBlock();
        Cell domeCell = new Cell(2,2,null);
        domeCell.buildDome();
        Cell workerCell = new Cell(3,3, null);
        Worker otherWorker = new Worker( new Player(null) );
        boolean check;

        // place worker on free cell
        check = worker.place( cell );
        assertTrue( check );
        assertTrue( worker.position().equals(cell) );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( cell.getPlaceableAt(0).equals(worker) );
        assertTrue( cell.repOk() );

        // place worker on cell where it is
        check = worker.place( cell );
        assertTrue( check );
        assertTrue( worker.position().equals(cell) );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( cell.getPlaceableAt(0).equals(worker) );
        assertTrue( cell.repOk() );

        // not place worker on cell with dome
        check = worker.place( domeCell );
        assertTrue( !check );
        assertTrue( worker.position().equals(cell) );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( cell.getPlaceableAt(0).equals(worker) );
        assertTrue( cell.repOk() );
        assertTrue( domeCell.getHeigth() == 1 );
        assertTrue( domeCell.getPlaceableAt(0).isDome() );
        assertTrue( domeCell.repOk() );

        // not place worker on cell with another worker
        otherWorker.place( workerCell );
        check = worker.place( workerCell );
        assertTrue( !check );
        assertTrue( worker.position().equals(cell) );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( cell.getPlaceableAt(0).equals(worker) );
        assertTrue( cell.repOk() );
        assertTrue( workerCell.getHeigth() == 1 );
        assertTrue( workerCell.getPlaceableAt(0).equals(otherWorker) );
        assertTrue( workerCell.repOk() );

        // place worker on cell with block
        check = worker.place( blockCell );
        assertTrue( check );
        assertTrue( worker.position().equals(blockCell) );
        assertTrue( cell.getHeigth() == 0 );
        assertTrue( cell.getPlaceableAt(0) == null );
        assertTrue( cell.repOk() );
        assertTrue( workerCell.getHeigth() == 2 );
        assertTrue( workerCell.getPlaceableAt(1).equals(worker) );
        assertTrue( workerCell.getPlaceableAt(0).isBlock() );
        assertTrue( workerCell.repOk() );
    }

    /**
     * Check if the Override is correct
     *
     * Black Box and White Box
     */
    @Test
    void isWorker() {

        assertTrue( worker.isWorker() );
    }

    /**
     * Check if getOwner() can return the correct Player after initialization
     *
     * Black Box and White Box
     */
    @Test
    void getOwner() {

        assertTrue( (worker.getOwner().equals(player)) );

        worker = new Worker(null);
        assertTrue( worker.getOwner() == null );
    }

    /**
     * Check if isChosen() can return the correct value after initialization and after set
     * Methods used:    setChosen( ChooseType )         of  Worker
     *
     * Black Box and White Box
     */
    @Test
    void isChosen() {
        boolean check;

        check = worker.isChosen();
        assertTrue( !check );

        worker.setChosen( ChooseType.CHOSEN );
        check = worker.isChosen();
        assertTrue( check );

    }

    /**
     * Check if getChosenStatus() can return the correct ChooseType after initialization and after set
     * Methods used:    setChosen( ChooseType )         of  Worker
     *
     * Black Box and White Box
     */
    @Test
    void getChosenStatus() {

        assertTrue( worker.getChosenStatus() == ChooseType.CAN_BE_CHOSEN );

        worker.setChosen( ChooseType.CHOSEN );
        assertTrue( worker.getChosenStatus() == ChooseType.CHOSEN);

    }

    /**
     * Check if setChosen( ChooseType ) can correctly set
     * Methods used:    getChosenStatus()       of  Worker
     *
     * Black Box and White Box
     */
    @Test
    void setChosen() {

        worker.setChosen( ChooseType.NOT_CHOSEN );
        assertTrue( worker.getChosenStatus() == ChooseType.NOT_CHOSEN );

        worker.setChosen( ChooseType.CHOSEN);
        assertTrue( worker.getChosenStatus() == ChooseType.CHOSEN );

    }

    /**
     * Check if toString(), getWorkerId(), setId( String ) and resetIdAndColorIndex() can return, set and reset workers'id
     * using equals and startWith( String ) to compare them and to check returned String
     * Methods used:        startWith( String )     of  String
     *                      equals( Object )        of  String
     *
     * Black Box and White Box
     */
    @Test
    void toStringAndGetAndSetAndResetId() {
        final String PREFIX = "[Worker]";
        Worker firstWorker = new Worker(player);
        Worker secondWorker = new Worker(null, player);

        // check if their Id start with [Worker]
        assertTrue( firstWorker.getWorkerId().startsWith(PREFIX) );
        assertTrue( secondWorker.getWorkerId().startsWith(PREFIX) );
        assertTrue( firstWorker.toString().startsWith(PREFIX) );
        assertTrue( secondWorker.toString().startsWith(PREFIX) );

        // check that Ids are different after initialization
        assertTrue( !(firstWorker.getWorkerId().equals(secondWorker.getWorkerId())) );
        assertTrue( !(firstWorker.toString().equals(secondWorker.toString())) );

        // check after setId( String ) (try to set a worker id like the other )
        firstWorker.setId( secondWorker.getWorkerId() );
        assertTrue( firstWorker.getWorkerId().equals(secondWorker.getWorkerId()) );

        // check after resetIdAndColorIndex()
        // ( make two new workers resetting their id  before initialization and compare them )
        Worker.resetIdAndColorIndex();
        firstWorker = new Worker(player);
        Worker.resetIdAndColorIndex();
        secondWorker = new Worker(player);

        assertTrue( firstWorker.getWorkerId().equals(secondWorker.getWorkerId()) );

    }

    /**
     * Check if getColor(), setColor( Color ), registerColor() and resetIdAndColorIndex() can return, set and reset
     * workers'color comparing the returned Color ( three or more couple of colors )
     *
     * Black Box and White Box
     */
    @Test
    void getAndSetAndRegisterColorAndResetColorIndex() {
        Worker worker1 = new Worker(player);
        Worker worker2 = new Worker(null, player);
        Worker worker3 = new Worker(player);
        Worker worker4 = new Worker(player);
        Worker worker5 = new Worker(player);
        Worker worker6 = new Worker(player);
        Worker worker7 = new Worker(player);
        Worker worker8 = new Worker(player);
        Worker worker9 = new Worker(player);

        // check color after initialization
        assertTrue( worker1.getColor() == null );
        assertTrue( worker2.getColor() == null );
        assertTrue( worker3.getColor() == null );
        assertTrue( worker4.getColor() == null );
        assertTrue( worker5.getColor() == null );
        assertTrue( worker6.getColor() == null );

        // check after registerColor()
        worker1.registerColor();
        worker2.registerColor();
        worker3.registerColor();
        worker4.registerColor();
        worker5.registerColor();
        worker6.registerColor();
        assertTrue( worker1.getColor() == worker2.getColor() );
        assertTrue( worker3.getColor() == worker4.getColor() );
        assertTrue( worker5.getColor() == worker6.getColor() );
        assertTrue( worker1.getColor() != worker3.getColor() );
        assertTrue( worker1.getColor() != worker5.getColor() );
        assertTrue( worker3.getColor() != worker5.getColor() );

        // check after setColor()
        worker5.setColor( worker1.getColor() );
        assertTrue( worker1.getColor() == worker5.getColor() );

        // check after resetColorIndex
        Worker.resetIdAndColorIndex();
        worker7.registerColor();
        worker8.registerColor();
        worker9.registerColor();
        assertTrue( worker1.getColor() == worker7.getColor() );
        assertTrue( worker2.getColor() == worker8.getColor() );
        assertTrue( worker3.getColor() == worker9.getColor() );


    }

}

