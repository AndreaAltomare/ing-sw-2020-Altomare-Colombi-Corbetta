package it.polimi.ingsw.model.move;

import it.polimi.ingsw.model.board.placeables.Block;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.IslandBoard;
import it.polimi.ingsw.model.board.placeables.Dome;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import it.polimi.ingsw.model.player.worker.Worker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Move class, aimed to verify it works properly
 *
 * @author Marco
 */

class MoveTest {

    /**
     * Check if calculateFloorDirection( Cell, Cell ) can set the correct value in the constructor
     * for all the near Cells and faraway
     * Methods used:    getCellAt( int, int)    of  IslandBoard
     *                  getFloorDirection()     of  Move
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
     * Check if calculateLevelDirection( Cell, Cell ) and calculateLevelDepth( Cell, Cell) can set the correct value
     * in the constructor when the two Cells have same height (with Worker or not) or not
     * methods used: 	placeOn( Placeable )	of  Cell
     *                  getLevelDepth()         of  Move
     *                  getLevelDirection()      of  Move
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
        assertTrue( move.getLevelDepth() == 0);
        assertTrue( move.getLevelDirection() == LevelDirection.SAME );

        // cell1:               cell2: B
        cell2.placeOn( new Block() );
        move = new Move( cell1, cell2 );
        assertTrue( move.getLevelDepth() == 1);
        assertTrue( move.getLevelDirection() == LevelDirection.UP );

        move = new Move( cell2, cell1 );
        assertTrue( move.getLevelDepth() == -1);
        assertTrue( move.getLevelDirection() == LevelDirection.DOWN );;

        // workerCell: W               cell2: BBB
        cell2.placeOn( new Block() );
        cell2.placeOn( new Block() );
        workerCell.placeOn( new Worker(null) );
        move = new Move( workerCell, cell2 );
        assertTrue( move.getLevelDepth() == 3);
        assertTrue( move.getLevelDirection() == LevelDirection.UP );

        move = new Move( cell2, workerCell );
        assertTrue( move.getLevelDepth() == -3);
        assertTrue( move.getLevelDirection() == LevelDirection.DOWN );

        // cell1: BBBW               cell2: BBB
        cell1.placeOn( new Block() );
        cell1.placeOn( new Block() );
        cell1.placeOn( new Block() );
        move = new Move( cell1, cell2 );
        assertTrue( move.getLevelDepth() == 0);
        assertTrue( move.getLevelDirection() == LevelDirection.SAME );

        // cell1: BBBW               cell2: BBBD
        cell2.placeOn( new Dome() );
        move = new Move( cell1, cell2 );
        assertTrue( move.getLevelDepth() == 0);
        assertTrue( move.getLevelDirection() == LevelDirection.SAME );

        // cell1: BBBD               cell2:             cell2 faraway cell1
        cell1 = cell2;
        cell2 = new Cell(2,2, null);
        move = new Move( cell1, cell2 );
        assertTrue( move.getLevelDepth() == -3);
        assertTrue( move.getLevelDirection() == LevelDirection.DOWN );
    }

    /**
     * Check if getFloorDirection() can return the correct value after initialization with second construction
     * (the first is used in calculateFloorDirection() with getFloorDirection()to check private class )
     * and if setFloorDirection can set the correct value
     *
     * Black Box and White Box
     */
    @Test
    void getFloorDirectionAndSetFloorDirection() {
        Move move;

        move = new Move(FloorDirection.NORTH, LevelDirection.UP, 1, null);
        assertTrue( move.getFloorDirection() == FloorDirection.NORTH );

        move.setFloorDirection( FloorDirection.ANY );
        assertTrue( move.getFloorDirection() == FloorDirection.ANY );

    }

    /**
     * Check if getLevelDirection() can return the correct value after initialization with second constructor
     * (the first is used in calculateLevelDirectionAndDepth() with getLevelDirection() to check private class )
     * and if setLevelDirection( LevelDirection ) can set the correct value
     *
     * Black Box and White Box
     */
    @Test
    void getLevelDirectionAndSetLevelDirection() {
        Move move;

        move = new Move(FloorDirection.ANY, LevelDirection.UP, 1, null);
        assertTrue( move.getLevelDirection() == LevelDirection.UP );

        move.setLevelDirection(LevelDirection.NONE);
        assertTrue( move.getLevelDirection() == LevelDirection.NONE );

    }

    /**
     * Check if getLevelDepth() can return the correct value after initialization with second constructor
     * (the first is used in calculateLevelDirectionAndDepth() with getLevelDepth() to check private class )
     * and if setLevelDirection( LevelDirection ) can set the correct value
     * Methods used: 	placeOn( Placeable ) 		of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void getLevelDepthAndSetLevelDepth() {
        Move move;

        move = new Move(FloorDirection.ANY, LevelDirection.UP, 1, null);
        assertTrue( move.getLevelDepth() == 1 );

        move.setLevelDepth(-2);
        assertTrue( move.getLevelDepth() == -2 );

    }

    /**
     * Check if getSelectedCell() can return the correct cell after initialization with near Cell or not
     * and check if setSelectedCell( Cell ) can set the correct cell
     *
     * Black Box and White Box
     */
    @Test
    void getSelectedCellAndsetSelectedCell() {
        Cell cell1 = new Cell( 0, 0, null);
        Cell cell2 = new Cell( 1, 1, null);
        Cell farawayCell = new Cell( 4, 4, null );
        Move move;

        // initialization with near cell, first constructor
        move = new Move( cell1, cell2);
        assertTrue( move.getSelectedCell() == cell2 );

        // initialization with faraway cell, first constructor
        move = new Move( cell1, farawayCell );
        assertTrue( move.getSelectedCell() == farawayCell );

        // initialization with second constructor
        move = new Move(FloorDirection.ANY, LevelDirection.ANY, 0, cell1);
        assertTrue( move.getSelectedCell() == cell1 );

        // with set method
        move = new Move(FloorDirection.ANY, LevelDirection.ANY, 0, null);
        move.setSelectedCell( cell1 );
        assertTrue( move.getSelectedCell() == cell1 );

    }
}