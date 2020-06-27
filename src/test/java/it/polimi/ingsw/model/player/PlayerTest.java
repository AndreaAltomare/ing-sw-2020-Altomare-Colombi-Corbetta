package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.IslandBoard;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.move.BuildMove;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.worker.ChooseType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.turn.StateType;
import it.polimi.ingsw.model.player.worker.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit test for Player class, aimed to verify it works properly
 *
 * @author Marco
 */
class PlayerTest {

    Player player;
    Board board;
    Cell cornerCell;
    Cell nearCornerCell1;
    Cell nearCornerCell2;
    Cell nearCornerCell3;
    Cell edgeCell;
    Cell nearEdgeCell1;
    Cell nearEdgeCell2;
    Cell nearEdgeCell3;
    Worker worker1;
    Worker worker2;

    /**
     * Initialization before method's test
     * Methods used:        getCellAt(int, int)         of  IslandBoard
     */
    @Test
    @BeforeEach
    void setUp() {
        boolean checkError = false;

        player = new Player( "Player1" );
        board = new IslandBoard();
        worker1 = new Worker(player);
        worker2 = new Worker(player);
        try {
            cornerCell = board.getCellAt(0,0);
            nearCornerCell1 = board.getCellAt(0,1);
            nearCornerCell2 = board.getCellAt(1,1);
            nearCornerCell3 = board.getCellAt(1,0);
            edgeCell = board.getCellAt(4,4 );
            nearEdgeCell1 = board.getCellAt(3,4 );
            nearEdgeCell2 = board.getCellAt(3,3 );
            nearEdgeCell3 = board.getCellAt(4,3 );
        }
        catch ( OutOfBoardException e ) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue( !checkError );

    }

    /**
     * Reset attribute after method's test
     */
    @AfterEach
    void tearDown() {

        player = null;
        board = null;
        cornerCell = null;
        nearCornerCell1 = null;
        nearCornerCell2 = null;
        nearCornerCell3 = null;
        edgeCell = null;
        nearEdgeCell1 = null;
        nearEdgeCell2 = null;
        nearEdgeCell3 = null;

    }

