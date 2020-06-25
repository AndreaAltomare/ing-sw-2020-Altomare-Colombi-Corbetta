package it.polimi.ingsw.model.card.move;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.IslandBoard;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardParser;
import it.polimi.ingsw.model.card.GodPower;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import it.polimi.ingsw.model.exceptions.WinException;
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
class MyMoveTest {

    boolean checkOutOfBoard;
    boolean checkWin;
    boolean checkMove;
    boolean checkExecute;
    boolean checkError;

    MyMove myMove;
    Move move;
    Board board;
    Player player = new Player("cpu");
    Worker worker;

    GodPower human;
    Card humanCard;

    GodPower apollo;
    Card apolloCard;

    GodPower minotaur;
    Card minotaurCard;

    GodPower artemis;
    Card artemisCard;

    GodPower prometheus;
    Card prometheusCard;


    /**
     * Initialization before method's test
     * Methods used:    *               of  GodPower
     *                  *               of  CardParser
     */
    @BeforeEach
    void setUp() {

        checkOutOfBoard = false;
        checkWin = false;
        checkMove  = false;
        checkExecute = false;
        checkError = false;

        board = new IslandBoard();
        worker = new Worker(player);

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

        // Apollo's GodPower and Card initialization
        apollo = new GodPower();
        apollo.setName("Apollo");
        apollo.setEpithet("God of Music");
        apollo.setDescription("A");
        apollo.setMovementsLeft(1);
        apollo.setConstructionLeft(1);
        apollo.setActiveOnMyMovement(true);
        apollo.setHotLastMoveDirection(LevelDirection.NONE);
        apollo.setMoveIntoOpponentSpace(true);
        apollo.setForceOpponentInto(FloorDirection.ANY);
        apollo.setDeniedDirection(LevelDirection.NONE);
        apollo.setOpponentDeniedDirection(LevelDirection.NONE);
        apolloCard = new Card(  apollo,
                                CardParser.getMoveCheckers(apollo),
                                CardParser.getMoveExecutor(apollo),
                                CardParser.getBuildCheckers(apollo),
                                CardParser.getBuildExecutor(apollo),
                                CardParser.getWinCheckers(apollo),
                                CardParser.getAdversaryMoveCheckers(apollo));

        // Minotaur's GodPower and Card initialization
        minotaur = new GodPower();
        minotaur.setName("Minotaur");
        minotaur.setEpithet("Bull-headed Monster");
        minotaur.setDescription("M");
        minotaur.setMovementsLeft(1);
        minotaur.setConstructionLeft(1);
        minotaur.setActiveOnMyMovement(true);
        minotaur.setHotLastMoveDirection(LevelDirection.NONE);
        minotaur.setMoveIntoOpponentSpace(true);
        minotaur.setForceOpponentInto(FloorDirection.SAME);
        minotaur.setDeniedDirection(LevelDirection.NONE);
        minotaur.setOpponentDeniedDirection(LevelDirection.NONE);
        minotaurCard = new Card(    minotaur,
                                    CardParser.getMoveCheckers(minotaur),
                                    CardParser.getMoveExecutor(minotaur),
                                    CardParser.getBuildCheckers(minotaur),
                                    CardParser.getBuildExecutor(minotaur),
                                    CardParser.getWinCheckers(minotaur),
                                    CardParser.getAdversaryMoveCheckers(minotaur));

        // Artemis's GodPower and Card initialization
        artemis = new GodPower();
        artemis.setName("Artemis");
        artemis.setEpithet("Goddess of the Hunt");
        artemis.setDescription("A");
        artemis.setMovementsLeft(2);
        artemis.setConstructionLeft(1);
        artemis.setStartingSpaceDenied(true);
        artemis.setActiveOnMyMovement(true);
        artemis.setHotLastMoveDirection(LevelDirection.NONE);
        artemis.setForceOpponentInto(FloorDirection.NONE);
        artemis.setDeniedDirection(LevelDirection.NONE);
        artemis.setOpponentDeniedDirection(LevelDirection.NONE);
        artemisCard = new Card( artemis,
                                CardParser.getMoveCheckers(artemis),
                                CardParser.getMoveExecutor(artemis),
                                CardParser.getBuildCheckers(artemis),
                                CardParser.getBuildExecutor(artemis),
                                CardParser.getWinCheckers(artemis),
                                CardParser.getAdversaryMoveCheckers(artemis));

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

        worker = null;
        myMove = null;
        board = null;

        human = null;
        humanCard = null;

        apollo = null;
        apolloCard = null;

        minotaur = null;
        minotaurCard = null;

        artemis = null;
        artemisCard = null;

        prometheus = null;
        prometheusCard = null;

    }


    /**
     * Check if the normal moves on board are correctly checked and executed
     * Methods used:        getCellAt( int, int)    of  IslandBoard
     *                      getX()                  of  Cell
     *                      getY()                  of  Cell
     *                      getHeigth()             of  Cell
     *                      repOk()                 of  Cell
     *                      getTop()                of  Cell
     *                      position()              of  Placeable
     *                      place( Cell )           of  Worker
     *                      getMyMove()             of  Card
     *                      getLastMove()           of  MyMove
     *
     * Black Box
     */
    @Test
    void checkAndExecuteMoveDefaultOnBoardBlack() {
        myMove = humanCard.getMyMove();
        Cell centralCell;
        Cell nextCell;

        try {
            centralCell = board.getCellAt(2, 2);


            //*** Move on near Cells (North, East and South-West) ***//

            // NORTH
            worker.place(centralCell);
            nextCell = board.getCellAt((worker.position().getX() - 1), worker.position().getY());
            move = new Move(worker.position(), nextCell);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(worker.position().equals(nextCell));
                assertTrue(nextCell.getTop().equals(worker));
                assertTrue(nextCell.getHeigth() == 1);
                assertTrue(nextCell.repOk());
                assertTrue(centralCell.getTop() == null);
                assertTrue( myMove.getLastMove().equals(move) );

            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }

            // EST
            worker.place(centralCell);
            nextCell = board.getCellAt(worker.position().getX(), (worker.position().getY() + 1));
            move = new Move(worker.position(), nextCell);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(worker.position().equals(nextCell));
                assertTrue(nextCell.getTop().equals(worker));
                assertTrue(nextCell.getHeigth() == 1);
                assertTrue(nextCell.repOk());
                assertTrue(centralCell.getTop() == null);
                assertTrue( myMove.getLastMove().equals(move) );

            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }

