package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.IslandBoard;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import it.polimi.ingsw.model.move.BuildMove;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for BuildMove class, aimed to verify it works properly
 *
 * @author Marco
 */
class BuildMoveTest {

    /**
     * Check if getBlockType() can return the correct value after initialization
     * Methods used:    getCellAt(int, int )        of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void getBlockType() {
        BuildMove buildMove;
        Board board = new IslandBoard();
        Cell cell1;
        Cell cell2;
        PlaceableType placeableType = PlaceableType.BLOCK;
        boolean checkError = false;

        try {
            cell1 = board.getCellAt(0,0);
            cell2 = board.getCellAt( 1,1);
            buildMove = new BuildMove(cell1, cell2, placeableType);

            assertTrue( buildMove.getBlockType() == placeableType );

        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue( !checkError );

    }
}