    /**
     * check if executeMove can execute move if it possible or trows the correct Exception in some cases
     * Methods used:        chooseCard(String)                      of  Player
     *                      registerWorker(Worker)                  of  Player
     *                      getCard()                               of  Player
     *                      setPlaying( boolean )                   of  Player
     *                      isPlaying()                             of  Player
     *                      setTurn( TurnType )                     of  Player
     *                      getTurnType()                           of  Player
     *                      getMyMove()                             of  Card
     *                      getMyConstruction()                     of  Card
     *                      hasExecutedMovement()                   of  Card
     *                      setMovementExecuted(boolean)            of  Card
     *                      hasExecutedConstruction()               of  Card
     *                      setConstructionExecuted(boolean)        of  Card
     *                      resetForStart()                         of  Card
     *                      decreaseMovesLeft()                     of  MyMove
     *                      getMovesLeft()                          of  MyMove
     *                      decreaseConstructionLeft()              of  MyConstruction
     *                      getConstructionLeft()                   of  MyConstruction
     *                      place(Worker)                           of  Worker
     *                      setChosen(ChooseType)                   of  Worker
     *                      getChosenStatus()                       of  Worker
     *                      clear()                                 of  Board
     *                      getHeight()                             of  Cell
     *                      getPLaceableAt( int, int)               of  Cell
     *                      getTop()                                of  Cell
     *                      repOk()                                 of  Cell
     *                      isBlock()                               of  PLaceable
     *                      isDome()                                of  Placeable
     *
     * Black Box and White Box
     */
    @Test
    void executeMove() {
        boolean checkTurnSwitch = false;
        boolean checkWin = false;
        boolean checkLose = false;
        boolean checkRunOut = false;
        boolean checkBuildBefore = false;
        boolean checkWrongWorker = false;
        boolean checkTurnOver = false;
        boolean checkExecute;
        Move move;

        player.registerWorker(worker1);

        //*** default card ***//
        player.chooseCard("default");

        // set player turn
        player.setPlaying(true);
        player.setTurn(StateType.MOVEMENT);

        /* WrongWorkerException when worker isn't chosen */
        worker1.setChosen(ChooseType.NOT_CHOSEN);
        // cornerCell: W        nearCornerCell1:        nearCornerCell2:      nearCornerCell3:
        worker1.place(cornerCell);

        move = new Move(worker1.position(), nearCornerCell1);

        try {
            checkExecute = player.executeMove(move, worker1);
            assertTrue(!checkExecute);      // ignored
        } catch (WinException e) {
            checkWin = true;
        } catch (LoseException e) {
            checkLose = true;
        } catch (RunOutMovesException e) {
            checkRunOut = true;
        } catch (BuildBeforeMoveException e) {
            checkBuildBefore = true;
        } catch (WrongWorkerException e) {
            checkWrongWorker = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } catch (TurnSwitchedException e) {
            checkTurnSwitch = true;
        } finally {

            // check error
            assertTrue(!checkWin);
            assertTrue(!checkLose);
            assertTrue(!checkRunOut);
            assertTrue(!checkBuildBefore);
            assertTrue(checkWrongWorker);
            assertTrue(!checkTurnOver);
            assertTrue(!checkTurnSwitch);

            // clear error
            checkWin = false;
            checkLose = false;
            checkRunOut = false;
            checkBuildBefore = false;
            checkWrongWorker = false;
            checkTurnOver = false;
            checkTurnSwitch = false;

            //check board
            assertTrue(cornerCell.getHeigth() == 1 );
            assertTrue(nearCornerCell1.getHeigth() == 0 );
            assertTrue(nearCornerCell2.getHeigth() == 0 );
            assertTrue(nearCornerCell3.getHeigth() == 0 );
            assertTrue( cornerCell.getTop().equals(worker1));
            assertTrue( nearCornerCell1.getTop() == null);
            assertTrue( nearCornerCell2.getTop() == null);
            assertTrue( nearCornerCell3.getTop() == null);
            assertTrue( cornerCell.repOk() );
            assertTrue( nearCornerCell1.repOk() );
            assertTrue( nearCornerCell2.repOk() );
            assertTrue( nearCornerCell3.repOk() );

            // check movement and construction parameter
            assertTrue(player.isPlaying());
            assertTrue(player.getTurnType() == StateType.MOVEMENT);
            assertTrue(!player.getCard().hasExecutedMovement());
            assertTrue(!player.getCard().hasExecutedConstruction());
            assertTrue(!player.getCard().isTurnCompleted());
            assertTrue(player.getCard().getMyMove().getMovesLeft() == 1 );
            assertTrue(player.getCard().getMyConstruction().getConstructionLeft() == 1 );
        }

        // clear cell and reset card
        board.clear();
        player.getCard().resetForStart();


        // set player turn
        player.setPlaying(true);
        player.setTurn(StateType.CONSTRUCTION);

        /* BuildBeforeMoveException when worker try to build before movement */
        worker1.setChosen(ChooseType.CHOSEN);
        // cornerCell: W        nearCornerCell1:        nearCornerCell2:      nearCornerCell3:
        worker1.place(cornerCell);

        move = new BuildMove(worker1.position(), nearCornerCell1, PlaceableType.BLOCK);

        try {
            checkExecute = player.executeMove(move, worker1);
            assertTrue(!checkExecute);      // ignored
        } catch (WinException e) {
            checkWin = true;
        } catch (LoseException e) {
            checkLose = true;
        } catch (RunOutMovesException e) {
            checkRunOut = true;
        } catch (BuildBeforeMoveException e) {
            checkBuildBefore = true;
        } catch (WrongWorkerException e) {
            checkWrongWorker = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } catch (TurnSwitchedException e) {
            checkTurnSwitch = true;
        } finally {

            // check error
            assertTrue(!checkWin);
            assertTrue(!checkLose);
            assertTrue(!checkRunOut);
            assertTrue(checkBuildBefore);
            assertTrue(!checkWrongWorker);
            assertTrue(!checkTurnOver);
            assertTrue(!checkTurnSwitch);

            // clear error
            checkWin = false;
            checkLose = false;
            checkRunOut = false;
            checkBuildBefore = false;
            checkWrongWorker = false;
            checkTurnOver = false;
            checkTurnSwitch = false;

            //check board
            assertTrue(cornerCell.getHeigth() == 1 );
            assertTrue(nearCornerCell1.getHeigth() == 0 );
            assertTrue(nearCornerCell2.getHeigth() == 0 );
            assertTrue(nearCornerCell3.getHeigth() == 0 );
            assertTrue( cornerCell.getTop().equals(worker1));
            assertTrue( nearCornerCell1.getTop() == null);
            assertTrue( nearCornerCell2.getTop() == null);
            assertTrue( nearCornerCell3.getTop() == null);
            assertTrue( cornerCell.repOk() );
            assertTrue( nearCornerCell1.repOk() );
            assertTrue( nearCornerCell2.repOk() );
            assertTrue( nearCornerCell3.repOk() );

            // check movement and construction parameter
            assertTrue(player.isPlaying());
            assertTrue(player.getTurnType() == StateType.CONSTRUCTION);
            assertTrue(!player.getCard().hasExecutedMovement());
            assertTrue(!player.getCard().hasExecutedConstruction());
            assertTrue(!player.getCard().isTurnCompleted());
            assertTrue(player.getCard().getMyMove().getMovesLeft() == 1 );
            assertTrue(player.getCard().getMyConstruction().getConstructionLeft() == 1 );
        }

        // clear cell and reset card
        board.clear();
        player.getCard().resetForStart();


        // set player turn
        player.setPlaying(true);
        player.setTurn(StateType.MOVEMENT);

        /* checkExecute == false when worker try to move but it can't */
        worker1.setChosen(ChooseType.CHOSEN);
        // cornerCell: W        nearCornerCell1: BB     nearCornerCell2:      nearCornerCell3:
        worker1.place(cornerCell);
        nearCornerCell1.buildBlock();
        nearCornerCell1.buildBlock();

        move = new Move(worker1.position(), nearCornerCell1);

        try {
            checkExecute = player.executeMove(move, worker1);
            assertTrue(!checkExecute);
        } catch (WinException e) {
            checkWin = true;
        } catch (LoseException e) {
            checkLose = true;
        } catch (RunOutMovesException e) {
            checkRunOut = true;
        } catch (BuildBeforeMoveException e) {
            checkBuildBefore = true;
        } catch (WrongWorkerException e) {
            checkWrongWorker = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } catch (TurnSwitchedException e) {
            checkTurnSwitch = true;
        } finally {

            // check error
            assertTrue(!checkWin);
            assertTrue(!checkLose);
            assertTrue(!checkRunOut);
            assertTrue(!checkBuildBefore);
            assertTrue(!checkWrongWorker);
            assertTrue(!checkTurnOver);
            assertTrue(!checkTurnSwitch);

            // clear error
            checkWin = false;
            checkLose = false;
            checkRunOut = false;
            checkBuildBefore = false;
            checkWrongWorker = false;
            checkTurnOver = false;
            checkTurnSwitch = false;

            //check board
            assertTrue(cornerCell.getHeigth() == 1 );
            assertTrue(nearCornerCell1.getHeigth() == 2 );
            assertTrue(nearCornerCell2.getHeigth() == 0 );
            assertTrue(nearCornerCell3.getHeigth() == 0 );
            assertTrue( cornerCell.getTop().equals(worker1));
            assertTrue( nearCornerCell1.getTop().isBlock());
            assertTrue( nearCornerCell1.getPlaceableAt(0).isBlock());
            assertTrue( nearCornerCell2.getTop() == null);
            assertTrue( nearCornerCell3.getTop() == null);
            assertTrue( cornerCell.repOk() );
            assertTrue( nearCornerCell1.repOk() );
            assertTrue( nearCornerCell2.repOk() );
            assertTrue( nearCornerCell3.repOk() );

            // check movement and construction parameter
            assertTrue(player.isPlaying());
            assertTrue(player.getTurnType() == StateType.MOVEMENT);
            assertTrue(!player.getCard().hasExecutedMovement());
            assertTrue(!player.getCard().hasExecutedConstruction());
            assertTrue(!player.getCard().isTurnCompleted());
            assertTrue(player.getCard().getMyMove().getMovesLeft() == 1 );
            assertTrue(player.getCard().getMyConstruction().getConstructionLeft() == 1 );
        }

        // clear cell and reset card
        board.clear();
        player.getCard().resetForStart();


        // set player turn
        player.setPlaying(true);
        player.setTurn(StateType.MOVEMENT);

        /* TurnSwitchException when worker move but it finishes its moves */
        worker1.setChosen(ChooseType.CHOSEN);
        // cornerCell: W        nearCornerCell1:        nearCornerCell2:      nearCornerCell3:
        worker1.place(cornerCell);

        move = new Move(worker1.position(), nearCornerCell1);

        try {
            checkExecute = player.executeMove(move, worker1);
            assertTrue(checkExecute);      // ignored
        } catch (WinException e) {
            checkWin = true;
        } catch (LoseException e) {
            checkLose = true;
        } catch (RunOutMovesException e) {
            checkRunOut = true;
        } catch (BuildBeforeMoveException e) {
            checkBuildBefore = true;
        } catch (WrongWorkerException e) {
            checkWrongWorker = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } catch (TurnSwitchedException e) {
            checkTurnSwitch = true;
        } finally {

            // check error
            assertTrue(!checkWin);
            assertTrue(!checkLose);
            assertTrue(!checkRunOut);
            assertTrue(!checkBuildBefore);
            assertTrue(!checkWrongWorker);
            assertTrue(!checkTurnOver);
            assertTrue(checkTurnSwitch);

            // clear error
            checkWin = false;
            checkLose = false;
            checkRunOut = false;
            checkBuildBefore = false;
            checkWrongWorker = false;
            checkTurnOver = false;
            checkTurnSwitch = false;

            //check board
            assertTrue(cornerCell.getHeigth() == 0 );
            assertTrue(nearCornerCell1.getHeigth() == 1 );
            assertTrue(nearCornerCell2.getHeigth() == 0 );
            assertTrue(nearCornerCell3.getHeigth() == 0 );
            assertTrue( cornerCell.getTop() == null);
            assertTrue( nearCornerCell1.getTop().equals(worker1));
            assertTrue( nearCornerCell2.getTop() == null);
            assertTrue( nearCornerCell3.getTop() == null);
            assertTrue( cornerCell.repOk() );
            assertTrue( nearCornerCell1.repOk() );
            assertTrue( nearCornerCell2.repOk() );
            assertTrue( nearCornerCell3.repOk() );

            // check movement and construction parameter
            assertTrue(player.isPlaying());
            assertTrue(player.getTurnType() == StateType.CONSTRUCTION);
            assertTrue(player.getCard().hasExecutedMovement());
            assertTrue(!player.getCard().hasExecutedConstruction());
            assertTrue(!player.getCard().isTurnCompleted());
            assertTrue(player.getCard().getMyMove().getMovesLeft() == 0 );
            assertTrue(player.getCard().getMyConstruction().getConstructionLeft() == 1 );


        }

        // clear cell and reset card
        board.clear();
        player.getCard().resetForStart();


        // set player turn
        player.setPlaying(true);
        player.setTurn(StateType.MOVEMENT);

        /* WinException when worker move at third level */
        worker1.setChosen(ChooseType.CHOSEN);
        // cornerCell: BBW      nearCornerCell1: BBB     nearCornerCell2:      nearCornerCell3:
        cornerCell.buildBlock();
        cornerCell.buildBlock();
        worker1.place(cornerCell);
        nearCornerCell1.buildBlock();
        nearCornerCell1.buildBlock();
        nearCornerCell1.buildBlock();

        move = new Move(worker1.position(), nearCornerCell1);

        try {
            checkExecute = player.executeMove(move, worker1);
            assertTrue(checkExecute);      // ignored
        } catch (WinException e) {
            checkWin = true;
        } catch (LoseException e) {
            checkLose = true;
        } catch (RunOutMovesException e) {
            checkRunOut = true;
        } catch (BuildBeforeMoveException e) {
            checkBuildBefore = true;
        } catch (WrongWorkerException e) {
            checkWrongWorker = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } catch (TurnSwitchedException e) {
            checkTurnSwitch = true;
        } finally {

            // check error
            assertTrue(checkWin);
            assertTrue(!checkLose);
            assertTrue(!checkRunOut);
            assertTrue(!checkBuildBefore);
            assertTrue(!checkWrongWorker);
            assertTrue(!checkTurnOver);
            assertTrue(!checkTurnSwitch);

            // clear error
            checkWin = false;
            checkLose = false;
            checkRunOut = false;
            checkBuildBefore = false;
            checkWrongWorker = false;
            checkTurnOver = false;
            checkTurnSwitch = false;

            //check board
            assertTrue(cornerCell.getHeigth() == 2 );
            assertTrue(nearCornerCell1.getHeigth() == 4 );
            assertTrue(nearCornerCell2.getHeigth() == 0 );
            assertTrue(nearCornerCell3.getHeigth() == 0 );
            assertTrue( cornerCell.getTop().isBlock());
            assertTrue( cornerCell.getPlaceableAt(0).isBlock());
            assertTrue( nearCornerCell1.getTop().equals(worker1));
            assertTrue( nearCornerCell1.getPlaceableAt(2).isBlock());
            assertTrue( nearCornerCell1.getPlaceableAt(1).isBlock());
            assertTrue( nearCornerCell1.getPlaceableAt(0).isBlock());
            assertTrue( nearCornerCell2.getTop() == null);
            assertTrue( nearCornerCell3.getTop() == null);
            assertTrue( cornerCell.repOk() );
            assertTrue( nearCornerCell1.repOk() );
            assertTrue( nearCornerCell2.repOk() );
            assertTrue( nearCornerCell3.repOk() );

            // check movement and construction parameter
            assertTrue(player.isPlaying());
            assertTrue(player.getTurnType() == StateType.MOVEMENT);
            assertTrue(!player.getCard().hasExecutedMovement());
            assertTrue(!player.getCard().hasExecutedConstruction());
            assertTrue(!player.getCard().isTurnCompleted());
            assertTrue(player.getCard().getMyMove().getMovesLeft() == 1 );
            assertTrue(player.getCard().getMyConstruction().getConstructionLeft() == 1 );


        }

        // clear cell and reset card
        board.clear();
        player.getCard().resetForStart();


        // set player turn
        player.setPlaying(true);
        player.setTurn(StateType.MOVEMENT);

        /* no LoseException with executeMove when worker can't move but return false */
        worker1.setChosen(ChooseType.CHOSEN);
        // cornerCell:   W      nearCornerCell1: BBB     nearCornerCell2: D     nearCornerCell3: D
        worker1.place(cornerCell);
        nearCornerCell1.buildBlock();
        nearCornerCell1.buildBlock();
        nearCornerCell1.buildBlock();
        nearCornerCell2.buildDome();
        nearCornerCell3.buildDome();

        move = new Move(worker1.position(), nearCornerCell1);

        try {
            checkExecute = player.executeMove(move, worker1);
            assertTrue(!checkExecute);
        } catch (WinException e) {
            checkWin = true;
        } catch (LoseException e) {
            checkLose = true;
        } catch (RunOutMovesException e) {
            checkRunOut = true;
        } catch (BuildBeforeMoveException e) {
            checkBuildBefore = true;
        } catch (WrongWorkerException e) {
            checkWrongWorker = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } catch (TurnSwitchedException e) {
            checkTurnSwitch = true;
        } finally {

            // check error
            assertTrue(!checkWin);
            assertTrue(!checkLose); // Lose Exception for impossibility to move is (and must) checked before execute move
            assertTrue(!checkRunOut);
            assertTrue(!checkBuildBefore);
            assertTrue(!checkWrongWorker);
            assertTrue(!checkTurnOver);
            assertTrue(!checkTurnSwitch);

            // clear error
            checkWin = false;
            checkLose = false;
            checkRunOut = false;
            checkBuildBefore = false;
            checkWrongWorker = false;
            checkTurnOver = false;
            checkTurnSwitch = false;

            //check board
            assertTrue(cornerCell.getHeigth() == 1 );
            assertTrue(nearCornerCell1.getHeigth() == 3 );
            assertTrue(nearCornerCell2.getHeigth() == 1 );
            assertTrue(nearCornerCell3.getHeigth() == 1 );
            assertTrue( cornerCell.getTop().equals(worker1));
            assertTrue( nearCornerCell1.getTop().isBlock());
            assertTrue( nearCornerCell1.getPlaceableAt(1).isBlock());
            assertTrue( nearCornerCell1.getPlaceableAt(0).isBlock());
            assertTrue( nearCornerCell2.getTop().isDome());
            assertTrue( nearCornerCell3.getTop().isDome());
            assertTrue( cornerCell.repOk() );
            assertTrue( nearCornerCell1.repOk() );
            assertTrue( nearCornerCell2.repOk() );
            assertTrue( nearCornerCell3.repOk() );

            // check movement and construction parameter
            assertTrue(player.isPlaying());
            assertTrue(player.getTurnType() == StateType.MOVEMENT);
            assertTrue(!player.getCard().hasExecutedMovement());
            assertTrue(!player.getCard().hasExecutedConstruction());
            assertTrue(!player.getCard().isTurnCompleted());
            assertTrue(player.getCard().getMyMove().getMovesLeft() == 1 );
            assertTrue(player.getCard().getMyConstruction().getConstructionLeft() == 1 );


        }

        // clear cell and reset card
        board.clear();
        player.getCard().resetForStart();


        // set player turn
        player.setPlaying(true);
        player.setTurn(StateType.MOVEMENT);

        /* RunOutMovesException when worker has not moveleft */
        worker1.setChosen(ChooseType.CHOSEN);
        player.getCard().getMyMove().decreaseMovesLeft();
        // cornerCell:   W      nearCornerCell1:     nearCornerCell2:       nearCornerCell3:
        worker1.place(cornerCell);

        move = new Move(worker1.position(), nearCornerCell1);

        try {
            checkExecute = player.executeMove(move, worker1);
            assertTrue(!checkExecute);      // ignored
        } catch (WinException e) {
            checkWin = true;
        } catch (LoseException e) {
            checkLose = true;
        } catch (RunOutMovesException e) {
            checkRunOut = true;
        } catch (BuildBeforeMoveException e) {
            checkBuildBefore = true;
        } catch (WrongWorkerException e) {
            checkWrongWorker = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } catch (TurnSwitchedException e) {
            checkTurnSwitch = true;
        } finally {

            // check error
            assertTrue(!checkWin);
            assertTrue(!checkLose);
            assertTrue(checkRunOut);
            assertTrue(!checkBuildBefore);
            assertTrue(!checkWrongWorker);
            assertTrue(!checkTurnOver);
            assertTrue(!checkTurnSwitch);

            // clear error
            checkWin = false;
            checkLose = false;
            checkRunOut = false;
            checkBuildBefore = false;
            checkWrongWorker = false;
            checkTurnOver = false;
            checkTurnSwitch = false;

            //check board
            assertTrue(cornerCell.getHeigth() == 1 );
            assertTrue(nearCornerCell1.getHeigth() == 0 );
            assertTrue(nearCornerCell2.getHeigth() == 0 );
            assertTrue(nearCornerCell3.getHeigth() == 0 );
            assertTrue( cornerCell.getTop().equals(worker1) );
            assertTrue( nearCornerCell1.getTop() == null );
            assertTrue( nearCornerCell2.getTop() == null );
            assertTrue( nearCornerCell3.getTop() == null );
            assertTrue( cornerCell.repOk() );
            assertTrue( nearCornerCell1.repOk() );
            assertTrue( nearCornerCell2.repOk() );
            assertTrue( nearCornerCell3.repOk() );

            // check movement and construction parameter
            assertTrue(player.isPlaying());
            assertTrue(player.getTurnType() == StateType.MOVEMENT);
            assertTrue(!player.getCard().hasExecutedMovement());
            assertTrue(!player.getCard().hasExecutedConstruction());
            assertTrue(!player.getCard().isTurnCompleted());
            assertTrue(player.getCard().getMyMove().getMovesLeft() == 0 );
            assertTrue(player.getCard().getMyConstruction().getConstructionLeft() == 1 );


        }

        // clear cell and reset card
        board.clear();
        player.getCard().resetForStart();


        // set player turn
        player.setPlaying(true);
        player.getCard().setMovementExecuted(true);
        player.setTurn(StateType.CONSTRUCTION);

        /* TurnOverException when worker finishes buildLeft after build */
        worker1.setChosen(ChooseType.CHOSEN);
        // cornerCell:   W      nearCornerCell1:     nearCornerCell2:       nearCornerCell3:
        worker1.place(cornerCell);

        move = new BuildMove(worker1.position(), nearCornerCell1, PlaceableType.BLOCK);

        try {
            checkExecute = player.executeMove(move, worker1);
            assertTrue(checkExecute);      // ignored
        } catch (WinException e) {
            checkWin = true;
        } catch (LoseException e) {
            checkLose = true;
        } catch (RunOutMovesException e) {
            checkRunOut = true;
        } catch (BuildBeforeMoveException e) {
            checkBuildBefore = true;
        } catch (WrongWorkerException e) {
            checkWrongWorker = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } catch (TurnSwitchedException e) {
            checkTurnSwitch = true;
        } finally {

            // check error
            assertTrue(!checkWin);
            assertTrue(!checkLose);
            assertTrue(!checkRunOut);
            assertTrue(!checkBuildBefore);
            assertTrue(!checkWrongWorker);
            assertTrue(checkTurnOver);
            assertTrue(!checkTurnSwitch);

            // clear error
            checkWin = false;
            checkLose = false;
            checkRunOut = false;
            checkBuildBefore = false;
            checkWrongWorker = false;
            checkTurnOver = false;
            checkTurnSwitch = false;

            //check board
            assertTrue(cornerCell.getHeigth() == 1 );
            assertTrue(nearCornerCell1.getHeigth() == 1 );
            assertTrue(nearCornerCell2.getHeigth() == 0 );
            assertTrue(nearCornerCell3.getHeigth() == 0 );
            assertTrue( cornerCell.getTop().equals(worker1) );
            assertTrue( nearCornerCell1.getTop().isBlock() );
            assertTrue( nearCornerCell2.getTop() == null );
            assertTrue( nearCornerCell3.getTop() == null );
            assertTrue( cornerCell.repOk() );
            assertTrue( nearCornerCell1.repOk() );
            assertTrue( nearCornerCell2.repOk() );
            assertTrue( nearCornerCell3.repOk() );

            // check movement and construction parameter
            assertTrue(player.isPlaying());
            assertTrue(player.getTurnType() == StateType.CONSTRUCTION);
            assertTrue(player.getCard().hasExecutedMovement());
            assertTrue(player.getCard().hasExecutedConstruction());
            assertTrue(player.getCard().isTurnCompleted());
            assertTrue(player.getCard().getMyMove().getMovesLeft() == 1 );
            assertTrue(player.getCard().getMyConstruction().getConstructionLeft() == 0 );


        }

        // clear cell and reset card
        board.clear();
        player.getCard().resetForStart();


        //*** Artemis' Card ***//
        player.chooseCard("artemis");

        // set player turn
        player.setPlaying(true);
        player.setTurn(StateType.MOVEMENT);

        /* checkExecute == true when worker move its firt movement */
        worker1.setChosen(ChooseType.CHOSEN);
        // cornerCell:   W      nearCornerCell1:     nearCornerCell2:       nearCornerCell3:
        worker1.place(cornerCell);

        move = new Move(worker1.position(), nearCornerCell1);

        try {
            checkExecute = player.executeMove(move, worker1);
            assertTrue(checkExecute);
        } catch (WinException e) {
            checkWin = true;
        } catch (LoseException e) {
            checkLose = true;
        } catch (RunOutMovesException e) {
            checkRunOut = true;
        } catch (BuildBeforeMoveException e) {
            checkBuildBefore = true;
        } catch (WrongWorkerException e) {
            checkWrongWorker = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } catch (TurnSwitchedException e) {
            checkTurnSwitch = true;
        } finally {

            // check error
            assertTrue(!checkWin);
            assertTrue(!checkLose);
            assertTrue(!checkRunOut);
            assertTrue(!checkBuildBefore);
            assertTrue(!checkWrongWorker);
            assertTrue(!checkTurnOver);
            assertTrue(!checkTurnSwitch);

            // clear error
            checkWin = false;
            checkLose = false;
            checkRunOut = false;
            checkBuildBefore = false;
            checkWrongWorker = false;
            checkTurnOver = false;
            checkTurnSwitch = false;

            //check board
            assertTrue(cornerCell.getHeigth() == 0 );
            assertTrue(nearCornerCell1.getHeigth() == 1 );
            assertTrue(nearCornerCell2.getHeigth() == 0 );
            assertTrue(nearCornerCell3.getHeigth() == 0 );
            assertTrue( cornerCell.getTop() == null );
            assertTrue( nearCornerCell1.getTop().equals(worker1) );
            assertTrue( nearCornerCell2.getTop() == null );
            assertTrue( nearCornerCell3.getTop() == null );
            assertTrue( cornerCell.repOk() );
            assertTrue( nearCornerCell1.repOk() );
            assertTrue( nearCornerCell2.repOk() );
            assertTrue( nearCornerCell3.repOk() );

            // check movement and construction parameter
            assertTrue(player.isPlaying());
            assertTrue(player.getTurnType() == StateType.MOVEMENT);
            assertTrue(player.getCard().hasExecutedMovement());
            assertTrue(!player.getCard().hasExecutedConstruction());
            assertTrue(!player.getCard().isTurnCompleted());
            assertTrue(player.getCard().getMyMove().getMovesLeft() == 1 );
            assertTrue(player.getCard().getMyConstruction().getConstructionLeft() == 1 );


        }

        // clear cell and reset card
        board.clear();
        player.getCard().resetForStart();


    }


