package it.polimi.ingsw.model;

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
     * Check if the position is correctly set or not when the Cell is free and when it is occupied by a Dome
     * Methods used:    position()          of  Placeable
     *                  place()             of  Dome
     *                  equals( Cell )      of  Cell
     *
     * Black Box
     */
    @Test
    void place() {
        Cell cell1 = new Cell(0,0,null);
        Cell cell2 = new Cell(1,1,null);
        Dome dome = new Dome();
        boolean check = false;

        check = worker.place( cell1 );
        assertTrue( check );
        assertTrue( worker.position().equals( cell1 ) );

        check = true;
        dome.place( cell2 );
        check = worker.place( cell2 );
        assertTrue( !check );
        assertTrue( worker.position().equals( cell1 ) );

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

    @Test
    void getOwner() {

        assertTrue( (worker.getOwner().equals(player)) );

        worker = new Worker(null);
        assertTrue( worker.getOwner() == null );
    }
}