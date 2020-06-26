package it.polimi.ingsw.model.card.adversaryMove;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.IslandBoard;
import it.polimi.ingsw.model.card.CardParser;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.GodPower;
import it.polimi.ingsw.model.card.move.MyMove;
import it.polimi.ingsw.model.exceptions.LoseException;
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
 * Unit test for AdversaryMove class, aimed to verify it works properly
 *
 * @author Marco
 */
class AdversaryMoveTest {
    Board board;
    boolean checkError;
    Player athenaPlayer;
    Player opponentPlayer;
    Worker athenaWorker;
    Worker opponentWorker;

    GodPower human;
    Card humanCard;

    GodPower athena;
    Card athenaCard;


    /**
     * Initialization before method's test
     */
    @BeforeEach
    void setUp() {

        board = new IslandBoard();
        checkError = false;

        athenaPlayer = new Player("Athena's disciple");
        opponentPlayer = new Player("Opponent");
        athenaWorker = new Worker(athenaPlayer);
        opponentWorker = new Worker(opponentPlayer);

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
        athena = new GodPower();
        athena.setName("Athena");
        athena.setEpithet("Goddess of Wisdom");
        athena.setDescription("A");
        athena.setMovementsLeft(1);
        athena.setConstructionLeft(1);
        athena.setMustObey(true);
        athena.setHotLastMoveDirection(LevelDirection.UP);
        athena.setForceOpponentInto(FloorDirection.NONE);
        athena.setDeniedDirection(LevelDirection.NONE);
        athena.setActiveOnOpponentMovement(true);
        athena.setOpponentDeniedDirection(LevelDirection.UP);
        athenaCard = new Card(  athena,
                                CardParser.getMoveCheckers(athena),
                                CardParser.getMoveExecutor(athena),
                                CardParser.getBuildCheckers(athena),
                                CardParser.getBuildExecutor(athena),
                                CardParser.getWinCheckers(athena),
                                CardParser.getAdversaryMoveCheckers(athena));


    }

    /**
     * Reset attribute after method's test
     */
    @AfterEach
    void tearDown() {

        board = null;
        athenaPlayer = null;
        opponentPlayer = null;
        athenaWorker = null;
        opponentWorker = null;

        human = null;
        humanCard = null;

        athena = null;
        athenaCard = null;


    }

