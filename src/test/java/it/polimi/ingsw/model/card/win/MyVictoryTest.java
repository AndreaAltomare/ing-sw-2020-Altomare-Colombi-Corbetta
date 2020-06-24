package it.polimi.ingsw.model.card.win;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.IslandBoard;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardParser;
import it.polimi.ingsw.model.card.GodPower;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import it.polimi.ingsw.model.move.FloorDirection;
import it.polimi.ingsw.model.move.LevelDirection;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for MyMove class, aimed to verify it works properly
 *
 * @author Marco
 */
class MyVictoryTest {

    boolean checkWin;

    MyVictory myVictory;
    Move move;
    Player player = new Player( "Player1");
    Board board;
    Cell cell;
    Cell nearCell;
    Worker worker;

    GodPower human;
    Card humanCard;

    GodPower pan;
    Card panCard;

    /**
     * Initialization before method's test
     * Methods used:        getCellAt( int, int)            of  IslandBoard
     */
    @BeforeEach
    void setUp() {

        checkWin = false;

        boolean checkErrorBefore = false;
        board = new IslandBoard();
        try {
            cell = board.getCellAt( 2,2 );
            nearCell = board.getCellAt(2,3);
        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkErrorBefore = true;
        }
        assertTrue( !checkErrorBefore );
        worker = new Worker( player );

        // god and card without power initialization
        human = new GodPower(); // CardGod without any power
        human.setName("Default");
        human.setEpithet("Default");
        human.setDescription("Default");
        human.setMovementsLeft(1);
        human.setConstructionLeft(1);
        human.setHotLastMoveDirection(LevelDirection.NONE);
        human.setForceOpponentInto(FloorDirection.NONE);
        human.setDeniedDirection(LevelDirection.NONE);
        human.setOpponentDeniedDirection(LevelDirection.NONE);
        humanCard = new Card(   human,
                                CardParser.getMoveCheckers(human),
                                CardParser.getMoveExecutor(human),
                                CardParser.getBuildCheckers(human),
                                CardParser.getBuildExecutor(human),
                                CardParser.getWinCheckers(human),
                                CardParser.getAdversaryMoveCheckers(human));

        // Pan's GodPower and Card initialization
        pan = new GodPower();
        pan.setName("Pan");
        pan.setEpithet("God of the Wild");
        pan.setDescription("P");
        pan.setMovementsLeft(1);
        pan.setConstructionLeft(1);
        pan.setMustObey(true);
        pan.setHotLastMoveDirection(LevelDirection.DOWN);
        pan.setForceOpponentInto(FloorDirection.NONE);
        pan.setDeniedDirection(LevelDirection.NONE);
        pan.setOpponentDeniedDirection(LevelDirection.NONE);
        pan.setNewVictoryCondition(true);
        pan.setHotLevelDepth(-2);
        panCard = new Card( pan,
                            CardParser.getMoveCheckers(pan),
                            CardParser.getMoveExecutor(pan),
                            CardParser.getBuildCheckers(pan),
                            CardParser.getBuildExecutor(pan),
                            CardParser.getWinCheckers(pan),
                            CardParser.getAdversaryMoveCheckers(pan));


    }

    /**
     * Reset attribute after method's test
     */
    @AfterEach
    void tearDown() {

        myVictory = null;
        move = null;
        board = null;
        cell = null;
        nearCell = null;
        worker = null;

        human = null;
        humanCard = null;

        pan = null;
        panCard = null;

    }

