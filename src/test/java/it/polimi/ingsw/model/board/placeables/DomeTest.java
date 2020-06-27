package it.polimi.ingsw.model.board.placeables;

import it.polimi.ingsw.model.board.Cell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Dome class, aimed to verify it works properly
 *
 * @author Marco
 */

class DomeTest {

    private Dome dome;

    /**
     * Initialization before method's test
     */
    @BeforeEach
    void setUp() {

        dome = new Dome();

    }

    /**
     * Reset attribute after method's test
     */
    @AfterEach
    void tearDown() {

        dome = null;

    }

    /**
     * Check if the position is correctly set or not when the Cell is free and when is occupied by a Dome
     * Methods used:    position()          of  Placeable
     *                  equals( Cell )      of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void place() {
        Cell cell = new Cell(0,0, null);
        Dome dome1 = new Dome();
        boolean check;

        // place a dome on a free cell
        check = dome.place( cell );
        assertTrue( check );
        assertTrue( dome.position() == cell  );

        // not place a dome on a cell with dome
        check = dome1.place( cell );
        assertTrue( !check );
        assertTrue( dome1.position() == null );

    }

    /**
     * Check if the Override is correct
     *
     * Black Box and White Box
     */
    @Test
    void isDome() {

        assertTrue(dome.isDome());

    }
}