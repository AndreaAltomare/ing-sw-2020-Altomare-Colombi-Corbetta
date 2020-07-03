package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.board.placeables.Block;
import it.polimi.ingsw.model.board.placeables.Dome;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.worker.Worker;
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

    private Board board;

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
     * IslandBoard Test
     * Check if each Cell has the correct status and position after initialization
     * Methods used:        getCellAt(int, int)         of  IslandBoard
     *                      getX()                      of  Cell
     *                      getY()                      of  Cell
     *                      getBoard()                  of  Cell
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
                    assertTrue( cell.getBoard().equals(board));
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
     * IslandBoard Test
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

        // a middle cell
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

        // first cell
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

        // last cell
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

        // cell with x coordinate to low
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

        // cell with x coordinate to high
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

        // cell with y coordinate to low
        try
        {
            cell = board.getCellAt( 5, -1 );
            check = true;
            assertTrue( cell.getX() == 5 );
            assertTrue( cell.getY() == -1 );
            assertTrue( cell.getBoard().equals( board ) );
        }
        catch ( OutOfBoardException e )
        {
            check = false;
        }
        assertTrue( !check );

        // cell with y coordinate to high
        try
        {
            cell = board.getCellAt( 4, 5 );
            check = true;
            assertTrue( cell.getX() == 4 );
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
     * IslandBoardTest
     * Check if getXDim() return the correct value after initialization
     *
     * Black Box and White Box
     */
    @Test
    void getXDim() {

        assertTrue( board.getXDim() == 5 );

    }

    /**
     * IslandBoard Test
     * Check if getYDim() return the correct value after initialization
     *
     * Black Box and White Box
     */
    @Test
    void getYDim() {

        assertTrue( board.getYDim() == 5 );

    }

    /**
     * Board Test on IslandBoard
     * Check if getAdjacentCells can return the correct List when the Cell is or not of the Board
     * Method used:     contains( Object )          of  List
     *                  isEmpty()                   of  List
     *                  size()                      of  List
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

        // corner cell
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

        // side cell
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

        // middle cell
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

        // other cell
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

        //***tests when the coordinates of cell are out the board***//

        // corner out cell
        try
        {
            cellList = board.getAdjacentCells( cornerOutCell );
            check = true;
            //it shouldn't do so...
            assertTrue( !(cellList.isEmpty()) );
            assertTrue( cellList.size() == 1 );
            assertTrue( !(cellList.contains( cornerCell )) );
            assertFalse( cellList.contains( new Cell(4,1, board)) );
        }
        catch (InvalidParameterException e)
        {
            check = false;
        }
        assertTrue( check );

        // near corner out cell
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

        // side out cell
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

        // out cell
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
     * Board Test on IslandBoard
     * Check if removeWorkers( PLayer ) can remove only the workers of selected player if there aren't
     * and doesn't modify the other cell
     * Methods used:        getCellAt( int, int)            of  IslandBoard
     *                      getXDim()                       of  IslandBoard
     *                      getYDim()                       of  IslandBoard
     *                      equals( Object )                of  Cell
     *                      placeOn( PLaceable)             of  Cell
     *                      getHeigth()                     of  Cell
     *                      getPLaceableAt( int )           of  Cell
     *                      isBlock()                       of  Placeable
     *                      isDome()                        of  Placeable
     *
     * Black Box and White Box
     */
    @Test
    void removeWorkers() {
        Player player = new Player( "player1");
        Player opponent = new Player( "opponent");
        Cell cell;
        Cell blocksCell; //two block
        Cell domeCell; //one block and a dome
        Cell workerPLayerCell1;
        Cell workerPLayerCell2;
        Cell workerOpponentCell1;
        Cell workerOpponentCell2;
        Worker workerPlayer1 = new Worker( player );
        Worker workerPLayer2 = new Worker( player );
        Worker workerPLayer3 = new Worker( player );
        Worker workerOpponent1 = new Worker( opponent );
        Worker workerOpponent2 = new Worker( opponent );
        boolean errorCheck = true;

        try {
            //***board without worker***//
            blocksCell = board.getCellAt(0,0);
            blocksCell.placeOn( new Block() );
            blocksCell.placeOn( new Block() );
            domeCell = board.getCellAt(1,4);
            domeCell.placeOn( new Block() );
            domeCell.placeOn( new Dome() );

            board.removeWorkers( player );

            // check blocksCell
            assertTrue( blocksCell.getHeigth() == 2);
            assertTrue( blocksCell.getPlaceableAt(0).isBlock() );
            assertTrue( blocksCell.getPlaceableAt(1).isBlock() );
            assertTrue( blocksCell.getPlaceableAt(2) == null );
            // check domeCell
            assertTrue( domeCell.getHeigth() == 2);
            assertTrue( domeCell.getPlaceableAt(0).isBlock() );
            assertTrue( domeCell.getPlaceableAt(1).isDome() );
            assertTrue( domeCell.getPlaceableAt(2) == null );
            // check other Cells
            for ( int i = 0; i < board.getXDim(); i++) {
                for ( int j = 0; j < board.getYDim(); j++) {
                    cell = board.getCellAt(i, j);
                    if ( !( cell.equals(blocksCell) || cell.equals(domeCell) ) ) {
                        assertTrue( cell.getHeigth() == 0);
                        assertTrue( cell.getPlaceableAt(0) == null);
                    }
                }
            }


            //***board with worker of player and opponent***//
            workerPLayerCell1 = board.getCellAt(4,4);
            workerPLayerCell1.placeOn( workerPlayer1 );
            workerPLayerCell2 = board.getCellAt(1,3);
            workerPLayerCell2.placeOn( workerPLayer2 );
            // player's worker on blocksCell
            blocksCell.placeOn( workerPLayer3 );
            workerOpponentCell1 = board.getCellAt(0,4); // with a block and opponent's worker
            workerOpponentCell1.placeOn( new Block() );
            workerOpponentCell1.placeOn( workerOpponent1 );
            workerOpponentCell2 = board.getCellAt(4,0);
            workerOpponentCell2.placeOn( workerOpponent2 );

            board.removeWorkers( player );
            // check blocksCell
            assertTrue( blocksCell.getHeigth() == 2);
            assertTrue( blocksCell.getPlaceableAt(0).isBlock() );
            assertTrue( blocksCell.getPlaceableAt(1).isBlock() );
            assertTrue( blocksCell.getPlaceableAt(2) == null );
            // check domeCell
            assertTrue( domeCell.getHeigth() == 2);
            assertTrue( domeCell.getPlaceableAt(0).isBlock() );
            assertTrue( domeCell.getPlaceableAt(1).isDome() );
            assertTrue( domeCell.getPlaceableAt(2) == null );
            // check workerPLayerCell1
            assertTrue( workerPLayerCell1.getHeigth() == 0);
            assertTrue( workerPLayerCell1.getPlaceableAt(0) == null );
            // check workerPLayerCell2
            assertTrue( workerPLayerCell2.getHeigth() == 0);
            assertTrue( workerPLayerCell2.getPlaceableAt(0) == null );
            // check workerOpponentCell1
            assertTrue( workerOpponentCell1.getHeigth() == 2);
            assertTrue( workerOpponentCell1.getPlaceableAt(0).isBlock() );
            assertTrue( workerOpponentCell1.getPlaceableAt(1).equals( workerOpponent1 ) );
            assertTrue( workerOpponentCell1.getPlaceableAt(2) == null );
            // check workerOpponentCell2
            assertTrue( workerOpponentCell2.getHeigth() == 1);
            assertTrue( workerOpponentCell2.getPlaceableAt(0).equals( workerOpponent2 ) );
            assertTrue( workerOpponentCell2.getPlaceableAt(1) == null );
            // check other Cells
            for ( int i = 0; i < board.getXDim(); i++) {
                for ( int j = 0; j < board.getYDim(); j++) {
                    cell = board.getCellAt(i, j);
                    if ( !( cell.equals(blocksCell) ||
                            cell.equals(domeCell) ||
                            cell.equals( workerPLayerCell1 ) ||
                            cell.equals( workerPLayerCell2 ) ||
                            cell.equals( workerOpponentCell1 ) ||
                            cell.equals( workerOpponentCell2 ) ) ) {
                        assertTrue( cell.getHeigth() == 0);
                        assertTrue( cell.getPlaceableAt(0) == null);
                    }
                }
            }


        } catch (OutOfBoardException e) {
            errorCheck = false;
        }
        assertTrue( errorCheck );

    }
    /**
     * Board Test on IslandBoard
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
        boolean errorCheck = true;

        xDim = board.getXDim();
        yDim = board.getYDim();
        try
        {
            // initialization cell
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


            board.clearCells();

            // check cells' status
            for ( int i = 0; i < xDim; i++ )
            {
                for ( int j = 0; j < yDim; j++)
                {
                    cell = board.getCellAt( i , j );
                    assertTrue( cell.getStatus() == Cell.PossibleStatus.UNCHECKED );
                }
            }
        }
        catch (OutOfBoardException e)
        {
            errorCheck = false;
        }
        assertTrue( errorCheck );

    }

    /**
     * Board Test on Island Board
     * Check if clear can remove all placeables on cells' board
     * Methods used:        getCellAt( int, int)            of  IslandBoard
     *                      getXDim()                       of  IslandBoard
     *                      getYDim()                       of  IslandBoard
     *                      placeOn( PLaceable)             of  Cell
     *                      getHeigth()                     of  Cell
     *                      getPLaceableAt( int )           of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void clear() {
        Player player = new Player( "player1");
        Player opponent = new Player( "opponent");
        Cell cell;
        Cell blocksCell; //two block
        Cell domeCell; //one block and a dome
        Cell towerCell;
        Cell workerPLayerCell1;
        Cell workerOpponentCell1;
        Worker workerPlayer1 = new Worker( player );
        Worker workerOpponent1 = new Worker( opponent );
        boolean errorCheck = true;

        try {

            blocksCell = board.getCellAt(0,0);
            blocksCell.placeOn( new Block() );
            blocksCell.placeOn( new Block() );
            domeCell = board.getCellAt(1,4);
            domeCell.placeOn( new Block() );
            domeCell.placeOn( new Dome() );
            towerCell = board.getCellAt(2,2);
            towerCell.placeOn( new Block() );
            towerCell.placeOn( new Block() );
            towerCell.placeOn( new Block() );
            towerCell.placeOn( new Dome() );
            workerPLayerCell1 = board.getCellAt(4,4);
            workerPLayerCell1.placeOn( workerPlayer1 );
            workerOpponentCell1 = board.getCellAt(0,4); // with a block and opponent's worker
            workerOpponentCell1.placeOn( new Block() );
            workerOpponentCell1.placeOn( workerOpponent1 );


            board.clear();


            for ( int i = 0; i < board.getXDim(); i++) {
                for ( int j = 0; j < board.getYDim(); j++) {
                    cell = board.getCellAt(i, j);
                        assertTrue( cell.getHeigth() == 0);
                        assertTrue( cell.getPlaceableAt(0) == null);
                }
            }


        } catch (OutOfBoardException e) {
            errorCheck = false;
        }
        assertTrue( errorCheck );


    }
}