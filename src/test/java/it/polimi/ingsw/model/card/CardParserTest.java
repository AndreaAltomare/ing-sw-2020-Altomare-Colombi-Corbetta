package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.IslandBoard;
import it.polimi.ingsw.model.board.placeables.Block;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.card.adversaryMove.AdversaryMoveChecker;
import it.polimi.ingsw.model.card.build.BuildChecker;
import it.polimi.ingsw.model.card.build.BuildExecutor;
import it.polimi.ingsw.model.card.move.MoveChecker;
import it.polimi.ingsw.model.card.move.MoveExecutor;
import it.polimi.ingsw.model.card.win.WinChecker;
import it.polimi.ingsw.model.exceptions.LoseException;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import it.polimi.ingsw.model.exceptions.WinException;
import it.polimi.ingsw.model.move.BuildMove;
import it.polimi.ingsw.model.move.FloorDirection;
import it.polimi.ingsw.model.move.LevelDirection;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CardInfo class, aimed to verify it works properly
 *
 * @author Marco
 */
class CardParserTest {

    CardParser cardParser;
    Board board;
    boolean outOfBoard;
    Card mutantCard;
    GodPower mutantGod;
    Cell cell;
    Cell nearCell;
    Worker worker;
    Worker opponentWorker;

    /**
     * Initialization before method's test
     */
    @BeforeEach
    void setUp() {

        board = new IslandBoard();
        outOfBoard = false;
        mutantGod = new GodPower();
        mutantGod.setName("mutant");
        mutantGod.setEpithet("mutant's epithet");
        mutantGod.setDescription("mutant's description");
        worker = new Worker(new Player("player"));
        opponentWorker = new Worker(new Player("opponent"));

    }

    /**
     * Reset after test
     */
    @AfterEach
    void tearDown() {

        board = null;
        mutantCard = null;
        mutantGod = null;
        cell = null;
        nearCell = null;
        worker= null;
        opponentWorker = null;

    }

    /**
     * Using a mutant GodPower changes its parameter to use getMoveCheckers(GodPower) of CardParser to have different
     * MoveChecker's list. Then checks that the element of the list are correct trying its elements' checkMove method
     * in some cases and see its returned value
     * Methods used:    getCellAt( int, int)            of  IslandBoard
     *                  clear()                         of  Board
     *                  placeOn( Placeable )            of  Cell
     *                  place( Cell )                   of Worker
     *                  checkMove(...)                  of  MoveChecker
     *                  setExecutedMovement()           of  Card
     *                  getMyConstruction()             of  Card
     *                  decreaseConstructionLeft()      of  MyConstruction
     *
     * White Box
     */
    @Test
    void getMoveCheckers() {
        List<MoveChecker> moveCheckerList;
        Move move;
        int moveLeft;


        try {
            cell = board.getCellAt(2,2);
            nearCell = board.getCellAt(3,3);


            //###god without power***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);

            moveCheckerList = cardParser.getMoveCheckers(mutantGod);

            assertTrue(moveCheckerList != null);
            assertTrue(moveCheckerList.size() == 2);

            //>> check moveCheckerList's first element with same position internal control
            worker.place(cell);
            move = new Move(worker.position(), cell);
            moveLeft = 1;
            mutantCard = new Card(mutantGod, moveCheckerList, null, null, null, null, null);

            assertFalse(moveCheckerList.get(0).checkMove(move, worker, worker.position(), moveLeft, mutantCard));

            move = new Move(worker.position(), nearCell);
            assertTrue(moveCheckerList.get(0).checkMove(move, worker, worker.position(), moveLeft, mutantCard));
            // I can say that the fist element is the moveChecker which I want

            //>> check moveCheckerList's second element with same position internal control
            move = new Move(worker.position(), nearCell);
            moveLeft = 1;
            mutantCard = new Card(mutantGod, moveCheckerList, null, null, null, null, null);

            assertTrue(moveCheckerList.get(1).checkMove(move, worker, worker.position(), moveLeft, mutantCard));

            opponentWorker.place(nearCell);
            move = new Move(worker.position(), nearCell);
            assertFalse(moveCheckerList.get(1).checkMove(move, worker, worker.position(), moveLeft, mutantCard));
            // I can say that the second element is the moveChecker which I want


            // clear all
            board.clear();
            cell = board.getCellAt(2,2);
            nearCell = board.getCellAt(3,3);


            //***god with movement power and can go on a cell with opponent worker (Apollo)***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setActiveOnMyMovement(true);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setMoveIntoOpponentSpace(true);
            mutantGod.setForceOpponentInto(FloorDirection.ANY);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);

            moveCheckerList = cardParser.getMoveCheckers(mutantGod);

            assertTrue(moveCheckerList != null);
            assertTrue(moveCheckerList.size() == 2);

            //>> check moveCheckerList's first element with same position internal control
            worker.place(cell);
            move = new Move(worker.position(), cell);
            moveLeft = 1;
            mutantCard = new Card(mutantGod, moveCheckerList, null, null, null, null, null);

            assertFalse(moveCheckerList.get(0).checkMove(move, worker, worker.position(), moveLeft, mutantCard));

            move = new Move(worker.position(), nearCell);
            assertTrue(moveCheckerList.get(0).checkMove(move, worker, worker.position(), moveLeft, mutantCard));
            // I can say that the fist element is the moveChecker which I want

            //>> check moveCheckerList's second element with go on cell with opponent's worker internal control
            move = new Move(worker.position(), nearCell);
            moveLeft = 1;
            mutantCard = new Card(mutantGod, moveCheckerList, null, null, null, null, null);

            assertTrue(moveCheckerList.get(1).checkMove(move, worker, worker.position(), moveLeft, mutantCard));

            opponentWorker.place(nearCell);
            move = new Move(worker.position(), nearCell);
            assertTrue(moveCheckerList.get(1).checkMove(move, worker, worker.position(), moveLeft, mutantCard));
            // I can say that the second element is the moveChecker which I want



            // clear all
            board.clear();
            cell = board.getCellAt(2,2);
            nearCell = board.getCellAt(3,3);


            //***god with movement power and can go on a cell with opponent worker forcing it at the same direction (Minotaur)***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setActiveOnMyMovement(true);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setMoveIntoOpponentSpace(true);
            mutantGod.setForceOpponentInto(FloorDirection.SAME);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);

