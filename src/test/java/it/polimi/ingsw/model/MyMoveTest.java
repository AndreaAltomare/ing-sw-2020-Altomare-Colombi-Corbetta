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
class MyMoveTest {

    boolean checkOutofBoard;
    boolean checkWin;
    boolean checkExecute;
    MyMove myMove;
    Move move;
    Board board;
    Player player = new Player("cpu");
    Worker worker;

    /**
     * Initialization before method's test
     */
    @BeforeEach
    void setUp() {

        checkOutofBoard = false;
        checkWin = false;
        checkExecute = false;
        board = new IslandBoard();
        worker = new Worker(player);
    }

    /**
     * Reset attribute after method's test
     */
    @AfterEach
    void tearDown() {

        worker = null;
        myMove = null;
        board = null;

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
     *
     * Black Box
     */
    @Test
    void executeMoveDefaultOnBoardBlack() {
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
        myMove = new MyMove(humanCard, human);
        Cell centralCell;
        Cell cornerCell;
        Cell nextCell;
        boolean checkError = false;

        try {
            centralCell = board.getCellAt(2, 2);
            cornerCell = board.getCellAt(0, 0);


            /* Move in near Cells (North, East and South-West) */

            // NORTH
            worker.place(centralCell);
            nextCell = board.getCellAt((worker.position().getX() - 1), worker.position().getY());
            move = new Move(worker.position(), nextCell);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(worker.position().equals(nextCell));
                assertTrue(nextCell.getTop().equals(worker));
                assertTrue(nextCell.getHeigth() == 1);
                assertTrue(nextCell.repOk());
                assertTrue(centralCell.getTop() == null);

            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);


            // EST
            worker.place(centralCell);
            nextCell = board.getCellAt(worker.position().getX(), (worker.position().getY() + 1));
            move = new Move(worker.position(), nextCell);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(worker.position().equals(nextCell));
                assertTrue(nextCell.getTop().equals(worker));
                assertTrue(nextCell.getHeigth() == 1);
                assertTrue(nextCell.repOk());
                assertTrue(centralCell.getTop() == null);

            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);


            // SOUTH-WEST
            worker.place(centralCell);
            nextCell = board.getCellAt((worker.position().getX() + 1), (worker.position().getY() - 1));
            move = new Move(worker.position(), nextCell);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(worker.position().equals(nextCell));
                assertTrue(nextCell.getTop().equals(worker));
                assertTrue(nextCell.getHeigth() == 1);
                assertTrue(nextCell.repOk());
                assertTrue(centralCell.getTop() == null);

            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);


