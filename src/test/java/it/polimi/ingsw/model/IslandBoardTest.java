package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for the methods of IslandBoard and Board
 * @author Marco
 */
class IslandBoardTest {

    private  Board board;

    /**
     * Initalization before method's Test
     */
    @BeforeEach
    void setUp() {

        board = new IslandBoard();

    }

    /**
     * Reset attribute after method's test
     */
    @AfterEach
    void tearDown() {

        board = null;

    }

    /**
     * Check if each Cell has the correct status and position after initialization
     * Methods used:        getCellAt(int, int)         of  IslandBoard
     *                      getX()                      of  Cell
     *                      getY()                      of  Cell
     *                      getStatus()                 of  Cell
     *
     * Black Box
     */
    @Test
    void initializationCheck () {
        Cell cell;
        boolean check = true;

        try
        {
            for ( int i = 0; i < 5; i++)
            {
                for ( int j = 0; j < 5; j++)
                {
                    cell = board.getCellAt( i, j );
                    assertTrue( cell.getX() == i );
                    assertTrue( cell.getY() == j );
                    assertTrue( cell.getStatus() == Cell.PossibleStatus.UNCHECKED );
                }
            }
        }
        catch ( OutOfBoardException e )
        {
            check = false;
        }
        assertTrue( check );

    }
    /**
     * Check if getCellAt( int, int ) can return the correct Cell or the correct exception when
     * the coordinates are correct or not
     * Methods used:	getX()		of  Cell
     *      			getY()		of  Cell
     * 		        	getBoard()	of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void getCellAt() {
        Cell cell;
        boolean check;

        try
        {
            assertTrue( board.getCellAt(2,2) == board.getCellAt(2,2) );
            check = true;
        }
        catch ( OutOfBoardException e )
        {
            check = false;
        }
        assertTrue( check );

        try
        {
            cell = board.getCellAt( 0, 0 );
            check = true;
            assertTrue( cell.getX() == 0 );
            assertTrue( cell.getY() == 0 );
            assertTrue( cell.getBoard().equals( board ) );
        }
        catch ( OutOfBoardException e )
        {
            check = false;
        }
        assertTrue( check );

        try
        {
            cell = board.getCellAt( 4, 4 );
            check = true;
            assertTrue( cell.getX() == 4 );
            assertTrue( cell.getY() == 4 );
            assertTrue( cell.getBoard().equals( board ) );
        }
        catch ( OutOfBoardException e )
        {
            check = false;
        }
        assertTrue( check );

        try
        {
            cell = board.getCellAt( -1, 0 );
            check = true;
            assertTrue( cell.getX() == -1 );
            assertTrue( cell.getY() == 0 );
            assertTrue( cell.getBoard().equals( board ) );
        }
        catch ( OutOfBoardException e )
        {
            check = false;
        }
        assertTrue( !check );

        try
        {
            cell = board.getCellAt( 5, 0 );
            check = true;
            assertTrue( cell.getX() == 5 );
            assertTrue( cell.getY() == 0 );
            assertTrue( cell.getBoard().equals( board ) );
        }
        catch ( OutOfBoardException e )
        {
            check = false;
        }
        assertTrue( !check );

        try
        {
            cell = board.getCellAt( 0, -1 );
            check = true;
            assertTrue( cell.getX() == 0 );
            assertTrue( cell.getY() == -1 );
            assertTrue( cell.getBoard().equals( board ) );
        }
        catch ( OutOfBoardException e )
        {
            check = false;
        }
        assertTrue( !check );

        try
        {
            cell = board.getCellAt( 0, 5 );
            check = true;
            assertTrue( cell.getX() == 0 );
            assertTrue( cell.getY() == 5 );
            assertTrue( cell.getBoard().equals( board ) );
        }
        catch ( OutOfBoardException e )
        {
            check = false;
        }
        assertTrue( !check );

    }

    /**
     * Check if getXDim() return the correct value after initialization
     *
     * Black Box and White Box
     */
    @Test
    void getXDim() {

        assertTrue( board.getXDim() == 5 );

    }