            moveCheckerList = cardParser.getMoveCheckers(mutantGod);

            assertTrue(moveCheckerList != null);
            assertTrue(moveCheckerList.size() == 3);

            //>> check moveCheckerList's first element with same position internal control
            worker.place(cell);
            move = new Move(worker.position(), cell);
            moveLeft = 1;
            mutantCard = new Card(mutantGod, moveCheckerList, null, null, null, null, null);

            assertFalse(moveCheckerList.get(0).checkMove(move, worker, worker.position(), moveLeft, mutantCard));

            move = new Move(worker.position(), nearCell);
            assertTrue(moveCheckerList.get(0).checkMove(move, worker, worker.position(), moveLeft, mutantCard));
            // I can say that the fist element is the moveChecker which I want

            //>> check moveCheckerList's second element with go on cell with opponent's worker internal control
            move = new Move(worker.position(), nearCell);
            moveLeft = 1;
            mutantCard = new Card(mutantGod, moveCheckerList, null, null, null, null, null);

            assertTrue(moveCheckerList.get(1).checkMove(move, worker, worker.position(), moveLeft, mutantCard));

            nearCell.placeOn(opponentWorker);
            move = new Move(worker.position(), nearCell);
            assertTrue(moveCheckerList.get(1).checkMove(move, worker, worker.position(), moveLeft, mutantCard));
            // I can say that the second element is the moveChecker which I want

            // clear all
            board.clear();
            cell = board.getCellAt(2,2);
            nearCell = board.getCellAt(3,3);

            //>> check moveCheckerList's third element with next cell at same direction control
            worker.place(cell);
            opponentWorker.place(nearCell);

            move = new Move(worker.position(), nearCell);
            moveLeft = 1;
            mutantCard = new Card(mutantGod, moveCheckerList, null, null, null, null, null);

            assertTrue(moveCheckerList.get(2).checkMove(move, worker, worker.position(), moveLeft, mutantCard));

            // clear all
            board.clear();
            cell = board.getCellAt(3,3);
            nearCell = board.getCellAt(4,4);

            worker.place(cell);
            opponentWorker.place(nearCell);
            move = new Move(worker.position(), nearCell);
            assertFalse(moveCheckerList.get(2).checkMove(move, worker, worker.position(), moveLeft, mutantCard));
            // I can say that the third element is the moveChecker which I want


            // clear all
            board.clear();
            cell = board.getCellAt(2,2);
            nearCell = board.getCellAt(3,3);

