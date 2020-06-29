package it.polimi.ingsw.model.move;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.IslandBoard;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for BuildMove class, aimed to verify it works properly
 *
 * @author Marco
 */
class BuildMoveTest {

    /**
     * Check if getBlockType() can return the correct value after initialization and
     * if setBlockType can set the correct value
     * Methods used:    getCellAt(int, int )        of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void getBlockTypeAndSetBlockType() {
        BuildMove buildMove;
        Board board = new IslandBoard();
        Cell cell1;
        Cell cell2;
        boolean findError = false;

        try {
            cell1 = board.getCellAt(0,0);
            cell2 = board.getCellAt( 1,1);

            // after initialization with first constructor
            buildMove = new BuildMove(cell1, cell2, PlaceableType.BLOCK);

            assertTrue( buildMove.getBlockType() == PlaceableType.BLOCK );

            // after initialization with second constructor
            buildMove = new BuildMove(FloorDirection.ANY, LevelDirection.ANY, 0, cell2, PlaceableType.WORKER);
            assertTrue( buildMove.getBlockType() == PlaceableType.WORKER );

            // after set method
            buildMove.setBlockType(PlaceableType.DOME);
            assertTrue( buildMove.getBlockType() == PlaceableType.DOME );

        } catch (OutOfBoardException e) {
            e.printStackTrace();
            findError = true;
        }
        assertTrue( !findError );


    }
}