    /**
     * Check if Athena's Power can correctly check opponent's Move ( without move )
     * Methods used:        getMyMove()                     of  Card
     *                      getAdversaryMove()              of  Card
     *                      setLastMove( Move )             of  MyMove
     *                      getCellAt( int, int )           of  Board
     *                      getHeigth()                     of  Cell
     *                      getTop()                        of  Cell
     *                      getPlaceableAt( int )           of  Cell
     *                      repOk()                         of  Cell
     *                      removePlaceable()               of  Cell
     *                      buildBlock()                    of  Cell
     *                      position()                      of  Placeable
     *                      isBlock()                       of  Placeable/Block
     *                      place( Cell)                    of  Worker
     *
     * Black Box
     */
    @Test
    void checkMoveAthenaBlack() {
        MyMove athenaMyMove = athenaCard.getMyMove();
        AdversaryMove athenaAdversaryMove = athenaCard.getAdversaryMove();
        Move athenaMove;
        Move opponentMove;
        Cell cellA1;
        Cell cellA2;
        Cell cellO1;
        Cell cellO2;
        boolean checkAdversary;
        boolean checkLose = false;

        try {
            cellA1 = board.getCellAt(1, 2);
            cellA2 = board.getCellAt(1, 3);
            cellO1 = board.getCellAt(3, 2);
            cellO2 = board.getCellAt(3, 3);


            /* opponent's worker is the first to move: it can go up */
            cellO2.buildBlock();
            opponentWorker.place(cellO1);
            athenaWorker.place(cellA1);
            opponentMove = new Move(opponentWorker.position(), cellO2);
            try {
                checkAdversary = athenaAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue(checkAdversary);
                assertTrue(cellA1.getHeigth() == 1);
                assertTrue(cellA2.getHeigth() == 0);
                assertTrue(cellO1.getHeigth() == 1);
                assertTrue(cellO2.getHeigth() == 1);
                assertTrue(cellA1.getTop().equals(athenaWorker));
                assertTrue(cellA2.getTop() == null);
                assertTrue(cellO1.getTop().equals(opponentWorker));
                assertTrue(cellO2.getTop().isBlock());
                assertTrue(cellA1.repOk());
                assertTrue(cellA2.repOk());
                assertTrue(cellO1.repOk());
                assertTrue(cellO2.repOk());
                assertTrue(athenaWorker.position().equals(cellA1));
                assertTrue(opponentWorker.position().equals(cellO1));
            } catch (LoseException l) {
                checkLose = true;
            } finally {
                assertTrue(!checkLose);
                checkLose = false;
            }

            // clear Cells
            while (cellA1.getTop() != null) {
                cellA1.removePlaceable();
            }
            while (cellA2.getTop() != null) {
                cellA2.removePlaceable();
            }
            while (cellO1.getTop() != null) {
                cellO1.removePlaceable();
            }
            while (cellO2.getTop() != null) {
                cellO2.removePlaceable();
            }


            /* opponent's worker can go up if Athena stay at same level*/
            cellA1.buildBlock();
            cellA2.buildBlock();
            cellO2.buildBlock();
            opponentWorker.place(cellO1);
            athenaWorker.place(cellA1);
            athenaMove = new Move(athenaWorker.position(), cellA2);
            athenaWorker.place(cellA2);
            athenaMyMove.setLastMove(athenaMove);
            opponentMove = new Move(opponentWorker.position(), cellO2);
            try {
                checkAdversary = athenaAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue(checkAdversary);
                assertTrue(cellA1.getHeigth() == 1);
                assertTrue(cellA2.getHeigth() == 2);
                assertTrue(cellO1.getHeigth() == 1);
                assertTrue(cellO2.getHeigth() == 1);
                assertTrue(cellA1.getTop().isBlock());
                assertTrue(cellA2.getTop().equals(athenaWorker));
                assertTrue(cellA2.getPlaceableAt(0).isBlock());
                assertTrue(cellO1.getTop().equals(opponentWorker));
                assertTrue(cellO2.getTop().isBlock());
                assertTrue(cellA1.repOk());
                assertTrue(cellA2.repOk());
                assertTrue(cellO1.repOk());
                assertTrue(cellO2.repOk());
                assertTrue(athenaWorker.position().equals(cellA2));
                assertTrue(opponentWorker.position().equals(cellO1));
            } catch (LoseException l) {
                checkLose = true;
            } finally {
                assertTrue(!checkLose);
                checkLose = false;
            }

            // clear Cells
            while (cellA1.getTop() != null) {
                cellA1.removePlaceable();
            }
            while (cellA2.getTop() != null) {
                cellA2.removePlaceable();
            }
            while (cellO1.getTop() != null) {
                cellO1.removePlaceable();
            }
            while (cellO2.getTop() != null) {
                cellO2.removePlaceable();
            }


            /* opponent's worker can go up if Athena go down*/
            cellA1.buildBlock();
            cellA1.buildBlock();
            cellO2.buildBlock();
            opponentWorker.place(cellO1);
            athenaWorker.place(cellA1);
            athenaMove = new Move(athenaWorker.position(), cellA2);
            athenaWorker.place(cellA2);
            athenaMyMove.setLastMove(athenaMove);
            opponentMove = new Move(opponentWorker.position(), cellO2);
            try {
                checkAdversary = athenaAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue(checkAdversary);
                assertTrue(cellA1.getHeigth() == 2);
                assertTrue(cellA2.getHeigth() == 1);
                assertTrue(cellO1.getHeigth() == 1);
                assertTrue(cellO2.getHeigth() == 1);
                assertTrue(cellA1.getTop().isBlock());
                assertTrue(cellA1.getPlaceableAt(0).isBlock());
                assertTrue(cellA2.getTop().equals(athenaWorker));
                assertTrue(cellO1.getTop().equals(opponentWorker));
                assertTrue(cellO2.getTop().isBlock());
                assertTrue(cellA1.repOk());
                assertTrue(cellA2.repOk());
                assertTrue(cellO1.repOk());
                assertTrue(cellO2.repOk());
                assertTrue(athenaWorker.position().equals(cellA2));
                assertTrue(opponentWorker.position().equals(cellO1));
            } catch (LoseException l) {
                checkLose = true;
            } finally {
                assertTrue(!checkLose);
                checkLose = false;
            }

            // clear Cells
            while (cellA1.getTop() != null) {
                cellA1.removePlaceable();
            }
            while (cellA2.getTop() != null) {
                cellA2.removePlaceable();
            }
            while (cellO1.getTop() != null) {
                cellO1.removePlaceable();
            }
            while (cellO2.getTop() != null) {
                cellO2.removePlaceable();
            }


            //* ATHENA GO UP*//

            cellA2.buildBlock();
            athenaWorker.place(cellA1);
            athenaMove = new Move(athenaWorker.position(), cellA2);
            athenaWorker.place(cellA2);
            athenaMyMove.setLastMove(athenaMove);

            /* opponent's Worker can stay at same level */
            cellO1.buildBlock();
            cellO2.buildBlock();
            opponentWorker.place(cellO1);
            opponentMove = new Move(opponentWorker.position(), cellO2);
            try {
                checkAdversary = athenaAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue(checkAdversary);
                assertTrue(cellA1.getHeigth() == 0);
                assertTrue(cellA2.getHeigth() == 2);
                assertTrue(cellO1.getHeigth() == 2);
                assertTrue(cellO2.getHeigth() == 1);
                assertTrue(cellA1.getTop() == null);
                assertTrue(cellA2.getTop().equals(athenaWorker));
                assertTrue(cellA2.getPlaceableAt(0).isBlock());
                assertTrue(cellO1.getTop().equals(opponentWorker));
                assertTrue(cellO1.getPlaceableAt(0).isBlock());
                assertTrue(cellO2.getTop().isBlock());
                assertTrue(cellA1.repOk());
                assertTrue(cellA2.repOk());
                assertTrue(cellO1.repOk());
                assertTrue(cellO2.repOk());
                assertTrue(athenaWorker.position().equals(cellA2));
                assertTrue(opponentWorker.position().equals(cellO1));
            } catch (LoseException l) {
                checkLose = true;
            } finally {
                assertTrue(!checkLose);
                checkLose = false;
            }

            // clear Cells O1 and O2
            while (cellO1.getTop() != null) {
                cellO1.removePlaceable();
            }
            while (cellO2.getTop() != null) {
                cellO2.removePlaceable();
            }

            /* opponent's Worker can go down */
            cellO1.buildBlock();
            cellO1.buildBlock();
            cellO2.buildBlock();
            opponentWorker.place(cellO1);
            opponentMove = new Move(opponentWorker.position(), cellO2);
            try {
                checkAdversary = athenaAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue(checkAdversary);
                assertTrue(cellA1.getHeigth() == 0);
                assertTrue(cellA2.getHeigth() == 2);
                assertTrue(cellO1.getHeigth() == 3);
                assertTrue(cellO2.getHeigth() == 1);
                assertTrue(cellA1.getTop() == null);
                assertTrue(cellA2.getTop().equals(athenaWorker));
                assertTrue(cellA2.getPlaceableAt(0).isBlock());
                assertTrue(cellO1.getTop().equals(opponentWorker));
                assertTrue(cellO1.getPlaceableAt(0).isBlock());
                assertTrue(cellO1.getPlaceableAt(1).isBlock());
                assertTrue(cellO2.getTop().isBlock());
                assertTrue(cellA1.repOk());
                assertTrue(cellA2.repOk());
                assertTrue(cellO1.repOk());
                assertTrue(cellO2.repOk());
                assertTrue(athenaWorker.position().equals(cellA2));
                assertTrue(opponentWorker.position().equals(cellO1));
            } catch (LoseException l) {
                checkLose = true;
            } finally {
                assertTrue(!checkLose);
                checkLose = false;
            }

            // clear Cells O1 and O2
            while (cellO1.getTop() != null) {
                cellO1.removePlaceable();
            }
            while (cellO2.getTop() != null) {
                cellO2.removePlaceable();
            }

            /* opponent's Worker can not go up */
            cellO1.buildBlock();
            cellO2.buildBlock();
            cellO2.buildBlock();
            opponentWorker.place(cellO1);
            opponentMove = new Move(opponentWorker.position(), cellO2);
            try {
                checkAdversary = athenaAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue(!checkAdversary); // ignored
            } catch (LoseException l) {
                checkLose = true;
            } finally {
                assertTrue(checkLose);
                checkLose = false;
            }
            assertTrue(cellA1.getHeigth() == 0);
            assertTrue(cellA2.getHeigth() == 2);
            assertTrue(cellO1.getHeigth() == 2);
            assertTrue(cellO2.getHeigth() == 2);
            assertTrue(cellA1.getTop() == null);
            assertTrue(cellA2.getTop().equals(athenaWorker));
            assertTrue(cellA2.getPlaceableAt(0).isBlock());
            assertTrue(cellO1.getTop().equals(opponentWorker));
            assertTrue(cellO1.getPlaceableAt(0).isBlock());
            assertTrue(cellO2.getTop().isBlock());
            assertTrue(cellO1.getPlaceableAt(0).isBlock());
            assertTrue(cellA1.repOk());
            assertTrue(cellA2.repOk());
            assertTrue(cellO1.repOk());
            assertTrue(cellO2.repOk());
            assertTrue(athenaWorker.position().equals(cellA2));
            assertTrue(opponentWorker.position().equals(cellO1));

            // clear Cells
            while (cellA1.getTop() != null) {
                cellA1.removePlaceable();
            }
            while (cellA2.getTop() != null) {
                cellA2.removePlaceable();
            }
            while (cellO1.getTop() != null) {
                cellO1.removePlaceable();
            }
            while (cellO2.getTop() != null) {
                cellO2.removePlaceable();
            }

        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue(!checkError);


    }


    /**
     * Check if with the default configure and special configure of checkAdversaryMoveList returned from CardParser
     * checkMyMove() can correctly check the victory
     * ( white test of second lambda expression of CardParser's getAdversaryMoveCheckers())
     * Methods used:        getMyMove()                     of  Card
     *                      getAdversaryMove()              of  Card
     *                      setLastMove( Move )             of  MyMove
     *                      getCellAt( int, int )           of  Board
     *                      getHeigth()                     of  Cell
     *                      getTop()                        of  Cell
     *                      getPlaceableAt( int )           of  Cell
     *                      repOk()                         of  Cell
     *                      buildBlock()                    of  Cell
     *                      position()                      of  Placeable
     *                      isBlock()                       of  Placeable/Block
     *                      place( Cell)                    of  Worker
     *
     * White Box
     */
    @Test
    void checkMoveWhite() {

        GodPower kindAthenaPower = new GodPower(); // have the same power of Athena's card but with "MustObey" = false
        kindAthenaPower.setName("kindAthena");
        kindAthenaPower.setEpithet("kindAthena");
        kindAthenaPower.setDescription("KA");
        kindAthenaPower.setMovementsLeft(1);
        kindAthenaPower.setConstructionLeft(1);
        kindAthenaPower.setMustObey(false);  // "MustObey" = false
        kindAthenaPower.setHotLastMoveDirection(LevelDirection.UP);
        kindAthenaPower.setForceOpponentInto(FloorDirection.NONE);
        kindAthenaPower.setDeniedDirection(LevelDirection.NONE);
        kindAthenaPower.setActiveOnOpponentMovement(true);
        kindAthenaPower.setOpponentDeniedDirection(LevelDirection.UP);
        Card kindAthenaCard = new Card( kindAthenaPower,
                                        CardParser.getMoveCheckers(kindAthenaPower),
                                        CardParser.getMoveExecutor(kindAthenaPower),
                                        CardParser.getBuildCheckers(kindAthenaPower),
                                        CardParser.getBuildExecutor(kindAthenaPower),
                                        CardParser.getWinCheckers(kindAthenaPower),
                                        CardParser.getAdversaryMoveCheckers(kindAthenaPower));

        Player player = new Player("player");
        MyMove myMove;
        AdversaryMove adversaryMove;
        Move move;
        Move opponentMove;
        Cell startPlayerCell;
        Cell highPlayerCell;
        Cell lowPlayerCell;
        Cell startOpponentCell;
        Cell highOpponentCell;
        Cell lowOpponentCell;
        Worker worker = new Worker(player);
        Worker opponentWorker = new Worker(opponentPlayer);
        boolean check;
        boolean checkLose = false;

        try {
            highPlayerCell = board.getCellAt(1,1);
            startPlayerCell = board.getCellAt(1,2);
            lowPlayerCell = board.getCellAt(1,3);
            highOpponentCell = board.getCellAt(3,1);
            startOpponentCell = board.getCellAt(3,2);
            lowOpponentCell = board.getCellAt(3,3);
            startPlayerCell.buildBlock();
            startOpponentCell.buildBlock();
            highPlayerCell.buildBlock();
            highPlayerCell.buildBlock();
            highOpponentCell.buildBlock();
            highOpponentCell.buildBlock();
            opponentWorker.place( startOpponentCell );


            //***god without power***//

            myMove = humanCard.getMyMove();
            adversaryMove = humanCard.getAdversaryMove();

            /* opponent's Worker can move  where it wants (before worker go up) */

            worker.place(startPlayerCell);
            move = new Move(worker.position(), highPlayerCell);
            worker.place(highPlayerCell);
            myMove.setLastMove(move);
            opponentMove = new Move( opponentWorker.position(), highOpponentCell);
            try {
                check = adversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue( check );
                assertTrue( highPlayerCell.getHeigth() == 3);
                assertTrue( startPlayerCell.getHeigth() == 1);
                assertTrue( lowPlayerCell.getHeigth() == 0);
                assertTrue( highOpponentCell.getHeigth() == 2);
                assertTrue( startOpponentCell.getHeigth() == 2);
                assertTrue( lowOpponentCell.getHeigth() == 0);
                assertTrue( highPlayerCell.getTop().equals( worker ) );
                assertTrue( highPlayerCell.getPlaceableAt(1).isBlock() );
                assertTrue( highPlayerCell.getPlaceableAt(0).isBlock() );
                assertTrue( startPlayerCell.getTop().isBlock() );
                assertTrue( lowPlayerCell.getTop() == null );
                assertTrue( highOpponentCell.getTop().isBlock() );
                assertTrue( highOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( startOpponentCell.getTop().equals( opponentWorker ) );
                assertTrue( startOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( lowOpponentCell.getTop() == null );
                assertTrue( highPlayerCell.repOk() );
                assertTrue( startPlayerCell.repOk() );
                assertTrue( lowPlayerCell.repOk() );
                assertTrue( highOpponentCell.repOk() );
                assertTrue( startOpponentCell.repOk() );
                assertTrue( lowOpponentCell.repOk() );
                assertTrue( worker.position().equals( highPlayerCell ));
                assertTrue( opponentWorker.position().equals( startOpponentCell ));
            } catch (LoseException l) {
                checkLose = true;
            } finally {
                assertTrue(!checkLose);
                checkLose = false;
            }

            //***god with opponent's up movement effect (Athena) ***//

            myMove = athenaCard.getMyMove();
            adversaryMove = athenaCard.getAdversaryMove();

            //>> opponent is the first to move: he/she can move where he/she wants
            worker.place(startPlayerCell);
            opponentWorker.place(startOpponentCell);
            opponentMove = new Move( opponentWorker.position(), highOpponentCell);
            try {
                check = adversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue( check );
                assertTrue( highPlayerCell.getHeigth() == 2);
                assertTrue( startPlayerCell.getHeigth() == 2);
                assertTrue( lowPlayerCell.getHeigth() == 0);
                assertTrue( highOpponentCell.getHeigth() == 2);
                assertTrue( startOpponentCell.getHeigth() == 2);
                assertTrue( lowOpponentCell.getHeigth() == 0);
                assertTrue( highPlayerCell.getTop().isBlock() );
                assertTrue( highPlayerCell.getPlaceableAt(0).isBlock() );
                assertTrue( startPlayerCell.getTop().equals( worker) );
                assertTrue( startPlayerCell.getPlaceableAt(0).isBlock() );
                assertTrue( lowPlayerCell.getTop() == null );
                assertTrue( highOpponentCell.getTop().isBlock() );
                assertTrue( highOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( startOpponentCell.getTop().equals( opponentWorker) );
                assertTrue( startOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( lowOpponentCell.getTop() == null );
                assertTrue( highPlayerCell.repOk() );
                assertTrue( startPlayerCell.repOk() );
                assertTrue( lowPlayerCell.repOk() );
                assertTrue( highOpponentCell.repOk() );
                assertTrue( startOpponentCell.repOk() );
                assertTrue( lowOpponentCell.repOk() );
                assertTrue( worker.position().equals( startPlayerCell ));
                assertTrue( opponentWorker.position().equals( startOpponentCell ));
            } catch (LoseException l) {
                checkLose = true;
            } finally {
                assertTrue(!checkLose);
                checkLose = false;
            }


            //>> player moves his/her worker with a LevelDirection != hotLastMoveDirection: opponent's Worker can move where he/she wants

            worker.place(startPlayerCell);
            opponentWorker.place(startOpponentCell);

            move = new Move( worker.position(), lowPlayerCell);
            worker.place(lowPlayerCell);
            myMove.setLastMove(move);

            opponentMove = new Move( opponentWorker.position(), highOpponentCell);
            try {
                check = adversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue( check );
                assertTrue( highPlayerCell.getHeigth() == 2);
                assertTrue( startPlayerCell.getHeigth() == 1);
                assertTrue( lowPlayerCell.getHeigth() == 1);
                assertTrue( highOpponentCell.getHeigth() == 2);
                assertTrue( startOpponentCell.getHeigth() == 2);
                assertTrue( lowOpponentCell.getHeigth() == 0);
                assertTrue( highPlayerCell.getTop().isBlock() );
                assertTrue( highPlayerCell.getPlaceableAt(0).isBlock() );
                assertTrue( startPlayerCell.getTop().isBlock() );
                assertTrue( lowPlayerCell.getTop().equals( worker ) );
                assertTrue( highOpponentCell.getTop().isBlock() );
                assertTrue( highOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( startOpponentCell.getTop().equals( opponentWorker) );
                assertTrue( startOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( lowOpponentCell.getTop() == null );
                assertTrue( highPlayerCell.repOk() );
                assertTrue( startPlayerCell.repOk() );
                assertTrue( lowPlayerCell.repOk() );
                assertTrue( highOpponentCell.repOk() );
                assertTrue( startOpponentCell.repOk() );
                assertTrue( lowOpponentCell.repOk() );
                assertTrue( worker.position().equals( lowPlayerCell ));
                assertTrue( opponentWorker.position().equals( startOpponentCell ));
            } catch (LoseException l) {
                checkLose = true;
            } finally {
                assertTrue(!checkLose);
                checkLose = false;
            }


            //>> player moves his/her worker with a LevelDirection == hotLastMoveDirection and
            //   opponentWorker moves with a LevelDirection != opponentDeniedDirection:
            //   opponent's Worker can move

            worker.place(startPlayerCell);
            opponentWorker.place(startOpponentCell);

            move = new Move(worker.position(), highPlayerCell);
            worker.place(highPlayerCell);
            myMove.setLastMove(move);
            opponentMove = new Move( opponentWorker.position(), lowOpponentCell );
            try {
                check = adversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue( check );
                assertTrue( highPlayerCell.getHeigth() == 3);
                assertTrue( startPlayerCell.getHeigth() == 1);
                assertTrue( lowPlayerCell.getHeigth() == 0);
                assertTrue( highOpponentCell.getHeigth() == 2);
                assertTrue( startOpponentCell.getHeigth() == 2);
                assertTrue( lowOpponentCell.getHeigth() == 0);
                assertTrue( highPlayerCell.getTop().equals( worker ) );
                assertTrue( highPlayerCell.getPlaceableAt(0).isBlock() );
                assertTrue( highPlayerCell.getPlaceableAt(1).isBlock() );
                assertTrue( startPlayerCell.getTop().isBlock() );
                assertTrue( lowPlayerCell.getTop() == null );
                assertTrue( highOpponentCell.getTop().isBlock() );
                assertTrue( highOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( startOpponentCell.getTop().equals( opponentWorker) );
                assertTrue( startOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( lowOpponentCell.getTop() == null );
                assertTrue( highPlayerCell.repOk() );
                assertTrue( startPlayerCell.repOk() );
                assertTrue( lowPlayerCell.repOk() );
                assertTrue( highOpponentCell.repOk() );
                assertTrue( startOpponentCell.repOk() );
                assertTrue( lowOpponentCell.repOk() );
                assertTrue( worker.position().equals( highPlayerCell ));
                assertTrue( opponentWorker.position().equals( startOpponentCell ));
            } catch (LoseException l) {
                checkLose = true;
            } finally {
                assertTrue(!checkLose);
                checkLose = false;
            }

            //>> player moves his/her worker with a LevelDirection == hotLastMoveDirection and
            //   opponentWorker moves with a LevelDirection != opponentDeniedDirection (MustObey == true):
            //   opponent loses

            worker.place(startPlayerCell);
            opponentWorker.place(startOpponentCell);

            move = new Move(worker.position(), highPlayerCell);
            worker.place(highPlayerCell);
            myMove.setLastMove(move);
            opponentMove = new Move( opponentWorker.position(), highOpponentCell );
            try {
                check = adversaryMove.checkMove(opponentMove, opponentWorker);
            } catch (LoseException l) {
                checkLose = true;
            } finally {
                assertTrue( checkLose );
                checkLose = false;
            }
            assertTrue( highPlayerCell.getHeigth() == 3);
            assertTrue( startPlayerCell.getHeigth() == 1);
            assertTrue( lowPlayerCell.getHeigth() == 0);
            assertTrue( highOpponentCell.getHeigth() == 2);
            assertTrue( startOpponentCell.getHeigth() == 2);
            assertTrue( lowOpponentCell.getHeigth() == 0);
            assertTrue( highPlayerCell.getTop().equals( worker ) );
            assertTrue( highPlayerCell.getPlaceableAt(0).isBlock() );
            assertTrue( highPlayerCell.getPlaceableAt(1).isBlock() );
            assertTrue( startPlayerCell.getTop().isBlock() );
            assertTrue( lowPlayerCell.getTop() == null );
            assertTrue( highOpponentCell.getTop().isBlock() );
            assertTrue( highOpponentCell.getPlaceableAt(0).isBlock() );
            assertTrue( startOpponentCell.getTop().equals( opponentWorker ) );
            assertTrue( startOpponentCell.getPlaceableAt(0).isBlock() );
            assertTrue( lowOpponentCell.getTop() == null );
            assertTrue( highPlayerCell.repOk() );
            assertTrue( startPlayerCell.repOk() );
            assertTrue( lowPlayerCell.repOk() );
            assertTrue( highOpponentCell.repOk() );
            assertTrue( startOpponentCell.repOk() );
            assertTrue( lowOpponentCell.repOk() );
            assertTrue( worker.position().equals( highPlayerCell ));
            assertTrue( opponentWorker.position().equals( startOpponentCell ));


            //>> player moves his/her worker with a LevelDirection == hotLastMoveDirection and
            //   opponentWorker moves with a LevelDirection != opponentDeniedDirection (kindAthena -> MustObey == false):
            //   opponent's worker can't move but opponent doesn't lose

            // set kindAthena
            myMove = kindAthenaCard.getMyMove();
            adversaryMove = kindAthenaCard.getAdversaryMove();

            worker.place(startPlayerCell);
            opponentWorker.place(startOpponentCell);

            move = new Move(worker.position(), highPlayerCell);
            worker.place(highPlayerCell);
            myMove.setLastMove(move);
            opponentMove = new Move( opponentWorker.position(), highOpponentCell );
            try {
                check = adversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue( !check );
                assertTrue( highPlayerCell.getHeigth() == 3);
                assertTrue( startPlayerCell.getHeigth() == 1);
                assertTrue( lowPlayerCell.getHeigth() == 0);
                assertTrue( highOpponentCell.getHeigth() == 2);
                assertTrue( startOpponentCell.getHeigth() == 2);
                assertTrue( lowOpponentCell.getHeigth() == 0);
                assertTrue( highPlayerCell.getTop().equals( worker ) );
                assertTrue( highPlayerCell.getPlaceableAt(0).isBlock() );
                assertTrue( highPlayerCell.getPlaceableAt(1).isBlock() );
                assertTrue( startPlayerCell.getTop().isBlock() );
                assertTrue( lowPlayerCell.getTop() == null );
                assertTrue( highOpponentCell.getTop().isBlock() );
                assertTrue( highOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( startOpponentCell.getTop().equals( opponentWorker ) );
                assertTrue( startOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( lowOpponentCell.getTop() == null );
                assertTrue( highPlayerCell.repOk() );
                assertTrue( startPlayerCell.repOk() );
                assertTrue( lowPlayerCell.repOk() );
                assertTrue( highOpponentCell.repOk() );
                assertTrue( startOpponentCell.repOk() );
                assertTrue( lowOpponentCell.repOk() );
                assertTrue( worker.position().equals( highPlayerCell ));
                assertTrue( opponentWorker.position().equals( startOpponentCell ));

            } catch (LoseException l) {
                checkLose = true;
            } finally {
                assertTrue( !checkLose );
                checkLose = false;
            }


        }
        catch ( OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue( !checkError );

    }

    /**
     * Check if getParentsCard() can return the correct value after initialization
     * Methods used:        getAdversaryMove()                      of  Card
     *
     * Black Box and White Box
     */
    @Test
    void getParentsCard() {
        AdversaryMove adversaryMove = humanCard.getAdversaryMove();

        assertTrue(adversaryMove.getParentCard().equals(humanCard));

    }
}