    /**
     * Check if the default victory condition  on board are correctly checked
     * Methods used:        getMyVictory()          of  Card
     *                      getHeigth()             of  Cell
     *                      repOk()                 of  Cell
     *                      getTop()                of  Cell
     *                      getPlaceableAt( int )   of  Cell
     *                      buildBlock()            of  Cell
     *                      position()              of  Placeable
     *                      isBlock()               of  Placeable/Block
     *                      place( Cell )           of  Worker
     *
     * Black Box
     */
    @Test
    void checkMoveDefaultBlack() {
        myVictory = humanCard.getMyVictory();


        /* can win when go up from Cell with two Blocks to near Cell with three Blocks*/
        cell.buildBlock();
        cell.buildBlock();
        nearCell.buildBlock();
        nearCell.buildBlock();
        nearCell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        worker.place(nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( checkWin );
        assertTrue( cell.getHeigth() == 2 );
        assertTrue( nearCell.getHeigth() == 4 );
        assertTrue( cell.getTop().isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().equals(worker) );
        assertTrue( nearCell.getTop().isBlock() );
        assertTrue( nearCell.getPlaceableAt(2).isBlock() );
        assertTrue( nearCell.getPlaceableAt(1).isBlock() );
        assertTrue( nearCell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(nearCell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }


        /* can't win when go up from Cell with one Blocks to near Cell with two Blocks*/
        cell.buildBlock();
        nearCell.buildBlock();
        nearCell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        worker.place(nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( !checkWin );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell.getHeigth() == 3 );
        assertTrue( cell.getTop().isBlock() );
        assertTrue( nearCell.getTop().equals(worker) );
        assertTrue( nearCell.getPlaceableAt(1).isBlock() );
        assertTrue( nearCell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(nearCell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }


        /* can't win when go from Cell with three Blocks to near Cell with three Blocks*/
        cell.buildBlock();
        cell.buildBlock();
        cell.buildBlock();
        nearCell.buildBlock();
        nearCell.buildBlock();
        nearCell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        worker.place(nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( !checkWin );
        assertTrue( cell.getHeigth() == 3 );
        assertTrue( nearCell.getHeigth() == 4 );
        assertTrue( cell.getTop().isBlock() );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().equals(worker) );
        assertTrue( nearCell.getPlaceableAt(2).isBlock() );
        assertTrue( nearCell.getPlaceableAt(1).isBlock() );
        assertTrue( nearCell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(nearCell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }


    }

    /**
     * Check if the Pan's victory condition  on board are correctly checked
     * Methods used:        getMyVictory()          of  Card
     *                      getHeigth()             of  Cell
     *                      repOk()                 of  Cell
     *                      getTop()                of  Cell
     *                      getPlaceableAt( int )   of  Cell
     *                      buildBlock()            of  Cell
     *                      position()              of  Placeable
     *                      isBlock()               of  Placeable/Block
     *                      place( Cell )           of  Worker
     *
     * Black Box
     */
    @Test
    void checkMovePanBlack() {
        myVictory = panCard.getMyVictory();


        /* can win when go up from Cell with two Blocks to near Cell with three Blocks*/
        cell.buildBlock();
        cell.buildBlock();
        nearCell.buildBlock();
        nearCell.buildBlock();
        nearCell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        worker.place(nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( checkWin );
        assertTrue( cell.getHeigth() == 2 );
        assertTrue( nearCell.getHeigth() == 4 );
        assertTrue( cell.getTop().isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().equals(worker) );
        assertTrue( nearCell.getTop().isBlock() );
        assertTrue( nearCell.getPlaceableAt(2).isBlock() );
        assertTrue( nearCell.getPlaceableAt(1).isBlock() );
        assertTrue( nearCell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(nearCell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }


        /* can't win when go down from Cell with two Blocks to near Cell with one Block*/
        cell.buildBlock();
        cell.buildBlock();
        nearCell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        worker.place(nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( !checkWin );
        assertTrue( cell.getHeigth() == 2 );
        assertTrue( nearCell.getHeigth() == 2 );
        assertTrue( cell.getTop().isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().equals(worker) );
        assertTrue( nearCell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(nearCell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }


        /* can win when go down from Cell with three Blocks to near Cell without Blocks*/
        cell.buildBlock();
        cell.buildBlock();
        cell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        worker.place(nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( checkWin );
        assertTrue( cell.getHeigth() == 3 );
        assertTrue( nearCell.getHeigth() == 1 );
        assertTrue( cell.getTop().isBlock() );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().equals(worker) );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(nearCell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }


        /* can win when go down from Cell with three Blocks to near Cell with one Blocks*/
        cell.buildBlock();
        cell.buildBlock();
        cell.buildBlock();
        nearCell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        worker.place(nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( checkWin );
        assertTrue( cell.getHeigth() == 3 );
        assertTrue( nearCell.getHeigth() == 2 );
        assertTrue( cell.getTop().isBlock() );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().equals(worker) );
        assertTrue( nearCell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(nearCell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }


    }

    /**
     * Check if with the default configure of checkWinList returned from CardParser checkMyMove() can correctly check
     * the victory ( white test of first lambda expression of CardParser's getWinCheckers())
     * Methods used:        getMyVictory()          of  Card
     *                      getHeigth()             of  Cell
     *                      repOk()                 of  Cell
     *                      getTop()                of  Cell
     *                      getPlaceableAt( int )   of  Cell
     *                      buildBlock()            of  Cell
     *                      position()              of  Placeable
     *                      isBlock()               of  Placeable/Block
     *                      place( Cell )           of  Worker
     *
     * White Box
     */
    @Test
    void checkDefaultRules() {
        myVictory = humanCard.getMyVictory();

        /* can't win when not go up ( for example go down )*/
        cell.buildBlock();
        cell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        worker.place(nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( !checkWin );
        assertTrue( cell.getHeigth() == 2 );
        assertTrue( nearCell.getHeigth() == 1 );
        assertTrue( cell.getTop().isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().equals(worker) );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(nearCell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }


        /* can't win when go up but not at last level*/
        cell.buildBlock();
        nearCell.buildBlock();
        nearCell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        worker.place(nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( !checkWin );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell.getHeigth() == 3 );
        assertTrue( cell.getTop().isBlock() );
        assertTrue( nearCell.getTop().equals(worker) );
        assertTrue( nearCell.getPlaceableAt(1).isBlock() );
        assertTrue( nearCell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(nearCell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }


        /* can win when go up at last level*/
        cell.buildBlock();
        cell.buildBlock();
        nearCell.buildBlock();
        nearCell.buildBlock();
        nearCell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        worker.place(nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( checkWin );
        assertTrue( cell.getHeigth() == 2 );
        assertTrue( nearCell.getHeigth() == 4 );
        assertTrue( cell.getTop().isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().equals(worker) );
        assertTrue( nearCell.getPlaceableAt(2).isBlock() );
        assertTrue( nearCell.getPlaceableAt(1).isBlock() );
        assertTrue( nearCell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(nearCell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }

    }

    /**
     * Check if with the "new Victory" configure of checkWinList returned from CardParser checkMove() can correctly
     * check the opponent's move ( white test of lambda expression of CardParser's getWinCheckers())
     * Methods used:        getMyVictory()                      of  Card
     *                      getTop()                            of  Cell
     *                      getHeigth()                         of  Cell
     *                      getPlaceableAt( int )               of  Cell
     *                      repOk()                             of  Cell
     *                      removePlaceable()                   of  Cell
     *                      buildBlock()                        of  Cell
     *                      position()                          of  Placeable
     *                      isBlock()                           of  Placeable/Block
     *                      place( Cell )                       of  Worker
     *
     * White Box
     */
    @Test
    void checkNewVictoryRules() {
        myVictory = panCard.getMyVictory();


        /* can't win with special rules  when lastMoveLevelDirection of Move is different from HotLastDirection of GodPower*/

        worker.place(cell);
        nearCell.buildBlock();
        move = new Move(worker.position(), nearCell);
        worker.place(nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( !checkWin );
        assertTrue( cell.getHeigth() == 0 );
        assertTrue( nearCell.getHeigth() == 2 );
        assertTrue( cell.getTop() == null );
        assertTrue( nearCell.getTop().equals(worker) );
        assertTrue( nearCell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(nearCell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }

        /* can't win with special rules when lastMoveLevelDirection of Move == HotLastDirection of GodPower
          but the height is too low*/

        cell.buildBlock();
        cell.buildBlock();
        cell.buildBlock();
        nearCell.buildBlock();
        nearCell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        worker.place(nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( !checkWin );
        assertTrue( cell.getHeigth() == 3 );
        assertTrue( nearCell.getHeigth() == 3 );
        assertTrue( cell.getTop().isBlock() );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().equals(worker) );
        assertTrue( nearCell.getPlaceableAt(1).isBlock() );
        assertTrue( nearCell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(nearCell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }


        /* can win when lastMoveLevelDirection of Move == HotLastDirection of GodPower and the Height is correct*/

        cell.buildBlock();
        cell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        worker.place(nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( checkWin );
        assertTrue( cell.getHeigth() == 2 );
        assertTrue( nearCell.getHeigth() == 1 );
        assertTrue( cell.getTop().isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().equals(worker) );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(nearCell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }
    }

    /**
     * Check if getGodPower() can return the correct value after initialization
     * Methods used:        getMyVictory()                      of  Card
     *
     * Black Box and White Box
     */
    @Test
    void getGodPower() {
        myVictory = humanCard.getMyVictory();

        assertTrue(myVictory.getGodPower().equals(human));

    }

}