            /* Move in same Cell */
            worker.place(centralCell);
            move = new Move(worker.position(), centralCell);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
                assertTrue(worker.position().equals(centralCell));
                assertTrue(centralCell.getTop().equals(worker));
                assertTrue(centralCell.getHeigth() == 1);
                assertTrue(centralCell.repOk());

            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);


            /* Move in faraway Cells (North, East and South-West) */

            // NORTH
            worker.place(centralCell);
            nextCell = board.getCellAt((worker.position().getX() - 2), worker.position().getY());
            move = new Move(worker.position(), nextCell);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
                assertTrue(worker.position().equals(centralCell));
                assertTrue(centralCell.getHeigth() == 1);
                assertTrue(centralCell.repOk());
                assertTrue(centralCell.getTop().equals(worker));
                assertTrue(nextCell.getTop() == null);

            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);


            // EST
            worker.place(centralCell);
            nextCell = board.getCellAt(worker.position().getX(), (worker.position().getY() + 2));
            move = new Move(worker.position(), nextCell);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
                assertTrue(worker.position().equals(centralCell));
                assertTrue(centralCell.getTop().equals(worker));
                assertTrue(centralCell.getHeigth() == 1);
                assertTrue(centralCell.repOk());
                assertTrue(nextCell.getTop() == null);

            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);


            // SOUTH-WEST
            worker.place(centralCell);
            nextCell = board.getCellAt((worker.position().getX() + 2), (worker.position().getY() - 2));
            move = new Move(worker.position(), nextCell);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
                assertTrue(worker.position().equals(centralCell));
                assertTrue(centralCell.getTop().equals(worker));
                assertTrue(centralCell.getHeigth() == 1);
                assertTrue(centralCell.repOk());
                assertTrue(nextCell.getTop() == null);

            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);


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
     *
     * Black Box
     */
    @Test
    void executeMoveDefaultBetweenCellsBlack() {
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
        myMove = new MyMove(humanCard, human);
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
        boolean checkError = false;
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
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(worker.position().equals(cell1Block));
                assertTrue(cell1Block.getHeigth() == (height + 1));
                assertTrue(cell1Block.getTop().equals(worker));
                assertTrue(cell1Block.getPlaceableAt(0).isBlock());
                assertTrue(cell1Block.repOk());
                assertTrue(freeCell.getTop() == null);
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);


            /* not go up of 2 level */
            worker.place(freeCell);
            height = cell2Block.getHeigth();
            move = new Move(worker.position(), cell2Block);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
                assertTrue(worker.position().equals(freeCell));
                assertTrue(cell2Block.getHeigth() == height);
                assertTrue(cell2Block.getTop().isBlock());
                assertTrue(cell2Block.getPlaceableAt(0).isBlock());
                assertTrue(cell2Block.getPlaceableAt(1).isBlock());
                assertTrue(cell2Block.repOk());
                assertTrue(freeCell.getTop().equals(worker));
                assertTrue(freeCell.getHeigth() == 1);
                assertTrue(freeCell.repOk());
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);



            /* not go up of 3 level */
            worker.place(freeCell);
            height = cell3Block.getHeigth();
            move = new Move(worker.position(), cell3Block);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
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
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);


            /* not go up on dome */
            worker.place(freeCell);
            height = domeCell.getHeigth();
            move = new Move(worker.position(), domeCell);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
                assertTrue(worker.position().equals(freeCell));
                assertTrue(domeCell.getHeigth() == height);
                assertTrue(domeCell.getTop().isDome());
                assertTrue(domeCell.repOk());
                assertTrue(freeCell.getTop().equals(worker));
                assertTrue(freeCell.getHeigth() == 1);
                assertTrue(freeCell.repOk());
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);


            /* not go up on other my worker */
            worker.place(freeCell);
            height = myWorkerCell.getHeigth();
            move = new Move(worker.position(), myWorkerCell);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
                assertTrue(worker.position().equals(freeCell));
                assertTrue(myWorkerCell.getHeigth() == height);
                assertTrue(myWorkerCell.getTop().equals(otherMyWorker));
                assertTrue(otherMyWorker.position().equals(myWorkerCell));
                assertTrue(myWorkerCell.repOk());
                assertTrue(freeCell.getTop().equals(worker));
                assertTrue(freeCell.getHeigth() == 1);
                assertTrue(freeCell.repOk());
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);


            /* not go up on opponent worker */
            worker.place(freeCell);
            height = opponentCell.getHeigth();
            move = new Move(worker.position(), opponentCell);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
                assertTrue(worker.position().equals(freeCell));
                assertTrue(opponentCell.getHeigth() == height);
                assertTrue(opponentCell.getTop().equals(opponentWorker));
                assertTrue(opponentWorker.position().equals(opponentCell));
                assertTrue(opponentCell.repOk());
                assertTrue(freeCell.getTop().equals(worker));
                assertTrue(freeCell.getHeigth() == 1);
                assertTrue(freeCell.repOk());
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);


            /* go down of 1 level */
            worker.place(cell1Block);
            height = cell1Block.getHeigth();
            move = new Move(worker.position(), freeCell);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(worker.position().equals(freeCell));
                assertTrue(cell1Block.getHeigth() == (height - 1));
                assertTrue(cell1Block.getPlaceableAt(0).isBlock());
                assertTrue(cell1Block.repOk());
                assertTrue(freeCell.getTop().equals(worker));
                assertTrue(freeCell.getHeigth() == 1);
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);


            /* go down of 3 level */
            worker.place(cell3Block);
            height = cell3Block.getHeigth();
            move = new Move(worker.position(), freeCell);
            try {
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
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);


            /* Default win condition */
            worker.place(cell2Block);
            height = cell3Block.getHeigth();
            move = new Move(worker.position(), cell3Block);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(checkWin);

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
     *
     * Black Box
     */
    @Test
    void executeMoveApolloBlack() {
        GodPower apollo = new GodPower();
        apollo.setName("Apollo");
        apollo.setEpithet("God of Music");
        apollo.setDescription("A");
        apollo.setMovementsLeft(1);
        apollo.setConstructionLeft(1);
        apollo.setActiveOnOpponentMovement(true);
        apollo.setHotLastMoveDirection(LevelDirection.NONE);
        apollo.setMoveIntoOpponentSpace(true);
        apollo.setForceOpponentInto(FloorDirection.ANY);
        apollo.setDeniedDirection(LevelDirection.NONE);
        apollo.setOpponentDeniedDirection(LevelDirection.NONE);
        Card apolloCard = new Card(apollo);
        myMove = new MyMove(apolloCard, apollo);
        Player opponent = new Player("Opp");
        Cell cell1;
        Cell cell2;
        Worker otherMyWorker = new Worker(player);
        Worker opponentWorker = new Worker(opponent);
        boolean checkError = false;

        try {

            cell1 = board.getCellAt(2, 2);
            cell2 = board.getCellAt(3, 3);

            //todo: review after speak with friends about this
            /* not go in a Cell where there is another my worker */
            worker.place(cell1);
            otherMyWorker.place(cell2);
            move = new Move(cell1, cell2);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
                assertTrue(cell1.getHeigth() == 1);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(cell1.getTop().equals(worker));
                assertTrue(cell2.getTop().equals(otherMyWorker));
                assertTrue(worker.position().equals(cell1));
                assertTrue(otherMyWorker.position().equals(cell2));
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);
            cell2.removeWorker();
            cell1.removeWorker();


            /* go in a Cell where there is a opponent worker */
            worker.place(cell1);
            opponentWorker.place(cell2);
            move = new Move(cell1, cell2);
            try {
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
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);
            cell2.removeWorker();
            cell1.removeWorker();


            /* not go up on a too high Cell where there is a opponent worker */
            worker.place(cell1);
            cell2.buildBlock();
            cell2.buildBlock();
            opponentWorker.place(cell2);
            move = new Move(cell1, cell2);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
                assertTrue(cell1.getHeigth() == 1);
                assertTrue(cell2.getHeigth() == 3);
                assertTrue(cell1.getTop().equals(worker));
                assertTrue(cell2.getTop().equals(opponentWorker));
                assertTrue(cell2.getPlaceableAt(0).isBlock());
                assertTrue(cell2.getPlaceableAt(1).isBlock());
                assertTrue(worker.position().equals(cell1));
                assertTrue(otherMyWorker.position().equals(cell2));
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);
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
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 3);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(cell1.getTop().equals(opponentWorker));
                assertTrue(cell1.getPlaceableAt(0).isBlock());
                assertTrue(cell1.getPlaceableAt(1).isBlock());
                assertTrue(cell1.getPlaceableAt(3).isBlock());
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(worker.position().equals(cell2));
                assertTrue(otherMyWorker.position().equals(cell1));
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);
            cell2.removeWorker();
            cell1.removeWorker();
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
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
     *
     * Black Box
     */
    @Test
    void executeMoveMinotaurBlack() {
        GodPower minotaur = new GodPower();
        minotaur.setName("Minotaur");
        minotaur.setEpithet("Bull-headed Monster");
        minotaur.setDescription("M");
        minotaur.setMovementsLeft(1);
        minotaur.setConstructionLeft(1);
        minotaur.setActiveOnOpponentMovement(true);
        minotaur.setHotLastMoveDirection(LevelDirection.NONE);
        minotaur.setMoveIntoOpponentSpace(true);
        minotaur.setForceOpponentInto(FloorDirection.SAME);
        minotaur.setDeniedDirection(LevelDirection.NONE);
        minotaur.setOpponentDeniedDirection(LevelDirection.NONE);
        Card minotaurCard = new Card(minotaur);
        myMove = new MyMove(minotaurCard, minotaur);
        Player opponent = new Player("Opp");
        Cell cell1;
        Cell cell2;
        Cell backCell;
        Cell cornerCell;
        Worker otherMyWorker = new Worker(player);
        Worker opponentWorker = new Worker(opponent);
        boolean checkError = false;

        try {
            cell1 = board.getCellAt(3, 3);
            cell2 = board.getCellAt(2, 2);
            backCell = board.getCellAt(1, 1);
            cornerCell = board.getCellAt(4, 4);

            //todo: review after speak with friends about this
            /* not go on a Cell where there is another my worker */
            worker.place(cell1);
            otherMyWorker.place(cell2);
            move = new Move(cell1, cell2);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
                assertTrue(cell1.getHeigth() == 1);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(cell1.getTop().equals(worker));
                assertTrue(cell2.getTop().equals(otherMyWorker));
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(worker.position().equals(cell1));
                assertTrue(otherMyWorker.position().equals(cell2));
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);
            cell1.removePlaceable();
            cell2.removePlaceable();


            /* go on a Cell where there is a opponent worker and back a free Cell */
            worker.place(cell1);
            opponentWorker.place(cell2);
            move = new Move(cell1, cell2);
            try {
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
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);
            cell1.removePlaceable();
            cell2.removePlaceable();
            backCell.removePlaceable();


            /* go on a Cell where there is a opponent worker and back a Cell with a Block */
            worker.place(cell1);
            opponentWorker.place(cell2);
            backCell.buildBlock();
            move = new Move(cell1, cell2);
            try {
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
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);
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
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);
            cell1.removePlaceable();
            cell2.removePlaceable();
            while (backCell.getTop() != null) {
                backCell.removePlaceable();
            }


            /* go on a Cell where there is a opponent worker and back a Cell with a Dome */
            worker.place(cell1);
            opponentWorker.place(cell2);
            backCell.buildDome();
            move = new Move(cell1, cell2);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
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
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);
            cell1.removePlaceable();
            cell2.removePlaceable();
            backCell.removePlaceable();

            /* go on a Cell where there is a opponent worker and back a Cell with a worker */
            worker.place(cell1);
            opponentWorker.place(cell2);
            otherMyWorker.place(backCell);
            move = new Move(cell1, cell2);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
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
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);
            cell1.removePlaceable();
            cell2.removePlaceable();
            backCell.removePlaceable();


            /* go on a Cell where there is a opponent worker at high level and back a free Cell */
            cell1.buildBlock();
            worker.place(cell1);
            cell2.buildBlock();
            cell2.buildBlock();
            opponentWorker.place(cell2);
            move = new Move(cell1, cell2);
            try {
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
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);
            cell1.removePlaceable();
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }
            backCell.removePlaceable();


            /* go on a Cell where there is a opponent worker at too high level and back a free Cell */
            worker.place(cell1);
            cell2.buildBlock();
            cell2.buildBlock();
            opponentWorker.place(cell2);
            move = new Move(cell1, cell2);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
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
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);
            cell1.removePlaceable();
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }
            backCell.removePlaceable();


            /* not go on a Cell where there is a opponent and back a Cell out the board */
            worker.place(cell1);
            opponentWorker.place(cornerCell);
            move = new Move(cell1, cornerCell);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
                assertTrue(cell1.getHeigth() == 1);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(cell1.getTop().equals(worker));
                assertTrue(cornerCell.getTop().equals(opponentWorker));
                assertTrue(cell1.repOk());
                assertTrue(cornerCell.repOk());
                assertTrue(worker.position().equals(cell1));
                assertTrue(opponentWorker.position().equals(cornerCell));
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);
            cell1.removePlaceable();
            cell2.removePlaceable();

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
     *
     * Black Box
     */
    @Test
    void executeMoveArtemisBlack() {
        GodPower artemis = new GodPower();
        artemis.setName("Artemis");
        artemis.setEpithet("Goddess of the Hunt");
        artemis.setDescription("A");
        artemis.setMovementsLeft(2);
        artemis.setConstructionLeft(1);
        artemis.setStartingSpaceDenied(true);
        artemis.setActiveOnOpponentMovement(true);
        artemis.setHotLastMoveDirection(LevelDirection.NONE);
        artemis.setForceOpponentInto(FloorDirection.NONE);
        artemis.setDeniedDirection(LevelDirection.NONE);
        artemis.setOpponentDeniedDirection(LevelDirection.NONE);
        Card artemisCard = new Card(artemis);
        myMove = new MyMove(artemisCard, artemis);
        Cell cell1;
        Cell cell2;
        Cell cell3;
        boolean checkError = false;

        try {
            cell1 = board.getCellAt(1, 1);
            cell2 = board.getCellAt(2, 2);
            cell3 = board.getCellAt(3, 3);


            /* go from cell1 to cell2 then from cell2 to cell3 */
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 0);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(cell1.getTop() == null);
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(worker.position().equals(cell2));
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);

            myMove.decreaseMovesLeft();
            artemisCard.setMovementExecuted(true);

            move = new Move(worker.position(), cell3);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell2.getHeigth() == 0);
                assertTrue(cell3.getHeigth() == 1);
                assertTrue(cell2.getTop() == null);
                assertTrue(cell3.getTop().equals(worker));
                assertTrue(cell2.repOk());
                assertTrue(cell3.repOk());
                assertTrue(worker.position().equals(cell3));
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);

            cell1.removePlaceable();
            cell2.removePlaceable();
            cell3.removePlaceable();
            myMove.resetMovesLeft();
            artemisCard.setMovementExecuted(false);


            /* go from cell1 to cell2 then not from cell2 to cell1 */
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 0);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(cell1.getTop() == null);
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(worker.position().equals(cell2));
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);

            myMove.decreaseMovesLeft();
            artemisCard.setMovementExecuted(true);

            move = new Move(worker.position(), cell1);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(cell1.getHeigth() == 0);
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(cell1.getTop() == null);
                assertTrue(cell2.repOk());
                assertTrue(cell1.repOk());
                assertTrue(worker.position().equals(cell2));
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);

            cell1.removePlaceable();
            cell2.removePlaceable();
            cell3.removePlaceable();
            myMove.resetMovesLeft();
            artemisCard.setMovementExecuted(false);

        } catch (OutOfBoardException e) {
            checkError = true;
        }
        assertTrue(!checkError);
    }

    /**
     * Check if Prometheus's power is correctly checked and executed after the first construction
     * Methods used:        getCellAt( int, int)    of  IslandBoard
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
     * Black Box
     */
    @Test
    void executeMovePrometheusBlack() {
        //TODO: Check again after create test for MyConstructor
        GodPower prometheus = new GodPower();
        prometheus.setName("Prometheus");
        prometheus.setEpithet("Titan Benefactor of Mankind");
        prometheus.setDescription("P");
        prometheus.setMovementsLeft(1);
        prometheus.setConstructionLeft(1); // after the first construction from 2 become 1
        prometheus.setActiveOnOpponentMovement(true);
        prometheus.setHotLastMoveDirection(LevelDirection.UP);
        prometheus.setForceOpponentInto(FloorDirection.NONE);
        prometheus.setDeniedDirection(LevelDirection.UP);
        prometheus.setOpponentDeniedDirection(LevelDirection.NONE);
        prometheus.setBuildBeforeMovement(true);
        Card humanCard = new Card(prometheus);
        myMove = new MyMove(humanCard, prometheus);
        Cell cell1;
        Cell cell2;
        boolean checkError = false;

        try {
            cell1 = board.getCellAt(1, 1);
            cell2 = board.getCellAt(2, 2);

            /* go down after construction */
            cell1.buildBlock();
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(checkExecute);
                assertTrue(cell1.getHeigth() == 1);
                assertTrue(cell2.getHeigth() == 1);
                assertTrue(cell1.getTop().isBlock());
                assertTrue(cell2.getTop().equals(worker));
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(worker.position().equals(cell2));
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);

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
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);

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
            try {
                checkExecute = myMove.executeMove(move, worker);
                assertTrue(!checkExecute);
                assertTrue(cell1.getHeigth() == 2);
                assertTrue(cell2.getHeigth() == 2);
                assertTrue(cell1.getTop().equals(worker));
                assertTrue(cell1.getPlaceableAt(0).isBlock());
                assertTrue(cell2.getTop().isBlock());
                assertTrue(cell2.getPlaceableAt(0).isBlock());
                assertTrue(cell1.repOk());
                assertTrue(cell2.repOk());
                assertTrue(worker.position().equals(cell1));
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);

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

