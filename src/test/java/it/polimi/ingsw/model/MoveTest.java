package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Move class, aimed to verify it works properly
 *
 * @author Marco
 */

class MoveTest {

    /**
     * Check if calculateFloorDirection( Cell, Cell ) can set the correct value in the constructor
     * for all the near Cells and faraway and
     * Methods used:    getCellAt( int, int)    of  IslandBoard
     *
     * White Box
     */
    @Test
    void calculateFloorDirection() {
        Move move;
        IslandBoard board = new IslandBoard();
        Cell centralCell;
        Cell selectedCell;
        Cell cornerCell;
        int xCell;
        int yCell;
        boolean check = true;

        // selectedCell = centralCell and selectedCell = near Cell
        try
        {
            xCell = 2;
            yCell = 2;
            centralCell = board.getCellAt( xCell, yCell );

            selectedCell = centralCell;
            move = new Move( centralCell, selectedCell );
            assertTrue( move.floorDirection == FloorDirection.NONE );

            selectedCell = board.getCellAt( (xCell - 1), yCell );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.floorDirection == FloorDirection.NORTH );

            selectedCell = board.getCellAt( (xCell - 1), (yCell -1) );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.floorDirection == FloorDirection.NORTH_WEST );

            selectedCell = board.getCellAt( xCell, (yCell - 1) );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.floorDirection == FloorDirection.WEST );

            selectedCell = board.getCellAt( (xCell + 1), (yCell - 1) );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.floorDirection == FloorDirection.SOUTH_WEST );

            selectedCell = board.getCellAt( (xCell + 1) , yCell );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.floorDirection == FloorDirection.SOUTH );

            selectedCell = board.getCellAt( (xCell + 1), (yCell + 1) );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.floorDirection == FloorDirection.SOUTH_EAST );

            selectedCell = board.getCellAt( xCell, (yCell + 1) );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.floorDirection == FloorDirection.EAST );

            selectedCell = board.getCellAt( (xCell - 1), (yCell + 1) );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.floorDirection == FloorDirection.NORTH_EAST );

        }
        catch ( OutOfBoardException e )
        {
            check = false;
        }
        assertTrue( check );

        // selectCell = faraway Cell
        try
        {
            check = true;
            xCell = 0;
            yCell = 0;
            cornerCell = board.getCellAt( xCell, yCell );

            selectedCell = board.getCellAt( (xCell + 4), yCell );
            move = new Move( cornerCell, selectedCell );
            assertTrue( move.floorDirection == FloorDirection.SOUTH );

            selectedCell = board.getCellAt( (xCell + 4), (yCell + 1) );
            move = new Move( cornerCell, selectedCell );
            assertTrue( move.floorDirection == FloorDirection.SOUTH_EAST );

            selectedCell = board.getCellAt( (xCell + 4), (yCell + 4) );
            move = new Move( cornerCell, selectedCell );
            assertTrue( move.floorDirection == FloorDirection.SOUTH_EAST );

            selectedCell = board.getCellAt( (xCell + 1), (yCell + 4) );
            move = new Move( cornerCell, selectedCell );
            assertTrue( move.floorDirection == FloorDirection.SOUTH_EAST );

            selectedCell = board.getCellAt( xCell, (yCell + 4) );
            move = new Move( cornerCell, selectedCell );
            assertTrue( move.floorDirection == FloorDirection.EAST );

        }
        catch ( OutOfBoardException e )
        {
            check = false;
        }
        assertTrue( check );

    }

    /**
     * Check if calculateLevelDirection( Cell, Cell ) and calculateLevelDepth( Cell, Cell) can set the correct value
     * in the constructor when the two Cells have same height (with Worker or not) or not
     * methods used: 	placeOn( Placeable )	of  Cell
     *
     * White Box
     */
    @Test
    void  calculateLevelDirectionAndDepth() {
        Move move;
        Cell cell1 = new Cell( 0, 0, null);
        Cell cell2 = new Cell( 1, 1, null);
        Cell workerCell = new Cell( 2, 2, null);

        // cell1:               cell2:
        move = new Move( cell1, cell2 );
        assertTrue( move.levelDepth == 0);
        assertTrue( move.levelDirection == LevelDirection.SAME );

        // cell1:               cell2: B
        cell2.placeOn( new Block() );
        move = new Move( cell1, cell2 );
        assertTrue( move.levelDepth == 1);
        assertTrue( move.levelDirection == LevelDirection.UP );

        move = new Move( cell2, cell1 );
        assertTrue( move.levelDepth == -1);
        assertTrue( move.levelDirection == LevelDirection.DOWN );;

        // workerCell: W               cell2: BBB
        cell2.placeOn( new Block() );
        cell2.placeOn( new Block() );
        workerCell.placeOn( new Worker (null) );
        move = new Move( workerCell, cell2 );
        assertTrue( move.levelDepth == 3);
        assertTrue( move.levelDirection == LevelDirection.UP );

        move = new Move( cell2, workerCell );
        assertTrue( move.levelDepth == -3);
        assertTrue( move.levelDirection == LevelDirection.DOWN );

        // cell1: BBBW               cell2: BBB
        cell1.placeOn( new Block() );
        cell1.placeOn( new Block() );
        cell1.placeOn( new Block() );
        cell1.placeOn( new Worker (null) );
        move = new Move( cell1, cell2 );
        assertTrue( move.levelDepth == 0);
        assertTrue( move.levelDirection == LevelDirection.SAME );

    }

    /**
     * Check if getFloorDirection() can return the correct value after initialization
     * Methods used:    getCellAt( int, int)    of  IslandBoard
     *
     * Black Box and White Box
     */
    @Test
    void getFloorDirection() {
        Move move;
        IslandBoard board = new IslandBoard();
        Cell centralCell;
        Cell selectedCell;
        Cell cornerCell;
        int xCell;
        int yCell;
        boolean check = true;

        // selectedCell = centralCell and selectedCell = near Cell
        try
        {
            xCell = 2;
            yCell = 2;
            centralCell = board.getCellAt( xCell, yCell );

            selectedCell = centralCell;
            move = new Move( centralCell, selectedCell );
            assertTrue( move.getFloorDirection() == FloorDirection.NONE );

            selectedCell = board.getCellAt( (xCell - 1), yCell );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.getFloorDirection() == FloorDirection.NORTH );

            selectedCell = board.getCellAt( (xCell - 1), (yCell -1) );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.getFloorDirection() == FloorDirection.NORTH_WEST );

            selectedCell = board.getCellAt( xCell, (yCell - 1) );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.getFloorDirection() == FloorDirection.WEST );

            selectedCell = board.getCellAt( (xCell + 1), (yCell - 1) );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.getFloorDirection() == FloorDirection.SOUTH_WEST );

            selectedCell = board.getCellAt( (xCell + 1) , yCell );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.getFloorDirection() == FloorDirection.SOUTH );

            selectedCell = board.getCellAt( (xCell + 1), (yCell + 1) );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.getFloorDirection() == FloorDirection.SOUTH_EAST );

            selectedCell = board.getCellAt( xCell, (yCell + 1) );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.getFloorDirection() == FloorDirection.EAST );

            selectedCell = board.getCellAt( (xCell - 1), (yCell + 1) );
            move = new Move( centralCell, selectedCell );
            assertTrue( move.getFloorDirection() == FloorDirection.NORTH_EAST );

        }
        catch ( OutOfBoardException e )
        {
            check = false;
        }
        assertTrue( check );

        // selectCell = faraway Cell
        try
        {
            check = true;
            xCell = 0;
            yCell = 0;
            cornerCell = board.getCellAt( xCell, yCell );

            selectedCell = board.getCellAt( (xCell + 4), yCell );
            move = new Move( cornerCell, selectedCell );
            assertTrue( move.getFloorDirection() == FloorDirection.SOUTH );

            selectedCell = board.getCellAt( (xCell + 4), (yCell + 1) );
            move = new Move( cornerCell, selectedCell );
            assertTrue( move.getFloorDirection() == FloorDirection.SOUTH_EAST );

            selectedCell = board.getCellAt( (xCell + 4), (yCell + 4) );
            move = new Move( cornerCell, selectedCell );
            assertTrue( move.getFloorDirection() == FloorDirection.SOUTH_EAST );

            selectedCell = board.getCellAt( (xCell + 1), (yCell + 4) );
            move = new Move( cornerCell, selectedCell );
            assertTrue( move.getFloorDirection() == FloorDirection.SOUTH_EAST );

            selectedCell = board.getCellAt( xCell, (yCell + 4) );
            move = new Move( cornerCell, selectedCell );
            assertTrue( move.getFloorDirection() == FloorDirection.EAST );

        }
        catch ( OutOfBoardException e )
        {
            check = false;
        }
        assertTrue( check );

    }

    /**
     * Check if getLevelDirection() can return the correct value after initialization (with Worker)
     * Methods used: 	placeOn( Placeable ) 		of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void getLevelDirection() {
        Move move;
        Cell cell = new Cell( 0, 0, null );
        Cell downCell = new Cell( 1, 1, null );
        Cell sameCell = new Cell( 2, 2, null );
        Cell upCell = new Cell( 3, 3, null );
        LevelDirection levelDirection;

        cell.placeOn( new Block() );
        cell.placeOn( new Block() );
        cell.placeOn( new Worker( null ) );
        move = new Move( cell, downCell );
        levelDirection = move.getLevelDirection();
        assertTrue( levelDirection == LevelDirection.DOWN );

        sameCell.placeOn( new Block() );
        sameCell.placeOn( new Block() );
        sameCell.placeOn( new Worker( null ) );
        move = new Move( cell, sameCell );
        levelDirection = move.getLevelDirection();
        assertTrue( levelDirection == LevelDirection.SAME );

        upCell.placeOn( new Block() );
        upCell.placeOn( new Block() );
        upCell.placeOn( new Block() );
        move = new Move( cell, sameCell );
        levelDirection = move.getLevelDirection();
        assertTrue( levelDirection == LevelDirection.UP );

    }

    /**
     * Check if getSelectedCell() can return the correct cell after initialization with near Cell or not
     *
     * Black Box and White Box
     */
    @Test
    void getSelectedCell() {
        Cell cell1 = new Cell( 0, 0, null);
        Cell cell2 = new Cell( 1, 1, null);
        Cell farawayCell = new Cell( 4, 4, null );
        Move move;

        move = new Move( cell1, cell2);
        assertTrue( move.getSelectedCell() == cell2 );

        move = new Move( cell1, farawayCell );
        assertTrue( move.getSelectedCell() == farawayCell );

    }
}