    /**
     * Check if getYDim() return the correct value after initialization
     *
     * Black Box and White Box
     */
    @Test
    void getYDim() {

        assertTrue( board.getYDim() == 5 );

    }

    /**
     * Check if getAdjacentCells can return the correct List when the Cell is or not of the Board
     * Method used:     contains( Object )          of  List<T>
     *                  isEmpty()                   of  List<T>
     *                  size()                      of  List<T>
     *
     * Black Box and White Box
     */
    @Test
    void getAdjacentCells() {
        Board board1 = new IslandBoard();
        List<Cell> cellList;
        Cell cornerCell = new Cell(0,0, board );
        Cell sideCell = new Cell(4,3, board );
        Cell middleCell = new Cell(1,3, board);
        Cell otherCell = new Cell(1,1, board1);
        Cell cornerOutCell = new Cell( 5, -1 , board );
        Cell nearCornerOutCell = new Cell( -1, 4, board );
        Cell sideOutCell = new Cell( 2, -1, board );
        Cell outCell = new Cell(2, 6, board);
        boolean check;

        try
        {
            cellList = board.getAdjacentCells( cornerCell );
            check = true;
            assertTrue( !(cellList.isEmpty()) );
            assertTrue( cellList.size() == 3 );
            assertTrue( !(cellList.contains( cornerCell )) );
            assertTrue( cellList.contains( new Cell(0,1, board)) );
            assertTrue( cellList.contains( new Cell(1,0, board)) );
            assertTrue( cellList.contains( new Cell(1,1, board)) );
        }
        catch (InvalidParameterException e)
        {
            check = false;
        }
        assertTrue( check );

        try
        {
            cellList = board.getAdjacentCells( sideCell );
            check = true;
            assertTrue( !(cellList.isEmpty()) );
            assertTrue( cellList.size() == 5 );
            assertTrue( !(cellList.contains( sideCell )) );
            assertTrue( cellList.contains( new Cell(4,2, board)) );
            assertTrue( cellList.contains( new Cell(4,4, board)) );
            assertTrue( cellList.contains( new Cell(3,2, board)) );
            assertTrue( cellList.contains( new Cell(3,3, board)) );
            assertTrue( cellList.contains( new Cell(3,4, board)) );
        }
        catch (InvalidParameterException e)
        {
            check = false;
        }
        assertTrue( check );

        try
        {
            cellList = board.getAdjacentCells( middleCell );
            check = true;
            assertTrue( !(cellList.isEmpty()) );
            assertTrue( cellList.size() == 8 );
            assertTrue( !(cellList.contains( middleCell )) );
            assertTrue( cellList.contains( new Cell(0,2, board)) );
            assertTrue( cellList.contains( new Cell(0,3, board)) );
            assertTrue( cellList.contains( new Cell(0,4, board)) );
            assertTrue( cellList.contains( new Cell(1,2, board)) );
            assertTrue( cellList.contains( new Cell(1,4, board)) );
            assertTrue( cellList.contains( new Cell(2,2, board)) );
            assertTrue( cellList.contains( new Cell(2,3, board)) );
            assertTrue( cellList.contains( new Cell(2,4, board)) );
        }
        catch (InvalidParameterException e)
        {
            check = false;
        }
        assertTrue( check );

        try
        {
            cellList = board.getAdjacentCells( otherCell );
            check = true;
            assertTrue( !(cellList.isEmpty()) );
            assertTrue( cellList.size() == 8 );
            assertTrue( !(cellList.contains( otherCell )) );
            assertTrue( cellList.contains( new Cell(0,0, board)) );
            assertTrue( cellList.contains( new Cell(0,1, board)) );
            assertTrue( cellList.contains( new Cell(0,2, board)) );
            assertTrue( cellList.contains( new Cell(1,0, board)) );
            assertTrue( cellList.contains( new Cell(1,2, board)) );
            assertTrue( cellList.contains( new Cell(2,0, board)) );
            assertTrue( cellList.contains( new Cell(2,1, board)) );
            assertTrue( cellList.contains( new Cell(2,2, board)) );
        }
        catch (InvalidParameterException e)
        {
            check = false;
        }
        assertTrue( !check );

        //tests when the coordinates of cell are out the board

        try
        {
            cellList = board.getAdjacentCells( cornerOutCell );
            check = true;
            assertTrue( !(cellList.isEmpty()) );
            assertTrue( cellList.size() == 1 );
            assertTrue( !(cellList.contains( cornerCell )) );
            assertTrue( cellList.contains( new Cell(4,1, board)) );
        }
        catch (InvalidParameterException e)
        {
            check = false;
        }
        assertTrue( check );


        try
        {
            cellList = board.getAdjacentCells( nearCornerOutCell );
            check = true;
            assertTrue( !(cellList.isEmpty()) );
            assertTrue( cellList.size() == 2 );
            assertTrue( !(cellList.contains( nearCornerOutCell )) );
            assertTrue( cellList.contains( new Cell(0,3, board)) );
            assertTrue( cellList.contains( new Cell(0,4, board)) );
        }
        catch (InvalidParameterException e)
        {
            check = false;
        }
        assertTrue( check );


        try
        {
            cellList = board.getAdjacentCells( sideOutCell );
            check = true;
            assertTrue( !(cellList.isEmpty()) );
            assertTrue( cellList.size() == 3 );
            assertTrue( !(cellList.contains( sideOutCell )) );
            assertTrue( cellList.contains( new Cell(1,0, board)) );
            assertTrue( cellList.contains( new Cell(2,0, board)) );
            assertTrue( cellList.contains( new Cell(3,0, board)) );
        }
        catch (InvalidParameterException e)
        {
            check = false;
        }
        assertTrue( check );


        try
        {
            cellList = board.getAdjacentCells( outCell );
            check = true;
            assertTrue( (cellList.isEmpty()) );
        }
        catch (InvalidParameterException e)
        {
            check = false;
        }
        assertTrue( check );

    }