            // SOUTH-WEST
            worker.place(centralCell);
            nextCell = board.getCellAt((worker.position().getX() + 1), (worker.position().getY() - 1));
            move = new Move(worker.position(), nextCell);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(worker.position().equals(nextCell));
                assertTrue(nextCell.getTop().equals(worker));
                assertTrue(nextCell.getHeigth() == 1);
                assertTrue(nextCell.repOk());
                assertTrue(centralCell.getTop() == null);
                assertTrue( myMove.getLastMove().equals(move) );

            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }


            //*** Move on same Cell but it can't ***//
            worker.place(centralCell);
            move = new Move(worker.position(), centralCell);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(worker.position().equals(centralCell));
            assertTrue(centralCell.getTop().equals(worker));
            assertTrue(centralCell.getHeigth() == 1);
            assertTrue(centralCell.repOk());


            //*** Move on faraway Cells (North, East and South-West) but it can't ***//

            // NORTH
            worker.place(centralCell);
            nextCell = board.getCellAt((worker.position().getX() - 2), worker.position().getY());
            move = new Move(worker.position(), nextCell);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(worker.position().equals(centralCell));
            assertTrue(centralCell.getHeigth() == 1);
            assertTrue(centralCell.repOk());
            assertTrue(centralCell.getTop().equals(worker));
            assertTrue(nextCell.getTop() == null);


            // EST
            worker.place(centralCell);
            nextCell = board.getCellAt(worker.position().getX(), (worker.position().getY() + 2));
            move = new Move(worker.position(), nextCell);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(worker.position().equals(centralCell));
            assertTrue(centralCell.getTop().equals(worker));
            assertTrue(centralCell.getHeigth() == 1);
            assertTrue(centralCell.repOk());
            assertTrue(nextCell.getTop() == null);


            // SOUTH-WEST
            worker.place(centralCell);
            nextCell = board.getCellAt((worker.position().getX() + 2), (worker.position().getY() - 2));
            move = new Move(worker.position(), nextCell);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(worker.position().equals(centralCell));
            assertTrue(centralCell.getTop().equals(worker));
            assertTrue(centralCell.getHeigth() == 1);
            assertTrue(centralCell.repOk());
            assertTrue(nextCell.getTop() == null);



        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue(!checkError);

    }

    /**
     * Check if the normal moves between two different building are correctly checked and executed
     * Methods used:        getCellAt( int, int)    of  IslandBoard
     *                      getX()                  of  Cell
     *                      getY()                  of  Cell
     *                      getTop()                of  Cell
     *                      getPlaceableAt( int )   of  Cell
     *                      getHeigth()             of  Cell
     *                      repOk()                 of  Cell
     *                      buildBlock()            of  Cell
     *                      buildDome()             of  Cell
     *                      position()              of  Placeable
     *                      isBlock()               of  Placeable/Block
     *                      isDome()                of  Placeable/Dome
     *                      place( Cell )           of  Worker
     *                      getMyMove()             of  Card
     *                      getLastMove()           of  MyMove
     * Black Box
     */
    @Test
    void checkAndExecuteMoveDefaultBetweenCellsBlack() {
        myMove = humanCard.getMyMove();
        Player opponent = new Player("Opp");
        Cell freeCell;
        Cell cell1Block;
        Cell cell2Block;
        Cell cell3Block;
        Cell domeCell;
        Cell myWorkerCell;
        Cell opponentCell;
        Worker otherMyWorker = new Worker(player);
        Worker opponentWorker = new Worker(opponent);
        int height;

        try {
            //                        cell1Block: B           cell2Block: BB
            //                        freeCell:               cell3Block: BBB
            //  opponentCell: W       workerCell: W           domeCell: D
            freeCell = board.getCellAt(2, 2);
            cell1Block = board.getCellAt((freeCell.getX() - 1), freeCell.getY());
            cell1Block.buildBlock();
            cell2Block = board.getCellAt((freeCell.getX() - 1), (freeCell.getY() + 1));
            cell2Block.buildBlock();
            cell2Block.buildBlock();
            cell3Block = board.getCellAt(freeCell.getX(), (freeCell.getY() + 1));
            cell3Block.buildBlock();
            cell3Block.buildBlock();
            cell3Block.buildBlock();
            domeCell = board.getCellAt((freeCell.getX() + 1), (freeCell.getY() + 1));
            domeCell.buildDome();
            myWorkerCell = board.getCellAt((freeCell.getX() + 1), freeCell.getY());
            otherMyWorker.place(myWorkerCell);
            opponentCell = board.getCellAt((freeCell.getX() + 1), (freeCell.getY() - 1));
            opponentWorker.place(opponentCell);


            /* go up of 1 level */
            worker.place(freeCell);
            height = cell1Block.getHeigth();
            move = new Move(worker.position(), cell1Block);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(worker.position().equals(cell1Block));
                assertTrue(cell1Block.getHeigth() == (height + 1));
                assertTrue(cell1Block.getTop().equals(worker));
                assertTrue(cell1Block.getPlaceableAt(0).isBlock());
                assertTrue(cell1Block.repOk());
                assertTrue(freeCell.getTop() == null);
                assertTrue( myMove.getLastMove().equals(move) );
            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }


            /* not go up of 2 level */
            worker.place(freeCell);
            height = cell2Block.getHeigth();
            move = new Move(worker.position(), cell2Block);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(worker.position().equals(freeCell));
            assertTrue(cell2Block.getHeigth() == height);
            assertTrue(cell2Block.getTop().isBlock());
            assertTrue(cell2Block.getPlaceableAt(0).isBlock());
            assertTrue(cell2Block.getPlaceableAt(1).isBlock());
            assertTrue(cell2Block.repOk());
            assertTrue(freeCell.getTop().equals(worker));
            assertTrue(freeCell.getHeigth() == 1);
            assertTrue(freeCell.repOk());



            /* not go up of 3 level */
            worker.place(freeCell);
            height = cell3Block.getHeigth();
            move = new Move(worker.position(), cell3Block);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(worker.position().equals(freeCell));
            assertTrue(cell3Block.getHeigth() == height);
            assertTrue(cell3Block.getTop().isBlock());
            assertTrue(cell3Block.getPlaceableAt(0).isBlock());
            assertTrue(cell3Block.getPlaceableAt(1).isBlock());
            assertTrue(cell3Block.getPlaceableAt(2).isBlock());
            assertTrue(cell3Block.repOk());
            assertTrue(freeCell.getTop().equals(worker));
            assertTrue(freeCell.getHeigth() == 1);
            assertTrue(freeCell.repOk());


            /* not go up on dome */
            worker.place(freeCell);
            height = domeCell.getHeigth();
            move = new Move(worker.position(), domeCell);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(worker.position().equals(freeCell));
            assertTrue(domeCell.getHeigth() == height);
            assertTrue(domeCell.getTop().isDome());
            assertTrue(domeCell.repOk());
            assertTrue(freeCell.getTop().equals(worker));
            assertTrue(freeCell.getHeigth() == 1);
            assertTrue(freeCell.repOk());


            /* not go up on other my worker */
            worker.place(freeCell);
            height = myWorkerCell.getHeigth();
            move = new Move(worker.position(), myWorkerCell);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(worker.position().equals(freeCell));
            assertTrue(myWorkerCell.getHeigth() == height);
            assertTrue(myWorkerCell.getTop().equals(otherMyWorker));
            assertTrue(otherMyWorker.position().equals(myWorkerCell));
            assertTrue(myWorkerCell.repOk());
            assertTrue(freeCell.getTop().equals(worker));
            assertTrue(freeCell.getHeigth() == 1);
            assertTrue(freeCell.repOk());


            /* not go up on opponent worker */
            worker.place(freeCell);
            height = opponentCell.getHeigth();
            move = new Move(worker.position(), opponentCell);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(worker.position().equals(freeCell));
            assertTrue(opponentCell.getHeigth() == height);
            assertTrue(opponentCell.getTop().equals(opponentWorker));
            assertTrue(opponentWorker.position().equals(opponentCell));
            assertTrue(opponentCell.repOk());
            assertTrue(freeCell.getTop().equals(worker));
            assertTrue(freeCell.getHeigth() == 1);
            assertTrue(freeCell.repOk());


            /* go down of 1 level */
            worker.place(cell1Block);
            height = cell1Block.getHeigth();
            move = new Move(worker.position(), freeCell);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(worker.position().equals(freeCell));
                assertTrue(cell1Block.getHeigth() == (height - 1));
                assertTrue(cell1Block.getPlaceableAt(0).isBlock());
                assertTrue(cell1Block.repOk());
                assertTrue(freeCell.getTop().equals(worker));
                assertTrue(freeCell.getHeigth() == 1);
                assertTrue( myMove.getLastMove().equals(move) );
            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }


            /* go down of 3 level */
            worker.place(cell3Block);
            height = cell3Block.getHeigth();
            move = new Move(worker.position(), freeCell);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(worker.position().equals(freeCell));
                assertTrue(cell3Block.getHeigth() == (height - 1));
                assertTrue(cell3Block.getTop().isBlock());
                assertTrue(cell3Block.getPlaceableAt(0).isBlock());
                assertTrue(cell3Block.getPlaceableAt(1).isBlock());
                assertTrue(cell3Block.getPlaceableAt(2).isBlock());
                assertTrue(cell3Block.repOk());
                assertTrue(freeCell.getTop().equals(worker));
                assertTrue(freeCell.getHeigth() == 1);
                assertTrue(freeCell.repOk());
                assertTrue( myMove.getLastMove().equals(move) );
            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }


            /* Default win condition */
            worker.place(cell2Block);
            height = cell3Block.getHeigth();
            move = new Move(worker.position(), cell3Block);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }

            assertTrue(worker.position().equals(cell3Block));
            assertTrue(cell3Block.getHeigth() == (height + 1));
            assertTrue(cell3Block.getTop().equals(worker));
            assertTrue(cell3Block.getPlaceableAt(0).isBlock());
            assertTrue(cell3Block.getPlaceableAt(1).isBlock());
            assertTrue(cell3Block.getPlaceableAt(2).isBlock());
            assertTrue(cell3Block.repOk());
            assertTrue(cell2Block.getHeigth() == 2);
            assertTrue(cell2Block.getTop().isBlock());
            assertTrue(cell2Block.getPlaceableAt(0).isBlock());
            assertTrue(cell2Block.getPlaceableAt(1).isBlock());
            assertTrue(cell2Block.repOk());
            assertTrue( myMove.getLastMove().equals(move) );

        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue(!checkError);

    }


    /**
     * Check if Apollo's power is correctly checked and executed
     * Methods used:        getCellAt( int, int)    of  IslandBoard
     *                      getTop()                of  Cell
     *                      getPlaceableAt( int )   of  Cell
     *                      getHeigth()             of  Cell
     *                      repOk()                 of  Cell
     *                      buildBlock()            of  Cell
     *                      removePlaceable()       of  Cell
     *                      removeWorker()          of  Cell
     *                      position()              of  Placeable
     *                      isBlock()               of  Placeable/Block
     *                      place( Cell )           of  Worker
     *                      getMyMove()             of  Card
     *
     * Black Box
     */
    @Test
    void checkAandExecuteMoveApolloBlack() {
        myMove = apolloCard.getMyMove();
        Player opponent = new Player("Opp");
        Cell cell1;
        Cell cell2;
        Worker otherMyWorker = new Worker(player);
        Worker opponentWorker = new Worker(opponent);

        try {

            cell1 = board.getCellAt(2, 2);
            cell2 = board.getCellAt(3, 3);

            /* not go in a Cell where there is another my worker */
            worker.place(cell1);
            otherMyWorker.place(cell2);
            move = new Move(cell1, cell2);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().equals(otherMyWorker));
            assertTrue(worker.position().equals(cell1));
            assertTrue(otherMyWorker.position().equals(cell2));
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());


            // clear cells
            cell2.removeWorker();
            cell1.removeWorker();


            /* go in a Cell where there is a opponent worker */
            worker.place(cell1);
            opponentWorker.place(cell2);
            move = new Move(cell1, cell2);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 1);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(cell1.getTop().equals(opponentWorker));
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(worker.position().equals(cell2));
                assertTrue(opponentWorker.position().equals(cell1));
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue( myMove.getLastMove().equals(move) );
            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }


            // clear cells
            cell2.removeWorker();
            cell1.removeWorker();


            /* not go up on a too high Cell where there is a opponent worker */
            worker.place(cell1);
            cell2.buildBlock();
            cell2.buildBlock();
            opponentWorker.place(cell2);
            move = new Move(cell1, cell2);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 3);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().equals(opponentWorker));
            assertTrue(cell2.getPlaceableAt(0).isBlock());
            assertTrue(cell2.getPlaceableAt(1).isBlock());
            assertTrue(worker.position().equals(cell1));
            assertTrue(opponentWorker.position().equals(cell2));
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());


            // clear cells
            cell2.removeWorker();
            cell1.removeWorker();
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }


            /* go down from third level on a Cell where there is a opponent worker without victory*/
            cell1.buildBlock();
            cell1.buildBlock();
            cell1.buildBlock();
            worker.place(cell1);
            opponentWorker.place(cell2);
            move = new Move(cell1, cell2);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 4);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(cell1.getTop().equals(opponentWorker));
                assertTrue(cell1.getPlaceableAt(0).isBlock());
                assertTrue(cell1.getPlaceableAt(1).isBlock());
                assertTrue(cell1.getPlaceableAt(2).isBlock());
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(worker.position().equals(cell2));
                assertTrue(opponentWorker.position().equals(cell1));
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue( myMove.getLastMove().equals(move) );
            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }


            // clear cells
            cell2.removeWorker();
            cell1.removeWorker();
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }


            /* go up from second level to third on a Cell where there is a opponent worker with victory*/
            cell1.buildBlock();
            cell1.buildBlock();
            worker.place(cell1);
            cell2.buildBlock();
            cell2.buildBlock();
            cell2.buildBlock();
            opponentWorker.place(cell2);
            move = new Move(cell1, cell2);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }

            assertTrue(cell1.getHeigth() == 3);
            assertTrue(cell2.getHeigth() == 4);
            assertTrue(cell1.getTop().equals(opponentWorker));
            assertTrue(cell1.getPlaceableAt(0).isBlock());
            assertTrue(cell1.getPlaceableAt(1).isBlock());
            assertTrue(cell2.getTop().equals(worker));
            assertTrue(cell2.getPlaceableAt(0).isBlock());
            assertTrue(cell2.getPlaceableAt(1).isBlock());
            assertTrue(cell2.getPlaceableAt(2).isBlock());
            assertTrue(worker.position().equals(cell2));
            assertTrue(opponentWorker.position().equals(cell1));
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue( myMove.getLastMove().equals(move) );


            // clear cells
            cell2.removeWorker();
            cell1.removeWorker();
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }

        } catch (OutOfBoardException e) {
            checkError = true;
        }
        assertTrue(!checkError);
    }

    /**
     * Check if Minotaur's power is correctly checked and executed
     * Methods used:        getCellAt( int, int)    of  IslandBoard
     *                      getTop()                of  Cell
     *                      getPlaceableAt( int )   of  Cell
     *                      getHeigth()             of  Cell
     *                      repOk()                 of  Cell
     *                      buildBlock()            of  Cell
     *                      buildDome()             of  Cell
     *                      removePlaceable()       of  Cell
     *                      position()              of  Placeable
     *                      isDome()                of  Placeable/Dome
     *                      isBlock()               of  Placeable/Block
     *                      place( Cell )           of  Worker
     *                      getMyMove()             of  Card
     *                      getLastMove()           of  MyMove
     * Black Box
     */
    @Test
    void checkAndExecuteMoveMinotaurBlack() {
        myMove = minotaurCard.getMyMove();
        Player opponent = new Player("Opp");
        Cell cell1;
        Cell cell2;
        Cell backCell;
        Cell cornerCell;
        Worker otherMyWorker = new Worker(player);
        Worker opponentWorker = new Worker(opponent);

        try {
            cell1 = board.getCellAt(3, 3);
            cell2 = board.getCellAt(2, 2);
            backCell = board.getCellAt(1, 1);
            cornerCell = board.getCellAt(4, 4);

            /* not go on a Cell where there is another my worker */
            worker.place(cell1);
            otherMyWorker.place(cell2);
            move = new Move(cell1, cell2);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().equals(otherMyWorker));
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(worker.position().equals(cell1));
            assertTrue(otherMyWorker.position().equals(cell2));

            // clear cell
            cell1.removePlaceable();
            cell2.removePlaceable();


            /* go on a Cell where there is a opponent worker and back a free Cell */
            worker.place(cell1);
            opponentWorker.place(cell2);
            move = new Move(cell1, cell2);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 0);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(backCell.getHeigth() == 1);
                assertTrue(cell1.getTop() == null);
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(backCell.getTop().equals(opponentWorker));
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(backCell.repOk());
                assertTrue(worker.position().equals(cell2));
                assertTrue(opponentWorker.position().equals(backCell));
                assertTrue( myMove.getLastMove().equals(move) );

            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }

            // clear cells
            cell1.removePlaceable();
            cell2.removePlaceable();
            backCell.removePlaceable();


            /* go on a Cell where there is a opponent worker and back a Cell with a Block */
            worker.place(cell1);
            opponentWorker.place(cell2);
            backCell.buildBlock();
            move = new Move(cell1, cell2);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 0);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(backCell.getHeigth() == 2);
                assertTrue(cell1.getTop() == null);
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(backCell.getTop().equals(opponentWorker));
                assertTrue(backCell.getPlaceableAt(0).isBlock());
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(backCell.repOk());
                assertTrue(worker.position().equals(cell2));
                assertTrue(opponentWorker.position().equals(backCell));
                assertTrue( myMove.getLastMove().equals(move) );

            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }

            // clear cells
            cell1.removePlaceable();
            cell2.removePlaceable();
            while (backCell.getTop() != null) {
                backCell.removePlaceable();
            }


            /* go on a Cell where there is a opponent worker and back a Cell with three Blocks ( no victory) */
            worker.place(cell1);
            opponentWorker.place(cell2);
            backCell.buildBlock();
            backCell.buildBlock();
            backCell.buildBlock();
            move = new Move(cell1, cell2);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 0);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(backCell.getHeigth() == 4);
                assertTrue(cell1.getTop() == null);
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(backCell.getTop().equals(opponentWorker));
                assertTrue(backCell.getPlaceableAt(0).isBlock());
                assertTrue(backCell.getPlaceableAt(1).isBlock());
                assertTrue(backCell.getPlaceableAt(2).isBlock());
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(backCell.repOk());
                assertTrue(worker.position().equals(cell2));
                assertTrue(opponentWorker.position().equals(backCell));
                assertTrue( myMove.getLastMove().equals(move) );

            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }

            // clear cells
            cell1.removePlaceable();
            cell2.removePlaceable();
            while (backCell.getTop() != null) {
                backCell.removePlaceable();
            }


            /* not go on a Cell where there is a opponent worker and back a Cell with a Dome */
            worker.place(cell1);
            opponentWorker.place(cell2);
            backCell.buildDome();
            move = new Move(cell1, cell2);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(backCell.getHeigth() == 1);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().equals(opponentWorker));
            assertTrue(backCell.getTop().isDome());
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(backCell.repOk());
            assertTrue(worker.position().equals(cell1));
            assertTrue(opponentWorker.position().equals(cell2));


            // clear cells
            cell1.removePlaceable();
            cell2.removePlaceable();
            backCell.removePlaceable();

            /* not go on a Cell where there is a opponent worker and back a Cell with a worker */
            worker.place(cell1);
            opponentWorker.place(cell2);
            otherMyWorker.place(backCell);
            move = new Move(cell1, cell2);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(backCell.getHeigth() == 1);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().equals(opponentWorker));
            assertTrue(backCell.getTop().equals(otherMyWorker));
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(backCell.repOk());
            assertTrue(worker.position().equals(cell1));
            assertTrue(opponentWorker.position().equals(cell2));
            assertTrue(otherMyWorker.position().equals(backCell));


            // clear cells
            cell1.removePlaceable();
            cell2.removePlaceable();
            backCell.removePlaceable();


            /* go on a Cell where there is a opponent worker at one higher level the worker's level and back a free Cell */
            cell1.buildBlock();
            worker.place(cell1);
            cell2.buildBlock();
            cell2.buildBlock();
            opponentWorker.place(cell2);
            move = new Move(cell1, cell2);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 1);
                assertTrue(cell2.getHeigth() == 3);
                assertTrue(backCell.getHeigth() == 1);
                assertTrue(cell1.getTop().isBlock());
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(cell2.getPlaceableAt(0).isBlock());
                assertTrue(cell2.getPlaceableAt(1).isBlock());
                assertTrue(backCell.getTop().equals(opponentWorker));
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(backCell.repOk());
                assertTrue(worker.position().equals(cell2));
                assertTrue(opponentWorker.position().equals(backCell));
                assertTrue( myMove.getLastMove().equals(move) );

            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }

            // clear cells
            cell1.removePlaceable();
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }
            backCell.removePlaceable();


            /* not go on a Cell where there is a opponent worker at too high level and back a free Cell */
            worker.place(cell1);
            cell2.buildBlock();
            cell2.buildBlock();
            opponentWorker.place(cell2);
            move = new Move(cell1, cell2);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 3);
            assertTrue(backCell.getHeigth() == 0);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().equals(opponentWorker));
            assertTrue(cell2.getPlaceableAt(0).isBlock());
            assertTrue(cell2.getPlaceableAt(1).isBlock());
            assertTrue(backCell.getTop() == null);
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(backCell.repOk());
            assertTrue(worker.position().equals(cell1));
            assertTrue(opponentWorker.position().equals(cell2));


            // clear cells
            cell1.removePlaceable();
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }
            backCell.removePlaceable();


            /* not go on a Cell where there is a opponent and back a Cell out the board */
            worker.place(cell1);
            opponentWorker.place(cornerCell);
            move = new Move(cell1, cornerCell);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cornerCell.getHeigth() == 1);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cornerCell.getTop().equals(opponentWorker));
            assertTrue(cell1.repOk());
            assertTrue(cornerCell.repOk());
            assertTrue(worker.position().equals(cell1));
            assertTrue(opponentWorker.position().equals(cornerCell));


            // clear cells
            cell1.removePlaceable();
            cornerCell.removePlaceable();

        } catch (OutOfBoardException e) {
            checkError = true;
        }
        assertTrue(!checkError);


    }

    /**
     * Check if Artemis's power is correctly checked and executed
     * Methods used:        decreaseMovesLeft()                 of  MyMove
     *                      resetMovesLeft()                    of  MyMove
     *                      setMovementExecuted( boolean )      of  Card
     *                      getCellAt( int, int)                of  IslandBoard
     *                      getTop()                            of  Cell
     *                      getHeigth()                         of  Cell
     *                      repOk()                             of  Cell
     *                      removePlaceable()                   of  Cell
     *                      position()                          of  Placeable
     *                      place( Cell )                       of  Worker
     *                      getMyMove()                         of  Card
     *                      setMovementExecuted( boolean )      of  Card
     *                      getLastMove()                       of  MyMove
     *                      decreaseMoveLeft()                  of  MyMove
     *                      resetMovesLeft()                    of  MyMove
     *
     * Black Box
     */
    @Test
    void CheckAndExecuteMoveArtemisBlack() {
        myMove = artemisCard.getMyMove();
        Cell cell1;
        Cell cell2;
        Cell cell3;

        try {
            cell1 = board.getCellAt(1, 1);
            cell2 = board.getCellAt(2, 2);
            cell3 = board.getCellAt(3, 3);


            /* go from cell1 to cell2 then from cell2 to cell3 */
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 0);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(cell1.getTop() == null);
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(worker.position().equals(cell2));
                assertTrue( myMove.getLastMove().equals(move) );

            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }

            myMove.decreaseMovesLeft();
            artemisCard.setMovementExecuted(true);

            move = new Move(worker.position(), cell3);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell2.getHeigth() == 0);
                assertTrue(cell3.getHeigth() == 1);
                assertTrue(cell2.getTop() == null);
                assertTrue(cell3.getTop().equals(worker));
                assertTrue(cell2.repOk());
                assertTrue(cell3.repOk());
                assertTrue(worker.position().equals(cell3));
                assertTrue( myMove.getLastMove().equals(move) );

            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }

            // clear cells and card's parameters
            cell1.removePlaceable();
            cell2.removePlaceable();
            cell3.removePlaceable();
            myMove.resetMovesLeft();
            artemisCard.setMovementExecuted(false);


            /* go from cell1 to cell2 then not from cell2 to cell1 */
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 0);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(cell1.getTop() == null);
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(worker.position().equals(cell2));
                assertTrue( myMove.getLastMove().equals(move) );

            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }

            myMove.decreaseMovesLeft();
            artemisCard.setMovementExecuted(true);

            move = new Move(worker.position(), cell1);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(cell1.getHeigth() == 0);
            assertTrue(cell2.getTop().equals(worker));
            assertTrue(cell1.getTop() == null);
            assertTrue(cell2.repOk());
            assertTrue(cell1.repOk());
            assertTrue(worker.position().equals(cell2));


            // clear cells and card's parameters
            cell1.removePlaceable();
            cell2.removePlaceable();
            cell3.removePlaceable();
            myMove.resetMovesLeft();
            artemisCard.setMovementExecuted(false);


            /* go from cell1 to cell2 then from cell2 to cell3 and can win */
            cell1.buildBlock();

            cell2.buildBlock();
            cell2.buildBlock();

            cell3.buildBlock();
            cell3.buildBlock();
            cell3.buildBlock();

            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 1);
                assertTrue(cell2.getHeigth() == 3);
                assertTrue(cell1.getTop().isBlock());
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(cell2.getPlaceableAt(0).isBlock());
                assertTrue(cell2.getPlaceableAt(1).isBlock());
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(worker.position().equals(cell2));
            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }

            myMove.decreaseMovesLeft();
            artemisCard.setMovementExecuted(true);

            move = new Move(worker.position(), cell3);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue(checkMove);
                checkExecute = myMove.executeMove(move, worker);
                assertTrue( checkExecute ); // ignored
            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }


            assertTrue(cell2.getHeigth() == 2);
            assertTrue(cell3.getHeigth() == 4);
            assertTrue(cell2.getTop().isBlock());
            assertTrue(cell2.getPlaceableAt(0).isBlock());
            assertTrue(cell3.getTop().equals(worker));
            assertTrue(cell3.getPlaceableAt(0).isBlock());
            assertTrue(cell3.getPlaceableAt(1).isBlock());
            assertTrue(cell3.getPlaceableAt(2).isBlock());
            assertTrue(cell2.repOk());
            assertTrue(cell3.repOk());
            assertTrue(worker.position().equals(cell3));
            assertTrue(myMove.getLastMove().equals(move));

            // clear cells and card's parameters
            while ( cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while ( cell2.getTop() != null) {
                cell2.removePlaceable();
            }
            while ( cell3.getTop() != null) {
                cell3.removePlaceable();
            }
            myMove.resetMovesLeft();
            artemisCard.setMovementExecuted(false);

        } catch (OutOfBoardException e) {
            checkError = true;
        }
        assertTrue(!checkError);
    }

    /**
     * Check if Prometheus's power is correctly checked and executed
     * Methods used:        getCellAt( int, int)                    of  IslandBoard
     *                      getTop()                                of  Cell
     *                      getHeigth()                             of  Cell
     *                      getPlaceableAt( int )                   of  Cell
     *                      repOk()                                 of  Cell
     *                      removePlaceable()                       of  Cell
     *                      buildBlock()                            of  Cell
     *                      position()                              of  Placeable
     *                      isBlock()                               of  Placeable/Block
     *                      place( Cell )                           of  Worker
     *                      getMyMove()                             of  Card
     *                      getMyConstruction()                     of  Card
     *                      setConstructionExecuted( boolean )      of  Card
     *                      getLastMove()                           of  MyMove
     *                      decreaseConstructionLeft()              of  MyConstruction
     *
     * Black Box
     */
    @Test
    void checkAndExecuteMovePrometheusBlack() {
        myMove = prometheusCard.getMyMove();
          Cell cell1;
        Cell cell2;

        try {
            cell1 = board.getCellAt(1, 1);
            cell2 = board.getCellAt(2, 2);


            //*** Before first construction ***//

            /* go down before construction */
            cell1.buildBlock();
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 1);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(cell1.getTop().isBlock());
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(worker.position().equals(cell2));
                assertTrue(myMove.getLastMove().equals(move));

            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }


            // clear cells
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }


            /* go to same level before construction */
            cell1.buildBlock();
            cell2.buildBlock();
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 1);
                assertTrue(cell2.getHeigth() == 2);
                assertTrue(cell1.getTop().isBlock());
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(cell2.getPlaceableAt(0).isBlock());
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(worker.position().equals(cell2));
                assertTrue(myMove.getLastMove().equals(move));

            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }

            // clear cells
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }


            /* go up after construction */
            cell1.buildBlock();
            cell2.buildBlock();
            cell2.buildBlock();
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue( checkExecute);
                assertTrue(cell1.getHeigth() == 1);
                assertTrue(cell2.getHeigth() == 3);
                assertTrue(cell1.getTop().isBlock());
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(cell2.getPlaceableAt(0).isBlock());
                assertTrue(cell2.getPlaceableAt(1).isBlock());
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(worker.position().equals(cell2));
            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }

            // clear cells
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }


            //*** After first construction ***//

            prometheusCard.getMyConstruction().decreaseConstructionLeft();
            prometheusCard.setConstructionExecuted(true);


            /* go down after construction */
            cell1.buildBlock();
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 1);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(cell1.getTop().isBlock());
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(worker.position().equals(cell2));
                assertTrue(myMove.getLastMove().equals(move));

            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }

            // clear cells
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }


            /* go to same level after construction */
            cell1.buildBlock();
            cell2.buildBlock();
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            try {
                checkMove = myMove.checkMove(move, worker);
                assertTrue( checkMove );
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 1);
                assertTrue(cell2.getHeigth() == 2);
                assertTrue(cell1.getTop().isBlock());
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(cell2.getPlaceableAt(0).isBlock());
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(worker.position().equals(cell2));
                assertTrue(myMove.getLastMove().equals(move));

            } catch (OutOfBoardException o) {
                checkOutOfBoard = true;
            } catch (WinException w) {
                checkWin = true;
            } finally {
                assertTrue(!checkOutOfBoard);
                assertTrue(!checkWin);
                checkOutOfBoard = false;
                checkWin = false;
            }


            // clear cells
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }


            /* not go up after construction */
            cell1.buildBlock();
            cell2.buildBlock();
            cell2.buildBlock();
            worker.place(cell1);
            move = new Move(worker.position(), cell2);

            checkMove = myMove.checkMove(move, worker);
            assertTrue( !checkMove );
            assertTrue(cell1.getHeigth() == 2);
            assertTrue(cell2.getHeigth() == 2);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell1.getPlaceableAt(0).isBlock());
            assertTrue(cell2.getTop().isBlock());
            assertTrue(cell2.getPlaceableAt(0).isBlock());
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(worker.position().equals(cell1));


            // clear cells
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }

        } catch (OutOfBoardException e) {
            checkError = true;
        }
        assertTrue(!checkError);


    }


    /**
     * Check if with the default configure of checkMoveList returned from CardParser checkMyMove() can correctly check
     * the movement ( white test of first two lambda expression of CardParser's getMoveCheckers() )
     * Methods used:        getMyMove()             of  Card
     *                      decreaseMovesLeft()     of  MyMove
     *                      resetMovesLeft          of  MyMove
     *                      getCellAt( int, int)    of  IslandBoard
     *                      getTop()                of  Cell
     *                      getHeigth()             of  Cell
     *                      getPlaceableAt( int )   of  Cell
     *                      repOk()                 of  Cell
     *                      removePlaceable()       of  Cell
     *                      buildBlock()            of  Cell
     *                      position()              of  Placeable
     *                      isBlock()               of  Placeable/Block
     *                      place( Cell )           of  Worker
     *
     *
     * White Box
     */
    @Test
    void checkMoveDefaultWhite() {
        myMove = humanCard.getMyMove();
        Cell cell1;
        Cell cell2;
        Worker otherWorker = new Worker(null);

        try {
            cell1 = board.getCellAt(2, 2);
            cell2 = board.getCellAt(3, 3);


            /* can not move when MovementsLeft <= 0 */
            myMove.decreaseMovesLeft();
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 0);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop() == null);
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(worker.position().equals(cell1));

            // clear board and resetMoveLeft()
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }
            myMove.resetMovesLeft();


            /* can not move on the same Cell where the worker is  */
            worker.place(cell1);
            move = new Move(worker.position(), worker.position());
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell1.repOk());
            assertTrue(worker.position().equals(cell1));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }

            /* can not go on cell too faraway */

            // CASE 1: cell2.getX() >> cell1.getX()
            worker.place(cell1);
            cell2 = board.getCellAt(cell1.getX() + 2, cell1.getY());
            move = new Move(worker.position(), cell2);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 0);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop() == null);
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(worker.position().equals(cell1));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }

            // CASE 2: cell2.getX() << cell1.getX()
            worker.place(cell1);
            cell2 = board.getCellAt(cell1.getX() - 2, cell1.getY());
            move = new Move(worker.position(), cell2);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 0);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop() == null);
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(worker.position().equals(cell1));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }

            // CASE 3: cell2.getY() >> cell1.getY()
            worker.place(cell1);
            cell2 = board.getCellAt(cell1.getX(), cell1.getY() + 2);
            move = new Move(worker.position(), cell2);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 0);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop() == null);
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(worker.position().equals(cell1));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }

            // CASE 4: cell2.getY() << cell1.getY()
            worker.place(cell1);
            cell2 = board.getCellAt(cell1.getX(), cell1.getY() - 2);
            move = new Move(worker.position(), cell2);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 0);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop() == null);
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(worker.position().equals(cell1));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }

            // set cell2 how a Cell near cell1
            cell2 = board.getCellAt(cell1.getX(), cell1.getY() - 1);


            /* can not move on a Cell too high  */
            worker.place(cell1);
            cell2.buildBlock();
            cell2.buildBlock();
            move = new Move(worker.position(), cell2);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 2);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().isBlock());
            assertTrue(cell2.getPlaceableAt(0).isBlock());
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(worker.position().equals(cell1));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }


            /* can not move on a Cell where there is a Dome  */
            worker.place(cell1);
            cell2.buildDome();
            move = new Move(worker.position(), cell2);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().isDome());
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(worker.position().equals(cell1));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }


            /* can not move on a Cell where there is a Worker  */
            worker.place(cell1);
            otherWorker.place(cell2);
            move = new Move(worker.position(), cell2);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().equals(otherWorker));
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(worker.position().equals(cell1));
            assertTrue(otherWorker.position().equals(cell2));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }


            /* can move on a near Cell without Dome or Worker*/
            worker.place(cell1);
            cell2.buildBlock();
            move = new Move(worker.position(), cell2);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(checkMove);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().isBlock());
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(worker.position().equals(cell1));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }

        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue(!checkError);

    }


    /**
     * Check if with the "move on opponent's worker" configure of checkMoveList returned from CardParser checkMyMove()
     * can correctly check the movement ( white test of two lambda expression of CardParser's getMoveCheckers() with
     * isMoveIntoOpponentSpace() == true)
     * Methods used:        getMyMove()                         of  Card
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
    void checkMoveOnOpponentSpace() {
        final Cell middleCell;
        final Cell nearCell;
        final Cell corneCell;
        Worker otherWorker = new Worker(player);
        Worker opponentWorker = new Worker( new Player("Opponent") );

        try {
            middleCell = board.getCellAt(2, 2);
            nearCell = board.getCellAt(3, 3);
            corneCell = board.getCellAt(4, 4);

            //***god with movement power and can go on a cell with opponent worker (Apollo)***//
            myMove = apolloCard.getMyMove();

            /* can move when cell have one block */
            nearCell.buildBlock();
            worker.place(middleCell);
            move = new Move(worker.position(), nearCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(checkMove);
            assertTrue(middleCell.getHeigth() == 1);
            assertTrue(nearCell.getHeigth() == 1);
            assertTrue(middleCell.getTop().equals(worker));
            assertTrue(nearCell.getTop().isBlock());
            assertTrue(middleCell.repOk());
            assertTrue(nearCell.repOk());
            assertTrue(worker.position().equals(middleCell));

            // clear board
            while (middleCell.getTop() != null) {
                middleCell.removePlaceable();
            }
            while (nearCell.getTop() != null) {
                nearCell.removePlaceable();
            }


            /* can go on a Cell with opponent's Worker */
            worker.place(middleCell);
            opponentWorker.place(nearCell);
            move = new Move(worker.position(), nearCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(checkMove);
            assertTrue(middleCell.getHeigth() == 1);
            assertTrue(nearCell.getHeigth() == 1);
            assertTrue(middleCell.getTop().equals(worker));
            assertTrue(nearCell.getTop().equals(opponentWorker));
            assertTrue(middleCell.repOk());
            assertTrue(nearCell.repOk());
            assertTrue(worker.position().equals(middleCell));
            assertTrue(opponentWorker.position().equals(nearCell));

            // clear board
            while (middleCell.getTop() != null) {
                middleCell.removePlaceable();
            }
            while (nearCell.getTop() != null) {
                nearCell.removePlaceable();
            }


            /* can go on a corner cell with opponent's Worker */
            worker.place(nearCell);
            opponentWorker.place(corneCell);
            move = new Move(worker.position(), corneCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(checkMove);
            assertTrue(nearCell.getHeigth() == 1);
            assertTrue(corneCell.getHeigth() == 1);
            assertTrue(nearCell.getTop().equals(worker));
            assertTrue(corneCell.getTop().equals(opponentWorker));
            assertTrue(nearCell.repOk());
            assertTrue(corneCell.repOk());
            assertTrue(worker.position().equals(nearCell));
            assertTrue(opponentWorker.position().equals(corneCell));

            // clear board
            while (middleCell.getTop() != null) {
                middleCell.removePlaceable();
            }
            while (nearCell.getTop() != null) {
                nearCell.removePlaceable();
            }


            /* can't go on a Cell with my Worker */
            worker.place(middleCell);
            otherWorker.place(nearCell);
            move = new Move(worker.position(), nearCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(middleCell.getHeigth() == 1);
            assertTrue(nearCell.getHeigth() == 1);
            assertTrue(middleCell.getTop().equals(worker));
            assertTrue(nearCell.getTop().equals(otherWorker));
            assertTrue(middleCell.repOk());
            assertTrue(nearCell.repOk());
            assertTrue(worker.position().equals(middleCell));
            assertTrue(otherWorker.position().equals(nearCell));

            // clear board
            while (middleCell.getTop() != null) {
                middleCell.removePlaceable();
            }
            while (nearCell.getTop() != null) {
                nearCell.removePlaceable();
            }





            //***god with movement power and can go on a cell with opponent worker forcing it at the same direction (Minotaur)***//

            myMove = minotaurCard.getMyMove();

            /* can go on a Cell with opponent's Worker and back a Cell with some Blocks*/
            worker.place(middleCell);
            opponentWorker.place(nearCell);
            corneCell.buildBlock();
            corneCell.buildBlock();
            corneCell.buildBlock();
            move = new Move(worker.position(), nearCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(checkMove);
            assertTrue(middleCell.getHeigth() == 1);
            assertTrue(nearCell.getHeigth() == 1);
            assertTrue(corneCell.getHeigth() == 3);
            assertTrue(middleCell.getTop().equals(worker));
            assertTrue(nearCell.getTop().equals(opponentWorker));
            assertTrue(corneCell.getPlaceableAt(0).isBlock());
            assertTrue(corneCell.getPlaceableAt(1).isBlock());
            assertTrue(corneCell.getPlaceableAt(2).isBlock());
            assertTrue(middleCell.repOk());
            assertTrue(nearCell.repOk());
            assertTrue(corneCell.repOk());
            assertTrue(worker.position().equals(middleCell));
            assertTrue(opponentWorker.position().equals(nearCell));

            // clear board
            while (middleCell.getTop() != null) {
                middleCell.removePlaceable();
            }
            while (nearCell.getTop() != null) {
                nearCell.removePlaceable();
            }
            while (corneCell.getTop() != null) {
                corneCell.removePlaceable();
            }


            /* can not go on a Cell with opponent's Worker and back a Cell with Dome */
            worker.place(middleCell);
            opponentWorker.place(nearCell);
            corneCell.buildDome();
            move = new Move(worker.position(), nearCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(middleCell.getHeigth() == 1);
            assertTrue(nearCell.getHeigth() == 1);
            assertTrue(corneCell.getHeigth() == 1);
            assertTrue(middleCell.getTop().equals(worker));
            assertTrue(nearCell.getTop().equals(opponentWorker));
            assertTrue(corneCell.getTop().isDome());
            assertTrue(middleCell.repOk());
            assertTrue(nearCell.repOk());
            assertTrue(corneCell.repOk());
            assertTrue(worker.position().equals(middleCell));
            assertTrue(opponentWorker.position().equals(nearCell));

            // clear board
            while (middleCell.getTop() != null) {
                middleCell.removePlaceable();
            }
            while (nearCell.getTop() != null) {
                nearCell.removePlaceable();
            }
            while (corneCell.getTop() != null) {
                corneCell.removePlaceable();
            }


            /* can not go on a Cell with opponent's Worker and back a Cell with Worker */
            worker.place(middleCell);
            opponentWorker.place(nearCell);
            otherWorker.place(corneCell);
            move = new Move(worker.position(), nearCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(middleCell.getHeigth() == 1);
            assertTrue(nearCell.getHeigth() == 1);
            assertTrue(corneCell.getHeigth() == 1);
            assertTrue(middleCell.getTop().equals(worker));
            assertTrue(nearCell.getTop().equals(opponentWorker));
            assertTrue(corneCell.getTop().equals(otherWorker));
            assertTrue(middleCell.repOk());
            assertTrue(nearCell.repOk());
            assertTrue(corneCell.repOk());
            assertTrue(worker.position().equals(middleCell));
            assertTrue(opponentWorker.position().equals(nearCell));
            assertTrue(otherWorker.position().equals(corneCell));

            // clear board
            while (middleCell.getTop() != null) {
                middleCell.removePlaceable();
            }
            while (nearCell.getTop() != null) {
                nearCell.removePlaceable();
            }
            while (corneCell.getTop() != null) {
                corneCell.removePlaceable();
            }


            /* can not go on a Cell with opponent's Worker and back nothing  */
            worker.place(nearCell);
            opponentWorker.place(corneCell);
            move = new Move(worker.position(), corneCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(nearCell.getHeigth() == 1);
            assertTrue(corneCell.getHeigth() == 1);
            assertTrue(nearCell.getTop().equals(worker));
            assertTrue(corneCell.getTop().equals(opponentWorker));
            assertTrue(nearCell.repOk());
            assertTrue(corneCell.repOk());
            assertTrue(worker.position().equals(nearCell));
            assertTrue(opponentWorker.position().equals(corneCell));

            // clear board
            while (middleCell.getTop() != null) {
                middleCell.removePlaceable();
            }
            while (nearCell.getTop() != null) {
                nearCell.removePlaceable();
            }
            while (corneCell.getTop() != null) {
                corneCell.removePlaceable();
            }



        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue(!checkError);

        }

    /**
     * Check if with the "active on my movement" and "same space denied" configure of checkMoveList returned from CardParser
     * checkMyMove() can correctly check the movement ( white test of "default active on my movement" and
     * "same space denied" lambda expression of CardParser's getMoveCheckers() )
     * Methods used:        getMyMove()                         of  Card
     *                      setExecuteMovement( boolean )       of  Card
     *                      setStartingSpace( cell )            of  MyMove
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
    void checkMoveNotAtSameSpace() {
        final Cell middleCell;
        final Cell nearCell;
        final Cell cornerCell;
        Worker opponentWorker = new Worker(new Player("Opponent"));

        try {
            middleCell = board.getCellAt(2, 2);
            nearCell = board.getCellAt(3, 3);
            cornerCell = board.getCellAt(4, 4);


            //***god with movement power, two movement and starting space denied (Artemis)***//

            myMove = artemisCard.getMyMove();

            /* can't go on a Cell with opponent's worker */
            worker.place(middleCell);
            opponentWorker.place(nearCell);
            move = new Move(worker.position(), nearCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(middleCell.getHeigth() == 1);
            assertTrue(nearCell.getHeigth() == 1);
            assertTrue(middleCell.getTop().equals(worker));
            assertTrue(nearCell.getTop().equals(opponentWorker));
            assertTrue(middleCell.repOk());
            assertTrue(nearCell.repOk());
            assertTrue(worker.position().equals(middleCell));
            assertTrue(opponentWorker.position().equals(nearCell));

            // clear board
            while (middleCell.getTop() != null) {
                middleCell.removePlaceable();
            }
            while (nearCell.getTop() != null) {
                nearCell.removePlaceable();
            }


            /* can go on a Cell like first movement */
            worker.place(middleCell);
            nearCell.buildBlock();
            move = new Move(worker.position(), nearCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(checkMove);
            assertTrue(middleCell.getHeigth() == 1);
            assertTrue(nearCell.getHeigth() == 1);
            assertTrue(middleCell.getTop().equals(worker));
            assertTrue(nearCell.getTop().isBlock());
            assertTrue(middleCell.repOk());
            assertTrue(nearCell.repOk());
            assertTrue(worker.position().equals(middleCell));
            assertTrue(myMove.getStartingPosition().equals(worker.position()));


            artemisCard.setMovementExecuted(true);

            /* can not return on first cell after first movement*/
            worker.place(nearCell);
            move = new Move(worker.position(), middleCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(middleCell.getHeigth() == 0);
            assertTrue(nearCell.getHeigth() == 2);
            assertTrue(middleCell.getTop() == null);
            assertTrue(nearCell.getTop().equals(worker));
            assertTrue(nearCell.getPlaceableAt(0).isBlock());
            assertTrue(middleCell.repOk());
            assertTrue(nearCell.repOk());
            assertTrue(worker.position().equals(nearCell));


            /* can go on different cell after first movement*/
            move = new Move(worker.position(), cornerCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(checkMove);
            assertTrue(nearCell.getHeigth() == 2);
            assertTrue(cornerCell.getHeigth() == 0);
            assertTrue(nearCell.getTop().equals(worker));
            assertTrue(nearCell.getPlaceableAt(0).isBlock());
            assertTrue(cornerCell.getTop() == null);
            assertTrue(nearCell.repOk());
            assertTrue(middleCell.repOk());
            assertTrue(worker.position().equals(nearCell));


            // clear board
            while (middleCell.getTop() != null) {
                middleCell.removePlaceable();
            }
            while (nearCell.getTop() != null) {
                nearCell.removePlaceable();
            }
            while (cornerCell.getTop() != null) {
                cornerCell.removePlaceable();
            }



        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue(!checkError);
    }

    /**
     * Check if with the "active on my movement" and "construction before movement" configure of checkMoveList returned
     * from CardParser checkMyMove() can correctly check the movement ( white test of "default active on my movement" and
     * "construction before movement" lambda expression of CardParser's getMoveCheckers() )
     * Methods used:        getMyMove()                         of  Card
     *                      getMyConstruction()                 of  Card
     *                      decreseConstructionLeft()           of  MyMovement
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
    void checkMoveAfterConstruction() {
        final Cell middleCell;
        final Cell nearCell;
        Worker opponentWorker = new Worker(new Player("Opponent"));

        try {
            middleCell = board.getCellAt(2, 2);
            nearCell = board.getCellAt(3, 3);

            //***god with movement power, two construction and hotLastMoveDirection != NULL (Prometheus)***//

            myMove = prometheusCard.getMyMove();

            //>> Before construction

            /* can't go on a Cell with opponent's worker */
            worker.place(middleCell);
            opponentWorker.place(nearCell);
            move = new Move(worker.position(), nearCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(middleCell.getHeigth() == 1);
            assertTrue(nearCell.getHeigth() == 1);
            assertTrue(middleCell.getTop().equals(worker));
            assertTrue(nearCell.getTop().equals(opponentWorker));
            assertTrue(middleCell.repOk());
            assertTrue(nearCell.repOk());
            assertTrue(worker.position().equals(middleCell));
            assertTrue(opponentWorker.position().equals(nearCell));

            // clear board
            while (middleCell.getTop() != null) {
                middleCell.removePlaceable();
            }
            while (nearCell.getTop() != null) {
                nearCell.removePlaceable();
            }

            /* can go on an high cell before construction */
            worker.place(middleCell);
            nearCell.buildBlock();
            move = new Move(worker.position(), nearCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(checkMove);
            assertTrue(middleCell.getHeigth() == 1);
            assertTrue(nearCell.getHeigth() == 1);
            assertTrue(middleCell.getTop().equals(worker));
            assertTrue(nearCell.getTop().isBlock());
            assertTrue(middleCell.repOk());
            assertTrue(nearCell.repOk());
            assertTrue(worker.position().equals(middleCell));
            assertTrue(myMove.getStartingPosition().equals(worker.position()));

            // clear board
            while (middleCell.getTop() != null) {
                middleCell.removePlaceable();
            }
            while (nearCell.getTop() != null) {
                nearCell.removePlaceable();
            }


            //>> After construction

            prometheusCard.getMyConstruction().decreaseConstructionLeft();

            /* can go down if it builds before movement */
            middleCell.buildBlock();
            worker.place(middleCell);
            move = new Move(worker.position(), nearCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(checkMove);
            assertTrue(middleCell.getHeigth() == 2);
            assertTrue(nearCell.getHeigth() == 0);
            assertTrue(middleCell.getTop().equals(worker));
            assertTrue(middleCell.getPlaceableAt(0).isBlock());
            assertTrue(nearCell.getTop() == null);
            assertTrue(middleCell.repOk());
            assertTrue(nearCell.repOk());
            assertTrue(worker.position().equals(middleCell));

            // clear board
            while (middleCell.getTop() != null) {
                middleCell.removePlaceable();
            }
            while (nearCell.getTop() != null) {
                nearCell.removePlaceable();
            }


            /* can't go up if it builds before movement */
            nearCell.buildBlock();
            worker.place(middleCell);
            move = new Move(worker.position(), nearCell);
            checkMove = myMove.checkMove(move, worker);
            assertTrue(!checkMove);
            assertTrue(middleCell.getHeigth() == 1);
            assertTrue(nearCell.getHeigth() == 1);
            assertTrue(middleCell.getTop().equals(worker));
            assertTrue(nearCell.getTop().isBlock());
            assertTrue(middleCell.repOk());
            assertTrue(nearCell.repOk());
            assertTrue(worker.position().equals(middleCell));

            // clear board
            while (middleCell.getTop() != null) {
                middleCell.removePlaceable();
            }
            while (nearCell.getTop() != null) {
                nearCell.removePlaceable();
            }





        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue(!checkError);
    }

    /**
     * check if calculateNextCell() can return the correct nextCell or Out of Board Exception for each direction
     * Methods used:    getCellAt( int, int )       of  Board
     *                  getX()                      of  Cell
     *                  getY()                      of  Cell
     *                  equals()                    of  Cell
     *
     * Black Box and White Box
     */
    @Test
    void calculateNextCell() {
        final Cell middleCell;
        final Cell NorthCell;
        final Cell SouthCell;
        final Cell EastCell;
        final Cell WestCell;
        final Cell North_EastCell;
        final Cell North_WestCell;
        final Cell South_EastCell;
        final Cell South_WestCell;
        final Cell tooWestCell;

        Cell nextCell;
        boolean outOfBoard  = false;

        try {
            middleCell = board.getCellAt(2,2);
            NorthCell = board.getCellAt( middleCell.getX() - 1, middleCell.getY() );
            SouthCell = board.getCellAt( middleCell.getX() + 1, middleCell.getY() );
            WestCell = board.getCellAt( middleCell.getX(), middleCell.getY() - 1 );
            EastCell = board.getCellAt( middleCell.getX(), middleCell.getY() + 1 );
            North_WestCell = board.getCellAt( middleCell.getX() - 1, middleCell.getY() - 1 );
            North_EastCell = board.getCellAt( middleCell.getX() - 1, middleCell.getY() + 1 );
            South_WestCell = board.getCellAt( middleCell.getX() + 1, middleCell.getY() - 1 );
            South_EastCell = board.getCellAt( middleCell.getX() + 1, middleCell.getY() + 1 );
            tooWestCell = board.getCellAt( WestCell.getX(), WestCell.getY() - 1 );

            // South to middle -> North
            move = new Move(SouthCell, middleCell);
            try {
                nextCell = MyMove.calculateNextCell(move);
                assertTrue(nextCell.equals(NorthCell));
            } catch (OutOfBoardException o) {
                outOfBoard = true;
            } finally {
                assertTrue( !outOfBoard );
                outOfBoard = false;
            }

            // South-East to middle -> North-West
            move = new Move(South_EastCell, middleCell);
            try {
                nextCell = MyMove.calculateNextCell(move);
                assertTrue(nextCell.equals(North_WestCell));
            } catch (OutOfBoardException o) {
                outOfBoard = true;
            } finally {
                assertTrue( !outOfBoard );
                outOfBoard = false;
            }

            // East to middle -> West
            move = new Move(EastCell, middleCell);
            try {
                nextCell = MyMove.calculateNextCell(move);
                assertTrue(nextCell.equals(WestCell));
            } catch (OutOfBoardException o) {
                outOfBoard = true;
            } finally {
                assertTrue( !outOfBoard );
                outOfBoard = false;
            }

            // North-East to middle -> South-West
            move = new Move(North_EastCell, middleCell);
            try {
                nextCell = MyMove.calculateNextCell(move);
                assertTrue(nextCell.equals(South_WestCell));
            } catch (OutOfBoardException o) {
                outOfBoard = true;
            } finally {
                assertTrue( !outOfBoard );
                outOfBoard = false;
            }

            // North to middle -> South
            move = new Move(NorthCell, middleCell);
            try {
                nextCell = MyMove.calculateNextCell(move);
                assertTrue(nextCell.equals(SouthCell));
            } catch (OutOfBoardException o) {
                outOfBoard = true;
            } finally {
                assertTrue( !outOfBoard );
                outOfBoard = false;
            }

            // North-West to middle -> South-East
            move = new Move(North_WestCell, middleCell);
            try {
                nextCell = MyMove.calculateNextCell(move);
                assertTrue(nextCell.equals(South_EastCell));
            } catch (OutOfBoardException o) {
                outOfBoard = true;
            } finally {
                assertTrue( !outOfBoard );
                outOfBoard = false;
            }

            // West to middle -> East
            move = new Move(WestCell, middleCell);
            try {
                nextCell = MyMove.calculateNextCell(move);
                assertTrue(nextCell.equals(EastCell));
            } catch (OutOfBoardException o) {
                outOfBoard = true;
            } finally {
                assertTrue( !outOfBoard );
                outOfBoard = false;
            }

            // South-West to middle -> North-East
            move = new Move(South_WestCell, middleCell);
            try {
                nextCell = MyMove.calculateNextCell(move);
                assertTrue(nextCell.equals(North_EastCell));
            } catch (OutOfBoardException o) {
                outOfBoard = true;
            } finally {
                assertTrue( !outOfBoard );
                outOfBoard = false;
            }

            // West to tooWest -> Out of Board
            move = new Move(WestCell, tooWestCell);
            try {
                nextCell = MyMove.calculateNextCell(move);
            } catch (OutOfBoardException o) {
                outOfBoard = true;
            } finally {
                assertTrue( outOfBoard );
            }



        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue( !checkError );
    }

    /**
     * Check if getGodPower() can return the correct value after initialization
     * Methods used:        getMyMove()         of  Card
     *
     * Black Box and White Box
     */
    @Test
    void getGodPower() {
        myMove = humanCard.getMyMove();

        assertTrue(myMove.getGodPower().equals(human));

    }

    /**
     * Check if getLastMove() and setLastMove( Move ) can set and get the correct value
     * Methods used:        getMyMove()                     of  Card
     *                      getCellAt( int, int )           of  IslandBoard
     *
     * Black Box and White Box
     */
    @Test
    void getAndSetLastMove() {
        myMove = humanCard.getMyMove();
        Move setMove;

        final Cell cell1;
        final Cell cell2;


        //*** after initialization ***//
        assertTrue(myMove.getLastMove() == null);

        //*** with set methods ***//
        try {
            cell1 = board.getCellAt(2, 2);
            cell2 = board.getCellAt(3, 3);

            setMove = new Move(cell2, cell1);
            myMove.setLastMove(setMove);
            assertTrue(myMove.getLastMove().equals(setMove));

        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue(!checkError);

    }

    /**
     * Check if getMovesLeft(), setMoveLeft( int ), decreaseMovesLeft(), resetMovesLeft()
     * can set and return the correct value
     * Methods used:    getMyMove()         of  Card
     *
     * Black Box and White Box
     */
    @Test
    void getAndSetAndDecreaseAndResetMovesLeft() {
        myMove = humanCard.getMyMove();

        // after initialization
        assertTrue(myMove.getMovesLeft() == 1);

        // after setMovesLeft
        myMove.setMovesLeft(3);
        assertTrue(myMove.getMovesLeft() == 4);


        // after decreaseMovesLeft
        myMove.decreaseMovesLeft();
        assertTrue(myMove.getMovesLeft() == 3);

        // after resetMovesLeft()
        myMove.resetMovesLeft();
        assertTrue(myMove.getMovesLeft() == 1);

    }

    /**
     * Check if getStartingPosition() and setStartingPosition( Move ) can set and get the correct value
     * Methods used:        getMyMove()                     of  Card
     *                      getCellAt( int, int)            of  IslandBoard
     *
     * Black Box and White Box
     */
    @Test
    void getAndSetStartingPosition() {
        myMove = humanCard.getMyMove();

        final Cell cell1;


        //*** after initialization ***//
        assertTrue(myMove.getStartingPosition() == null);

        //*** with set methods ***//
        try {
            cell1 = board.getCellAt(2, 2);

            myMove.setStartingPosition(cell1);

            assertTrue(myMove.getStartingPosition().equals(cell1));

        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue(!checkError);

    }

    /**
     * Check if getOpponentForcedMove(), setOpponentForcedMove( WorkerMove ), resetOpponentForcedMove()
     * can set and return the correct value
     * Methods used:    getMyMove()                     of  Card
     *                  getCellAt( int, int )           of  Board
     *                  getX()                          of  Cell
     *                  getY()                          of  Cell
     *                  position()                      of  Placeable
     *                  place( Cell )                   of  PLaceable (Worker)
     *                  getWorkerId()                   of  Worker
     *
     * Black Box and White Box
     */
    @Test
    void getAndSetAndResetOpponentForcedMove() {
        myMove = humanCard.getMyMove();
        MyMove.WorkerMoved workerMoved;

        final Cell cell1;
        final Cell cell2;


        //*** after initialization ***//
        assertTrue(myMove.getOpponentForcedMove() == null);

        //*** with set methods ***//
        try {
            cell1 = board.getCellAt(2, 2);
            cell2 = board.getCellAt(3, 3);
            worker.place(cell1);
            workerMoved = new MyMove.WorkerMoved(   worker.getWorkerId(),
                                                    worker.position().getX(),
                                                    worker.position().getY(),
                                                    cell2.getX(),
                                                    cell2.getY());

            myMove.setOpponentForcedMove(workerMoved);

            assertTrue(myMove.getStartingPosition().equals(workerMoved));

        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue(!checkError);

        // after resetOpponentForcedMove()
        myMove.resetOpponentForcedMove();
        assertTrue(myMove.getOpponentForcedMove() == null);

    }

    /**
     * Check if get methods of internal class WorkerMove can return the correct value after initialization
     * Methods used:    getMyMove()                     of  Card
     *                  getCellAt( int, int )           of  Board
     *                  getX()                          of  Cell
     *                  getY()                          of  Cell
     *                  position()                      of  Placeable
     *                  place( Cell )                   of  PLaceable (Worker)
     *                  getWorkerId()                   of  Worker
     *
     * Black Box and White Box
     */
    @Test
    void getMethodsWorkerMove() {
        MyMove.WorkerMoved workerMoved;

        final Cell cell1;
        final Cell cell2;

        try {
            cell1 = board.getCellAt(2, 2);
            cell2 = board.getCellAt(3, 3);
            worker.place(cell1);
            workerMoved = new MyMove.WorkerMoved(   worker.getWorkerId(),
                    worker.position().getX(),
                    worker.position().getY(),
                    cell2.getX(),
                    cell2.getY());

            assertTrue( workerMoved.getWorkerId().equals(worker.getWorkerId()) );
            assertTrue(workerMoved.getInitialX() == worker.position().getX() );
            assertTrue(workerMoved.getInitialY() == worker.position().getY() );
            assertTrue(workerMoved.getFinalX() == cell2.getX() );
            assertTrue(workerMoved.getFinalY() == cell2.getY() );

        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue(!checkError);

        // after resetOpponentForcedMove()
        myMove.resetOpponentForcedMove();
        assertTrue(myMove.getOpponentForcedMove() == null);

    }

}