    /**
     * check if startTurn() can correctly reset the internal status of Model's Classes
     * Methods used:        chooseCard(String)                      of  Player
     *                      registerWorker(Worker)                  of  Player
     *                      getCard()                               of  Player
     *                      isPlaying()                             of  Player
     *                      getTurnType()                           of  Player
     *                      getMyMove()                             of  Card
     *                      getMyConstruction()                     of  Card
     *                      hasExecutedMovement()                   of  Card
     *                      setMovementExecuted(boolean)            of  Card
     *                      hasExecutedConstruction()               of  Card
     *                      setConstructionExecuted(boolean)        of  Card
     *                      decreaseMovesLeft()                     of  MyMove
     *                      getMovesLeft()                          of  MyMove
     *                      decreaseConstructionLeft()              of  MyConstruction
     *                      getConstructionLeft()                   of  MyConstruction
     *                      place(Worker)                           of  Worker
     *                      setChosen(ChooseType)                   of  Worker
     *                      getChosenStatus()                       of  Worker
     *
     * Black Box and White Box
     */
    @Test
    void startTurn() {
        boolean checkLose = false;

        // initialization
        player.chooseCard("artemis");
        player.registerWorker(worker1);
        player.registerWorker(worker2);
        worker1.place(cornerCell);
        worker2.place(edgeCell);

        // change internal attribute of Model's Classes for simulate an execution
        player.getCard().setMovementExecuted(true);
        player.getCard().setConstructionExecuted(true);
        player.getCard().getMyMove().decreaseMovesLeft();
        player.getCard().getMyConstruction().decreaseConstructionLeft();
        worker1.setChosen(ChooseType.CHOSEN);
        worker2.setChosen(ChooseType.NOT_CHOSEN);

        // reset and check
        try {
            player.startTurn();

            assertTrue( player.getCard().hasExecutedMovement() == false );
            assertTrue( player.getCard().hasExecutedConstruction() == false) ;
            assertTrue( player.getCard().getMyMove().getMovesLeft() == 2 );
            assertTrue( player.getCard().getMyConstruction().getConstructionLeft() == 1 );
            assertTrue( worker1.getChosenStatus() == ChooseType.CAN_BE_CHOSEN );
            assertTrue( worker2.getChosenStatus() == ChooseType.CAN_BE_CHOSEN);
            assertTrue( player.isPlaying() );
            assertTrue( player.getTurnType() == StateType.MOVEMENT);
        } catch (LoseException e) {
            checkLose = true;
        }
        assertTrue( !checkLose );


    }


