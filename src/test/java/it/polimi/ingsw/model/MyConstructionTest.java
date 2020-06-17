package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.IslandBoard;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.GodPower;
import it.polimi.ingsw.model.card.build.MyConstruction;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import it.polimi.ingsw.model.move.BuildMove;
import it.polimi.ingsw.model.move.FloorDirection;
import it.polimi.ingsw.model.move.LevelDirection;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for MyConstruction class, aimed to verify it works properly
 *
 * @author Marco
 */
class MyConstructionTest {
    MyConstruction myConstruction;
    BuildMove buildMove;
    Player player = new Player( "Player1");
    Board board;
    Cell cell;
    Cell nearCell1;
    Cell nearCell2;
    Cell farawayCell;
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
            nearCell1 = board.getCellAt(2,3);
            nearCell2 = board.getCellAt( 3,3);
            farawayCell = board.getCellAt( 4,4);
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

        myConstruction = null;
        buildMove = null;
        board = null;
        cell = null;
        nearCell1 = null;
        nearCell2 = null;
        worker = null;

    }

    /**
     * Check if the default moves on board are correctly checked and executed
     * Methods used:        getCellAt( int, int)    of  IslandBoard
     *                      getHeigth()             of  Cell
     *                      repOk()                 of  Cell
     *                      getTop()                of  Cell
     *                      getPlaceableAt( int )   of  Cell
     *                      buildBlock()            of  Cell
     *                      buildDome()             of  Cell
     *                      position()              of  Placeable
     *                      isBlock()               of  Placeable/Block
     *                      isDome()                of  Placeable/Dome
     *                      place( Cell )           of  Worker
     *
     * Black Box
     */
    @Test
    void executeMoveDefaultBlack() {
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
        myConstruction = new MyConstruction(humanCard, human );
        Worker otherWorker = new Worker( null );
        boolean checkExecute;
        boolean checkOutOfBoard = false;


        /* can build a Block on a near free Cell */
        worker.place( cell );
        buildMove = new BuildMove( worker.position(), nearCell1, PlaceableType.BLOCK );
        try {
            checkExecute = myConstruction.executeMove( buildMove, worker );
            assertTrue( checkExecute);
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( worker.position().equals( cell ) );

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // clear Cells
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        //todo: probably not necessary ( in this case comment this test or eliminate )
        /* can't build a Dome on a near free Cell */
        worker.place( cell );
        buildMove = new BuildMove( worker.position(), nearCell1, PlaceableType.DOME );
        try {
            checkExecute = myConstruction.executeMove( buildMove, worker );
            assertTrue( !checkExecute);
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 0 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop() == null );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( worker.position().equals( cell ) );

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // clear Cells
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        /* can build a Block on a near Cell  with two Block */
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        worker.place( cell );
        buildMove = new BuildMove( worker.position(), nearCell1, PlaceableType.BLOCK );
        try {
            checkExecute = myConstruction.executeMove( buildMove, worker );
            assertTrue( checkExecute);
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 3 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( nearCell1.getPlaceableAt(1).isBlock() );
            assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( worker.position().equals( cell ) );

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // clear Cells
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        //todo: probably not necessary ( in this case comment this test or eliminate )
        /* can't build a Dome on a near Cell with two Block */
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        worker.place( cell );
        buildMove = new BuildMove( worker.position(), nearCell1, PlaceableType.DOME );
        try {
            checkExecute = myConstruction.executeMove( buildMove, worker );
            assertTrue( !checkExecute);
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 2 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( worker.position().equals( cell ) );

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // clear Cells
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        /* can't build a Block on the same Cell where there is the Worker */
        worker.place( cell );
        buildMove = new BuildMove( worker.position(), cell, PlaceableType.BLOCK );
        try {
            checkExecute = myConstruction.executeMove( buildMove, worker );
            assertTrue( !checkExecute);
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( cell.repOk() );
            assertTrue( worker.position().equals( cell ) );

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // clear Cells
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }


        /* can build on a faraway Cell */
        worker.place( cell );
        buildMove = new BuildMove( worker.position(), farawayCell, PlaceableType.BLOCK );
        try {
            checkExecute = myConstruction.executeMove( buildMove, worker );
            assertTrue( !checkExecute);
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( farawayCell.getHeigth() == 0 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( farawayCell.getTop() == null );
            assertTrue( cell.repOk() );
            assertTrue( farawayCell.repOk() );
            assertTrue( worker.position().equals( cell ) );

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // clear Cells
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( farawayCell.getTop() != null ) {
            farawayCell.removePlaceable();
        }


        /* can build a Dome on a near Cell with three Block */
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        worker.place( cell );
        buildMove = new BuildMove( worker.position(), nearCell1, PlaceableType.DOME );
        try {
            checkExecute = myConstruction.executeMove( buildMove, worker );
            assertTrue( checkExecute);
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 4 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isDome() );
            assertTrue( nearCell1.getPlaceableAt(2).isBlock() );
            assertTrue( nearCell1.getPlaceableAt(1).isBlock() );
            assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( worker.position().equals( cell ) );

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // clear Cells
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        //todo: probably not necessary ( in this case comment this test or eliminate)
        /* can't build a Block on a near Cell with three Block */
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        worker.place( cell );
        buildMove = new BuildMove( worker.position(), nearCell1, PlaceableType.BLOCK );
        try {
            checkExecute = myConstruction.executeMove( buildMove, worker );
            assertTrue( !checkExecute);
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 3 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( nearCell1.getPlaceableAt(1).isBlock() );
            assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( worker.position().equals( cell ) );

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // clear Cells
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        /* can't build on a near Cell with a Dome */
        nearCell1.buildDome();
        worker.place( cell );
        buildMove = new BuildMove( worker.position(), nearCell1, PlaceableType.BLOCK );
        try {
            checkExecute = myConstruction.executeMove( buildMove, worker );
            assertTrue( !checkExecute);
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isDome() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( worker.position().equals( cell ) );

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // clear Cells
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        /* can't build on a near Cell with another Worker */
        worker.place( cell );
        otherWorker.place( nearCell1 );
        buildMove = new BuildMove( worker.position(), nearCell1, PlaceableType.BLOCK );
        try {
            checkExecute = myConstruction.executeMove( buildMove, worker );
            assertTrue( !checkExecute);
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().equals( otherWorker ) );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( worker.position().equals( cell ) );
            assertTrue( otherWorker.position().equals( nearCell1 ) );

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // clear Cells
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        //todo: probably not necessary
        /* can't build a Dome on a near Cell with three Block if there is another Worker */
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        worker.place( cell );
        otherWorker.place( nearCell1 );
        buildMove = new BuildMove( worker.position(), nearCell1, PlaceableType.DOME );
        try {
            checkExecute = myConstruction.executeMove( buildMove, worker );
            assertTrue( !checkExecute);
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 4 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().equals( otherWorker ) );
            assertTrue( nearCell1.getPlaceableAt(2).isBlock() );
            assertTrue( nearCell1.getPlaceableAt(1).isBlock() );
            assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( worker.position().equals( cell ) );
            assertTrue( otherWorker.position().equals( nearCell1 ) );

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // clear Cells
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


    }

    /**
     * Check if the Demetra's Power is correctly checked and executed
     * Methods used:        decreaseConstructionLeft()      of  MyConstruction
     *                      resetConstructionLeft()         of  MyConstruction
     *                      getHeigth()                     of  Cell
     *                      repOk()                         of  Cell
     *                      getTop()                        of  Cell
     *                      getPlaceableAt( int )           of  Cell
     *                      buildBlock()                    of  Cell
     *                      position()                      of  Placeable
     *                      isBlock()                       of  Placeable/Block
     *                      place( Cell )                   of  Worker
     *
     * Black Box
     */
    @Test
    void executeMoveDemetraBlack() {
        GodPower demetra = new GodPower();
        demetra.setName("Demetra");
        demetra.setEpithet("Goddess of the Harvest");
        demetra.setDescription("D");
        demetra.setMovementsLeft(1);
        demetra.setConstructionLeft(2);
        demetra.setSameSpaceDenied(true);
        demetra.setHotLastMoveDirection(LevelDirection.NONE);
        demetra.setForceOpponentInto(FloorDirection.NONE);
        demetra.setDeniedDirection(LevelDirection.NONE);
        demetra.setOpponentDeniedDirection(LevelDirection.NONE);
        demetra.setActiveOnMyConstruction(true);
        Card demetraCard = new Card(demetra);
        myConstruction = new MyConstruction(demetraCard, demetra);
        boolean checkExecute;
        boolean checkOutOfBoard = false;


        /* can build a Block at the first time and another Block on another Cell at the second Time*/
        nearCell2.buildBlock();
        worker.place( cell );
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue( checkExecute );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( nearCell2.getHeigth() == 1 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( nearCell2.getTop().isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( nearCell2.repOk() );
            assertTrue( worker.position().equals( cell ) );
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        myConstruction.decreaseConstructionLeft();

        buildMove = new BuildMove(worker.position(), nearCell2, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue( checkExecute );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( nearCell2.getHeigth() == 2 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( nearCell2.getTop().isBlock() );
            assertTrue( nearCell2.getPlaceableAt(0).isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( nearCell2.repOk() );
            assertTrue( worker.position().equals( cell ) );
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // reset ConstructionLeft and clear Cells
        myConstruction.resetConstructionLeft();
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }
        while ( nearCell2.getTop() != null ) {
            nearCell2.removePlaceable();
        }


        /* can build a Block at the first time but not another Block on the same Cell at the second Time*/
        worker.place( cell );
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue( checkExecute );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( worker.position().equals( cell ) );
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        myConstruction.decreaseConstructionLeft();

        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue( !checkExecute );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( worker.position().equals( cell ) );
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // reset ConstructionLeft and clear Cells
        myConstruction.resetConstructionLeft();
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }
        while ( nearCell2.getTop() != null ) {
            nearCell2.removePlaceable();
        }


        /* can build a Block at the first time and another Block on faraway Cell at the second Time*/
        worker.place( cell );
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue( checkExecute );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( worker.position().equals( cell ) );
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        myConstruction.decreaseConstructionLeft();

        buildMove = new BuildMove(worker.position(), farawayCell, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue( !checkExecute );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( farawayCell.getHeigth() == 0 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( farawayCell.getTop() == null );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( farawayCell.repOk() );
            assertTrue( worker.position().equals( cell ) );
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // reset ConstructionLeft and clear Cells
        myConstruction.resetConstructionLeft();
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }
        while ( farawayCell.getTop() != null ) {
            farawayCell.removePlaceable();
        }


        /* can build a Block at the first time and a Dome on another Cell with three Blocks at the second Time*/
        nearCell2.buildBlock();
        nearCell2.buildBlock();
        nearCell2.buildBlock();
        worker.place( cell );
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue( checkExecute );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( nearCell2.getHeigth() == 3 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( nearCell2.getTop().isBlock() );
            assertTrue( nearCell2.getPlaceableAt(1).isBlock() );
            assertTrue( nearCell2.getPlaceableAt(0).isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( nearCell2.repOk() );
            assertTrue( worker.position().equals( cell ) );
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        myConstruction.decreaseConstructionLeft();

        buildMove = new BuildMove(worker.position(), nearCell2, PlaceableType.DOME);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue( checkExecute );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( nearCell2.getHeigth() == 4 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( nearCell2.getTop().isDome() );
            assertTrue( nearCell2.getPlaceableAt(2).isBlock() );
            assertTrue( nearCell2.getPlaceableAt(1).isBlock() );
            assertTrue( nearCell2.getPlaceableAt(0).isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( nearCell2.repOk() );
            assertTrue( worker.position().equals( cell ) );
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // reset ConstructionLeft and clear Cells
        myConstruction.resetConstructionLeft();
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }
        while ( nearCell2.getTop() != null ) {
            nearCell2.removePlaceable();
        }

        //todo: probably not necessary ( in this case comment this test or eliminate )
        /* can build a Block at the first time but not a Block on another Cell with three Blocks at the second Time*/
        nearCell2.buildBlock();
        nearCell2.buildBlock();
        nearCell2.buildBlock();
        worker.place( cell );
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue( checkExecute );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( nearCell2.getHeigth() == 3 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( nearCell2.getTop().isBlock() );
            assertTrue( nearCell2.getPlaceableAt(1).isBlock() );
            assertTrue( nearCell2.getPlaceableAt(0).isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( nearCell2.repOk() );
            assertTrue( worker.position().equals( cell ) );
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        myConstruction.decreaseConstructionLeft();

        buildMove = new BuildMove(worker.position(), nearCell2, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue( !checkExecute );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( nearCell2.getHeigth() == 3 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( nearCell2.getTop().isBlock() );
            assertTrue( nearCell2.getPlaceableAt(1).isBlock() );
            assertTrue( nearCell2.getPlaceableAt(0).isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( nearCell2.repOk() );
            assertTrue( worker.position().equals( cell ) );
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

        // reset ConstructionLeft and clear Cells
        myConstruction.resetConstructionLeft();
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }
        while ( nearCell2.getTop() != null ) {
            nearCell2.removePlaceable();
        }

    }

    /**
     * Check if Hephaestus' Power is correctly checked and executed
     * Methods used:        decreaseConstructionLeft()      of  MyConstruction
     *                      resetConstructionLeft()         of  MyConstruction
     *                      getHeigth()                     of  Cell
     *                      repOk()                         of  Cell
     *                      getTop()                        of  Cell
     *                      getPlaceableAt( int )           of  Cell
     *                      buildBlock()                    of  Cell
     *                      position()                      of  Placeable
     *                      isBlock()                       of  Placeable/Block
     *                      place( Cell )                   of  Worker
     *
     * Black Box
     */
    @Test
    void executeMoveHephaestusBlack() {
        GodPower hephaestus = new GodPower();
        hephaestus.setName("Hephaestus");
        hephaestus.setEpithet("God of Blacksmiths");
        hephaestus.setDescription("H");
        hephaestus.setMovementsLeft(1);
        hephaestus.setConstructionLeft(2);
        hephaestus.setHotLastMoveDirection(LevelDirection.NONE);
        hephaestus.setForceOpponentInto(FloorDirection.NONE);
        hephaestus.setDeniedDirection(LevelDirection.NONE);
        hephaestus.setOpponentDeniedDirection(LevelDirection.NONE);
        hephaestus.setActiveOnMyConstruction(true);
        hephaestus.setForceConstructionOnSameSpace( true );
        Card hephaestusCard = new Card(hephaestus);
        myConstruction = new MyConstruction(hephaestusCard, hephaestus);
        boolean checkExecute;
        boolean checkOutOfBoard = false;


        /* can build a Block at the first time on a Cells without Blocks and another Block on same Cell at the second time*/
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue(checkExecute);
            assertTrue(cell.getHeigth() == 1);
            assertTrue(nearCell1.getHeigth() == 1);
            assertTrue(cell.getTop().equals(worker));
            assertTrue(nearCell1.getTop().isBlock());
            assertTrue(cell.repOk());
            assertTrue(nearCell1.repOk());
            assertTrue(worker.position().equals(cell));
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue(!checkOutOfBoard);

        myConstruction.decreaseConstructionLeft();

        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue(checkExecute);
            assertTrue(cell.getHeigth() == 1);
            assertTrue(nearCell1.getHeigth() == 2);
            assertTrue(cell.getTop().equals(worker));
            assertTrue(nearCell1.getTop().isBlock());
            assertTrue(nearCell1.getPlaceableAt(0).isBlock());
            assertTrue(cell.repOk());
            assertTrue(nearCell1.repOk());
            assertTrue(worker.position().equals(cell));
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue(!checkOutOfBoard);

        // reset ConstructionLeft and clear Cells
        myConstruction.resetConstructionLeft();
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }


        /* can build a Block at the first time on a Cells but not another Block on different Cell at the second time*/
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue(checkExecute);
            assertTrue(cell.getHeigth() == 1);
            assertTrue(nearCell1.getHeigth() == 2);
            assertTrue(cell.getTop().equals(worker));
            assertTrue(nearCell1.getTop().isBlock());
            assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
            assertTrue(cell.repOk());
            assertTrue(nearCell1.repOk());
            assertTrue(worker.position().equals(cell));
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue(!checkOutOfBoard);

        myConstruction.decreaseConstructionLeft();

        buildMove = new BuildMove(worker.position(), nearCell2, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue( !checkExecute );
            assertTrue(cell.getHeigth() == 1);
            assertTrue(nearCell1.getHeigth() == 2);
            assertTrue(nearCell2.getHeigth() == 0);
            assertTrue(cell.getTop().equals(worker));
            assertTrue(nearCell1.getTop().isBlock());
            assertTrue(nearCell1.getPlaceableAt(0).isBlock());
            assertTrue(nearCell2.getTop() == null );
            assertTrue(cell.repOk());
            assertTrue(nearCell1.repOk());
            assertTrue(nearCell2.repOk());
            assertTrue(worker.position().equals(cell));
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue(!checkOutOfBoard);

        // reset ConstructionLeft and clear Cells
        myConstruction.resetConstructionLeft();
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }
        while (nearCell2.getTop() != null) {
            nearCell2.removePlaceable();
        }


        /* can build a Block at the first time on a Cells with two Blocks but not a Dome on same Cell at the second time*/
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue(checkExecute);
            assertTrue(cell.getHeigth() == 1);
            assertTrue(nearCell1.getHeigth() == 3);
            assertTrue(cell.getTop().equals(worker));
            assertTrue(nearCell1.getTop().isBlock());
            assertTrue( nearCell1.getPlaceableAt(1).isBlock() );
            assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
            assertTrue(cell.repOk());
            assertTrue(nearCell1.repOk());
            assertTrue(worker.position().equals(cell));
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue(!checkOutOfBoard);

        myConstruction.decreaseConstructionLeft();

        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue( !checkExecute );
            assertTrue(cell.getHeigth() == 1);
            assertTrue(nearCell1.getHeigth() == 3);
            assertTrue(cell.getTop().equals(worker));
            assertTrue(nearCell1.getTop().isBlock());
            assertTrue(nearCell1.getPlaceableAt(1).isBlock());
            assertTrue(nearCell1.getPlaceableAt(0).isBlock());
            assertTrue(cell.repOk());
            assertTrue(nearCell1.repOk());
            assertTrue(worker.position().equals(cell));
        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue(!checkOutOfBoard);

        // reset ConstructionLeft and clear Cells
        myConstruction.resetConstructionLeft();
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }

    }

    /**
     * Check if Atlas' Power is correctly checked and executed
     * Methods used:        getHeigth()             of  Cell
     *                      repOk()                 of  Cell
     *                      getTop()                of  Cell
     *                      getPlaceableAt( int )   of  Cell
     *                      buildBlock()            of  Cell
     *                      buildDome()             of  Cell
     *                      position()              of  Placeable
     *                      isBlock()               of  Placeable/Block
     *                      isDome()                of  Placeable/Dome
     *                      place( Cell )           of  Worker
     *
     * Black Box
     */
    @Test
    void executeMoveAtlasBlack() {
        GodPower atlas = new GodPower();
        atlas.setName("Atlas");
        atlas.setEpithet("Titan Shouldering the Heavens");
        atlas.setDescription("A");
        atlas.setMovementsLeft(1);
        atlas.setConstructionLeft(1);
        atlas.setHotLastMoveDirection(LevelDirection.NONE);
        atlas.setForceOpponentInto(FloorDirection.NONE);
        atlas.setDeniedDirection(LevelDirection.NONE);
        atlas.setOpponentDeniedDirection(LevelDirection.NONE);
        atlas.setActiveOnMyConstruction(true);
        atlas.setDomeAtAnyLevel(true);
        Card atlasCard = new Card(atlas);
        myConstruction = new MyConstruction(atlasCard, atlas);
        Worker otherWorker = new Worker(null);
        boolean checkExecute;
        boolean checkOutOfBoard = false;


        /* can build a Block on a near Cell */
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue(checkExecute);
            assertTrue(cell.getHeigth() == 1);
            assertTrue(nearCell1.getHeigth() == 2);
            assertTrue(cell.getTop().equals(worker));
            assertTrue(nearCell1.getTop().isBlock());
            assertTrue(nearCell1.getPlaceableAt(0).isBlock() );
            assertTrue(cell.repOk());
            assertTrue(nearCell1.repOk());
            assertTrue(worker.position().equals(cell));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue(!checkOutOfBoard);

        // clear Cells
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }


        /* can build a Dome on a near free Cell */
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue(checkExecute);
            assertTrue(cell.getHeigth() == 1);
            assertTrue(nearCell1.getHeigth() == 1);
            assertTrue(cell.getTop().equals(worker));
            assertTrue(nearCell1.getTop().isDome());
            assertTrue(cell.repOk());
            assertTrue(nearCell1.repOk());
            assertTrue(worker.position().equals(cell));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue(!checkOutOfBoard);

        // clear Cells
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }


        /* can build a Dome on a near Cell with a Block */
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue(checkExecute);
            assertTrue(cell.getHeigth() == 1);
            assertTrue(nearCell1.getHeigth() == 2);
            assertTrue(cell.getTop().equals(worker));
            assertTrue(nearCell1.getTop().isDome());
            assertTrue(nearCell1.getPlaceableAt(0).isBlock());
            assertTrue(cell.repOk());
            assertTrue(nearCell1.repOk());
            assertTrue(worker.position().equals(cell));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue(!checkOutOfBoard);

        // clear Cells
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }


        /* can't build a Dome on a near Cell with a Dome */
        nearCell1.buildDome();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue(!checkExecute);
            assertTrue(cell.getHeigth() == 1);
            assertTrue(nearCell1.getHeigth() == 1);
            assertTrue(cell.getTop().equals(worker));
            assertTrue(nearCell1.getTop().isDome());
            assertTrue(cell.repOk());
            assertTrue(nearCell1.repOk());
            assertTrue(worker.position().equals(cell));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue(!checkOutOfBoard);

        // clear Cells
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }


        /* can't build a Dome on a near Cell with a Worker */
        worker.place(cell);
        otherWorker.place(nearCell1);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue(!checkExecute);
            assertTrue(cell.getHeigth() == 1);
            assertTrue(nearCell1.getHeigth() == 1);
            assertTrue(cell.getTop().equals(worker));
            assertTrue(nearCell1.getTop().equals(otherWorker));
            assertTrue(cell.repOk());
            assertTrue(nearCell1.repOk());
            assertTrue(worker.position().equals(cell));
            assertTrue(otherWorker.position().equals(nearCell1));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue(!checkOutOfBoard);

        // clear Cells
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }


        /* can't build a Dome on a faraway Cell */
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), farawayCell, PlaceableType.DOME);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue(!checkExecute);
            assertTrue(cell.getHeigth() == 1);
            assertTrue(farawayCell.getHeigth() == 0);
            assertTrue(cell.getTop().equals(worker));
            assertTrue(farawayCell.getTop() == null);
            assertTrue(cell.repOk());
            assertTrue(farawayCell.repOk());
            assertTrue(worker.position().equals(cell));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue(!checkOutOfBoard);

        // clear Cells
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (farawayCell.getTop() != null) {
            farawayCell.removePlaceable();
        }
    }


    /**
     * Check if default rules ( in CheckMove() ) are correctly checked
     * Methods used:        decreaseConstructionLeft()      of  MyConstruction
     *                      resetConstructionLeft           of  MyConstruction
     *                      getCellAt( int, int)            of  IslandBoard
     *                      getTop()                        of  Cell
     *                      getHeigth()                     of  Cell
     *                      getPlaceableAt( int )           of  Cell
     *                      repOk()                         of  Cell
     *                      removePlaceable()               of  Cell
     *                      buildBlock()                    of  Cell
     *                      buildDome()                     of  Cell
     *                      position()                      of  Placeable
     *                      isBlock()                       of  Placeable/Block
     *                      isDome()                        of  Placeable/Dome
     *                      place( Cell )                   of  Worker
     *
     * White Box
     */
    @Test
    void checkMoveDefault() {
        GodPower human = new GodPower(); // CardGod without any power
        human.setName("Default");
        human.setEpithet("Default");
        human.setDescription("Default");
        human.setMovementsLeft(1);
        human.setConstructionLeft(1);
        human.setHotLastMoveDirection(LevelDirection.NONE);
        human.setForceOpponentInto(FloorDirection.NONE);
        human.setDeniedDirection(LevelDirection.NONE);
        human.setOpponentDeniedDirection(LevelDirection.NONE);
        Card humanCard = new Card(human);
        myConstruction = new MyConstruction(humanCard ,human);
        Worker otherWorker = new Worker(null);
        boolean check;
        boolean checkError = false;


        /* can't build when ConstructionLeft <= 0 */
        myConstruction.decreaseConstructionLeft();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 0 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop() == null );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }
        myConstruction.resetConstructionLeft();


        /* can't build on the same cell where there is the Worker' */
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), cell, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( cell.repOk() );
        assertTrue( worker.position().equals( cell ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }


        /* can't build on cell too faraway */

        try {
            // CASE 1: farawayCell.getX() >> cell.getX()
            farawayCell = board.getCellAt( cell.getX() + 2, cell.getY());
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), farawayCell, PlaceableType.BLOCK);
            check = myConstruction.checkMove( buildMove, worker);
            assertTrue( !check );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( farawayCell.getHeigth() == 0 );
            assertTrue( cell.getTop().equals( worker ) );
            assertTrue( farawayCell.getTop() == null );
            assertTrue( cell.repOk() );
            assertTrue( farawayCell.repOk() );
            assertTrue( worker.position().equals( cell ));

            // clear board
            while ( cell.getTop() != null ) {
                cell.removePlaceable();
            }
            while ( farawayCell.getTop() != null ) {
                farawayCell.removePlaceable();
            }

            // CASE 2: farawayCell << cell.getX()
            farawayCell = board.getCellAt( cell.getX() - 2, cell.getY());
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), farawayCell, PlaceableType.BLOCK);
            check = myConstruction.checkMove( buildMove, worker);
            assertTrue( !check );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( farawayCell.getHeigth() == 0 );
            assertTrue( cell.getTop().equals( worker ) );
            assertTrue( farawayCell.getTop() == null );
            assertTrue( cell.repOk() );
            assertTrue( farawayCell.repOk() );
            assertTrue( worker.position().equals( cell ));

            // clear board
            while ( cell.getTop() != null ) {
                cell.removePlaceable();
            }
            while ( farawayCell.getTop() != null ) {
                farawayCell.removePlaceable();
            }

            // CASE 3: farawayCell.getY() >> cell.getY()
            farawayCell = board.getCellAt( cell.getX(), cell.getY() + 2);
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), farawayCell, PlaceableType.BLOCK);
            check = myConstruction.checkMove( buildMove, worker);
            assertTrue( !check );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( farawayCell.getHeigth() == 0 );
            assertTrue( cell.getTop().equals( worker ) );
            assertTrue( farawayCell.getTop() == null );
            assertTrue( cell.repOk() );
            assertTrue( farawayCell.repOk() );
            assertTrue( worker.position().equals( cell ));

            // clear board
            while ( cell.getTop() != null ) {
                cell.removePlaceable();
            }
            while ( farawayCell.getTop() != null ) {
                farawayCell.removePlaceable();
            }

            // CASE 3: farawayCell.getY() << cell.getY()
            farawayCell = board.getCellAt( cell.getX(), cell.getY() - 2);
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), farawayCell, PlaceableType.BLOCK);
            check = myConstruction.checkMove( buildMove, worker);
            assertTrue( !check );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( farawayCell.getHeigth() == 0 );
            assertTrue( cell.getTop().equals( worker ) );
            assertTrue( farawayCell.getTop() == null );
            assertTrue( cell.repOk() );
            assertTrue( farawayCell.repOk() );
            assertTrue( worker.position().equals( cell ));

            // clear board
            while ( cell.getTop() != null ) {
                cell.removePlaceable();
            }
            while ( farawayCell.getTop() != null ) {
                farawayCell.removePlaceable();
            }
        }
        catch ( OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue( !checkError );


        /* can't build on a Cell where there is another Worker */
        worker.place(cell);
        otherWorker.place(nearCell1);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 1 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().equals(otherWorker) );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));
        assertTrue( otherWorker.position().equals( nearCell1 ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }

        /* can't build on a Cell where there is a Dome */
        nearCell1.buildDome();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 1 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isDome() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        /* can't build a Dome on a Cell where there isn't three Blocks */
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 1 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        /* can build a Block on a Cell where there isn't three Blocks */
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 1 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        /* can build a Dome on a Cell where there is three Blocks */
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 3 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( nearCell1.getPlaceableAt(1).isBlock());
        assertTrue( nearCell1.getPlaceableAt(0).isBlock());
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }

    }

    /**
     * Check if special rules ( in CheckMove() ) are correctly checked
     * Methods used:        executeMove(BuildMove, Worker )     of  MyConstruction
     *                      decreaseConstructionLeft()          of  MyConstruction
     *                      resetConstructionLeft()             of  MyConstruction
     *                      setMovementExecuted( boolean )      of  Card
     *                      getCellAt( int, int)                of  IslandBoard
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
    void checkMoveSpecial() {
        GodPower mutantGod = new GodPower();
        mutantGod.setName("Mutant God");
        mutantGod.setEpithet("Mutant");
        mutantGod.setDescription("M");
        mutantGod.setMovementsLeft(1);
        mutantGod.setConstructionLeft(2);
        mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
        mutantGod.setForceOpponentInto(FloorDirection.NONE);
        mutantGod.setDeniedDirection(LevelDirection.NONE);
        mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);
        mutantGod.setActiveOnMyConstruction(true);
        Card mutantCard = new Card(mutantGod);
        myConstruction = new MyConstruction(mutantCard ,mutantGod);
        Worker otherWorker = new Worker(null);
        boolean check;
        boolean checkExecute;
        boolean checkError = false;

        //* Default rules for special God *//
        mutantGod.setDomeAtAnyLevel(true);
        mutantGod.setSameSpaceDenied(true);
        mutantGod.setForceConstructionOnSameSpace(true);

        /* can't build when ConstructionLeft <= 0 */
        myConstruction.decreaseConstructionLeft();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 0 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop() == null );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }
        myConstruction.resetConstructionLeft();


        /* can't build on the same cell where there is the Worker' */
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), cell, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( cell.repOk() );
        assertTrue( worker.position().equals( cell ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }


        /* can't build on cell too faraway */

        try {
            // CASE 1: farawayCell.getX() >> cell.getX()
            farawayCell = board.getCellAt( cell.getX() + 2, cell.getY());
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), farawayCell, PlaceableType.BLOCK);
            check = myConstruction.checkMove( buildMove, worker);
            assertTrue( !check );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( farawayCell.getHeigth() == 0 );
            assertTrue( cell.getTop().equals( worker ) );
            assertTrue( farawayCell.getTop() == null );
            assertTrue( cell.repOk() );
            assertTrue( farawayCell.repOk() );
            assertTrue( worker.position().equals( cell ));

            // clear board
            while ( cell.getTop() != null ) {
                cell.removePlaceable();
            }
            while ( farawayCell.getTop() != null ) {
                farawayCell.removePlaceable();
            }

            // CASE 2: farawayCell << cell.getX()
            farawayCell = board.getCellAt( cell.getX() - 2, cell.getY());
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), farawayCell, PlaceableType.BLOCK);
            check = myConstruction.checkMove( buildMove, worker);
            assertTrue( !check );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( farawayCell.getHeigth() == 0 );
            assertTrue( cell.getTop().equals( worker ) );
            assertTrue( farawayCell.getTop() == null );
            assertTrue( cell.repOk() );
            assertTrue( farawayCell.repOk() );
            assertTrue( worker.position().equals( cell ));

            // clear board
            while ( cell.getTop() != null ) {
                cell.removePlaceable();
            }
            while ( farawayCell.getTop() != null ) {
                farawayCell.removePlaceable();
            }

            // CASE 3: farawayCell.getY() >> cell.getY()
            farawayCell = board.getCellAt( cell.getX(), cell.getY() + 2);
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), farawayCell, PlaceableType.BLOCK);
            check = myConstruction.checkMove( buildMove, worker);
            assertTrue( !check );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( farawayCell.getHeigth() == 0 );
            assertTrue( cell.getTop().equals( worker ) );
            assertTrue( farawayCell.getTop() == null );
            assertTrue( cell.repOk() );
            assertTrue( farawayCell.repOk() );
            assertTrue( worker.position().equals( cell ));

            // clear board
            while ( cell.getTop() != null ) {
                cell.removePlaceable();
            }
            while ( farawayCell.getTop() != null ) {
                farawayCell.removePlaceable();
            }

            // CASE 3: farawayCell.getY() << cell.getY()
            farawayCell = board.getCellAt( cell.getX(), cell.getY() - 2);
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), farawayCell, PlaceableType.BLOCK);
            check = myConstruction.checkMove( buildMove, worker);
            assertTrue( !check );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( farawayCell.getHeigth() == 0 );
            assertTrue( cell.getTop().equals( worker ) );
            assertTrue( farawayCell.getTop() == null );
            assertTrue( cell.repOk() );
            assertTrue( farawayCell.repOk() );
            assertTrue( worker.position().equals( cell ));

            // clear board
            while ( cell.getTop() != null ) {
                cell.removePlaceable();
            }
            while ( farawayCell.getTop() != null ) {
                farawayCell.removePlaceable();
            }
        }
        catch ( OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue( !checkError );
        checkError = false;


        /* can't build on a Cell where there is another Worker */
        worker.place(cell);
        otherWorker.place(nearCell1);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 1 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().equals(otherWorker) );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));
        assertTrue( otherWorker.position().equals( nearCell1 ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }

        /* can't build on a Cell where there is a Dome */
        nearCell1.buildDome();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 1 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isDome() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }



        //* mutantGod with DomeAtAnyLevel == true ( Atlas ) *//
        mutantGod.setDomeAtAnyLevel( true );
        mutantGod.setSameSpaceDenied( false );
        mutantGod.setForceConstructionOnSameSpace( false );

        /* can build a Dome on a Cell where there isn't three Blocks */
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 1 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        //* mutantGod with DomeAtAnyLevel == false *//
        mutantGod.setDomeAtAnyLevel( false );
        mutantGod.setSameSpaceDenied( false );
        mutantGod.setForceConstructionOnSameSpace( false );

        /* can't build a Dome on a Cell where there isn't three Blocks */
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 1 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        /* can build a Block on a Cell where there isn't three Blocks */
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 1 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        /* can build a Dome on a Cell where there is three Blocks */
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 3 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( nearCell1.getPlaceableAt(1).isBlock());
        assertTrue( nearCell1.getPlaceableAt(0).isBlock());
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }



        //* mutantGod with SameSpaceDenied == true, fist Construction ( Demetra ) *//
        mutantGod.setDomeAtAnyLevel( false );
        mutantGod.setSameSpaceDenied( true );
        mutantGod.setForceConstructionOnSameSpace( false );

        /* can build a Block on a near Cell at the first time ( lastMove == null )*/
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 1 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));



        //* mutantGod with ForceConstructionOnSameCell == true, fist Construction ( Hephaestus ) *//
        mutantGod.setDomeAtAnyLevel( false );
        mutantGod.setSameSpaceDenied( false );
        mutantGod.setForceConstructionOnSameSpace( true );

        /* can build a Block on a near Cell at the first time ( lastMove == null )*/
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 1 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));

        // clear board
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        // First BuildMove executed

        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue( checkExecute );
        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue( !checkError );
        checkError = false;

        myConstruction.decreaseConstructionLeft();


        //* mutantGod with SameSpaceDenied == true, second Construction ( Demetra ) *//
        mutantGod.setDomeAtAnyLevel( false );
        mutantGod.setSameSpaceDenied( true );
        mutantGod.setForceConstructionOnSameSpace( false );

        /* can't build a Block on the same Cell at the second time ( lastMove == nearCell1 )*/
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 2 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));


        /* can build a Block on different Cell at the second time ( lastMove == nearCell1 )*/
        buildMove = new BuildMove(worker.position(), nearCell2, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 2 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));



        //* mutantGod with ForceConstructionOnSameSpace == true, second Construction ( Hephaestus ) *//
        mutantGod.setDomeAtAnyLevel( false );
        mutantGod.setSameSpaceDenied( false );
        mutantGod.setForceConstructionOnSameSpace( true );

        /* can build a Block on the same Cell at the second time ( lastMove == nearCell1 )*/
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 2 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));


        /* can't build a Block on different Cell at the second time ( lastMove == nearCell1 )*/
        buildMove = new BuildMove(worker.position(), nearCell2, PlaceableType.BLOCK);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( !check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 2 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));


        /* can't build a Dome on the same Cell ( with three Blocks ) at the second time ( lastMove == nearCell1 )*/
        nearCell1.buildBlock();
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 3 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( nearCell1.getPlaceableAt(1).isBlock() );
        assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));



        // reset ConstructionLeft: new  my turn start
        myConstruction.resetConstructionLeft();


        //* mutantGod with SameSpaceDenied == true, first Construction after new turn ( Demetra ) *//
        mutantGod.setDomeAtAnyLevel( false );
        mutantGod.setSameSpaceDenied( true );
        mutantGod.setForceConstructionOnSameSpace( false );

        /* can build on the same Cell where I built in the last my turn at the first time in this turn ( lastMove != null ) */
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 3 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( nearCell1.getPlaceableAt(1).isBlock() );
        assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));



        //* mutantGod with ForceConstructionLeft == true, first Construction after new turn ( Hephaestus ) *//
        mutantGod.setDomeAtAnyLevel( false );
        mutantGod.setSameSpaceDenied( false );
        mutantGod.setForceConstructionOnSameSpace( true );

        /* can build a Dome on different Cell where I built in the last my turn at the first time in this turn ( lastMove != null ) */
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);
        check = myConstruction.checkMove( buildMove, worker);
        assertTrue( check );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 3 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( nearCell1.getPlaceableAt(1).isBlock() );
        assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));

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
        myConstruction = new MyConstruction(card, godPower);

        assertTrue(myConstruction.getGodPower().equals(godPower));

    }

    /**
     * Check if getLastMove() can return the correct value after a move correct or not
     * Methods used:        executeMove( BuildMove, worker)      of  MyConstructiom
     *                      position()                      of  Placeable
     *                      place()                         of  Worker
     *
     * Black and White Box
     */
    @Test
    void getLastMove() {
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
        myConstruction = new MyConstruction(humanCard, human );
        BuildMove correctMove;
        BuildMove wrongMove;
        boolean checkExecute;
        boolean checkOutOfBoard = false;

        // after initialization
        assertTrue( myConstruction.getLastMove() == null );


        /* Check if LastMove is correctly changed after a correct BuildMove */
        worker.place(cell);
        correctMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK );
        try {
            checkExecute = myConstruction.executeMove(correctMove, worker);
            assertTrue( checkExecute );
            assertTrue( myConstruction.getLastMove().equals(correctMove) );
        } catch (OutOfBoardException e) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );


        /* Check if LastMove is correctly changed after a correct BuildMove */
        worker.place(cell);
        wrongMove = new BuildMove(worker.position(), cell, PlaceableType.BLOCK );
        try {
            checkExecute = myConstruction.executeMove(wrongMove, worker);
            assertTrue( !checkExecute );
            assertTrue( myConstruction.getLastMove().equals(correctMove) );
        } catch (OutOfBoardException e) {
            checkOutOfBoard = true;
        }
        assertTrue( !checkOutOfBoard );

    }

    /**
     * Check if getConstructionLeft(), decreaseConstructionLeft(), resetConstructionLeft() can set and return the correct value
     *
     * Black and White Box
     */
    @Test
    void getConstructionLeftAndDecreaseConstructionLeftAndResetConstructionLeft() {
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
        myConstruction = new MyConstruction(humanCard, human );


        // after initialization
        assertTrue(myConstruction.getConstructionLeft() == 1);

        // after decreaseMovesLeft
        myConstruction.resetConstructionLeft();
        assertTrue(myConstruction.getConstructionLeft() == 0);

        // after resetMovesLeft()
        myConstruction.resetConstructionLeft();
        assertTrue(myConstruction.getConstructionLeft() == 1);

    }


}