//TODO: Add test case for executeMove() in White Box if it is necessary

    /**
     * Check if default rules ( in CheckMove() ) are correctly checked
     * Methods used:        decreaseMovesLeft()     of  MyMove
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
        myMove = new MyMove(humanCard, human);
        Cell cell1;
        Cell cell2;
        Worker otherWorker = new Worker(null);
        boolean check;
        boolean checkError = false;

        try {
            cell1 = board.getCellAt(2, 2);
            cell2 = board.getCellAt(3, 3);


            /* can not move when MovementsLeft <= 0 */
            myMove.decreaseMovesLeft();
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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
            myMove.resetMovesLeft();


            /* can not move on the same Cell where the worker is  */
            worker.place(cell1);
            move = new Move(worker.position(), worker.position());
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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


            /* can not move on a Cell where there is a Worker  */
            worker.place(cell1);
            otherWorker.place(cell2);
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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


            /* can not move on a Cell where there is a Dome  */
            worker.place(cell1);
            cell2.buildDome();
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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


            /* can move on a near Cell without Dome or Worker*/
            worker.place(cell1);
            cell2.buildBlock();
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(check);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().isBlock());
            ;
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
     * Check if special rules ( in CheckMove() ) are correctly checked
     * Methods used:        decreaseMovesLeft()                 of  MyMove
     *                      resetMovesLeft                      of  MyMove
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
        mutantGod.setMovementsLeft(2);
        mutantGod.setConstructionLeft(2);
        mutantGod.setActiveOnMyMovement(true);
        mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
        mutantGod.setForceOpponentInto(FloorDirection.NONE);
        mutantGod.setDeniedDirection(LevelDirection.NONE);
        mutantGod.setOpponentDeniedDirection(LevelDirection.NONE);
        Card mutantCard = new Card(mutantGod);
        myMove = new MyMove(mutantCard, mutantGod);
        Player opponent = new Player("Opp");
        Cell cell1;
        Cell cell2;
        Cell cell3;
        Worker otherWorker = new Worker(player);
        Worker opponentWorker = new Worker(opponent);
        boolean check;
        boolean checkError = false;

        try {
            cell1 = board.getCellAt(2, 2);
            cell2 = board.getCellAt(3, 3);


            /* can not move when MovementsLeft <= 0 */
            myMove.decreaseMovesLeft();
            myMove.decreaseMovesLeft();
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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
            myMove.resetMovesLeft();


            /* can not move on the same Cell where the worker is  */
            worker.place(cell1);
            move = new Move(worker.position(), worker.position());
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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


            /* can not move on the Cell when it is too faraway  */

            // CASE 1: cell2.getX() >> cell1.getX()
            worker.place(cell1);
            cell2 = board.getCellAt(cell1.getX() + 2, cell1.getY());
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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


            //* GOD WITHOUT POWER TO GO IN CELL WITH OTHER WORKER *//

            mutantGod.setActiveOnMyMovement(false);

            /* can not move on a Cell where there is a Worker  */
            worker.place(cell1);
            opponentWorker.place(cell2);
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().equals(opponentWorker));
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(worker.position().equals(cell1));
            assertTrue(opponentWorker.position().equals(cell2));

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
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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


            //* ARTEMIS PART: GOD WITH START_SPACE_DENIED *//

            mutantGod.setStartingSpaceDenied(true);

            /* can go on a Cell like first movement */
            worker.place(cell1);
            cell2.buildBlock();
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(check);
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


            mutantCard.setMovementExecuted(true);

            /* can not return on first cell after first movement*/
            cell2.buildBlock();
            worker.place(cell2);
            move = new Move(worker.position(), cell1);
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
            assertTrue(cell1.getHeigth() == 0);
            assertTrue(cell2.getHeigth() == 2);
            assertTrue(cell1.getTop() == null);
            assertTrue(cell2.getTop().equals(worker));
            assertTrue(cell2.getPlaceableAt(0).isBlock());
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(worker.position().equals(cell2));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }


            /* can go on different cell after first movement*/
            cell2.buildBlock();
            worker.place(cell2);
            cell3 = board.getCellAt(cell2.getX(), cell2.getY() - 1);
            move = new Move(worker.position(), cell3);
            check = myMove.checkMove(move, worker);
            assertTrue(check);
            assertTrue(cell2.getHeigth() == 2);
            assertTrue(cell3.getHeigth() == 0);
            assertTrue(cell2.getTop().equals(worker));
            assertTrue(cell2.getPlaceableAt(0).isBlock());
            assertTrue(cell3.getTop() == null);
            assertTrue(cell2.repOk());
            assertTrue(cell1.repOk());
            assertTrue(worker.position().equals(cell2));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }
            while (cell3.getTop() != null) {
                cell3.removePlaceable();
            }

            // mutantGod loses Artemis' power
            mutantGod.setStartingSpaceDenied(false);
            mutantCard.setMovementExecuted(false);
            myMove.resetMovesLeft();


            //* PROMETHEUS PART: GOD WITH HOT_LAST_MOVEMENT_DIRECTION BUT WITHOUT MOVE_INTO_OPPONENT_SPACE *//

            mutantGod.setHotLastMoveDirection(LevelDirection.UP);

            /* can go up if it doesn't build before movement */
            cell2.buildBlock();
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(check);
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

            mutantCard.getMyConstruction().decreaseConstructionLeft();

            /* can go down if it builds before movement */
            cell1.buildBlock();
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(check);
            assertTrue(cell1.getHeigth() == 2);
            assertTrue(cell2.getHeigth() == 0);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell1.getPlaceableAt(0).isBlock());
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


            /* can't go up if it builds before movement */
            cell2.buildBlock();
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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

            mutantGod.setHotLastMoveDirection(LevelDirection.NONE);
            mutantCard.getMyConstruction().resetConstructionLeft();


            //* APOLLO AND MINOTAUR PART: GOD WITH MOVE_INTO_OPPONENT_SPACE BUT WITHOUT HOT_LAST_MOVEMENT_DIRECTION *//

            mutantGod.setMoveIntoOpponentSpace(true);

            /* can move when Cell is free */
            cell2.buildBlock();
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(check);
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


            /* can not move when Cell has a Dome */
            cell2.buildDome();
            worker.place(cell1);
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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


            //* Apollo: forceOpponentInto == ANY *//
            mutantGod.setForceOpponentInto(FloorDirection.ANY);

            /* can go on a Cell with opponent's Worker */
            worker.place(cell1);
            opponentWorker.place(cell2);
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(check);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().equals(opponentWorker));
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(worker.position().equals(cell1));
            assertTrue(opponentWorker.position().equals(cell2));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }

            //todo: review after speeck with friends about this
            /* can't go on a Cell with my Worker */
            worker.place(cell1);
            otherWorker.place(cell2);
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
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


            //* Minotaur: forceOpponentInto != ANY *//
            mutantGod.setForceOpponentInto(FloorDirection.SAME);
            cell3 = board.getCellAt(cell2.getX(), cell2.getY() - 1);

            /* can go on a Cell with opponent's Worker and back a Cell with some Blocks*/
            worker.place(cell1);
            opponentWorker.place(cell2);
            cell3.buildBlock();
            cell3.buildBlock();
            cell3.buildBlock();
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(check);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(cell3.getHeigth() == 3);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().equals(opponentWorker));
            assertTrue(cell3.getPlaceableAt(0).isBlock());
            assertTrue(cell3.getPlaceableAt(1).isBlock());
            assertTrue(cell3.getPlaceableAt(2).isBlock());
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(cell3.repOk());
            assertTrue(worker.position().equals(cell1));
            assertTrue(opponentWorker.position().equals(cell2));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }
            while (cell3.getTop() != null) {
                cell3.removePlaceable();
            }

            //todo: review after speeck with friends about this
            /* can not go on a Cell with my Worker and back a Cell with some Blocks*/
            worker.place(cell1);
            otherWorker.place(cell2);
            cell3.buildBlock();
            cell3.buildBlock();
            cell3.buildBlock();
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(cell3.getHeigth() == 3);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().equals(otherWorker));
            assertTrue(cell3.getPlaceableAt(0).isBlock());
            assertTrue(cell3.getPlaceableAt(1).isBlock());
            assertTrue(cell3.getPlaceableAt(2).isBlock());
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(cell3.repOk());
            assertTrue(worker.position().equals(cell1));
            assertTrue(otherWorker.position().equals(cell2));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }
            while (cell3.getTop() != null) {
                cell3.removePlaceable();
            }

            /* can not go on a Cell with opponent's Worker and back a Cell with Dome */
            worker.place(cell1);
            opponentWorker.place(cell2);
            cell3.buildDome();
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(cell3.getHeigth() == 1);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().equals(opponentWorker));
            assertTrue(cell3.getTop().isDome());
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(cell3.repOk());
            assertTrue(worker.position().equals(cell1));
            assertTrue(opponentWorker.position().equals(cell2));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }
            while (cell3.getTop() != null) {
                cell3.removePlaceable();
            }

            /* can not go on a Cell with opponent's Worker and back a Cell with Worker */
            worker.place(cell1);
            opponentWorker.place(cell2);
            otherWorker.place(cell3);
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(cell3.getHeigth() == 1);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().equals(opponentWorker));
            assertTrue(cell3.getTop().equals(otherWorker));
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(cell3.repOk());
            assertTrue(worker.position().equals(cell1));
            assertTrue(opponentWorker.position().equals(cell2));
            assertTrue(otherWorker.position().equals(cell3));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }
            while (cell3.getTop() != null) {
                cell3.removePlaceable();
            }

            /* can not go on a Cell with opponent's Worker and back nothing  */
            cell1 = board.getCellAt(3, 3);
            cell2 = board.getCellAt(4, 4);
            worker.place(cell1);
            opponentWorker.place(cell2);
            move = new Move(worker.position(), cell2);
            check = myMove.checkMove(move, worker);
            assertTrue(!check);
            assertTrue(cell1.getHeigth() == 1);
            assertTrue(cell2.getHeigth() == 1);
            assertTrue(cell1.getTop().equals(worker));
            assertTrue(cell2.getTop().equals(opponentWorker));
            assertTrue(cell1.repOk());
            assertTrue(cell2.repOk());
            assertTrue(worker.position().equals(cell1));
            assertTrue(opponentWorker.position().equals(cell2));

            // clear board
            while (cell1.getTop() != null) {
                cell1.removePlaceable();
            }
            while (cell2.getTop() != null) {
                cell2.removePlaceable();
            }
            while (cell3.getTop() != null) {
                cell3.removePlaceable();
            }


        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue(!checkError);

    }

    /**
     * Check if getGodPower() can return the correct value after initialization
     *
     * Black Box and White Box
     */
    @Test
    void getGodPower() {
        GodPower godPower = new GodPower();
        godPower.setConstructionLeft(0);
        godPower.setMovementsLeft(0);
        Card card = new Card(godPower);
        myMove = new MyMove(card, godPower);

        assertTrue(myMove.getGodPower().equals(godPower));

    }

    /**
     * Check if getLastMove() can return the correct value after a move correct or not
     * Methods used:        executeMove( move, worker)      of  MyMove
     *                      getCellAt( int)                 of  IslandBoard
     *                      position()                      of  Placeable
     *                      place()                         of  Worker
     *
     * Black Box and White Box
     */
    @Test
    void getLastMove() {
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
        myMove = new MyMove(humanCard, human);
        Move correctMove;
        Move wrongMove;
        Cell cell1;
        Cell cell2;
        Worker otherWorker = new Worker(null);
        boolean checkError = false;


        // after initialization
        assertTrue(myMove.getLastMove() == null);


        try {
            cell1 = board.getCellAt(2, 2);
            cell2 = board.getCellAt(3, 3);

            //correct move: from cell1 to cell2
            worker.place(cell1);
            correctMove = new Move(worker.position(), cell2);
            try {
                checkExecute = myMove.executeMove(correctMove, worker);
                assertTrue(checkExecute);
                assertTrue(myMove.getLastMove().equals(correctMove));
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);


            //wrong move: return on cell1 but is occupied
            otherWorker.place(cell1);
            wrongMove = new Move(worker.position(), cell1);
            try {
                checkExecute = myMove.executeMove(wrongMove, worker);
                assertTrue(!checkExecute);
                assertTrue(myMove.getLastMove().equals(correctMove)); // lastMove doesn't change
            } catch (OutOfBoardException o) {
                checkOutofBoard = true;
            } catch (WinException w) {
                checkWin = true;
            }
            assertTrue(!checkOutofBoard);
            assertTrue(!checkWin);

        } catch (OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue(!checkError);

    }

    /**
     * Check if getMovesLeft(), decreaseMovesLeft(), resetMovesLeft() can set and return the correct vaòue
     *
     * Black Box and White Box
     */
    @Test
    void getMovesLeftAndDecreaseMovesLeftAndResetMovesLeft() {
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
        myMove = new MyMove(humanCard, human);

        // after initialization
        assertTrue(myMove.getMovesLeft() == 1);

        // after decreaseMovesLeft
        myMove.resetMovesLeft();
        assertTrue(myMove.getMovesLeft() == 0);

        // after resetMovesLeft()
        myMove.resetMovesLeft();
        assertTrue(myMove.getMovesLeft() == 1);

    }
}