    /**
     * Check if endTurn() can correctly set the playing status
     * Methods used:    setPlaying( boolean )       of  Player
     *                  isPlaying()                 of  Player
     *
     * Black and White Box
     */
    @Test
    void endTurn() {

        player.setPlaying(true);
        player.endTurn();

        assertTrue( !player.isPlaying() );
    }

    /**
     * Check if chooseState( StateType ) can correctly change status if it is possible
     * Methods used:        chooseCard(String)                  of  Player
     *                      registerWorker(Worker)              of  Player
     *                      getStatusType()                     of  Player
     *                      setTurn( StateType )                of  Player
     *                      getCard()                           of  Card
     *                      setExecutedMovement( boolean )      of  Card
     *                      setExecutedConstruction( boolean )  of  Card
     *                      resetForStar()                      of  Card
     *                      getMyMove()                         of  Card
     *                      getMyConstruction()                 of  Card
     *                      decreaseMovesLeft()                 of  MyMove
     *                      decreaseConstructionLeft()          of  MyConstruction
     *                      clear()                             of  Board
     *                      buildBlock()                        of  Cell
     *                      buildDome()                         of  Cell
     *                      place(Cell)                         of  Worker
     *                      setChosen( ChosenType )             of  Worker
     *
     * Black Box
     */
    @Test
    void chooseStateBlack() {
        Player opponent = new Player("opponent");
        Worker opponentWorker = new Worker(opponent);
        boolean checkLose = false;
        boolean checkTurnOver = false;
        boolean checkChangeStatus;

        player.registerWorker(worker1);  // use only one worker for simplicity

        //*** Apollo's Card ***//
        player.chooseCard("apollo"); // because there is a condition where Apollo can move but not build


        //>> LOSE EXCEPTION CHECK

        //* MOVEMENT (from construction)*//

        /* LoseException when all Workers can't move but build*/
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: BB     nearCornerCell3: BBB
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildBlock();
        nearCornerCell2.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        player.setTurn(StateType.CONSTRUCTION);
        try {
            checkChangeStatus = player.chooseState(StateType.MOVEMENT);
            assertTrue( !checkChangeStatus );   // ignored
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset board
        while (cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }
        while (nearCornerCell1.getTop() != null) {
            nearCornerCell1.removePlaceable();
        }
        while (nearCornerCell2.getTop() != null) {
            nearCornerCell2.removePlaceable();
        }
        while (nearCornerCell3.getTop() != null) {
            nearCornerCell3.removePlaceable();
        }

        /* no LoseException when all Workers can move but not build*/
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: W (opponent)     nearCornerCell3: BBBD
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        opponentWorker.place(nearCornerCell2);
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildDome();
        player.setTurn(StateType.CONSTRUCTION);
        try {
            checkChangeStatus = player.chooseState(StateType.MOVEMENT);
            assertTrue( checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.MOVEMENT );
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset board
        while (cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }
        while (nearCornerCell1.getTop() != null) {
            nearCornerCell1.removePlaceable();
        }
        while (nearCornerCell2.getTop() != null) {
            nearCornerCell2.removePlaceable();
        }
        while (nearCornerCell3.getTop() != null) {
            nearCornerCell3.removePlaceable();
        }



        //* CONSTRUCTION (from Movement) *//

        /* LoseException when all Workers can't build but move*/
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: W (opponent)     nearCornerCell3: BBBD
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        opponentWorker.place(nearCornerCell2);
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildDome();
        player.setTurn(StateType.MOVEMENT);
        try {
            checkChangeStatus = player.chooseState(StateType.CONSTRUCTION);
            assertTrue( !checkChangeStatus );       // ignored
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }
        // reset board
        while (cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }
        while (nearCornerCell1.getTop() != null) {
            nearCornerCell1.removePlaceable();
        }
        while (nearCornerCell2.getTop() != null) {
            nearCornerCell2.removePlaceable();
        }
        while (nearCornerCell3.getTop() != null) {
            nearCornerCell3.removePlaceable();
        }

        /* no LoseException when all Workers can build but not move */
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: BB     nearCornerCell3: BBB
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildBlock();
        nearCornerCell2.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        player.setTurn(StateType.MOVEMENT);
        try {
            checkChangeStatus = player.chooseState(StateType.CONSTRUCTION);
            assertTrue( checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.CONSTRUCTION );
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }
        // reset board and checkLose
        while (cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }
        while (nearCornerCell1.getTop() != null) {
            nearCornerCell1.removePlaceable();
        }
        while (nearCornerCell2.getTop() != null) {
            nearCornerCell2.removePlaceable();
        }
        while (nearCornerCell3.getTop() != null) {
            nearCornerCell3.removePlaceable();
        }

        //>> TURN EXCEPTION CHECK

        // cornerCell: W        nearCornerCell1: B    nearCornerCell2:        nearCornerCell3:
        worker1.place(cornerCell);
        nearCornerCell1.buildBlock();

        /* TurnException when MOVEMENT --> NONE with TurnCompleted == true */
        player.setTurn(StateType.MOVEMENT);
        player.getCard().setTurnCompleted(true);

        try {
            checkChangeStatus = player.chooseState(StateType.NONE);
            assertTrue( checkChangeStatus );       // ignored
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }
        assertTrue( player.getTurnType() != StateType.NONE );

        // reset card status
        player.getCard().resetForStart();


        /* TurnException when CONSTRUCTION --> NONE with TurnCompleted == true */
        player.setTurn(StateType.CONSTRUCTION);
        player.getCard().setTurnCompleted(true);

        try {
            checkChangeStatus = player.chooseState(StateType.NONE);
            assertTrue( checkChangeStatus );       // ignored
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset card status
        player.getCard().resetForStart();


        /* no TurnException when MOVEMENT --> NONE with TurnCompleted == false */
        player.setTurn(StateType.MOVEMENT);
        player.getCard().setTurnCompleted(false);

        try {
            checkChangeStatus = player.chooseState(StateType.NONE);
            assertTrue( !checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.MOVEMENT );
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset card status
        player.getCard().resetForStart();


        /* no TurnException when CONSTRUCTION --> NONE with TurnCompleted == false */
        player.setTurn(StateType.CONSTRUCTION);
        player.getCard().setTurnCompleted(false);

        try {
            checkChangeStatus = player.chooseState(StateType.NONE);
            assertTrue( !checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.CONSTRUCTION );
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset card status
        player.getCard().resetForStart();


        /* no TurnException when MOVEMENT --> NONE with TurnCompleted == false,
                                                        MovementExecuted == true,
                                                        ConstructionExecuted == true */
        player.setTurn(StateType.MOVEMENT);
        player.getCard().setMovementExecuted(true);
        player.getCard().setConstructionExecuted(true);
        player.getCard().setTurnCompleted(false);

        try {
            checkChangeStatus = player.chooseState(StateType.NONE);
            assertTrue( !checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.MOVEMENT );
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset card status
        player.getCard().resetForStart();



        // reset board
        while (cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }
        while (nearCornerCell1.getTop() != null) {
            nearCornerCell1.removePlaceable();
        }
        while (nearCornerCell2.getTop() != null) {
            nearCornerCell2.removePlaceable();
        }
        while (nearCornerCell3.getTop() != null) {
            nearCornerCell3.removePlaceable();
        }

        //>> other change status cases

        // cornerCell: W        nearCornerCell1: B    nearCornerCell2:        nearCornerCell3:
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();

        /* not change when MOVEMENT --> CONSTRUCTION with ConstructionLeft == 0 */
        player.setTurn(StateType.MOVEMENT);
        player.getCard().getMyConstruction().decreaseConstructionLeft();

        try {
            checkChangeStatus = player.chooseState(StateType.CONSTRUCTION);
            assertTrue( !checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.MOVEMENT );
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset card status
        player.getCard().resetForStart();

        /* not change when CONSTRUCTION --> MOVEMENT with MovementLeft == 0 */
        player.setTurn(StateType.CONSTRUCTION);
        player.getCard().getMyMove().decreaseMovesLeft();

        try {
            checkChangeStatus = player.chooseState(StateType.MOVEMENT);
            assertTrue( !checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.CONSTRUCTION );
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset card status
        player.getCard().resetForStart();

    }

    /**
     * Check if chooseState( StateType ) (and private classes preliminaryCheck(StateType) and checkIfTurnCompleted() )
     * can correctly change status if it is possible
     * Methods used:        chooseCard(String)                  of  Player
     *                      registerWorker(Worker)              of  Player
     *                      getStatusType()                     of  Player
     *                      setTurn( StateType )                of  Player
     *                      getCard()                           of  Card
     *                      setExecutedMovement( boolean )      of  Card
     *                      setExecutedConstruction( boolean )  of  Card
     *                      resetForStar()                      of  Card
     *                      getMyMove()                         of  Card
     *                      getMyConstruction()                 of  Card
     *                      decreaseMovesLeft()                 of  MyMove
     *                      decreaseConstructionLeft()          of  MyConstruction
     *                      clear()                             of  Board
     *                      buildBlock()                        of  Cell
     *                      buildDome()                         of  Cell
     *                      place(Cell)                         of  Worker
     *                      setChosen( ChosenType )             of  Worker
     *
     * White Box
     */
    @Test
    void chooseStateWhite() {
        Player opponent = new Player("opponent");
        Worker opponentWorker = new Worker(opponent);
        boolean checkLose = false;
        boolean checkTurnOver = false;
        boolean checkChangeStatus;

        //*** default's Card ***//
        player.chooseCard("default");
        worker1.setChosen(ChooseType.CHOSEN);
        player.registerWorker(worker1);

        // cornerCell: W        nearCornerCell1: B    nearCornerCell2:        nearCornerCell3:
        worker1.place(cornerCell);
        nearCornerCell1.buildBlock();

        /* not change when MOVEMENT --> ANY */
        player.setTurn(StateType.MOVEMENT);

        try {
            checkChangeStatus = player.chooseState(StateType.ANY);
            assertTrue( !checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.MOVEMENT );
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset card status
        player.getCard().resetForStart();

        /* change when MOVEMENT --> MOVEMENT */
        player.setTurn(StateType.MOVEMENT);

        try {
            checkChangeStatus = player.chooseState(StateType.MOVEMENT);
            assertTrue( checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.MOVEMENT );
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset card status
        player.getCard().resetForStart();

        /* change when CONSTRUCTION --> MOVEMENT with movementLeft > 0 */
        player.setTurn(StateType.CONSTRUCTION);

        try {
            checkChangeStatus = player.chooseState(StateType.MOVEMENT);
            assertTrue( checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.MOVEMENT );
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset card status
        player.getCard().resetForStart();

        /* not change when CONSTRUCTION --> MOVEMENT with movementLeft == 0 */
        player.setTurn(StateType.CONSTRUCTION);
        player.getCard().getMyMove().decreaseMovesLeft();

        try {
            checkChangeStatus = player.chooseState(StateType.MOVEMENT);
            assertTrue( !checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.CONSTRUCTION );
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset card status
        player.getCard().resetForStart();

        /* change when MOVEMENT --> CONSTRUCTION with constructionLeft > 0 */
        player.setTurn(StateType.MOVEMENT);

        try {
            checkChangeStatus = player.chooseState(StateType.CONSTRUCTION);
            assertTrue( checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.CONSTRUCTION );
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset card status
        player.getCard().resetForStart();

        /* not change when MOVEMENT --> CONSTRUCTION with constructionLeft == 0 */
        player.setTurn(StateType.MOVEMENT);
        player.getCard().getMyConstruction().decreaseConstructionLeft();

        try {
            checkChangeStatus = player.chooseState(StateType.CONSTRUCTION);
            assertTrue( !checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.MOVEMENT );
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset card status
        player.getCard().resetForStart();

        /* change and TurnOverException when MOVEMENT --> NONE with TurnCompleted == true */
        player.setTurn(StateType.MOVEMENT);
        player.getCard().setTurnCompleted(true);

        try {
            checkChangeStatus = player.chooseState(StateType.NONE);
            assertTrue( checkChangeStatus );        // ignored
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset card status
        player.getCard().resetForStart();

        /* not change when CONSTRUCTION --> NONE with TurnCompleted == false */
        player.setTurn(StateType.CONSTRUCTION);
        player.getCard().setTurnCompleted(false);

        try {
            checkChangeStatus = player.chooseState(StateType.NONE);
            assertTrue( !checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.CONSTRUCTION );
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(!checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }

        // reset card status
        player.getCard().resetForStart();


        // reset board
        while (cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }
        while (nearCornerCell1.getTop() != null) {
            nearCornerCell1.removePlaceable();
        }
        while (nearCornerCell2.getTop() != null) {
            nearCornerCell2.removePlaceable();
        }
        while (nearCornerCell3.getTop() != null) {
            nearCornerCell3.removePlaceable();
        }

        //new board configure
        // cornerCell: W        nearCornerCell1: D    nearCornerCell2: W (opponent)    nearCornerCell3: BBBD
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        opponentWorker.place(nearCornerCell2);
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildDome();


        /* not change and LoseException when CONSTRUCTION --> MOVEMENT with movementLeft > 0 but can't move*/
        player.setTurn(StateType.CONSTRUCTION);

        try {
            checkChangeStatus = player.chooseState(StateType.MOVEMENT);
            assertTrue( !checkChangeStatus );       // ignored
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }
        assertTrue( player.getTurnType() == StateType.CONSTRUCTION );


        // reset card status
        player.getCard().resetForStart();


        /* not change and LoseException when MOVEMENT --> CONSTRUCTION with movementLeft > 0 but can't build*/
        player.setTurn(StateType.MOVEMENT);

        try {
            checkChangeStatus = player.chooseState(StateType.CONSTRUCTION);
            assertTrue( !checkChangeStatus );       // ignored
        } catch (LoseException e) {
            checkLose = true;
        } catch (TurnOverException e) {
            checkTurnOver = true;
        } finally {
            assertTrue(checkLose);
            assertTrue(!checkTurnOver);
            checkLose = false;
            checkTurnOver = false;
        }
        assertTrue( player.getTurnType() == StateType.MOVEMENT );


        // reset card status
        player.getCard().resetForStart();

    }

    /**
     * Check if switchState( TurnManager ) can correctly evaluate the lose condition of movement and of construction
     * Methods used:        chooseCard(String)                  of  Player
     *                      registerWorker(Worker)              of  Player
     *                      getMovementManager()                of  Player
     *                      getConstructionManager()            of  Player
     *                      getStatusType()                     of  Player
     *                      getCard()                           of  Card
     *                      setExecutedMovement( boolean )      of  Card
     *                      setExecutedConstruction( boolean )  of  Card
     *                      getMyMove()                         of  Card
     *                      getMyConstruction()                 of  Card
     *                      clear()                             of  Board
     *                      decreaseMovesLeft()                 of  MyMove
     *                      setStartingPosition( cell )         of  MyMove
     *                      setLastMove( Move )                 of  MyMove
     *                      decreaseConstructionLeft()          of  MyConstruction
     *                      setLastMove( BuildMove )            of  MyConstruction
     *                      buildBlock()                        of  Cell
     *                      buildDome()                         of  Cell
     *                      place(Cell)                         of  Worker
     *                      setChosen( ChosenType )             of  Worker
     *
     * Black Box
     */
    @Test
    void switchStateBlack() {
        Player opponent = new Player("opponent");
        Worker opponentWorker = new Worker(opponent);
        boolean checkLose = false;
        boolean checkChangeStatus;


        player.registerWorker(worker1);
        player.registerWorker(worker2);

        //*** Apollo card ***//
        player.chooseCard("apollo");

        //>> to movementTurn | not already move | all worker | can't build
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: D      nearCornerCell3: D
        // edgeCell: W          nearEdgeCell1: D        nearEdgeCell2: D        nearEdgeCell3: O (opponent's Worker)
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildDome();
        nearCornerCell3.buildDome();
        nearEdgeCell1.buildDome();
        nearEdgeCell2.buildDome();
        opponentWorker.place(nearEdgeCell3);
        worker2.place(edgeCell);
        try {
            checkChangeStatus = player.switchState( player.getMovementManager() );
            assertTrue( checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.MOVEMENT );
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(!checkLose);
            checkLose = false;
        }

        // clear board
        board.clear();

        //>> to constructionTurn | not already move | all worker | can't build
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: D      nearCornerCell3: D
        // edgeCell: W          nearEdgeCell1: D        nearEdgeCell2: D        nearEdgeCell3: O (Opponent's Worker)
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildDome();
        nearCornerCell3.buildDome();
        nearEdgeCell1.buildDome();
        nearEdgeCell2.buildDome();
        opponentWorker.place(nearEdgeCell3);
        worker2.place(edgeCell);
        try {
            checkChangeStatus = player.switchState( player.getConstructionManager() );
            assertTrue( !checkChangeStatus ); // ignored
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(checkLose);
            checkLose = false;
        }

        // clear board
        board.clear();

        //>> to movementTurn | not already move | all worker | can't move
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: BB     nearCornerCell3: BBB
        // edgeCell: W          nearEdgeCell1: D        nearEdgeCell2: D        nearEdgeCell3: BB
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildBlock();
        nearCornerCell2.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearEdgeCell1.buildDome();
        nearEdgeCell2.buildDome();
        nearEdgeCell3.buildBlock();
        nearEdgeCell3.buildBlock();
        worker2.place(edgeCell);
        try {
            checkChangeStatus = player.switchState( player.getMovementManager() );
            assertTrue( !checkChangeStatus );       // ignored
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(checkLose);
            checkLose = false;
        }

        // clear board
        board.clear();

        //>> to movementTurn | not already move | selected worker | can't move
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: BB     nearCornerCell3: BBB
        // edgeCell: W          nearEdgeCell1:          nearEdgeCell2:          nearEdgeCell3:
        worker1.setChosen(ChooseType.CHOSEN);
        worker2.setChosen(ChooseType.NOT_CHOSEN);
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildBlock();
        nearCornerCell2.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        worker2.place(edgeCell);
        try {
            checkChangeStatus = player.switchState( player.getMovementManager() );
            assertTrue( !checkChangeStatus );       // ignored
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(checkLose);
            checkLose = false;
        }

        // clear board
        board.clear();

        //>> to movementTurn | not already move | not selected worker | can't move
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: BB     nearCornerCell3: BBB
        // edgeCell: W          nearEdgeCell1:          nearEdgeCell2:          nearEdgeCell3:
        worker1.setChosen(ChooseType.NOT_CHOSEN);
        worker2.setChosen(ChooseType.CHOSEN);
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildBlock();
        nearCornerCell2.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        worker2.place(edgeCell);
        try {
            checkChangeStatus = player.switchState( player.getMovementManager() );
            assertTrue( checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.MOVEMENT );
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(!checkLose);
            checkLose = false;
        }

        // clear board
        board.clear();

        /* already move*/
        player.getCard().setMovementExecuted(true);
        player.getCard().getMyMove().decreaseMovesLeft();

        //>> to constructionTurn | already move | all worker | can't build
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: D       nearCornerCell3: D
        // edgeCell: W          nearEdgeCell1: D        nearEdgeCell2: D         nearEdgeCell3: O
        worker1.setChosen(ChooseType.CAN_BE_CHOSEN);
        worker2.setChosen(ChooseType.CAN_BE_CHOSEN);
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildDome();
        nearCornerCell3.buildDome();
        nearEdgeCell1.buildDome();
        nearEdgeCell2.buildDome();
        opponentWorker.place(nearEdgeCell3);
        worker2.place(edgeCell);
        try {
            checkChangeStatus = player.switchState( player.getConstructionManager() );
            assertTrue( !checkChangeStatus );
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(checkLose);
            checkLose = false;
        }

        // clear board
        board.clear();

        //>> to constructionTurn | already move | selected worker | can't build
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: D       nearCornerCell3: D
        // edgeCell: W          nearEdgeCell1: B         nearEdgeCell2: B        nearEdgeCell3: BBB
        worker1.setChosen(ChooseType.CHOSEN);
        worker2.setChosen(ChooseType.NOT_CHOSEN);
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildDome();
        nearCornerCell3.buildDome();
        nearEdgeCell1.buildBlock();
        nearEdgeCell2.buildBlock();
        nearEdgeCell3.buildBlock();
        nearEdgeCell3.buildBlock();
        nearEdgeCell3.buildBlock();
        worker2.place(edgeCell);
        try {
            checkChangeStatus = player.switchState( player.getConstructionManager() );
            assertTrue( !checkChangeStatus );
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(checkLose);
            checkLose = false;
        }

        // clear board
        board.clear();

        //>> to constructionTurn | already move | selected worker | can build
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: D       nearCornerCell3: D
        // edgeCell: W          nearEdgeCell1: B         nearEdgeCell2: B        nearEdgeCell3: BBB
        worker1.setChosen(ChooseType.NOT_CHOSEN);
        worker2.setChosen(ChooseType.CHOSEN);
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildDome();
        nearCornerCell3.buildDome();
        nearEdgeCell1.buildBlock();
        nearEdgeCell2.buildBlock();
        nearEdgeCell3.buildBlock();
        nearEdgeCell3.buildBlock();
        nearEdgeCell3.buildBlock();
        worker2.place(edgeCell);
        try {
            checkChangeStatus = player.switchState( player.getConstructionManager() );
            assertTrue( checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.CONSTRUCTION );
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(!checkLose);
            checkLose = false;
        }

        // clear board
        board.clear();

        /* already build*/
        player.getCard().setConstructionExecuted(true);
        player.getCard().getMyConstruction().decreaseConstructionLeft();

        //>> to constructionTurn | already move and build | selected worker | can't build
        // cornerCell: W        nearCornerCell1: B      nearCornerCell2:         nearCornerCell3:
        // edgeCell: W          nearEdgeCell1:          nearEdgeCell2:           nearEdgeCell3:
        worker1.setChosen(ChooseType.CHOSEN);
        worker2.setChosen(ChooseType.NOT_CHOSEN);
        worker1.place(cornerCell);
        nearCornerCell1.buildBlock();
        worker2.place(edgeCell);
        try {
            checkChangeStatus = player.switchState( player.getConstructionManager() );
            assertTrue( !checkChangeStatus );
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(checkLose);
            checkLose = false;
        }

        // clear board
        board.clear();


        //*** Artemis card ***//
        player.chooseCard("artemis");

        // already move
        player.getCard().setMovementExecuted(true);
        player.getCard().getMyMove().decreaseMovesLeft();
        player.getCard().getMyMove().setStartingPosition(nearCornerCell3);
        player.getCard().getMyMove().setLastMove( new Move(nearCornerCell3, cornerCell) );

        // chosen worker
        worker1.setChosen(ChooseType.CHOSEN);
        worker2.setChosen(ChooseType.NOT_CHOSEN);

        //>> to movementTurn | already move | selected worker | can't move
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: D      nearCornerCell3: D
        // edgeCell: W          nearEdgeCell1:          nearEdgeCell2:          nearEdgeCell3:
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildDome();
        nearCornerCell3.buildDome();
        worker2.place(edgeCell);
        try {
            checkChangeStatus = player.switchState( player.getMovementManager() );
            assertTrue( checkChangeStatus );   // player doesn't move so he/she changes or stays on MovementTurn
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(!checkLose);
            checkLose = false;
        }

        // clear board
        board.clear();

        //>> to movementTurn | already move | selected worker | can move
        // cornerCell: W        nearCornerCell1: B      nearCornerCell2: D      nearCornerCell3: D
        // edgeCell: W          nearEdgeCell1:          nearEdgeCell2:          nearEdgeCell3:
        worker1.place(cornerCell);
        nearCornerCell1.buildBlock();
        nearCornerCell2.buildDome();
        nearCornerCell3.buildDome();
        worker2.place(edgeCell);
        try {
            checkChangeStatus = player.switchState( player.getMovementManager() );
            assertTrue( checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.MOVEMENT );
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(!checkLose);
            checkLose = false;
        }

        // clear board
        board.clear();


        //*** Demeter card ***//
        player.chooseCard("demeter");

        // already move and build
        player.getCard().setMovementExecuted(true);
        player.getCard().getMyMove().decreaseMovesLeft();
        player.getCard().getMyMove().setLastMove( new Move( nearCornerCell3, cornerCell ) );
        player.getCard().setConstructionExecuted(true);
        player.getCard().getMyConstruction().decreaseConstructionLeft();
        player.getCard().getMyConstruction().setLastMove( new BuildMove(cornerCell, nearCornerCell3, PlaceableType.BLOCK) );

        // chosen worker
        worker1.setChosen(ChooseType.CHOSEN);
        worker2.setChosen(ChooseType.NOT_CHOSEN);

        //>> to constructionTurn | already move and build | selected worker | can't build
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: D      nearCornerCell3: BBBD
        // edgeCell: W          nearEdgeCell1:          nearEdgeCell2:          nearEdgeCell3:
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildDome();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildDome();
        worker2.place(edgeCell);
        try {
            checkChangeStatus = player.switchState( player.getConstructionManager() );
            assertTrue( !checkChangeStatus );       // ignored
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(checkLose);
            checkLose = false;
        }

        // clear board
        board.clear();

        //>> to constructionTurn | already move and build | selected worker | can build
        // cornerCell: W        nearCornerCell1: B      nearCornerCell2: D      nearCornerCell3: BBBD
        // edgeCell: W          nearEdgeCell1:          nearEdgeCell2:          nearEdgeCell3:
        worker1.place(cornerCell);
        nearCornerCell1.buildBlock();
        nearCornerCell2.buildDome();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildDome();
        worker2.place(edgeCell);
        try {
            checkChangeStatus = player.switchState( player.getConstructionManager() );
            assertTrue( checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.CONSTRUCTION );
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(!checkLose);
            checkLose = false;
        }

        // clear board
        board.clear();


        //*** Prometheus card ***//
        player.chooseCard("prometheus");

        // already move and build
        player.getCard().setMovementExecuted(true);
        player.getCard().getMyMove().decreaseMovesLeft();
        player.getCard().getMyMove().setLastMove( new Move( nearCornerCell3, cornerCell ) );
        player.getCard().setConstructionExecuted(true);
        player.getCard().getMyConstruction().decreaseConstructionLeft();
        player.getCard().getMyConstruction().setLastMove( new BuildMove(cornerCell, nearCornerCell3, PlaceableType.BLOCK) );

        // chosen worker
        worker1.setChosen(ChooseType.CHOSEN);
        worker2.setChosen(ChooseType.NOT_CHOSEN);

        //>> to constructionTurn | already move and build | selected worker | can build
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: D      nearCornerCell3: BB
        // edgeCell: W          nearEdgeCell1:          nearEdgeCell2:          nearEdgeCell3:
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildDome();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        worker2.place(edgeCell);
        try {
            checkChangeStatus = player.switchState( player.getConstructionManager() );
            assertTrue( checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.CONSTRUCTION );
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(!checkLose);
            checkLose = false;
        }

        // clear board
        board.clear();

        //>> to movementTurn | already move and build | selected worker | can move
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: D      nearCornerCell3:
        // edgeCell: W          nearEdgeCell1:          nearEdgeCell2:          nearEdgeCell3:
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildDome();
        worker2.place(edgeCell);
        try {
            checkChangeStatus = player.switchState( player.getMovementManager() );
            assertTrue( !checkChangeStatus );       // ignored
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(checkLose); // if a player build and move, he/she can return to movementTurn
            checkLose = false;
        }

        // clear board
        board.clear();

    }


    /**
     * Check if switchState( TurnManager ) can correctly evaluate the lose condition
     * of movement (checkForLostByMovement()/checkForWorkerLostByMovement())
     * and of construction (checkForLostByConstruction()/checkForWorkerLostByConstruction())
     * Methods used:        chooseCard(String)                  of  Player
     *                      registerWorker(Worker)              of  Player
     *                      getMovementManager()                of  Player
     *                      getConstructionManager()            of  Player
     *                      getTurnType()                       of  Player
     *                      getCard()                           of  Player
     *                      setExecutedMovement( boolean )      of  Card
     *                      setExecutedConstruction( boolean )  of  Card
     *                      getMyMove()                         of  Card
     *                      getMyConstruction()                 of  Card
     *                      resetForStart()                     of  Card
     *                      clear()                             of  Board
     *                      decreaseMovesLeft()                 of  MyMove
     *                      setStartingPosition( cell )         of  MyMove
     *                      setLastMove( Move )                 of  MyMove
     *                      decreaseConstructionLeft()          of  MyConstruction
     *                      setLastMove( BuildMove )            of  MyConstruction
     *                      buildBlock()                        of  Cell
     *                      buildDome()                         of  Cell
     *                      place(Cell)                         of  Worker
     *                      setChosen( ChosenType )             of  Worker
     *
     * White Box
     */
    @Test
    void switchStateWhite() {
        boolean checkChangeStatus = false;
        boolean checkLose = false;

        player.chooseCard("default" );
        player.registerWorker(worker1);
        player.registerWorker(worker2);



        //*** checkForLostByMovement ***//


        //>> to MovementTurn | not already move or build | all workers | can't move

        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: D       nearCornerCell3: D
        // edgeCell: W          nearEdgeCell1: BB       nearEdgeCell2: BB         nearEdgeCell3: BBB
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildDome();
        nearCornerCell3.buildDome();
        nearEdgeCell1.buildBlock();
        nearEdgeCell1.buildBlock();
        nearEdgeCell2.buildBlock();
        nearEdgeCell2.buildBlock();
        nearEdgeCell3.buildBlock();
        nearEdgeCell3.buildBlock();
        nearEdgeCell3.buildBlock();
        worker2.place(edgeCell);

        // chosen worker
        worker1.setChosen(ChooseType.CAN_BE_CHOSEN);
        worker2.setChosen(ChooseType.CAN_BE_CHOSEN);

        try {
            checkChangeStatus = player.switchState( player.getMovementManager() );
            assertTrue( !checkChangeStatus ); // ignored
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(checkLose);
            checkLose = false;
        }

        // clear board and card
        board.clear();
        player.getCard().resetForStart();


        //>> to MovementTurn | already build | only select workers | can move

        // cornerCell: W        nearCornerCell1:         nearCornerCell2:          nearCornerCell3:
        // edgeCell: W          nearEdgeCell1: D         nearEdgeCell2: D          nearEdgeCell3: D
        worker1.place(cornerCell);
        worker2.place(edgeCell);
        nearEdgeCell1.buildDome();
        nearEdgeCell2.buildDome();
        nearEdgeCell3.buildDome();

        // already build
        player.getCard().setConstructionExecuted(true);

        // chosen worker
        worker1.setChosen(ChooseType.CHOSEN);
        worker2.setChosen(ChooseType.NOT_CHOSEN);

        try {
            checkChangeStatus = player.switchState( player.getMovementManager() );
            assertTrue( checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.MOVEMENT);
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(!checkLose);
            checkLose = false;
        }


        // clear board and card
        board.clear();
        player.getCard().resetForStart();


        //>> to MovementTurn | already move and build | only select workers | can't move

        // cornerCell: W        nearCornerCell1:         nearCornerCell2:          nearCornerCell3:
        // edgeCell: W          nearEdgeCell1: D         nearEdgeCell2: D          nearEdgeCell3: D
        worker1.place(cornerCell);
        worker2.place(edgeCell);
        nearEdgeCell1.buildDome();
        nearEdgeCell2.buildDome();
        nearEdgeCell3.buildDome();

        // already move and build
        player.getCard().setConstructionExecuted(true);
        player.getCard().setConstructionExecuted(true);

        // chosen worker
        worker1.setChosen(ChooseType.NOT_CHOSEN);
        worker2.setChosen(ChooseType.CHOSEN);

        try {
            checkChangeStatus = player.switchState( player.getMovementManager() );
            assertTrue( !checkChangeStatus );       // ignored
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(checkLose);
            checkLose = false;
        }


        // clear board and card
        board.clear();
        player.getCard().resetForStart();

        //>> to MovementTurn | already move | only select workers | can't move

        // cornerCell: W        nearCornerCell1:         nearCornerCell2:          nearCornerCell3:
        // edgeCell: W          nearEdgeCell1: D         nearEdgeCell2: D          nearEdgeCell3: D
        worker1.place(cornerCell);
        worker2.place(edgeCell);
        nearEdgeCell1.buildDome();
        nearEdgeCell2.buildDome();
        nearEdgeCell3.buildDome();

        // already move
        player.getCard().setMovementExecuted(true);

        // chosen worker
        worker1.setChosen(ChooseType.NOT_CHOSEN);
        worker2.setChosen(ChooseType.CHOSEN);

        try {
            checkChangeStatus = player.switchState( player.getMovementManager() );
            assertTrue( checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.MOVEMENT);
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(!checkLose);
            checkLose = false;
        }

        // clear board and card
        board.clear();
        player.getCard().resetForStart();



        //*** checkForLostByConstruction ***//

        //>> to ConstructionTurn | not move | all workers | can't build

        // cornerCell: W        nearCornerCell1: D       nearCornerCell2: D        nearCornerCell3: BBBD
        // edgeCell: W          nearEdgeCell1: D         nearEdgeCell2: D          nearEdgeCell3: D
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildDome();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildDome();
        worker2.place(edgeCell);
        nearEdgeCell1.buildDome();
        nearEdgeCell2.buildDome();
        nearEdgeCell3.buildDome();

        // not move

        // chosen worker
        worker1.setChosen(ChooseType.CAN_BE_CHOSEN);
        worker2.setChosen(ChooseType.CAN_BE_CHOSEN);

        try {
            checkChangeStatus = player.switchState( player.getConstructionManager() );
            assertTrue( !checkChangeStatus );       // ignored
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(checkLose);
            checkLose = false;
        }


        // clear board and card
        board.clear();
        player.getCard().resetForStart();


        //>> to ConstructionTurn | already move | selected workers | can build

        // cornerCell: W        nearCornerCell1:         nearCornerCell2:          nearCornerCell3:
        // edgeCell: W          nearEdgeCell1: D         nearEdgeCell2: D          nearEdgeCell3: D
        worker1.place(cornerCell);
        worker2.place(edgeCell);
        nearEdgeCell1.buildDome();
        nearEdgeCell2.buildDome();
        nearEdgeCell3.buildDome();

        // already move
        player.getCard().setMovementExecuted(true);

        // chosen worker
        worker1.setChosen(ChooseType.CHOSEN);
        worker2.setChosen(ChooseType.NOT_CHOSEN);

        try {
            checkChangeStatus = player.switchState( player.getConstructionManager() );
            assertTrue( checkChangeStatus );
            assertTrue( player.getTurnType() == StateType.CONSTRUCTION);
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(!checkLose);
            checkLose = false;
        }

        // clear board and card
        board.clear();
        player.getCard().resetForStart();


        //>> to ConstructionTurn | already move | select workers | can't build

        // cornerCell: W        nearCornerCell1: D       nearCornerCell2: D        nearCornerCell3: BBBD
        // edgeCell: W          nearEdgeCell1:          nearEdgeCell2:           nearEdgeCell3:
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildDome();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildDome();
        worker2.place(edgeCell);

        // already move
        player.getCard().setMovementExecuted(true);

        // chosen worker
        worker1.setChosen(ChooseType.CHOSEN);
        worker2.setChosen(ChooseType.NOT_CHOSEN);

        try {
            checkChangeStatus = player.switchState( player.getConstructionManager() );
            assertTrue( !checkChangeStatus );       // ignored
        } catch (LoseException e) {
            checkLose = true;
        } finally {
            assertTrue(checkLose);
            checkLose = false;
        }

        // clear board and card
        board.clear();
        player.getCard().resetForStart();


    }

    /**
     * Check if chooseCard( Card ) can set the correctly value and
     * if getCard(), getMovementManager() and getConstructionManager() can read the correct value
     * Methods used:        getGodPower()           of  Card
     *                      getName()               of  GodPower
     *                      getEpithet()            of  GodPower
     *
     * Black Box and White Box
     */
    @Test
    void chooseCard() {
        Card card;
        // before initialization
        assertTrue( player.getCard() == null );
        assertTrue( player.getMovementManager() == null );
        assertTrue( player.getConstructionManager() == null );

        // after initialization
        player.chooseCard("default");
        card = player.getCard();
        assertTrue( card != null );
        assertTrue( card.getGodPower().getName().equals("Default") );
        assertTrue( card.getGodPower().getEpithet().equals("Default") );
        assertTrue( player.getMovementManager() != null );
        assertTrue( player.getConstructionManager() != null );
    }

    /**
     * Check if registerWorker( Worker ), clearWorker(), getWorker( Worker ), getWorkers() can correctly
     * add, remove and return Workers on Player
     * Methods used:        isEmpty()           of  List
     *                      contains()          of  List
     *
     * Black and White Box
     */
    @Test
    void registerAndClearAndGetWorkerAndWorkers() {
        List<Worker> workerList;

        // after initialization
        assertTrue( player.getWorker(worker1.getWorkerId()) == null );
        assertTrue( player.getWorker(worker2.getWorkerId()) == null );
        assertTrue( player.getWorkers().isEmpty() );

        // after register worker
        player.registerWorker(worker1);
        player.registerWorker(worker2);
        workerList = player.getWorkers();
        assertTrue( player.getWorker(worker1.getWorkerId()).equals(worker1) );
        assertTrue( player.getWorker(worker2.getWorkerId()).equals(worker2) );
        assertTrue( workerList.contains(worker1) );
        assertTrue( workerList.contains(worker2) );

        // after clear
        player.clearWorkers();
        assertTrue( player.getWorker(worker1.getWorkerId()) == null );
        assertTrue( player.getWorker(worker2.getWorkerId()) == null );
        assertTrue( player.getWorkers().isEmpty() );

    }

    /**
     * Check if chooseWorker( Worker ) can correctly change the Worker's status when it is possible
     * Methods used:        chooseCard(String)          of  Card
     *                      registerWorker(Worker)      of  Player
     *                      equals( obj )               of  Cell
     *                      getTop()                    of  Cell
     *                      removePLaceable()           of  Cell
     *                      buildBlock()                of  Cell
     *                      buildDome()                 of  Cell
     *                      position()                  of  Placeable
     *                      place(Cell)                 of  Worker
     *                      getChosenStatus()           of  Worker
     *                      setChosen(ChooseType)       of  Worker
     *
     * Black Box
     */
    @Test
    void chooseWorker() {
        Player otherPlayer = new Player("Player2");
        Worker otherWorker = new Worker(otherPlayer);
        boolean check;

        player.chooseCard("default");
        player.registerWorker(worker1);
        player.registerWorker(worker2);

        /* Player can't choose a Worker which it isn't his*/
        otherWorker.place(cornerCell);
        check = player.chooseWorker(otherWorker);
        assertTrue( !check );
        assertTrue( otherWorker.position().equals(cornerCell) );
        assertTrue( otherWorker.getChosenStatus() == ChooseType.CAN_BE_CHOSEN );
        assertTrue( worker1.getChosenStatus() == ChooseType.CAN_BE_CHOSEN );
        assertTrue( worker2.getChosenStatus() == ChooseType.CAN_BE_CHOSEN );

        // reset board
        while ( cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }


        /* Player can't choose a Worker if it can't move */
        worker1.place(cornerCell);
        otherWorker.place(nearCornerCell1);
        nearCornerCell2.buildDome();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        check = player.chooseWorker(worker1);
        assertTrue( !check );
        assertTrue( worker1.position().equals(cornerCell) );
        assertTrue( worker1.getChosenStatus() == ChooseType.CAN_BE_CHOSEN );
        assertTrue( worker2.getChosenStatus() == ChooseType.CAN_BE_CHOSEN );

        // reset board
        while ( cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }
        while ( nearCornerCell1.getTop() != null) {
            nearCornerCell1.removePlaceable();
        }
        while ( nearCornerCell2.getTop() != null) {
            nearCornerCell2.removePlaceable();
        }
        while ( nearCornerCell3.getTop() != null) {
            nearCornerCell3.removePlaceable();
        }


        /* Player can choose a Worker if it can move */
        worker1.place(cornerCell);
        otherWorker.place(nearCornerCell1);
        nearCornerCell2.buildDome();
        nearCornerCell3.buildBlock();
        check = player.chooseWorker(worker1);
        assertTrue( check );
        assertTrue( worker1.position().equals(cornerCell) );
        assertTrue( worker1.getChosenStatus() == ChooseType.CHOSEN );
        assertTrue( worker2.getChosenStatus() == ChooseType.NOT_CHOSEN );

        // reset board and Workers' status
        while ( cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }
        while ( nearCornerCell1.getTop() != null) {
            nearCornerCell1.removePlaceable();
        }
        while ( nearCornerCell2.getTop() != null) {
            nearCornerCell2.removePlaceable();
        }
        while ( nearCornerCell3.getTop() != null) {
            nearCornerCell3.removePlaceable();
        }
        worker1.setChosen(ChooseType.CAN_BE_CHOSEN);
        worker2.setChosen(ChooseType.CAN_BE_CHOSEN);



        //* GodPower Apollo/Minotaur *//
        player.chooseCard("apollo");

        /* Player can't choose a Worker if it can't move */
        worker1.place(cornerCell);
        worker2.place(nearCornerCell1);
        nearCornerCell2.buildDome();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        check = player.chooseWorker(worker1);
        assertTrue( !check );
        assertTrue( worker1.position().equals(cornerCell) );
        assertTrue( worker1.getChosenStatus() == ChooseType.CAN_BE_CHOSEN );
        assertTrue( worker2.getChosenStatus() == ChooseType.CAN_BE_CHOSEN );

        // reset board and Workers' status
        while ( cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }
        while ( nearCornerCell1.getTop() != null) {
            nearCornerCell1.removePlaceable();
        }
        while ( nearCornerCell2.getTop() != null) {
            nearCornerCell2.removePlaceable();
        }
        while ( nearCornerCell3.getTop() != null) {
            nearCornerCell3.removePlaceable();
        }
        worker1.setChosen(ChooseType.CAN_BE_CHOSEN);
        worker2.setChosen(ChooseType.CAN_BE_CHOSEN);


        /* Player can choose a Worker if there is an opponent's Worker on a near Cell */
        worker1.place(cornerCell);
        otherWorker.place(nearCornerCell1);
        nearCornerCell2.buildDome();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        check = player.chooseWorker(worker1);
        assertTrue( check );
        assertTrue( worker1.position().equals(cornerCell) );
        assertTrue( worker1.getChosenStatus() == ChooseType.CHOSEN );
        assertTrue( worker2.getChosenStatus() == ChooseType.NOT_CHOSEN );

        // reset board and Workers' status
        while ( cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }
        while ( nearCornerCell1.getTop() != null) {
            nearCornerCell1.removePlaceable();
        }
        while ( nearCornerCell2.getTop() != null) {
            nearCornerCell2.removePlaceable();
        }
        while ( nearCornerCell3.getTop() != null) {
            nearCornerCell3.removePlaceable();
        }
        worker1.setChosen(ChooseType.CAN_BE_CHOSEN);
        worker2.setChosen(ChooseType.CAN_BE_CHOSEN);

    }

    /**
     * Check if chooseWorker( Worker ) ( checkForWorkerLostByMovement )
     * can correctly change the Worker's status when it is possible
     * Methods used:        chooseCard(String)          of  Card
     *                      registerWorker(Worker)      of  Player
     *                      equals( obj )               of  Cell
     *                      getTop()                    of  Cell
     *                      removePLaceable()           of  Cell
     *                      buildBlock()                of  Cell
     *                      buildDome()                 of  Cell
     *                      position()                  of  Placeable
     *                      place(Cell)                 of  Worker
     *                      getChosenStatus()           of  Worker
     *                      setChosen(ChooseType)       of  Worker
     *
     * White Box
     */
    @Test
    void chooseWorkerWhite() {
        Player otherPlayer = new Player("Player2");
        Worker otherWorker = new Worker(otherPlayer);
        boolean check;

        player.chooseCard("default");
        player.registerWorker(worker1);
        player.registerWorker(worker2);


        /* Player can't choose a Worker if it can't move */
        worker1.place(cornerCell);
        otherWorker.place(nearCornerCell1);
        nearCornerCell2.buildDome();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        check = player.chooseWorker(worker1);
        assertTrue( !check );
        assertTrue( worker1.position().equals(cornerCell) );
        assertTrue( worker1.getChosenStatus() == ChooseType.CAN_BE_CHOSEN );
        assertTrue( worker2.getChosenStatus() == ChooseType.CAN_BE_CHOSEN );

        // reset board and Workers' status
        while ( cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }
        while ( nearCornerCell1.getTop() != null) {
            nearCornerCell1.removePlaceable();
        }
        while ( nearCornerCell2.getTop() != null) {
            nearCornerCell2.removePlaceable();
        }
        while ( nearCornerCell3.getTop() != null) {
            nearCornerCell3.removePlaceable();
        }
        worker1.setChosen(ChooseType.CAN_BE_CHOSEN);
        worker2.setChosen(ChooseType.CAN_BE_CHOSEN);


        /* Player can't choose a Worker if it can move but the Worker is an opponent's Worker */
        otherWorker.place(cornerCell);
        check = player.chooseWorker(otherWorker);
        assertTrue( !check );
        assertTrue( otherWorker.position().equals(cornerCell) );
        assertTrue( otherWorker.getChosenStatus() == ChooseType.CAN_BE_CHOSEN);
        assertTrue( worker1.getChosenStatus() == ChooseType.CAN_BE_CHOSEN );
        assertTrue( worker2.getChosenStatus() == ChooseType.CAN_BE_CHOSEN );

        // reset board and Workers' status
        while ( cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }
        while ( nearCornerCell1.getTop() != null) {
            nearCornerCell1.removePlaceable();
        }
        while ( nearCornerCell2.getTop() != null) {
            nearCornerCell2.removePlaceable();
        }
        while ( nearCornerCell3.getTop() != null) {
            nearCornerCell3.removePlaceable();
        }
        worker1.setChosen(ChooseType.CAN_BE_CHOSEN);
        worker2.setChosen(ChooseType.CAN_BE_CHOSEN);


        /* Player can't choose a Worker if its status is NOT_CHOSEN */
        worker1.setChosen(ChooseType.NOT_CHOSEN);
        worker1.place(cornerCell);
        otherWorker.place(nearCornerCell1);
        nearCornerCell2.buildDome();
        nearCornerCell3.buildBlock();
        check = player.chooseWorker(worker1);
        assertTrue( !check );
        assertTrue( worker1.position().equals(cornerCell) );
        assertTrue( worker1.getChosenStatus() == ChooseType.NOT_CHOSEN );
        assertTrue( worker2.getChosenStatus() == ChooseType.CAN_BE_CHOSEN );

        // reset board and Workers' status
        while ( cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }
        while ( nearCornerCell1.getTop() != null) {
            nearCornerCell1.removePlaceable();
        }
        while ( nearCornerCell2.getTop() != null) {
            nearCornerCell2.removePlaceable();
        }
        while ( nearCornerCell3.getTop() != null) {
            nearCornerCell3.removePlaceable();
        }
        worker1.setChosen(ChooseType.CAN_BE_CHOSEN);
        worker2.setChosen(ChooseType.CAN_BE_CHOSEN);


        /* Player can choose a Worker if its status is CAN_BE_CHOSEN */
        worker1.place(cornerCell);
        otherWorker.place(nearCornerCell1);
        nearCornerCell2.buildDome();
        nearCornerCell3.buildBlock();
        check = player.chooseWorker(worker1);
        assertTrue( check );
        assertTrue( worker1.position().equals(cornerCell) );
        assertTrue( worker1.getChosenStatus() == ChooseType.CHOSEN );
        assertTrue( worker2.getChosenStatus() == ChooseType.NOT_CHOSEN );

        // reset board and Workers' status
        while ( cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }
        while ( nearCornerCell1.getTop() != null) {
            nearCornerCell1.removePlaceable();
        }
        while ( nearCornerCell2.getTop() != null) {
            nearCornerCell2.removePlaceable();
        }
        while ( nearCornerCell3.getTop() != null) {
            nearCornerCell3.removePlaceable();
        }
        worker1.setChosen(ChooseType.CAN_BE_CHOSEN);
        worker2.setChosen(ChooseType.CAN_BE_CHOSEN);


        /* Player can choose a Worker if its status is CHOSEN */
        worker1.setChosen(ChooseType.CHOSEN);
        worker1.place(cornerCell);
        otherWorker.place(nearCornerCell1);
        nearCornerCell2.buildDome();
        nearCornerCell3.buildBlock();
        check = player.chooseWorker(worker1);
        assertTrue( check );
        assertTrue( worker1.position().equals(cornerCell) );
        assertTrue( worker1.getChosenStatus() == ChooseType.CHOSEN );
        assertTrue( worker2.getChosenStatus() != ChooseType.NOT_CHOSEN );

        // reset board and Workers' status
        while ( cornerCell.getTop() != null) {
            cornerCell.removePlaceable();
        }
        while ( nearCornerCell1.getTop() != null) {
            nearCornerCell1.removePlaceable();
        }
        while ( nearCornerCell2.getTop() != null) {
            nearCornerCell2.removePlaceable();
        }
        while ( nearCornerCell3.getTop() != null) {
            nearCornerCell3.removePlaceable();
        }
        worker1.setChosen(ChooseType.CAN_BE_CHOSEN);
        worker2.setChosen(ChooseType.CAN_BE_CHOSEN);
    }

    /**
     * Check if getNickname() can return the correct value after initialization
     *
     * Black Box and White Box
     */
    @Test
    void getNickname() {

        assertTrue( player.getNickname().equals("Player1") );

    }

    /**
     * Check if setChallenger( boolean ) and isChallenger() can set and read the correct value
     *
     * Black Box and White Box
     */
    @Test
    void setChallengerAndIsChallenger() {

        //after initialization
        assertTrue( player.isChallenger() == false );

        //after set
        player.setChallenger( true );
        assertTrue( player.isChallenger() == true );
    }

    /**
     * Check if setStartingPlayer( boolean ) and isStartingPlayer() can set and read the correct value
     *
     * Black Box and White Box
     */
    @Test
    void setStartingPlayerAndIsStartingPlayer() {

        //after initialization
        assertTrue( !player.isStartingPlayer() );

        //after set
        player.setStartingPlayer( true );
        assertTrue( player.isStartingPlayer() );
    }

    /**
     * Check if setPlaying( boolean ) and isPlaying() can set and read the correct value
     *
     * Black Box and White Box
     */
    @Test
    void setPlayingAndIsPlaying() {

        //after initialization
        assertTrue( !player.isPlaying() );

        //after set
        player.setPlaying( true );
        assertTrue( player.isPlaying() );
    }

    /**
     * Check if setTurn( TurnType ) and getTurnType() can correctly set and get the TurnType
     *
     * Black Box and White Box
     */
    @Test
    void setTurnAndGetTurnType() {

        // after initialization
        assertTrue(player.getTurnType() == StateType.NONE );


        //*** after setTurn() ***//

        // MOVEMENT
        player.setTurn(StateType.MOVEMENT);
        assertTrue( player.getTurnType() == StateType.MOVEMENT );

        // CONSTRUCTION
        player.setTurn(StateType.CONSTRUCTION);
        assertTrue( player.getTurnType() == StateType.CONSTRUCTION );

        // ANY (default is NONE)
        player.setTurn(StateType.ANY);
        assertTrue( player.getTurnType() == StateType.NONE );

        // NONE
        player.setTurn(StateType.NONE);
        assertTrue( player.getTurnType() == StateType.NONE );

    }

}