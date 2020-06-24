package it.polimi.ingsw.model.board.placeables;

import it.polimi.ingsw.model.board.Cell;
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
     * Check if method setPosition( Cell ) and position can set and read the correct cell or null.
     * To valuate the protected method setPosition( Cell ) is used a constructor of Placeable's sub class
     * Methods used:    Block( Cell )   of  Block
     *                  equals( Cell )  of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void setPositionAndPosition() {

        // check after initialization
        assertTrue( placeable.position() == null);

        // set position with Block( Cell ) and check with position()
        placeable = new Block( cell );
        assertTrue( placeable.position().equals(cell) );

        // set null position with Block( Cell ) and check with position()
        placeable = new Block( null );
        assertTrue(placeable.position() == null);
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