            //***god with movement power, two movement and starting space denied (Artemis)***//
            mutantGod.setMovementsLeft(2);
            mutantGod.setConstructionLeft(1);
            mutantGod.setStartingSpaceDenied(true);
            mutantGod.setActiveOnMyMovement(true);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setMoveIntoOpponentSpace(false);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);

            moveCheckerList = cardParser.getMoveCheckers(mutantGod);

            assertTrue(moveCheckerList != null);
            assertTrue(moveCheckerList.size() == 3);

            //>> check moveCheckerList's first element with same position internal control
            worker.place(cell);
            move = new Move(worker.position(), cell);
            moveLeft = 1;
            mutantCard = new Card(mutantGod, moveCheckerList, null, null, null, null, null);

            assertFalse(moveCheckerList.get(0).checkMove(move, worker, worker.position(), moveLeft, mutantCard));

            move = new Move(worker.position(), nearCell);
            assertTrue(moveCheckerList.get(0).checkMove(move, worker, worker.position(), moveLeft, mutantCard));
            // I can say that the fist element is the moveChecker which I want

            //>> check moveCheckerList's second element with go on cell with opponent's worker internal control
            move = new Move(worker.position(), nearCell);
            moveLeft = 1;
            mutantCard = new Card(mutantGod, moveCheckerList, null, null, null, null, null);

            assertTrue(moveCheckerList.get(1).checkMove(move, worker, worker.position(), moveLeft, mutantCard));

            opponentWorker.place(nearCell);
            move = new Move(worker.position(), nearCell);
            assertFalse(moveCheckerList.get(1).checkMove(move, worker, worker.position(), moveLeft, mutantCard));
            // I can say that the second element is the moveChecker which I want

            // clear all
            board.clear();
            cell = board.getCellAt(2,2);
            nearCell = board.getCellAt(3,3);

            //>> check moveCheckerList's third element with come back at the starting cell after "move" control
            worker.place(cell);

            move = new Move(worker.position(), nearCell);
            moveLeft = 1;
            mutantCard = new Card(mutantGod, moveCheckerList, null, null, null, null, null);

            assertTrue(moveCheckerList.get(2).checkMove(move, worker, worker.position(), moveLeft, mutantCard));

            mutantCard.setMovementExecuted(true);
            board.clear();
            worker.place(nearCell);
            move = new Move(worker.position(), cell);
            assertFalse(moveCheckerList.get(2).checkMove(move, worker, cell, moveLeft, mutantCard));
            // I can say that the third element is the moveChecker which I want


            // clear all
            board.clear();
            cell = board.getCellAt(2,2);
            nearCell = board.getCellAt(3,3);

            //***god with movement power, two construction and hotLastMoveDirection != NULL (Prometheus)***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(2);
            mutantGod.setStartingSpaceDenied(false);
            mutantGod.setActiveOnMyMovement(true);
            mutantGod.setHotLastMoveDirection(LevelDirection.UP);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.UP);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);

            moveCheckerList = cardParser.getMoveCheckers(mutantGod);

            assertTrue(moveCheckerList != null);
            assertTrue(moveCheckerList.size() == 3);

            //>> check moveCheckerList's first element with same position internal control
            worker.place(cell);
            move = new Move(worker.position(), cell);
            moveLeft = 1;
            mutantCard = new Card(mutantGod, moveCheckerList, null, null, null, null, null);

            assertFalse(moveCheckerList.get(0).checkMove(move, worker, worker.position(), moveLeft, mutantCard));

            move = new Move(worker.position(), nearCell);
            assertTrue(moveCheckerList.get(0).checkMove(move, worker, worker.position(), moveLeft, mutantCard));
            // I can say that the fist element is the moveChecker which I want

            //>> check moveCheckerList's second element with go on cell with opponent's worker internal control
            move = new Move(worker.position(), nearCell);
            moveLeft = 1;
            mutantCard = new Card(mutantGod, moveCheckerList, null, null, null, null, null);

            assertTrue(moveCheckerList.get(1).checkMove(move, worker, worker.position(), moveLeft, mutantCard));

            opponentWorker.place(nearCell);
            move = new Move(worker.position(), nearCell);
            assertFalse(moveCheckerList.get(1).checkMove(move, worker, worker.position(), moveLeft, mutantCard));
            // I can say that the second element is the moveChecker which I want

            // clear all
            board.clear();
            cell = board.getCellAt(2,2);
            nearCell = board.getCellAt(3,3);

            //>> check moveCheckerList's third element with go up after using construction power
            worker.place(cell);
            nearCell.placeOn(new Block());

            move = new Move(worker.position(), nearCell);
            moveLeft = 1;
            mutantCard = new Card(mutantGod, moveCheckerList, null, null, null, null, null);

            assertTrue(moveCheckerList.get(2).checkMove(move, worker, worker.position(), moveLeft, mutantCard));

            mutantCard.getMyConstruction().decreaseConstructionLeft();
            move = new Move(worker.position(), nearCell);
            assertFalse(moveCheckerList.get(2).checkMove(move, worker, worker.position(), moveLeft, mutantCard));
            // I can say that the third element is the moveChecker which I want

        } catch (OutOfBoardException e) {
            e.printStackTrace();
            outOfBoard = true;
        }
        assertTrue( !outOfBoard );

    }

    /**
     * check if getMoveExecutor() can return the correct MoveExecutor and if it can move correctly
     * Methods used:    getCellAt( int, int )           of  IslandBoard
     *                  clear()                         of  Board
     *                  getHeight()                     of  Cell
     *                  getPlaceableAt( int )           of  Cell
     *                  place( Cell )                   of  Worker
     *
     * Black Box and White Box
     */
    @Test
    void getMoveExecutor() {
        MoveExecutor moveExecutor;
        Move move;
        Cell farawayCell;
        boolean check;
        boolean victory = false;
        boolean outOfBoardMoved = false;

        try {
            cell = board.getCellAt(2,2);
            nearCell = board.getCellAt(3,3);
            farawayCell = board.getCellAt(4,4);

            //***god without any power***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);

            moveExecutor = CardParser.getMoveExecutor(mutantGod);

            //>> move on a near free cell
            worker.place(cell);
            move = new Move(worker.position(), nearCell);
            mutantCard = new Card(mutantGod, null, moveExecutor, null, null, null, null);

            try {
                check = moveExecutor.executeMove(move, worker, mutantCard);

                assertTrue( check );
                assertTrue( cell.getHeigth() == 0 );
                assertTrue( cell.getPlaceableAt(0) == null );
                assertTrue( nearCell.getHeigth() == 1 );
                assertTrue( nearCell.getPlaceableAt(0).equals(worker) );
                assertTrue( nearCell.getPlaceableAt(1) == null);
            } catch (OutOfBoardException o) {
                o.printStackTrace();
                outOfBoardMoved = true;
            } catch (WinException w) {
                w.printStackTrace();
                victory = true;
            } finally {
                assertTrue( !outOfBoardMoved );
                assertTrue( !victory );
                outOfBoardMoved = false;
                victory = false;
            }

            //clear board
            board.clear();

            //>> move on a cell with an opponent's worker but it can't
            worker.place(cell);
            opponentWorker.place(nearCell);
            move = new Move(worker.position(), nearCell);
            mutantCard = new Card(mutantGod, null, moveExecutor, null, null, null, null);

            try {
                check = moveExecutor.executeMove(move, worker, mutantCard);

                assertTrue( !check );
                assertTrue( cell.getHeigth() == 1 );
                assertTrue( cell.getPlaceableAt(0).equals(worker) );
                assertTrue( cell.getPlaceableAt(1) == null );
                assertTrue( nearCell.getHeigth() == 0 );
                assertTrue( nearCell.getPlaceableAt(0) == null );
            } catch (OutOfBoardException o) {
                o.printStackTrace();
                outOfBoardMoved = true;
            } catch (WinException w) {
                w.printStackTrace();
                victory = true;
            } finally {
                assertTrue( !outOfBoardMoved );
                assertTrue( !victory );
                outOfBoardMoved = false;
                victory = false;
            }

            // clear board
            board.clear();

            //***god who can go on cell with opponent's worker (Apollo)***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setActiveOnMyMovement(true);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setMoveIntoOpponentSpace(true);
            mutantGod.setForceOpponentInto(FloorDirection.ANY);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);

            moveExecutor = CardParser.getMoveExecutor(mutantGod);

            //>> move on a near free cell
            worker.place(cell);
            move = new Move(worker.position(), nearCell);
            mutantCard = new Card(mutantGod, null, moveExecutor, null, null, null, null);

            try {
                check = moveExecutor.executeMove(move, worker, mutantCard);

                assertTrue( check );
                assertTrue( cell.getHeigth() == 0 );
                assertTrue( cell.getPlaceableAt(0) == null );
                assertTrue( nearCell.getHeigth() == 1 );
                assertTrue( nearCell.getPlaceableAt(0).equals(worker) );
                assertTrue( nearCell.getPlaceableAt(1) == null);
            } catch (OutOfBoardException o) {
                o.printStackTrace();
                outOfBoardMoved = true;
            } catch (WinException w) {
                w.printStackTrace();
                victory = true;
            } finally {
                assertTrue( !outOfBoardMoved );
                assertTrue( !victory );
                outOfBoardMoved = false;
                victory = false;
            }

            // clear board
            board.clear();


            moveExecutor = CardParser.getMoveExecutor(mutantGod);

            //>> move on a cell with an opponnet's worker and put it on starting cell
            worker.place(cell);
            opponentWorker.place(nearCell);
            move = new Move(worker.position(), nearCell);
            mutantCard = new Card(mutantGod, null, moveExecutor, null, null, null, null);

            try {
                check = moveExecutor.executeMove(move, worker, mutantCard);

                assertTrue( check );
                assertTrue( cell.getHeigth() == 1 );
                assertTrue( cell.getPlaceableAt(0).equals(opponentWorker) );
                assertTrue( cell.getPlaceableAt(1) == null );
                assertTrue( nearCell.getHeigth() == 1 );
                assertTrue( nearCell.getPlaceableAt(0).equals(worker) );
                assertTrue( nearCell.getPlaceableAt(1) == null);
            } catch (OutOfBoardException o) {
                o.printStackTrace();
                outOfBoardMoved = true;
            } catch (WinException w) {
                w.printStackTrace();
                victory = true;
            } finally {
                assertTrue( !outOfBoardMoved );
                assertTrue( !victory );
                outOfBoardMoved = false;
                victory = false;
            }


            //clear board
            board.clear();


            //***god who can go on cell with opponent's worker and push it at same direction of movement (Minotaur)***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setActiveOnMyMovement(true);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setMoveIntoOpponentSpace(true);
            mutantGod.setForceOpponentInto(FloorDirection.SAME);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);

            moveExecutor = CardParser.getMoveExecutor(mutantGod);

            //>> move in a near free cell
            worker.place(cell);
            move = new Move(worker.position(), nearCell);
            mutantCard = new Card(mutantGod, null, moveExecutor, null, null, null, null);

            try {
                check = moveExecutor.executeMove(move, worker, mutantCard);

                assertTrue( check );
                assertTrue( cell.getHeigth() == 0 );
                assertTrue( cell.getPlaceableAt(0) == null );
                assertTrue( nearCell.getHeigth() == 1 );
                assertTrue( nearCell.getPlaceableAt(0).equals(worker) );
                assertTrue( nearCell.getPlaceableAt(1) == null);
            } catch (OutOfBoardException o) {
                o.printStackTrace();
                outOfBoardMoved = true;
            } catch (WinException w) {
                w.printStackTrace();
                victory = true;
            } finally {
                assertTrue( !outOfBoardMoved );
                assertTrue( !victory );
                outOfBoardMoved = false;
                victory = false;
            }

            // clear board
            board.clear();


            moveExecutor = CardParser.getMoveExecutor(mutantGod);

            //>> move on a cell with an opponent's worker and put it on the cell at the same direction
            worker.place(cell);
            opponentWorker.place(nearCell);
            move = new Move(worker.position(), nearCell);
            mutantCard = new Card(mutantGod, null, moveExecutor, null, null, null, null);

            try {
                check = moveExecutor.executeMove(move, worker, mutantCard);

                assertTrue( check );
                assertTrue( cell.getHeigth() == 0 );
                assertTrue( cell.getPlaceableAt(0) == null );
                assertTrue( nearCell.getHeigth() == 1 );
                assertTrue( nearCell.getPlaceableAt(0).equals(worker) );
                assertTrue( nearCell.getPlaceableAt(1) == null);
                assertTrue( farawayCell.getHeigth() == 1);
                assertTrue( farawayCell.getPlaceableAt(0).equals(opponentWorker) );
                assertTrue( farawayCell.getPlaceableAt(1) == null);
            } catch (OutOfBoardException o) {
                o.printStackTrace();
                outOfBoardMoved = true;
            } catch (WinException w) {
                w.printStackTrace();
                victory = true;
            } finally {
                assertTrue( !outOfBoardMoved );
                assertTrue( !victory );
                outOfBoardMoved = false;
                victory = false;
            }

            // clear board
            board.clear();

            //***god who have an effect in movement phase but nothing special movement***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setActiveOnMyMovement(true);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setMoveIntoOpponentSpace(false);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);

            moveExecutor = CardParser.getMoveExecutor(mutantGod);

            //>> move on a near free cell
            worker.place(cell);
            move = new Move(worker.position(), nearCell);
            mutantCard = new Card(mutantGod, null, moveExecutor, null, null, null, null);

            try {
                check = moveExecutor.executeMove(move, worker, mutantCard);

                assertTrue( check );
                assertTrue( cell.getHeigth() == 0 );
                assertTrue( cell.getPlaceableAt(0) == null );
                assertTrue( nearCell.getHeigth() == 1 );
                assertTrue( nearCell.getPlaceableAt(0).equals(worker) );
                assertTrue( nearCell.getPlaceableAt(1) == null);
            } catch (OutOfBoardException o) {
                o.printStackTrace();
                outOfBoardMoved = true;
            } catch (WinException w) {
                w.printStackTrace();
                victory = true;
            } finally {
                assertTrue( !outOfBoardMoved );
                assertTrue( !victory );
                outOfBoardMoved = false;
                victory = false;
            }

            // clear board
            board.clear();


            moveExecutor = CardParser.getMoveExecutor(mutantGod);

            //>> move on a cell with an opponent's worker but it can't
            worker.place(cell);
            opponentWorker.place(nearCell);
            move = new Move(worker.position(), nearCell);
            mutantCard = new Card(mutantGod, null, moveExecutor, null, null, null, null);

            try {
                check = moveExecutor.executeMove(move, worker, mutantCard);

                assertTrue( !check );
                assertTrue( cell.getHeigth() == 1 );
                assertTrue( cell.getPlaceableAt(0).equals(worker) );
                assertTrue( cell.getPlaceableAt(1) == null );
                assertTrue( nearCell.getHeigth() == 0 );
                assertTrue( nearCell.getPlaceableAt(0) == null );
            } catch (OutOfBoardException o) {
                o.printStackTrace();
                outOfBoardMoved = true;
            } catch (WinException w) {
                w.printStackTrace();
                victory = true;
            } finally {
                assertTrue( !outOfBoardMoved );
                assertTrue( !victory );
                outOfBoardMoved = false;
                victory = false;
            }


        } catch (OutOfBoardException e) {
            e.printStackTrace();
            outOfBoard = true;
        }
        assertTrue( !outOfBoard );


    }

    /**
     * Using a mutant GodPower changes its parameter to use getBuildCheckers(GodPower) of CardParser to have different
     * BuildChecker's list. Then checks that the element of the list are correct trying its elements' checkBuild method
     * in some cases and see its returned value
     * Methods used:    getCellAt( int, int )                   of  IslandBoard
     *                  clear()                                 of  Board
     *                  placeOn( Placeable )                    of  Cell
     *                  place( Cell )                           of  Worker
     *                  checkBuild(...)                         of  BuildChecker
     *                  setConstructionExecuted( boolean )      of  Card
     *
     * White Box
     */
    @Test
    void getBuildCheckers() {
        List<BuildChecker> buildCheckerList;
        BuildMove buildMove;
        BuildMove lastBuildMove;
        int constructionLeft;
        Cell otherNearCell;

        try {
            cell = board.getCellAt(2, 2);
            nearCell = board.getCellAt(3, 3);
            otherNearCell = board.getCellAt(2, 3);

            //***god without power***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);

            buildCheckerList = CardParser.getBuildCheckers(mutantGod);

            assertTrue(buildCheckerList != null);
            assertTrue(buildCheckerList.size() == 2);

            //>> check buildCheckerList's fist element with build on worker's cell control
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), nearCell, PlaceableType.BLOCK);
            lastBuildMove = null;
            constructionLeft = 1;
            mutantCard = new Card(mutantGod, null, null, buildCheckerList, null, null, null);

            assertTrue(buildCheckerList.get(0).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            buildMove = new BuildMove(worker.position(), worker.position(), PlaceableType.BLOCK);

            assertFalse(buildCheckerList.get(0).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            // clear board
            board.clear();

            //>> check buildCheckerList's second element with build a dome on free cell control
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), nearCell, PlaceableType.DOME);
            lastBuildMove = null;
            constructionLeft = 1;
            mutantCard = new Card(mutantGod, null, null, buildCheckerList, null, null, null);

            assertFalse(buildCheckerList.get(1).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            nearCell.placeOn(new Block());
            nearCell.placeOn(new Block());
            nearCell.placeOn(new Block());

            assertTrue(buildCheckerList.get(1).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            //clear board
            board.clear();


            //***god who can build dome at any level (Atlas)***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);
            mutantGod.setActiveOnMyConstruction(true);
            mutantGod.setDomeAtAnyLevel(true);

            buildCheckerList = CardParser.getBuildCheckers(mutantGod);

            assertTrue(buildCheckerList != null);
            assertTrue(buildCheckerList.size() == 1);

            //>> check buildCheckerList's fist element with build on worker's cell control
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), nearCell, PlaceableType.BLOCK);
            lastBuildMove = null;
            constructionLeft = 1;
            mutantCard = new Card(mutantGod, null, null, buildCheckerList, null, null, null);

            assertTrue(buildCheckerList.get(0).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            buildMove = new BuildMove(worker.position(), worker.position(), PlaceableType.BLOCK);

            assertFalse(buildCheckerList.get(0).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            // clear board
            board.clear();


            //***god who have special effect on his construction but he can't build dome at any level***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);
            mutantGod.setActiveOnMyConstruction(true);
            mutantGod.setDomeAtAnyLevel(false);

            buildCheckerList = CardParser.getBuildCheckers(mutantGod);

            assertTrue(buildCheckerList != null);
            assertTrue(buildCheckerList.size() == 2);

            //>> check buildCheckerList's fist element with build on worker's cell control
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), nearCell, PlaceableType.BLOCK);
            lastBuildMove = null;
            constructionLeft = 1;
            mutantCard = new Card(mutantGod, null, null, buildCheckerList, null, null, null);

            assertTrue(buildCheckerList.get(0).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            buildMove = new BuildMove(worker.position(), worker.position(), PlaceableType.BLOCK);

            assertFalse(buildCheckerList.get(0).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            // clear board
            board.clear();

            //>> check buildCheckerList's second element with build a dome on free cell control
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), nearCell, PlaceableType.DOME);
            lastBuildMove = null;
            constructionLeft = 1;
            mutantCard = new Card(mutantGod, null, null, buildCheckerList, null, null, null);

            assertFalse(buildCheckerList.get(1).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            nearCell.placeOn(new Block());
            nearCell.placeOn(new Block());
            nearCell.placeOn(new Block());

            assertTrue(buildCheckerList.get(1).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            //clear board
            board.clear();


            //***god who can build two time but not on the same cell (Demeter)***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(2);
            mutantGod.setSameSpaceDenied(true);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);
            mutantGod.setActiveOnMyConstruction(true);
            mutantGod.setDomeAtAnyLevel(false);

            buildCheckerList = CardParser.getBuildCheckers(mutantGod);

            assertTrue(buildCheckerList != null);
            assertTrue(buildCheckerList.size() == 3);

            //>> check buildCheckerList's fist element with build on worker's cell control
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), nearCell, PlaceableType.BLOCK);
            lastBuildMove = null;
            constructionLeft = 1;
            mutantCard = new Card(mutantGod, null, null, buildCheckerList, null, null, null);

            assertTrue(buildCheckerList.get(0).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            buildMove = new BuildMove(worker.position(), worker.position(), PlaceableType.BLOCK);

            assertFalse(buildCheckerList.get(0).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            // clear board
            board.clear();

            //>> check buildCheckerList's second element with build a dome on free cell control
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), nearCell, PlaceableType.DOME);
            lastBuildMove = null;
            constructionLeft = 1;
            mutantCard = new Card(mutantGod, null, null, buildCheckerList, null, null, null);

            assertFalse(buildCheckerList.get(1).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            nearCell.placeOn(new Block());
            nearCell.placeOn(new Block());
            nearCell.placeOn(new Block());

            assertTrue(buildCheckerList.get(1).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            //clear board
            board.clear();

            //>> check buildCheckerList's third element with control on second construction
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), otherNearCell, PlaceableType.BLOCK);
            lastBuildMove = new BuildMove(worker.position(), nearCell, PlaceableType.BLOCK);
            constructionLeft = 1;
            mutantCard = new Card(mutantGod, null, null, buildCheckerList, null, null,null);
            mutantCard.setConstructionExecuted(true);

            assertTrue(buildCheckerList.get(2).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            buildMove = new BuildMove(worker.position(), nearCell, PlaceableType.BLOCK);
            assertFalse(buildCheckerList.get(2).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            //clear board
            board.clear();



            //***god who can build two time only on the same cell (Hephaestus)***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(2);
            mutantGod.setSameSpaceDenied(false);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);
            mutantGod.setActiveOnMyConstruction(true);
            mutantGod.setDomeAtAnyLevel(false);
            mutantGod.setForceConstructionOnSameSpace(true);

            buildCheckerList = CardParser.getBuildCheckers(mutantGod);

            assertTrue(buildCheckerList != null);
            assertTrue(buildCheckerList.size() == 3);

            //>> check buildCheckerList's fist element with build on worker's cell control
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), nearCell, PlaceableType.BLOCK);
            lastBuildMove = null;
            constructionLeft = 1;
            mutantCard = new Card(mutantGod, null, null, buildCheckerList, null, null, null);

            assertTrue(buildCheckerList.get(0).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            buildMove = new BuildMove(worker.position(), worker.position(), PlaceableType.BLOCK);

            assertFalse(buildCheckerList.get(0).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            // clear board
            board.clear();

            //>> check buildCheckerList's second element with build a dome on free cell control
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), nearCell, PlaceableType.DOME);
            lastBuildMove = null;
            constructionLeft = 1;
            mutantCard = new Card(mutantGod, null, null, buildCheckerList, null, null, null);

            assertFalse(buildCheckerList.get(1).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            nearCell.placeOn(new Block());
            nearCell.placeOn(new Block());
            nearCell.placeOn(new Block());

            assertTrue(buildCheckerList.get(1).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            //clear board
            board.clear();

            //>> check buildCheckerList's third element with control on second construction
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), otherNearCell, PlaceableType.BLOCK);
            lastBuildMove = new BuildMove(worker.position(), nearCell, PlaceableType.BLOCK);
            constructionLeft = 1;
            mutantCard = new Card(mutantGod, null, null, buildCheckerList, null, null,null);
            mutantCard.setConstructionExecuted(true);

            assertFalse(buildCheckerList.get(2).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            buildMove = new BuildMove(worker.position(), nearCell, PlaceableType.BLOCK);
            assertTrue(buildCheckerList.get(2).checkBuild(buildMove, worker, lastBuildMove, constructionLeft, mutantCard));

            //clear board
            board.clear();


        } catch (OutOfBoardException e) {
            e.printStackTrace();
            outOfBoard = true;
        }
        assertTrue(!outOfBoard);

    }

    /**
     * Calls different buildExecutors changing godPower's parameters and checks if the buildExecutor can build only block and dome
     * Methods used:    getCellAt( int, int )               of  IslandBoard
     *                  clear()                             of  Board
     *                  getHeight()                         of  Cell
     *                  getPLaceableAt( int )               of  Cell
     *                  place( Cell )                       of  Worker
     *                  isBlock()                           of  Placeable
     *                  isDome()                            of  Placeable
     *                  executeBuild( BuildMove, Worker )   of  BuildExecutor
     *
     * Black Box and White Box
     */
    @Test
    void getBuildExecutor() {
        BuildExecutor buildExecutor;
        BuildMove buildMove;
        boolean outOfBoardBuild = false;
        boolean checkBuild;

        try {
            cell = board.getCellAt(2,2);
            nearCell = board.getCellAt(3,3);


            //###god without power***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);

            buildExecutor = CardParser.getBuildExecutor(mutantGod);

            //>> build a block
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), nearCell,PlaceableType.BLOCK);
            try {
                checkBuild = buildExecutor.executeBuild(buildMove, worker);

                assertTrue( checkBuild );
                assertTrue( cell.getHeigth() == 1 );
                assertTrue( cell.getPlaceableAt(0).equals(worker) );
                assertTrue( cell.getPlaceableAt(1) == null );
                assertTrue( nearCell.getHeigth() == 1 );
                assertTrue( nearCell.getPlaceableAt(0).isBlock() );
                assertTrue( nearCell.getPlaceableAt(1) == null );

            } catch (OutOfBoardException o) {
                o.printStackTrace();
                outOfBoardBuild = true;
            } finally {
                assertTrue( !outOfBoardBuild );
                outOfBoardBuild = false;
            }

            // clear board
            board.clear();

            //>> build a dome
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), nearCell,PlaceableType.DOME);
            try {
                checkBuild = buildExecutor.executeBuild(buildMove, worker);

                assertTrue( checkBuild );
                assertTrue( cell.getHeigth() == 1 );
                assertTrue( cell.getPlaceableAt(0).equals(worker) );
                assertTrue( cell.getPlaceableAt(1) == null );
                assertTrue( nearCell.getHeigth() == 1 );
                assertTrue( nearCell.getPlaceableAt(0).isDome() );
                assertTrue( nearCell.getPlaceableAt(1) == null );

            } catch (OutOfBoardException o) {
                o.printStackTrace();
                outOfBoardBuild = true;
            } finally {
                assertTrue( !outOfBoardBuild );
                outOfBoardBuild = false;
            }

            // clear board
            board.clear();

            //>> try to build something which isn't a dome or a block
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), nearCell, PlaceableType.WORKER);
            try {
                checkBuild = buildExecutor.executeBuild(buildMove, worker);

                assertTrue( !checkBuild );
                assertTrue( cell.getHeigth() == 1 );
                assertTrue( cell.getPlaceableAt(0).equals(worker) );
                assertTrue( cell.getPlaceableAt(1) == null );
                assertTrue( nearCell.getHeigth() == 0 );
                assertTrue( nearCell.getPlaceableAt(0) == null );

            } catch (OutOfBoardException o) {
                o.printStackTrace();
                outOfBoardBuild = true;
            } finally {
                assertTrue( !outOfBoardBuild );
                outOfBoardBuild = false;
            }

            // clear board
            board.clear();


            //###god with some construction effects***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);
            mutantGod.setActiveOnMyConstruction(true);
            mutantGod.setDomeAtAnyLevel(true);
            mutantGod.setForceConstructionOnSameSpace(true);

            buildExecutor = CardParser.getBuildExecutor(mutantGod);

            //>> build a block
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), nearCell,PlaceableType.BLOCK);
            try {
                checkBuild = buildExecutor.executeBuild(buildMove, worker);

                assertTrue( checkBuild );
                assertTrue( cell.getHeigth() == 1 );
                assertTrue( cell.getPlaceableAt(0).equals(worker) );
                assertTrue( cell.getPlaceableAt(1) == null );
                assertTrue( nearCell.getHeigth() == 1 );
                assertTrue( nearCell.getPlaceableAt(0).isBlock() );
                assertTrue( nearCell.getPlaceableAt(1) == null );

            } catch (OutOfBoardException o) {
                o.printStackTrace();
                outOfBoardBuild = true;
            } finally {
                assertTrue( !outOfBoardBuild );
                outOfBoardBuild = false;
            }

            // clear board
            board.clear();

            //>> build a dome
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), nearCell,PlaceableType.DOME);
            try {
                checkBuild = buildExecutor.executeBuild(buildMove, worker);

                assertTrue( checkBuild );
                assertTrue( cell.getHeigth() == 1 );
                assertTrue( cell.getPlaceableAt(0).equals(worker) );
                assertTrue( cell.getPlaceableAt(1) == null );
                assertTrue( nearCell.getHeigth() == 1 );
                assertTrue( nearCell.getPlaceableAt(0).isDome() );
                assertTrue( nearCell.getPlaceableAt(1) == null );

            } catch (OutOfBoardException o) {
                o.printStackTrace();
                outOfBoardBuild = true;
            } finally {
                assertTrue( !outOfBoardBuild );
                outOfBoardBuild = false;
            }

            // clear board
            board.clear();

            //>> try to build something which isn't a dome or a block
            worker.place(cell);
            buildMove = new BuildMove(worker.position(), nearCell, PlaceableType.WORKER);
            try {
                checkBuild = buildExecutor.executeBuild(buildMove, worker);

                assertTrue( !checkBuild );
                assertTrue( cell.getHeigth() == 1 );
                assertTrue( cell.getPlaceableAt(0).equals(worker) );
                assertTrue( cell.getPlaceableAt(1) == null );
                assertTrue( nearCell.getHeigth() == 0 );
                assertTrue( nearCell.getPlaceableAt(0) == null );

            } catch (OutOfBoardException o) {
                o.printStackTrace();
                outOfBoardBuild = true;
            } finally {
                assertTrue( !outOfBoardBuild );
                outOfBoardBuild = false;
            }

            // clear board
            board.clear();




        } catch (OutOfBoardException e) {
            e.printStackTrace();
            outOfBoard = true;
        }
        assertTrue( !outOfBoard );
    }

    /**
     * Using a mutant GodPower changes its parameter to use getWinCheckers(GodPower) of CardParser to have different
     * BuildChecker's list. Then checks that the element of the list are correct trying its elements' checkWin method
     * in some cases and see its returned value
     * Methods used:    getCellAt( int, int )                   of  IslandBoard
     *                  clear()                                 of  Board
     *                  placeOn( Placeable )                    of  Cell
     *                  place( Cell )                           of  Worker
     *                  checkWin( Move, Worker )                of  BuildChecker
     *
     * White Box
     */
    @Test
    void getWinCheckers() {
        List<WinChecker> winCheckerList;
        Move move;

        try {
            cell = board.getCellAt(2, 2);
            nearCell = board.getCellAt(3, 3);

            //***god without power***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);

            winCheckerList = CardParser.getWinCheckers(mutantGod);

            assertTrue( winCheckerList != null );
            assertTrue( winCheckerList.size() == 1 );

            //>> check winCheckerList's first element with control from second to third block
            move = new Move(cell, nearCell);
            worker.place(nearCell);

            assertFalse(winCheckerList.get(0).checkWin(move, worker));

            //clear board
            board.clear();

            cell.placeOn( new Block() );
            cell.placeOn( new Block() );
            nearCell.placeOn( new Block() );
            nearCell.placeOn( new Block() );
            nearCell.placeOn( new Block() );
            move = new Move(cell, nearCell);
            worker.place(nearCell);

            assertTrue(winCheckerList.get(0).checkWin(move, worker));

            //clear board
            board.clear();



            //***god who can win also go down of two level (Pan)***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setMustObey(true);
            mutantGod.setHotLastMoveDirection(LevelDirection.DOWN);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);
            mutantGod.setNewVictoryCondition(true);

            winCheckerList = CardParser.getWinCheckers(mutantGod);

            assertTrue( winCheckerList != null );
            assertTrue( winCheckerList.size() == 2 );

            //>> check winCheckerList's first element with control from second to third block
            move = new Move(cell, nearCell);
            worker.place(nearCell);

            assertFalse(winCheckerList.get(0).checkWin(move, worker));

            //clear board
            board.clear();

            cell.placeOn( new Block() );
            cell.placeOn( new Block() );
            nearCell.placeOn( new Block() );
            nearCell.placeOn( new Block() );
            nearCell.placeOn( new Block() );
            move = new Move(cell, nearCell);
            worker.place(nearCell);

            assertTrue(winCheckerList.get(0).checkWin(move, worker));

            //clear board
            board.clear();

            //>> check winCheckerList's second element with go down of two level control
            cell.placeOn( new Block() );
            move = new Move(cell, nearCell);
            worker.place(nearCell);

            assertFalse(winCheckerList.get(1).checkWin(move, worker));

            //clear board
            board.clear();

            cell.placeOn( new Block() );
            cell.placeOn( new Block() );
            move = new Move(cell, nearCell);
            worker.place(nearCell);

            assertTrue(winCheckerList.get(1).checkWin(move, worker));

            //clear board
            board.clear();



        } catch ( OutOfBoardException e ) {
            e.printStackTrace();
            outOfBoard = true;
        }
        assertTrue( !outOfBoard );
    }

    /**
     * Using a mutant GodPower changes its parameter to use getAdversaryMoveCheckers(GodPower) of CardParser
     * to have different AdversaryMoveChecker's list. Then checks that the element of the list are correct
     * trying its elements' checkWin method in some cases and see its returned value
     * Methods used:    getCellAt( int, int )                   of  IslandBoard
     *                  clear()                                 of  Board
     *                  placeOn( Placeable )                    of  Cell
     *                  place( Cell )                           of  Worker
     *                  checkMove( ... )                        of  AdversaryMoveChecker
     *                  getMyMove()                             of  Card
     *                  executeMove( Move, Worker )             of  MyMove
     *
     * White Box
     */
    @Test
    void getAdversaryMoveCheckers() {
        List<AdversaryMoveChecker> adversaryMoveCheckerList;
        Move move;
        Move opponentMove;
        boolean loose = false;
        Cell opponetCell;
        Cell opponentNearCell;

        try {
            opponetCell = board.getCellAt(0,0);
            opponentNearCell = board.getCellAt(1,1);
            cell = board.getCellAt(2, 2);
            nearCell = board.getCellAt(3, 3);

            //***god without power***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);

            adversaryMoveCheckerList = CardParser.getAdversaryMoveCheckers(mutantGod);

            assertTrue( adversaryMoveCheckerList != null );
            assertTrue( adversaryMoveCheckerList.size() == 1);

            //>> check adversaryMoveCheckerList's first element with a control of opponent's worker movement after worker movement
            mutantCard = new Card(mutantGod, CardParser.getMoveCheckers(mutantGod),
                                                CardParser.getMoveExecutor(mutantGod),
                                                null, null,
                                                CardParser.getWinCheckers(mutantGod),
                                                adversaryMoveCheckerList);

            //-worker move to register lastMove of Worker
            worker.place(cell);
            nearCell.placeOn( new Block() );
            move = new Move( worker.position(), nearCell );
            mutantCard.getMyMove().setLastMove(move);


            //-opponent's moves
            opponentWorker.place(opponetCell);
            opponentMove = new Move(opponentWorker.position(), opponentNearCell);

            try {
                assertTrue(adversaryMoveCheckerList.get(0).checkMove(opponentMove, opponentWorker, mutantCard));
            } catch (LoseException l) {
                l.printStackTrace();
                loose = true;
            } finally {
                assertTrue( !loose );
                loose = false;
            }

            opponentNearCell.placeOn( new Block() );
            opponentMove = new Move(opponentWorker.position(), opponentNearCell);
            try {
                assertTrue(adversaryMoveCheckerList.get(0).checkMove(opponentMove, opponentWorker, mutantCard));
            } catch (LoseException l) {
                l.printStackTrace();
                loose = true;
            } finally {
                assertTrue( !loose );
                loose = false;
            }

            //clear board
            board.clear();


            //***god who blocks opponent's up movement if his worker moves up (Athena) ***//
            mutantGod.setMovementsLeft(1);
            mutantGod.setConstructionLeft(1);
            mutantGod.setMustObey(true);
            mutantGod.setHotLastMoveDirection(LevelDirection.UP);
            mutantGod.setForceOpponentInto(FloorDirection.NONE);
            mutantGod.setDeniedDirection(LevelDirection.NONE);
            mutantGod.setActiveOnOpponentMovement(true);
            mutantGod.setOpponentDeniedDirection(LevelDirection.UP);

            adversaryMoveCheckerList = CardParser.getAdversaryMoveCheckers(mutantGod);

            assertTrue( adversaryMoveCheckerList != null );
            assertTrue( adversaryMoveCheckerList.size() == 1);

            //>> check adversaryMoveCheckerList's first element with a control of opponent's worker movement after worker movement
            mutantCard = new Card(mutantGod, CardParser.getMoveCheckers(mutantGod),
                                                CardParser.getMoveExecutor(mutantGod),
                                                null, null,
                                                CardParser.getWinCheckers(mutantGod),
                                                adversaryMoveCheckerList);

            //-worker move to register lastMove of Worker
            worker.place(cell);
            nearCell.placeOn( new Block() );
            move = new Move( worker.position(), nearCell );
            mutantCard.getMyMove().setLastMove(move);


            //-opponent's moves
            opponentWorker.place(opponetCell);
            opponentMove = new Move(opponentWorker.position(), opponentNearCell);

            try {
                assertTrue(adversaryMoveCheckerList.get(0).checkMove(opponentMove, opponentWorker, mutantCard));
            } catch (LoseException l) {
                l.printStackTrace();
                loose = true;
            } finally {
                assertTrue( !loose );
                loose = false;
            }

            opponentNearCell.placeOn( new Block() );
            opponentMove = new Move(opponentWorker.position(), opponentNearCell);
            try {
                assertFalse(adversaryMoveCheckerList.get(0).checkMove(opponentMove, opponentWorker, mutantCard));
            } catch (LoseException l) {
                l.printStackTrace();
                loose = true;
            } finally {
                assertTrue( loose );
                loose = false;
            }

            //clear board
            board.clear();


        } catch ( OutOfBoardException e ) {
            e.printStackTrace();
            outOfBoard = true;
        }
        assertTrue( !outOfBoard );


    }
}