package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Placeable class, aimed to verify it works properly
 *
 * @author Marco
 */

class PlaceableTest {

    private Cell cell = new Cell(0, 0, null);
    private Placeable placeable;

    /**
     * Initialization before method's test
     */
    @BeforeEach
    void setUp() {

        placeable = new Placeable() {
            @Override
            public boolean place(Cell destination) {
                return false;
            }
        };

    }

    /**
     * Reset attribute after method's test
     */
    @AfterEach
    void tearDown() {

        placeable = null;

    }

    /**
     * Check if method setPosition( Cell ) can set the correct cell or null
     * Methods used:    position()      of  Placeable
     *                  equals( Cell )  of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void setPosition() {

        placeable.setPosition(cell);
        assertTrue( placeable.position().equals(cell));

        placeable.setPosition(null);
        assertTrue(placeable.position() == null);
    }

    /**
     * Check if position() can read the correct position
     * Methods used:    setPosition( Cell )     of Placeable
     *                  equals( Cell )          of Cell
     *
     * Black Box and White Box
     */
    @Test
    void position() {

        assertTrue( placeable.position() == null);

        placeable.setPosition(cell);
        assertTrue(placeable.position().equals(cell));

    }

    /**
     * Check if placeable can be a Block
     *
     * Black Box and White Box
     */
    @Test
    void isBlock() {

        assertTrue( (!placeable.isBlock()) );

    }


    /**
     * Check if placeable can be a Dome
     *
     * Black Box and White Box
     */
    @Test
    void isDome() {

        assertTrue( (!placeable.isDome()) );
    }

    /**
     * Check if placeable can be a Worker
     *
     * Black Box and White Box
     */
    @Test
    void isWorker() {

        assertTrue( (!placeable.isWorker()) );
    }
}