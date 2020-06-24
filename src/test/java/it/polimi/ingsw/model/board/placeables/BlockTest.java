package it.polimi.ingsw.model.board.placeables;


import it.polimi.ingsw.model.board.Cell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Block class, aimed to verify it works properly
 *
 * @author Marco
 */

class BlockTest {

    private Block block;

    /**
     * Initialization before method's test
     */
    @BeforeEach
    void setUp() {

        block = new Block();

    }

    /**
     * Reset attribute after method's test
     */
    @AfterEach
    void tearDown() {

        block = null;

    }

    /**
     * Check if the position is correctly set or not when the Cell is free and when is occupied by a Dome
     * Methods used:    position()          of  Placeable
     *                  place( Cell )       of  Dome
     *                  equals( Cell )      of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void place() {
        Cell cell = new Cell(0,0,null);
        Block block1 = new Block();
        Dome dome = new Dome();
        boolean check;

        // place a block on a free cell
        check = block.place( cell );
        assertTrue( check );
        assertTrue( block.position() == cell  );

        // place a block on a cell with a dome
        dome.place( cell );
        check = block1.place( cell );
        assertTrue( !check );
        assertTrue( block1.position() == null );

    }

    /**
     * Check if the Override is correct
     *
     * Black Box and White Box
     */
    @Test
    void isBlock() {

        assertTrue( block.isBlock() );
    }
}