package it.polimi.ingsw.model;

import com.sun.org.apache.xpath.internal.objects.XBoolean;
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
        Dome dome = new Dome();
        boolean check = false;

        check = block.place( cell );
        assertTrue( check );
        assertTrue( block.position().equals( cell ) );

        check = true;
        dome.place( cell );
        check = block.place( cell );
        assertTrue( !check );
        assertTrue( block.position() == null );

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