package it.polimi.ingsw.model.card.build;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.IslandBoard;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardParser;
import it.polimi.ingsw.model.card.GodPower;
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

    boolean checkExecute;
    boolean checkBuild;

    MyConstruction myConstruction;
    BuildMove buildMove;
    Player player = new Player( "Player1");
    Board board;
    Cell cell;
    Cell nearCell1;
    Cell nearCell2;
    Cell farawayCell;
    Worker worker;

    GodPower human;
    Card humanCard;

    GodPower atlas;
    Card atlasCard;

    GodPower demeter;
    Card demeterCard;

    GodPower hephaestus;
    Card hephaestusCard;

    GodPower prometheus;
    Card prometheusCard;


    /**
     * Initialization before method's test
     * Methods used:        getCellAt( ini, int)            of  IslandBoard
     */
    @BeforeEach
    void setUp() {

        checkExecute = false;
        checkBuild = false;

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

        // Atlas's GodPower and Card initialization
        atlas = new GodPower();
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
        atlasCard = new Card(   atlas,
                                CardParser.getMoveCheckers(atlas),
                                CardParser.getMoveExecutor(atlas),
                                CardParser.getBuildCheckers(atlas),
                                CardParser.getBuildExecutor(atlas),
                                CardParser.getWinCheckers(atlas),
                                CardParser.getAdversaryMoveCheckers(atlas));

        // Demeter's GodPower and Card initialization
        demeter = new GodPower();
        demeter.setName("Demeter");
        demeter.setEpithet("Goddess of the Harvest");
        demeter.setDescription("D");
        demeter.setMovementsLeft(1);
        demeter.setConstructionLeft(2);
        demeter.setSameSpaceDenied(true);
        demeter.setHotLastMoveDirection(LevelDirection.NONE);
        demeter.setForceOpponentInto(FloorDirection.NONE);
        demeter.setDeniedDirection(LevelDirection.NONE);
        demeter.setOpponentDeniedDirection(LevelDirection.NONE);
        demeter.setActiveOnMyConstruction(true);
        demeterCard = new Card( demeter,
                                CardParser.getMoveCheckers(demeter),
                                CardParser.getMoveExecutor(demeter),
                                CardParser.getBuildCheckers(demeter),
                                CardParser.getBuildExecutor(demeter),
                                CardParser.getWinCheckers(demeter),
                                CardParser.getAdversaryMoveCheckers(demeter));

        // Hephaestus's GodPower and Card initialization
        hephaestus = new GodPower();
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
        hephaestus.setForceConstructionOnSameSpace(true);
        hephaestusCard = new Card(  hephaestus,
                                    CardParser.getMoveCheckers(hephaestus),
                                    CardParser.getMoveExecutor(hephaestus),
                                    CardParser.getBuildCheckers(hephaestus),
                                    CardParser.getBuildExecutor(hephaestus),
                                    CardParser.getWinCheckers(hephaestus),
                                    CardParser.getAdversaryMoveCheckers(hephaestus));

        // Prometheus's GodPower and Card initialization
        prometheus = new GodPower();
        prometheus.setName("Prometheus");
        prometheus.setEpithet("Titan Benefactor of Mankind");
        prometheus.setDescription("P");
        prometheus.setMovementsLeft(1);
        prometheus.setConstructionLeft(2);
        prometheus.setActiveOnMyMovement(true);
        prometheus.setHotLastMoveDirection(LevelDirection.UP);
        prometheus.setForceOpponentInto(FloorDirection.NONE);
        prometheus.setDeniedDirection(LevelDirection.UP);
        prometheus.setOpponentDeniedDirection(LevelDirection.NONE);
        prometheus.setActiveOnMyConstruction(true);
        prometheus.setBuildBeforeMovement(true);
        prometheusCard = new Card(  prometheus,
                                    CardParser.getMoveCheckers(prometheus),
                                    CardParser.getMoveExecutor(prometheus),
                                    CardParser.getBuildCheckers(prometheus),
                                    CardParser.getBuildExecutor(prometheus),
                                    CardParser.getWinCheckers(prometheus),
                                    CardParser.getAdversaryMoveCheckers(prometheus));


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

        human = null;
        humanCard = null;

        atlas = null;
        atlasCard = null;

        demeter = null;
        demeterCard = null;

        hephaestus = null;
        hephaestusCard = null;

        prometheus = null;
        prometheusCard = null;

    }

    /**
     * Check if the default moves on board are correctly checked and executed
     * Methods used:        getMyConstruction()     of  Card
     *                      getLastMove()           of  MyConstruction
     *                      getCellAt( int, int)    of  IslandBoard
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
    void checkAndExecuteMoveDefaultBlack() {
        myConstruction = humanCard.getMyConstruction();
        Worker otherWorker = new Worker( null );
        boolean checkOutOfBoard = false;


        /* can build a Block on a near free Cell */
        worker.place( cell );
        buildMove = new BuildMove( worker.position(), nearCell1, PlaceableType.BLOCK );
        try {
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
            checkExecute = myConstruction.executeMove( buildMove, worker );
            assertTrue( checkExecute );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( worker.position().equals( cell ) );
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }

        // clear Cells
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        /* can't build a Dome on a near free Cell */
        worker.place( cell );
        buildMove = new BuildMove( worker.position(), nearCell1, PlaceableType.DOME );

        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue( !checkBuild );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 0 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( nearCell1.getTop() == null );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ) );

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
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
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
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }

        // clear Cells
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        /* can't build a Dome on a near Cell with two Block */
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        worker.place( cell );
        buildMove = new BuildMove( worker.position(), nearCell1, PlaceableType.DOME );

        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue( !checkBuild );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 2 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ) );

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

        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue( !checkBuild );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( cell.repOk() );
        assertTrue( worker.position().equals( cell ) );

        // clear Cells
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }


        /* can't build on a faraway Cell */
        worker.place( cell );
        buildMove = new BuildMove( worker.position(), farawayCell, PlaceableType.BLOCK );

        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue( !checkBuild );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( farawayCell.getHeigth() == 0 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( farawayCell.getTop() == null );
        assertTrue( cell.repOk() );
        assertTrue( farawayCell.repOk() );
        assertTrue( worker.position().equals( cell ) );

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
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
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


        /* can't build a Block on a near Cell with three Block */
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        worker.place( cell );
        buildMove = new BuildMove( worker.position(), nearCell1, PlaceableType.BLOCK );

        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue( checkBuild );  // control for build block at the third block is Cell methods used during execution and not in check
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 3 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( nearCell1.getPlaceableAt(1).isBlock() );
        assertTrue( nearCell1.getPlaceableAt(0).isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ) );

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

        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue( !checkBuild );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 1 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( nearCell1.getTop().isDome() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ) );

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

        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue( !checkBuild );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 1 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( nearCell1.getTop().equals( otherWorker ) );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ) );
        assertTrue( otherWorker.position().equals( nearCell1 ) );

        // clear Cells
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }


        /* can't build a Dome on a near Cell with three Block if there is another Worker */
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        worker.place( cell );
        otherWorker.place( nearCell1 );
        buildMove = new BuildMove( worker.position(), nearCell1, PlaceableType.DOME );

        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue( !checkBuild );
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
     * Methods used:        getMyConstruction()                     of  Card
     *                      setConstructionExecuted( boolean )      of  Card
     *                      getLastMove()                           of  MyConstruction
     *                      decreaseConstructionLeft()              of  MyConstruction
     *                      resetConstructionLeft()                 of  MyConstruction
     *                      getHeigth()                             of  Cell
     *                      repOk()                                 of  Cell
     *                      getTop()                                of  Cell
     *                      getPlaceableAt( int )                   of  Cell
     *                      buildBlock()                            of  Cell
     *                      position()                              of  Placeable
     *                      isBlock()                               of  Placeable/Block
     *                      place( Cell )                           of  Worker
     *
     * Black Box
     */
    @Test
    void executeMoveDemetraBlack() {
        myConstruction = demeterCard.getMyConstruction();
        boolean checkOutOfBoard = false;


        /* can build a Block at the first time and another Block on another Cell at the second time*/
        nearCell2.buildBlock();
        worker.place( cell );
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
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
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }

        myConstruction.decreaseConstructionLeft();
        demeterCard.setConstructionExecuted(true);

        buildMove = new BuildMove(worker.position(), nearCell2, PlaceableType.BLOCK);
        try {
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
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
        } finally {
        assertTrue(!checkOutOfBoard);
        checkOutOfBoard = false;
        }

        // reset ConstructionLeft, constructionExecuted and clear Cells
        myConstruction.resetConstructionLeft();
        demeterCard.setConstructionExecuted(false);
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }
        while ( nearCell2.getTop() != null ) {
            nearCell2.removePlaceable();
        }


        /* can build a Block at the first time but not another Block on the same Cell at the second time*/
        worker.place( cell );
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue( checkExecute );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( worker.position().equals( cell ) );
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }


        myConstruction.decreaseConstructionLeft();
        demeterCard.setConstructionExecuted(true);

        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);

        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue( !checkBuild );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 1 );
        assertTrue( cell.getTop().equals(worker) );
        assertTrue( nearCell1.getTop().isBlock() );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ) );


        // reset ConstructionLeft, constructionExecuted and clear Cells
        myConstruction.resetConstructionLeft();
        demeterCard.setConstructionExecuted(false);
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }
        while ( nearCell2.getTop() != null ) {
            nearCell2.removePlaceable();
        }


        /* can build a Block at the first time but not another Block on faraway Cell at the second time*/
        worker.place( cell );
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue( checkExecute );
            assertTrue( cell.getHeigth() == 1 );
            assertTrue( nearCell1.getHeigth() == 1 );
            assertTrue( cell.getTop().equals(worker) );
            assertTrue( nearCell1.getTop().isBlock() );
            assertTrue( cell.repOk() );
            assertTrue( nearCell1.repOk() );
            assertTrue( worker.position().equals( cell ) );
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }

        myConstruction.decreaseConstructionLeft();
        demeterCard.setConstructionExecuted(true);

        buildMove = new BuildMove(worker.position(), farawayCell, PlaceableType.BLOCK);

        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue( !checkBuild );
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

        // reset ConstructionLeft, constructionExecuted and clear Cells
        myConstruction.resetConstructionLeft();
        demeterCard.setConstructionExecuted(false);
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }
        while ( farawayCell.getTop() != null ) {
            farawayCell.removePlaceable();
        }


        /* can build a Block at the first time and a Dome on another Cell with three Blocks at the second time*/
        nearCell2.buildBlock();
        nearCell2.buildBlock();
        nearCell2.buildBlock();
        worker.place( cell );
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
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
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }

        myConstruction.decreaseConstructionLeft();
        demeterCard.setConstructionExecuted(true);

        buildMove = new BuildMove(worker.position(), nearCell2, PlaceableType.DOME);
        try {
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
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
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }

        // reset ConstructionLeft, constructionExecuted and clear Cells
        myConstruction.resetConstructionLeft();
        demeterCard.setConstructionExecuted(false);
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }
        while ( nearCell2.getTop() != null ) {
            nearCell2.removePlaceable();
        }

        /* can build a Block at the first time but not a Block on another Cell with three Blocks at the second time*/
        nearCell2.buildBlock();
        nearCell2.buildBlock();
        nearCell2.buildBlock();
        worker.place( cell );
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
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
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }

        myConstruction.decreaseConstructionLeft();
        demeterCard.setConstructionExecuted(true);

        buildMove = new BuildMove(worker.position(), nearCell2, PlaceableType.BLOCK);

        try {
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );  // control of can't build a block on a cell with three block is executed by Cell's methods during execution, not by checkMove
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
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }


        // reset ConstructionLeft, constructionExecuted and clear Cells
        myConstruction.resetConstructionLeft();
        demeterCard.setConstructionExecuted(false);
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
     * Methods used:        getMyConstruction()                     of  Card
     *                      setConstructionExecuted( boolean )      of  Card
     *                      getLastMove()                           of  MyConstruction
     *                      decreaseConstructionLeft()              of  MyConstruction
     *                      resetConstructionLeft()                 of  MyConstruction
     *                      getHeigth()                             of  Cell
     *                      repOk()                                 of  Cell
     *                      getTop()                                of  Cell
     *                      getPlaceableAt( int )                   of  Cell
     *                      buildBlock()                            of  Cell
     *                      position()                              of  Placeable
     *                      isBlock()                               of  Placeable/Block
     *                      place( Cell )                           of  Worker
     *
     * Black Box
     */
    @Test
    void executeMoveHephaestusBlack() {
        myConstruction = hephaestusCard.getMyConstruction();
        boolean checkOutOfBoard = false;


        /* can build a Block at the first time on a Cells without Blocks and another Block on same Cell at the second time*/
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue(checkExecute);
            assertTrue(cell.getHeigth() == 1);
            assertTrue(nearCell1.getHeigth() == 1);
            assertTrue(cell.getTop().equals(worker));
            assertTrue(nearCell1.getTop().isBlock());
            assertTrue(cell.repOk());
            assertTrue(nearCell1.repOk());
            assertTrue(worker.position().equals(cell));
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }

        myConstruction.decreaseConstructionLeft();
        hephaestusCard.setConstructionExecuted(true);

        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
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
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        }
        assertTrue(!checkOutOfBoard);

        // reset ConstructionLeft, constructionExecuted and clear Cells
        myConstruction.resetConstructionLeft();
        hephaestusCard.setConstructionExecuted(false);
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
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
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
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }


        myConstruction.decreaseConstructionLeft();
        hephaestusCard.setConstructionExecuted(true);

        buildMove = new BuildMove(worker.position(), nearCell2, PlaceableType.BLOCK);

        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue( !checkBuild );
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

        // reset ConstructionLeft, constructionExecuted and clear Cells
        myConstruction.resetConstructionLeft();
        hephaestusCard.setConstructionExecuted(false);
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
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
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
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }

        myConstruction.decreaseConstructionLeft();
        hephaestusCard.setConstructionExecuted(true);

        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);

        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue( !checkBuild );
        assertTrue(cell.getHeigth() == 1);
        assertTrue(nearCell1.getHeigth() == 3);
        assertTrue(cell.getTop().equals(worker));
        assertTrue(nearCell1.getTop().isBlock());
        assertTrue(nearCell1.getPlaceableAt(1).isBlock());
        assertTrue(nearCell1.getPlaceableAt(0).isBlock());
        assertTrue(cell.repOk());
        assertTrue(nearCell1.repOk());
        assertTrue(worker.position().equals(cell));

        // reset ConstructionLeft, constructionExecuted and clear Cells
        myConstruction.resetConstructionLeft();
        hephaestusCard.setConstructionExecuted(false);
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }


        /* can build a Block at the first time on a Cells with two Blocks and another Block on same Cell at the second time*/
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
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
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }

        myConstruction.decreaseConstructionLeft();
        hephaestusCard.setConstructionExecuted(true);

        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);

        try {
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue(checkBuild);
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue(checkExecute);
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
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }

        // reset ConstructionLeft, constructionExecuted and clear Cells
        myConstruction.resetConstructionLeft();
        hephaestusCard.setConstructionExecuted(false);
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }

    }

    /**
     * Check if Atlas' Power is correctly checked and executed
     * Methods used:        getMyConstruction()     of  Card
     *                      getLastMove()           of  MyConstruction
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
    void executeMoveAtlasBlack() {
        myConstruction = atlasCard.getMyConstruction();
        Worker otherWorker = new Worker(null);
        boolean checkOutOfBoard = false;


        /* can build a Block on a near Cell */
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        try {
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
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
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }

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
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
            checkExecute = myConstruction.executeMove(buildMove, worker);
            assertTrue(checkExecute);
            assertTrue(cell.getHeigth() == 1);
            assertTrue(nearCell1.getHeigth() == 1);
            assertTrue(cell.getTop().equals(worker));
            assertTrue(nearCell1.getTop().isDome());
            assertTrue(cell.repOk());
            assertTrue(nearCell1.repOk());
            assertTrue(worker.position().equals(cell));
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }

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
            checkBuild = myConstruction.checkMove(buildMove, worker);
            assertTrue( checkBuild );
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
            assertTrue( myConstruction.getLastMove().equals(buildMove));

        } catch (OutOfBoardException o) {
            checkOutOfBoard = true;
        } finally {
            assertTrue(!checkOutOfBoard);
            checkOutOfBoard = false;
        }


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

        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue( !checkBuild );
        assertTrue(cell.getHeigth() == 1);
        assertTrue(nearCell1.getHeigth() == 1);
        assertTrue(cell.getTop().equals(worker));
        assertTrue(nearCell1.getTop().isDome());
        assertTrue(cell.repOk());
        assertTrue(nearCell1.repOk());
        assertTrue(worker.position().equals(cell));


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

        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue( !checkBuild );
        assertTrue(cell.getHeigth() == 1);
        assertTrue(nearCell1.getHeigth() == 1);
        assertTrue(cell.getTop().equals(worker));
        assertTrue(nearCell1.getTop().equals(otherWorker));
        assertTrue(cell.repOk());
        assertTrue(nearCell1.repOk());
        assertTrue(worker.position().equals(cell));
        assertTrue(otherWorker.position().equals(nearCell1));


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

        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue( !checkBuild );
        assertTrue(cell.getHeigth() == 1);
        assertTrue(farawayCell.getHeigth() == 0);
        assertTrue(cell.getTop().equals(worker));
        assertTrue(farawayCell.getTop() == null);
        assertTrue(cell.repOk());
        assertTrue(farawayCell.repOk());
        assertTrue(worker.position().equals(cell));


        // clear Cells
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (farawayCell.getTop() != null) {
            farawayCell.removePlaceable();
        }
    }


    /**
     * Check if with the default configure of checkBuildList returned from CardParser checkMyMove() can correctly check
     * the construction ( white test of first two lambda expression of CardParser's getBuildCheckers())
     * Methods used:        getMyConstruction()             of  Card
     *                      decreaseConstructionLeft()      of  MyConstruction
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
    void checkMoveDefaultWhite() {
        Worker otherWorker = new Worker(null);
        boolean checkError = false;

        //***god without power***//

        myConstruction = humanCard.getMyConstruction();


        //>> default effect checkMove

        /* can't build when ConstructionLeft <= 0 */
        myConstruction.decreaseConstructionLeft();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.BLOCK);
        checkBuild = myConstruction.checkMove( buildMove, worker);
        assertTrue( !checkBuild );
        assertTrue( cell.getHeigth() == 1 );
        assertTrue( nearCell1.getHeigth() == 0 );
        assertTrue( cell.getTop().equals( worker ) );
        assertTrue( nearCell1.getTop() == null );
        assertTrue( cell.repOk() );
        assertTrue( nearCell1.repOk() );
        assertTrue( worker.position().equals( cell ));

        // clear board and reset constructionLeft
        while ( cell.getTop() != null ) {
            cell.removePlaceable();
        }
        while ( nearCell1.getTop() != null ) {
            nearCell1.removePlaceable();
        }
        myConstruction.resetConstructionLeft();


        /* can't build on the same cell where there is the worker */
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), cell, PlaceableType.BLOCK);
        checkBuild = myConstruction.checkMove( buildMove, worker);
        assertTrue( !checkBuild );
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
            checkBuild = myConstruction.checkMove( buildMove, worker);
            assertTrue( !checkBuild );
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
            checkBuild = myConstruction.checkMove( buildMove, worker);
            assertTrue( !checkBuild );
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
            checkBuild = myConstruction.checkMove( buildMove, worker);
            assertTrue( !checkBuild );
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
            checkBuild = myConstruction.checkMove( buildMove, worker);
            assertTrue( !checkBuild );
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
        checkBuild = myConstruction.checkMove( buildMove, worker);
        assertTrue( !checkBuild );
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
        checkBuild = myConstruction.checkMove( buildMove, worker);
        assertTrue( !checkBuild );
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


        //>> effect for cards which haven't "active on my construction" configure

        /* can't build a Dome on a Cell where there isn't three Blocks */
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);
        checkBuild = myConstruction.checkMove( buildMove, worker);
        assertTrue( !checkBuild );
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
        checkBuild = myConstruction.checkMove( buildMove, worker);
        assertTrue( checkBuild );
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
        checkBuild = myConstruction.checkMove( buildMove, worker);
        assertTrue( checkBuild );
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
     * Check if with "only active on my construction" configure of checkBuildList returned from CardParser
     * checkMyMove() can correctly check the construction
     * ( white test of first special rule of CardParser's getBuildCheckers())
     * Methods used:        getMyConstruction()             of  Card
     *                      decreaseConstructionLeft()      of  MyConstruction
     *                      resetConstructionLeft           of  MyConstruction
     *                      getCellAt( int, int)            of  IslandBoard
     *                      getTop()                        of  Cell
     *                      getHeigth()                     of  Cell
     *                      getPlaceableAt( int )           of  Cell
     *                      repOk()                         of  Cell
     *                      removePlaceable()               of  Cell
     *                      buildBlock()                    of  Cell
     *                      position()                      of  Placeable
     *                      isBlock()                       of  Placeable/Block
     *                      place( Cell )                   of  Worker
     *
     * White Box
     */
    @Test
    void checkMoveActiveOnMyConstructionWhite() {

        //***god who have special effect on his construction but he can't build dome at any level***//

        myConstruction = prometheusCard.getMyConstruction();


        //>> effect for cards which have "active on my construction" configure

        /* can't build a Dome on a Cell where there isn't three Blocks */
        nearCell1.buildBlock();
        worker.place(cell);
        buildMove = new BuildMove(worker.position(), nearCell1, PlaceableType.DOME);
        checkBuild = myConstruction.checkMove( buildMove, worker);
        assertTrue( !checkBuild );
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
        checkBuild = myConstruction.checkMove( buildMove, worker);
        assertTrue( checkBuild );
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
        checkBuild = myConstruction.checkMove( buildMove, worker);
        assertTrue( checkBuild );
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
     * Check if with "cannot build at the same cell at the second time" configure of checkBuildList
     * returned from CardParser checkMyMove() can correctly check the construction
     * ( white test of "cannot build at the same cell at the second time" rule of CardParser's getBuildCheckers())
     * Methods used:        getMyConstruction()                     of  Card
     *                      setExecutedConstructio( boolean )       of  Card
     *                      setLastMove( BuildMove )                of  MyConstruction
     *                      getCellAt( int, int)                    of  IslandBoard
     *                      getTop()                                of  Cell
     *                      getHeigth()                             of  Cell
     *                      getPlaceableAt( int )                   of  Cell
     *                      repOk()                                 of  Cell
     *                      removePlaceable()                       of  Cell
     *                      buildBlock()                            of  Cell
     *                      position()                              of  Placeable
     *                      isBlock()                               of  Placeable/Block
     *                      place( Cell )                           of  Worker
     *
     * White Box
     */
    @Test
    void checkMoveSameSpaceDeniedWhite() {
        BuildMove lastBuildMove;

        //***god who can build two time but not on the same cell (Demeter)***//

        myConstruction = demeterCard.getMyConstruction();

        //>> effect for cards which have "cannot build at the same cell at the second time" configure

        // can build a block on near free cell at the first time
        worker.place(cell);
        buildMove = new BuildMove(cell, nearCell1, PlaceableType.BLOCK);
        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue(checkBuild);
        assertTrue(cell.getHeigth() == 1);
        assertTrue(nearCell1.getHeigth() == 0);
        assertTrue(cell.getTop().equals(worker));
        assertTrue(nearCell1.getTop() == null);
        assertTrue(cell.repOk());
        assertTrue(nearCell1.repOk());
        assertTrue(worker.position().equals(cell));

        // clear board
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }


        // can't build a block on same cell at the second time
        lastBuildMove = new BuildMove(cell, nearCell1, PlaceableType.BLOCK);
        demeterCard.setConstructionExecuted(true);
        myConstruction.setLastMove(lastBuildMove);
        worker.place(cell);
        nearCell1.buildBlock();
        buildMove = new BuildMove(cell, nearCell1, PlaceableType.BLOCK);
        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue(!checkBuild);
        assertTrue(cell.getHeigth() == 1);
        assertTrue(nearCell1.getHeigth() == 1);
        assertTrue(cell.getTop().equals(worker));
        assertTrue(nearCell1.getTop().isBlock());
        assertTrue(cell.repOk());
        assertTrue(nearCell1.repOk());
        assertTrue(worker.position().equals(cell));

        // clear board
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }

        // can build a block on different cell at the second time
        lastBuildMove = new BuildMove(cell, nearCell1, PlaceableType.BLOCK);
        demeterCard.setConstructionExecuted(true);
        myConstruction.setLastMove(lastBuildMove);
        worker.place(cell);
        nearCell1.buildBlock();
        buildMove = new BuildMove(cell, nearCell2, PlaceableType.BLOCK);
        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue(checkBuild);
        assertTrue(cell.getHeigth() == 1);
        assertTrue(nearCell1.getHeigth() == 1);
        assertTrue(nearCell2.getHeigth() == 0);
        assertTrue(cell.getTop().equals(worker));
        assertTrue(nearCell1.getTop().isBlock());
        assertTrue(nearCell2.getTop() == null);
        assertTrue(cell.repOk());
        assertTrue(nearCell1.repOk());
        assertTrue(nearCell2.repOk());
        assertTrue(worker.position().equals(cell));

        // clear board
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }
        while (nearCell2.getTop() != null) {
            nearCell2.removePlaceable();
        }
    }

    /**
     * Check if with "must build a blockat the same cell at the second time" configure of checkBuildList
     * returned from CardParser checkMyMove() can correctly check the construction
     * ( white test of "must build a block at the same cell at the second time" rule of CardParser's getBuildCheckers())
     * Methods used:        getMyConstruction()                     of  Card
     *                      setExecutedConstructio( boolean )       of  Card
     *                      setLastMove( BuildMove )                of  MyConstruction
     *                      getCellAt( int, int)                    of  IslandBoard
     *                      getTop()                                of  Cell
     *                      getHeigth()                             of  Cell
     *                      getPlaceableAt( int )                   of  Cell
     *                      repOk()                                 of  Cell
     *                      removePlaceable()                       of  Cell
     *                      buildBlock()                            of  Cell
     *                      position()                              of  Placeable
     *                      isBlock()                               of  Placeable/Block
     *                      place( Cell )                           of  Worker
     *
     * White Box
     */
    @Test
    void checkMoveSameSpaceBlockWhite() {
        BuildMove lastBuildMove;

        //***god who can build two time only on the same cell (Hephaestus)***//

        myConstruction = hephaestusCard.getMyConstruction();

        //>> effect for cards which have "must build a block at the same cell at the second time" configure

        // can build a dome on near cell with three blocks at the first time
        worker.place(cell);
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        buildMove = new BuildMove(cell, nearCell1, PlaceableType.DOME);
        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue(checkBuild);
        assertTrue(cell.getHeigth() == 1);
        assertTrue(nearCell1.getHeigth() == 3);
        assertTrue(cell.getTop().equals(worker));
        assertTrue(nearCell1.getTop().isBlock());
        assertTrue(nearCell1.getPlaceableAt(1).isBlock());
        assertTrue(nearCell1.getPlaceableAt(0).isBlock());
        assertTrue(cell.repOk());
        assertTrue(nearCell1.repOk());
        assertTrue(worker.position().equals(cell));

        // clear board
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }


        // can build a block on same cell at the second time
        lastBuildMove = new BuildMove(cell, nearCell1, PlaceableType.BLOCK);
        hephaestusCard.setConstructionExecuted(true);
        myConstruction.setLastMove(lastBuildMove);
        worker.place(cell);
        nearCell1.buildBlock();
        buildMove = new BuildMove(cell, nearCell1, PlaceableType.BLOCK);
        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue(checkBuild);
        assertTrue(cell.getHeigth() == 1);
        assertTrue(nearCell1.getHeigth() == 1);
        assertTrue(cell.getTop().equals(worker));
        assertTrue(nearCell1.getTop().isBlock());
        assertTrue(cell.repOk());
        assertTrue(nearCell1.repOk());
        assertTrue(worker.position().equals(cell));

        // clear board
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }


        // can't build a block on different cell at the second time
        lastBuildMove = new BuildMove(cell, nearCell1, PlaceableType.BLOCK);
        hephaestusCard.setConstructionExecuted(true);
        myConstruction.setLastMove(lastBuildMove);
        worker.place(cell);
        nearCell1.buildBlock();
        buildMove = new BuildMove(cell, nearCell2, PlaceableType.BLOCK);
        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue(!checkBuild);
        assertTrue(cell.getHeigth() == 1);
        assertTrue(nearCell1.getHeigth() == 1);
        assertTrue(nearCell2.getHeigth() == 0);
        assertTrue(cell.getTop().equals(worker));
        assertTrue(nearCell1.getTop().isBlock());
        assertTrue(nearCell2.getTop() == null);
        assertTrue(cell.repOk());
        assertTrue(nearCell1.repOk());
        assertTrue(nearCell2.repOk());
        assertTrue(worker.position().equals(cell));

        // clear board
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }
        while (nearCell2.getTop() != null) {
            nearCell2.removePlaceable();
        }


        // can't build a dome on same cell at the second time
        lastBuildMove = new BuildMove(cell, nearCell1, PlaceableType.BLOCK);
        hephaestusCard.setConstructionExecuted(true);
        myConstruction.setLastMove(lastBuildMove);
        worker.place(cell);
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        buildMove = new BuildMove(cell, nearCell1, PlaceableType.DOME);
        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue(!checkBuild);
        assertTrue(cell.getHeigth() == 1);
        assertTrue(nearCell1.getHeigth() == 3);
        assertTrue(cell.getTop().equals(worker));
        assertTrue(nearCell1.getTop().isBlock());
        assertTrue(nearCell1.getPlaceableAt(1).isBlock());
        assertTrue(nearCell1.getPlaceableAt(0).isBlock());
        assertTrue(cell.repOk());
        assertTrue(nearCell1.repOk());
        assertTrue(worker.position().equals(cell));

        // clear board
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }


        // can't build a dome on different cell at the second time
        lastBuildMove = new BuildMove(cell, nearCell1, PlaceableType.BLOCK);
        hephaestusCard.setConstructionExecuted(true);
        myConstruction.setLastMove(lastBuildMove);
        worker.place(cell);
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        nearCell1.buildBlock();
        nearCell2.buildBlock();
        nearCell2.buildBlock();
        nearCell2.buildBlock();
        buildMove = new BuildMove(cell, nearCell2, PlaceableType.DOME);
        checkBuild = myConstruction.checkMove(buildMove, worker);
        assertTrue(!checkBuild);
        assertTrue(cell.getHeigth() == 1);
        assertTrue(nearCell1.getHeigth() == 3);
        assertTrue(nearCell2.getHeigth() == 3);
        assertTrue(cell.getTop().equals(worker));
        assertTrue(nearCell1.getTop().isBlock());
        assertTrue(nearCell1.getPlaceableAt(1).isBlock());
        assertTrue(nearCell1.getPlaceableAt(0).isBlock());
        assertTrue(nearCell2.getTop().isBlock());
        assertTrue(nearCell2.getPlaceableAt(1).isBlock());
        assertTrue(nearCell2.getPlaceableAt(0).isBlock());
        assertTrue(cell.repOk());
        assertTrue(nearCell1.repOk());
        assertTrue(nearCell2.repOk());
        assertTrue(worker.position().equals(cell));

        // clear board
        while (cell.getTop() != null) {
            cell.removePlaceable();
        }
        while (nearCell1.getTop() != null) {
            nearCell1.removePlaceable();
        }
        while (nearCell2.getTop() != null) {
            nearCell2.removePlaceable();
        }


    }

    /**
     * Check if getGodPower() can return the correct value after initialization
     * Methods used:        getMyConstruction()         of  Card
     *
     * Black Box and White Box
     */
    @Test
    void getGodPower() {
        myConstruction = humanCard.getMyConstruction();

        assertTrue(myConstruction.getGodPower().equals(human));

    }

    /**
     * Check if getLastMove() and setLastMove( BuildMove ) can set and get the correct value
     * Methods used:        getMyMove()                     of  Card
     *
     * Black Box and White Box
     */
    @Test
    void getAndSetLastMove() {
        myConstruction = humanCard.getMyConstruction();
        BuildMove setBuild;

        //*** after initialization ***//
        assertTrue(myConstruction.getLastMove() == null);

        //*** with set methods ***//

        setBuild = new BuildMove(cell, nearCell1, PlaceableType.BLOCK);
        myConstruction.setLastMove(setBuild);
        assertTrue(myConstruction.getLastMove().equals(setBuild));

    }


    /**
     * Check if getConstructionLeft(), setConstructionLeft(), decreaseConstructionLeft(), resetConstructionLeft()
     * can set and return the correct value
     * Methods used:        getMyConstruction()         of  Card
     *
     * Black and White Box
     */
    @Test
    void getConstructionLeftAndDecreaseConstructionLeftAndResetConstructionLeft() {
        myConstruction = humanCard.getMyConstruction();

        // after initialization
        assertTrue(myConstruction.getConstructionLeft() == 1);

        // after setConstructionLeft
        myConstruction.setConstructionLeft(4);
        assertTrue( myConstruction.getConstructionLeft() == 4);

        // after decreaseConstructionLeft
        myConstruction.decreaseConstructionLeft();
        assertTrue(myConstruction.getConstructionLeft() == 3);

        // after resetConstructionLeft()
        myConstruction.resetConstructionLeft();
        assertTrue(myConstruction.getConstructionLeft() == 1);

    }


}