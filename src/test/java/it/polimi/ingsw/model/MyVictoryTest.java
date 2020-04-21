package it.polimi.ingsw.model;

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

    MyVictory myVictory;
    Move move;
    Player player = new Player( "Player1");
    Board board;
    Cell cell;
    Cell nearCell;
    Worker worker;

    /**
     * Initialization before method's test
     * Methods used:        getCellAt( ini, int)            of  IslandBoard
     */
    @BeforeEach
    void setUp() {

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

    }

    /**
     * Check if the default victory condition  on board are correctly checked
     * Methods used:        getHeigth()             of  Cell
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
        GodPower human = new GodPower(); // CardGod without any power
        human.setName( "Default" );
        human.setEpithet( "Default" );
        human.setDescription( "Default" );
        human.setMovementsLeft( 1 );
        human.setConstructionLeft( 1 );
        human.setHotLastMoveDirection( LevelDirection.NONE );
        human.setForceOpponentInto( FloorDirection.NONE );
        human.setDeniedDirection( LevelDirection.NONE );
        human.setOpponentDeniedDirection( LevelDirection.NONE );
        Card humanCard = new Card( human );
        myVictory = new MyVictory(humanCard, human);
        boolean checkWin;


        /* can win when go up from Cell with two Blocks to near Cell with three Blocks*/
        cell.buildBlock();
        cell.buildBlock();
        nearCell.buildBlock();
        nearCell.buildBlock();
        nearCell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( checkWin );
        assertTrue( cell.getHeigth() == 3 );
        assertTrue( nearCell.getHeigth() == 3 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().isBlock() );
        assertTrue( nearCell.getPlaceableAt(1).isBlock() );
        assertTrue( nearCell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(cell) );

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
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( !checkWin );
        assertTrue( cell.getHeigth() == 2 );
        assertTrue( nearCell.getHeigth() == 2 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().isBlock() );
        assertTrue( nearCell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(cell) );

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
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( !checkWin );
        assertTrue( cell.getHeigth() == 4 );
        assertTrue( nearCell.getHeigth() == 3 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( cell.getPlaceableAt(2).isBlock() );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().isBlock() );
        assertTrue( nearCell.getPlaceableAt(1).isBlock() );
        assertTrue( nearCell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(cell) );

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
     * Methods used:        getHeigth()             of  Cell
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
        GodPower pan = new GodPower();
        pan.setName( "Pan" );
        pan.setEpithet( "God of the Wild" );
        pan.setDescription( "P" );
        pan.setMovementsLeft( 1 );
        pan.setConstructionLeft( 1 );
        pan.setMustObey(true);
        pan.setHotLastMoveDirection( LevelDirection.DOWN );
        pan.setForceOpponentInto( FloorDirection.NONE );
        pan.setDeniedDirection( LevelDirection.NONE );
        pan.setOpponentDeniedDirection( LevelDirection.NONE );
        pan.setNewVictoryCondition(true);
        Card panCard = new Card( pan );
        myVictory = new MyVictory(panCard, pan);
        boolean checkWin;


        /* can win when go up from Cell with two Blocks to near Cell with three Blocks*/
        cell.buildBlock();
        cell.buildBlock();
        nearCell.buildBlock();
        nearCell.buildBlock();
        nearCell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( checkWin );
        assertTrue( cell.getHeigth() == 3 );
        assertTrue( nearCell.getHeigth() == 3 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().isBlock() );
        assertTrue( nearCell.getPlaceableAt(1).isBlock() );
        assertTrue( nearCell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(cell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }


        /* can't win when go down from Cell with two Blocks to near Cell with one Blocks*/
        cell.buildBlock();
        cell.buildBlock();
        nearCell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( !checkWin );
        assertTrue( cell.getHeigth() == 3 );
        assertTrue( nearCell.getHeigth() == 1 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(cell) );

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
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( checkWin );
        assertTrue( cell.getHeigth() == 4 );
        assertTrue( nearCell.getHeigth() == 0 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( cell.getPlaceableAt(2).isBlock() );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop() == null );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(cell) );

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
        checkWin = myVictory.checkMove(move, worker);
        assertTrue( checkWin );
        assertTrue( cell.getHeigth() == 4 );
        assertTrue( nearCell.getHeigth() == 1 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( cell.getPlaceableAt(2).isBlock() );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(cell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }


    }

    /**
     * Check if the DefaultCheckRules ( in CheckMove ) returns the correct value
     * Methods used:        getHeigth()             of  Cell
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
        GodPower human = new GodPower(); // CardGod without any power
        human.setName( "Default" );
        human.setEpithet( "Default" );
        human.setDescription( "Default" );
        human.setMovementsLeft( 1 );
        human.setConstructionLeft( 1 );
        human.setHotLastMoveDirection( LevelDirection.NONE );
        human.setForceOpponentInto( FloorDirection.NONE );
        human.setDeniedDirection( LevelDirection.NONE );
        human.setOpponentDeniedDirection( LevelDirection.NONE );
        Card humanCard = new Card( human );
        myVictory = new MyVictory(humanCard, human);
        boolean check;

        /* can't win when not go up ( for example go down )*/
        cell.buildBlock();
        cell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        check = myVictory.checkMove(move, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 3 );
        assertTrue( nearCell.getHeigth() == 0 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop() == null );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(cell) );

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
        check = myVictory.checkMove(move, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 2 );
        assertTrue( nearCell.getHeigth() == 2 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().isBlock() );
        assertTrue( nearCell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(cell) );

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
        check = myVictory.checkMove(move, worker);
        assertTrue( check );
        assertTrue( cell.getHeigth() == 3 );
        assertTrue( nearCell.getHeigth() == 3 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop().isBlock() );
        assertTrue( nearCell.getPlaceableAt(1).isBlock() );
        assertTrue( nearCell.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(cell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }

    }
    /**
     * Check if special rules ( in CheckMove() ) are correctly checked
     * Methods used:        getTop()                            of  Cell
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
    void checkSpecialRules() {
        GodPower mutantGod = new GodPower();
        mutantGod.setName("Mutant God");
        mutantGod.setEpithet("Mutant");
        mutantGod.setDescription("M");
        mutantGod.setMovementsLeft(1);
        mutantGod.setConstructionLeft(1);
        mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
        mutantGod.setForceOpponentInto(FloorDirection.NONE);
        mutantGod.setDeniedDirection(LevelDirection.NONE);
        mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);
        mutantGod.setNewVictoryCondition(true);
        Card mutantCard = new Card(mutantGod);
        boolean check;


        /* default win rules when lastMoveLevelDirection of Move is different from HotLastDirection of GodPower*/
        mutantGod.setHotLastMoveDirection(LevelDirection.SAME);

        cell.buildBlock();
        cell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        check = myVictory.checkMove(move, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 3 );
        assertTrue( nearCell.getHeigth() == 0 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop() == null );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(cell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }

        /* default win rules when lastMoveLevelDirection of Move == HotLastDirection of GodPower the height is too low*/
        mutantGod.setHotLastMoveDirection(LevelDirection.UP);

        cell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        check = myVictory.checkMove(move, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 2 );
        assertTrue( nearCell.getHeigth() == 0 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop() == null );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(cell) );

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell.getTop() != null ) {
            nearCell.removePlaceable();
        }


        /* can win when lastMoveLevelDirection of Move == HotLastDirection of GodPower and the Height is correct*/
        mutantGod.setHotLastMoveDirection(LevelDirection.SAME);

        cell.buildBlock();
        cell.buildBlock();
        worker.place(cell);
        move = new Move(worker.position(), nearCell);
        check = myVictory.checkMove(move, worker);
        assertTrue( check );
        assertTrue( cell.getHeigth() == 3 );
        assertTrue( nearCell.getHeigth() == 0 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( cell.getPlaceableAt(1).isBlock() );
        assertTrue( cell.getPlaceableAt(0).isBlock() );
        assertTrue( nearCell.getTop() == null );
        assertTrue( cell.repOk() );
        assertTrue( nearCell.repOk() );
        assertTrue( worker.position().equals(cell) );

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
     *
     * Black Box and White Box
     */
    @Test
    void getGodPower() {
        GodPower godPower = new GodPower();
        Card card = new Card(godPower);
        myVictory = new MyVictory(card, godPower);

        assertTrue(myVictory.getGodPower().equals(godPower));

    }

}