    /**
     * Check if clearCells() can correctly change the status of each Cell
     * Method used:     getXDim()                       of  IslandBoard
     *                  getYDim()                       of  IslandBoard
     *                  getCellAt(int, int)             of  IslandBoard
     *                  setStatus( PossibleStatus )     of  Cell
     *                  getStatus()                     of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void clearCells() {
        int xDim;
        int yDim;
        Cell cornerUpLeft;
        Cell cornerUpRight;
        Cell cornerDownLeft;
        Cell cornerDownRight;
        Cell centreCell;
        Cell cell;
        boolean check = true;

        xDim = board.getXDim();
        yDim = board.getYDim();
        try
        {
            cornerUpLeft =  board.getCellAt(0,0 );
            cornerDownLeft =  board.getCellAt( (xDim - 1),0 );
            cornerUpRight =  board.getCellAt(0, (yDim - 1) );
            cornerDownRight =  board.getCellAt((xDim- 1), (yDim- 1) );
            centreCell = board.getCellAt( (xDim - 1)/2, (yDim -1)/2 );
            cornerUpLeft.setStatus( Cell.PossibleStatus.REACHABLE );
            cornerUpRight.setStatus( Cell.PossibleStatus.BLOCKED );
            cornerDownLeft.setStatus( Cell.PossibleStatus.UNREACHABLE );
            cornerDownRight.setStatus( Cell.PossibleStatus.UNREACHBLOCKED );
            centreCell.setStatus( Cell.PossibleStatus.UNREACHABLE );
        }
        catch (OutOfBoardException e)
        {
            check = false;
        }

        if ( check == true )
        {
            try
            {
                for ( int i = 0; i < xDim; i++ )
                {
                    for ( int j = 0; j < yDim; j++)
                    {
                        cell = board.getCellAt( i , j );
                        assertTrue( cell.getStatus() == Cell.PossibleStatus.UNCHECKED );
                    }
                }
            }
            catch ( OutOfBoardException e )
            {
                check = false;
            }
        }
        assertTrue( check );

    }
}