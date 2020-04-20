package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import sun.java2d.opengl.WGLSurfaceData;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for AdversaryMove class, aimed to verify it works properly
 *
 * @author Marco
 */
class AdversaryMoveTest {
    Board board;
    boolean checkError;

    /**
     * Initialization before method's test
     */
    @BeforeEach
    void setUp() {

        board = new IslandBoard();
        checkError = false;

    }

    /**
     * Reset attribute after method's test
     */
    @AfterEach
    void tearDown() {

        board = null;

    }

    /**
     * Check if Athena's Power can correctly check opponent's Move ( without move )
     * Methods' used:       executeMove( Move, Worker )     of  MyMove
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
        Player athenaPlayer = new Player("Athena's disciple");
        Player opponentPlayer = new Player("Opponent");
        GodPower athenaPower = new GodPower();
        athenaPower.setName("Athena");
        athenaPower.setEpithet("Goddess of Wisdom");
        athenaPower.setDescription("A");
        athenaPower.setMovementsLeft(1);
        athenaPower.setConstructionLeft(1);
        athenaPower.setMustObey(true);
        athenaPower.setHotLastMoveDirection(LevelDirection.UP);
        athenaPower.setForceOpponentInto(FloorDirection.NONE);
        athenaPower.setDeniedDirection(LevelDirection.NONE);
        athenaPower.setActiveOnOpponentMovement(true);
        athenaPower.setOpponentDeniedDirection(LevelDirection.UP);
        Card athenaCard = new Card(athenaPower);
        MyMove athenaMyMove = new MyMove(athenaCard, athenaPower);
        AdversaryMove athenaAdversaryMove = new AdversaryMove(athenaCard, athenaPower);
        Move athenaMove;
        Move opponentMove;
        Cell cellA1;
        Cell cellA2;
        Cell cellO1;
        Cell cellO2;
        Worker athenaWorker = new Worker(athenaPlayer);
        Worker opponentWorker = new Worker(opponentPlayer);
        boolean check;
        boolean checkLose = false;
        boolean checkExecute;

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
                check = athenaAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue(check);
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
            }
            assertTrue(!checkLose);

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
            checkExecute = athenaMyMove.executeMove(athenaMove, athenaWorker);
            assertTrue(checkExecute);
            opponentMove = new Move(opponentWorker.position(), cellO2);
            try {
                check = athenaAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue(check);
                assertTrue(cellA1.getHeigth() == 1);
                assertTrue(cellA2.getHeigth() == 2);
                assertTrue(cellO1.getHeigth() == 1);
                assertTrue(cellO2.getHeigth() == 1);
                assertTrue(cellA1.getTop().isWorker());
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
            }
            assertTrue(!checkLose);

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
            checkExecute = athenaMyMove.executeMove(athenaMove, athenaWorker);
            assertTrue(checkExecute);
            opponentMove = new Move(opponentWorker.position(), cellO2);
            try {
                check = athenaAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue(check);
                assertTrue(cellA1.getHeigth() == 2);
                assertTrue(cellA2.getHeigth() == 1);
                assertTrue(cellO1.getHeigth() == 1);
                assertTrue(cellO2.getHeigth() == 1);
                assertTrue(cellA1.getTop().isWorker());
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
            }
            assertTrue(!checkLose);

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
            checkExecute = athenaMyMove.executeMove(athenaMove, athenaWorker);
            assertTrue(checkExecute);

            /* opponent's Worker can stay at same level */
            cellO1.buildBlock();
            cellO2.buildBlock();
            opponentWorker.place(cellO1);
            opponentMove = new Move(opponentWorker.position(), cellO2);
            try {
                check = athenaAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue(check);
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
            }
            assertTrue(!checkLose);

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
                check = athenaAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue(check);
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
            }
            assertTrue(!checkLose);

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
                check = athenaAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue(!check);
            } catch (LoseException l) {
                checkLose = true;
            }
            assertTrue(checkLose);
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
        } catch (WinException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue(!checkError);


    }


    /**
     * Check if checkMove() can return the correct value ( only check )
     * Methods' used:       executeMove( Move, Worker )     of  MyMove
     *                      getCellAt( int, int )           of  Board
     *                      getHeigth()                     of  Cell
     *                      getTop()                        of  Cell
     *                      getPlaceableAt( int )           of  Cell
     *                      repOk()                         of  Cell
     *                      removeWorker()                  of  Cell
     *                      buildBlock()                    of  Cell
     *                      position()                      of  Placeable
     *                      isBlock()                       of  Placeable/Block
     *                      place( Cell)                    of  Worker
     *
     * White Box
     */
    @Test
    void checkMoveWhite() {
        Player mutantPlayer = new Player("Mutant's disciple");
        Player opponentPlayer = new Player("opponent");
        GodPower mutantPower = new GodPower(); // change GodPower's attribute during test
        mutantPower.setName("Mutant");
        mutantPower.setEpithet("mutant");
        mutantPower.setDescription("M");
        mutantPower.setMovementsLeft(1);
        mutantPower.setConstructionLeft(1);
        mutantPower.setHotLastMoveDirection(LevelDirection.NONE);
        mutantPower.setForceOpponentInto(FloorDirection.NONE);
        mutantPower.setDeniedDirection(LevelDirection.NONE);
        mutantPower.setOpponentDeniedDirection(LevelDirection.NONE);
        Card mutantCard = new Card(mutantPower);
        MyMove mutantMyMove = new MyMove(mutantCard, mutantPower);
        AdversaryMove mutantAdversaryMove = new AdversaryMove(mutantCard, mutantPower);
        Move mutantMove;
        Move opponentMove;
        Cell startMutantCell;
        Cell highMutantCell;
        Cell lowMutantCell;
        Cell startOpponentCell;
        Cell highOpponentCell;
        Cell lowOpponentCell;
        Worker mutantWorker = new Worker(mutantPlayer);
        Worker opponentWorker = new Worker(opponentPlayer);
        boolean check;
        boolean checkLose = false;
        boolean checkExecute;

        try {
            highMutantCell = board.getCellAt(1,1);
            startMutantCell = board.getCellAt(1,2);
            lowMutantCell = board.getCellAt(1,3);
            highOpponentCell = board.getCellAt(3,1);
            startOpponentCell = board.getCellAt(3,2);
            lowOpponentCell = board.getCellAt(3,3);
            startMutantCell.buildBlock();
            startOpponentCell.buildBlock();
            highMutantCell.buildBlock();
            highMutantCell.buildBlock();
            highOpponentCell.buildBlock();
            highOpponentCell.buildBlock();
            mutantWorker.place( startMutantCell );
            opponentWorker.place( startOpponentCell );


            /* mutantPower without activeOnOpponentMovement: opponent's Worker can move  */
            mutantPower.setActiveOnOpponentMovement( false );

            opponentMove = new Move( opponentWorker.position(), highOpponentCell);
            try {
                check = mutantAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue( check );
                assertTrue( highMutantCell.getHeigth() == 2);
                assertTrue( startMutantCell.getHeigth() == 2);
                assertTrue( lowMutantCell.getHeigth() == 0);
                assertTrue( highOpponentCell.getHeigth() == 2);
                assertTrue( startOpponentCell.getHeigth() == 2);
                assertTrue( lowOpponentCell.getHeigth() == 0);
                assertTrue( highMutantCell.getTop().isBlock() );
                assertTrue( highMutantCell.getPlaceableAt(0).isBlock() );
                assertTrue( startMutantCell.getTop().equals( mutantWorker) );
                assertTrue( startMutantCell.getPlaceableAt(0).isBlock() );
                assertTrue( lowMutantCell.getTop() == null );
                assertTrue( highOpponentCell.getTop().isBlock() );
                assertTrue( highOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( startOpponentCell.getTop().equals( opponentWorker ) );
                assertTrue( startOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( lowOpponentCell.getTop() == null );
                assertTrue( highMutantCell.repOk() );
                assertTrue( startMutantCell.repOk() );
                assertTrue( lowMutantCell.repOk() );
                assertTrue( highOpponentCell.repOk() );
                assertTrue( startOpponentCell.repOk() );
                assertTrue( lowOpponentCell.repOk() );
                assertTrue( mutantWorker.position().equals( startMutantCell ));
                assertTrue( opponentWorker.position().equals( startOpponentCell ));
            } catch (LoseException l) {
                checkLose = true;
            }
            assertTrue( !checkLose );


            /*  mutantPower with activeOnOpponentMovement == true but opponent's Worker is first to move:
                opponent's Worker can move  */
            mutantPower.setActiveOnOpponentMovement( true );

            opponentMove = new Move( opponentWorker.position(), highOpponentCell);
            try {
                check = mutantAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue( check );
                assertTrue( highMutantCell.getHeigth() == 2);
                assertTrue( startMutantCell.getHeigth() == 2);
                assertTrue( lowMutantCell.getHeigth() == 0);
                assertTrue( highOpponentCell.getHeigth() == 2);
                assertTrue( startOpponentCell.getHeigth() == 2);
                assertTrue( lowOpponentCell.getHeigth() == 0);
                assertTrue( highMutantCell.getTop().isBlock() );
                assertTrue( highMutantCell.getPlaceableAt(0).isBlock() );
                assertTrue( startMutantCell.getTop().equals( mutantWorker) );
                assertTrue( startMutantCell.getPlaceableAt(0).isBlock() );
                assertTrue( lowMutantCell.getTop() == null );
                assertTrue( highOpponentCell.getTop().isBlock() );
                assertTrue( highOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( startOpponentCell.getTop().equals( opponentWorker) );
                assertTrue( startOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( lowOpponentCell.getTop() == null );
                assertTrue( highMutantCell.repOk() );
                assertTrue( startMutantCell.repOk() );
                assertTrue( lowMutantCell.repOk() );
                assertTrue( highOpponentCell.repOk() );
                assertTrue( startOpponentCell.repOk() );
                assertTrue( lowOpponentCell.repOk() );
                assertTrue( mutantWorker.position().equals( startMutantCell ));
                assertTrue( opponentWorker.position().equals( startOpponentCell ));
            } catch (LoseException l) {
                checkLose = true;
            }
            assertTrue( !checkLose );


            /*  mutantPower with activeOnOpponentMovement == true,
                mutantWorker moves with a LevelDirection != hotLastMoveDirection of mutantPower:
                opponent's Worker can move  */
            mutantPower.setActiveOnOpponentMovement( true );
            mutantPower.setHotLastMoveDirection( LevelDirection.UP );

            mutantMove = new Move( mutantWorker.position(), lowMutantCell );
            checkExecute = mutantMyMove.executeMove( mutantMove, mutantWorker );
            assertTrue( checkExecute );
            opponentMove = new Move( opponentWorker.position(), highOpponentCell);
            try {
                check = mutantAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue( check );
                assertTrue( highMutantCell.getHeigth() == 2);
                assertTrue( startMutantCell.getHeigth() == 1);
                assertTrue( lowMutantCell.getHeigth() == 1);
                assertTrue( highOpponentCell.getHeigth() == 2);
                assertTrue( startOpponentCell.getHeigth() == 2);
                assertTrue( lowOpponentCell.getHeigth() == 0);
                assertTrue( highMutantCell.getTop().isBlock() );
                assertTrue( highMutantCell.getPlaceableAt(0).isBlock() );
                assertTrue( startMutantCell.getTop().isWorker() );
                assertTrue( lowMutantCell.getTop().equals( mutantWorker ) );
                assertTrue( highOpponentCell.getTop().isBlock() );
                assertTrue( highOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( startOpponentCell.getTop().equals( opponentWorker) );
                assertTrue( startOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( lowOpponentCell.getTop() == null );
                assertTrue( highMutantCell.repOk() );
                assertTrue( startMutantCell.repOk() );
                assertTrue( lowMutantCell.repOk() );
                assertTrue( highOpponentCell.repOk() );
                assertTrue( startOpponentCell.repOk() );
                assertTrue( lowOpponentCell.repOk() );
                assertTrue( mutantWorker.position().equals( lowMutantCell ));
                assertTrue( opponentWorker.position().equals( startOpponentCell ));
            } catch (LoseException l) {
                checkLose = true;
            }
            assertTrue( !checkLose );

            // reset board situation
            lowMutantCell.removeWorker();
            mutantWorker.place(startMutantCell);


            /*  mutantPower with activeOnOpponentMovement == true,
                mutantWorker moves with a LevelDirection == hotLastMoveDirection of mutantPower,
                opponentWorker moves with a LevelDirection != opponentDeniedDirection of mutantPower:
                opponent's Worker can move  */
            mutantPower.setActiveOnOpponentMovement( true );
            mutantPower.setHotLastMoveDirection( LevelDirection.UP );
            mutantPower.setOpponentDeniedDirection(LevelDirection.UP);

            mutantMove = new Move( mutantWorker.position(), highMutantCell );
            checkExecute = mutantMyMove.executeMove( mutantMove, mutantWorker );
            assertTrue( checkExecute );
            opponentMove = new Move( opponentWorker.position(), lowOpponentCell );
            try {
                check = mutantAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue( check );
                assertTrue( highMutantCell.getHeigth() == 3);
                assertTrue( startMutantCell.getHeigth() == 1);
                assertTrue( lowMutantCell.getHeigth() == 0);
                assertTrue( highOpponentCell.getHeigth() == 2);
                assertTrue( startOpponentCell.getHeigth() == 2);
                assertTrue( lowOpponentCell.getHeigth() == 0);
                assertTrue( highMutantCell.getTop().equals( mutantWorker ) );
                assertTrue( highMutantCell.getPlaceableAt(0).isBlock() );
                assertTrue( highMutantCell.getPlaceableAt(1).isBlock() );
                assertTrue( startMutantCell.getTop().isBlock() );
                assertTrue( lowMutantCell.getTop() == null );
                assertTrue( highOpponentCell.getTop().isBlock() );
                assertTrue( highOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( startOpponentCell.getTop().equals( opponentWorker) );
                assertTrue( startOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( lowOpponentCell.getTop() == null );
                assertTrue( highMutantCell.repOk() );
                assertTrue( startMutantCell.repOk() );
                assertTrue( lowMutantCell.repOk() );
                assertTrue( highOpponentCell.repOk() );
                assertTrue( startOpponentCell.repOk() );
                assertTrue( lowOpponentCell.repOk() );
                assertTrue( mutantWorker.position().equals( highMutantCell ));
                assertTrue( opponentWorker.position().equals( startOpponentCell ));
            } catch (LoseException l) {
                checkLose = true;
            }
            assertTrue( !checkLose );

            // reset board situation
            highMutantCell.removeWorker();
            mutantWorker.place(startMutantCell);


             /*  mutantPower with activeOnOpponentMovement == true,
                mutantWorker moves with a LevelDirection == hotLastMoveDirection of mutantPower,
                opponentWorker moves with a LevelDirection == opponentDeniedDirection of mutantPower,
                mutantPower's mustObey == false:
                opponent's Worker can't move  */
            mutantPower.setActiveOnOpponentMovement( true );
            mutantPower.setHotLastMoveDirection( LevelDirection.UP );
            mutantPower.setOpponentDeniedDirection(LevelDirection.UP);
            mutantPower.setMustObey( false );

            mutantMove = new Move( mutantWorker.position(), highMutantCell );
            checkExecute = mutantMyMove.executeMove( mutantMove, mutantWorker );
            assertTrue( checkExecute );
            opponentMove = new Move( opponentWorker.position(), highOpponentCell );
            try {
                check = mutantAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue( !check );
                assertTrue( highMutantCell.getHeigth() == 3);
                assertTrue( startMutantCell.getHeigth() == 1);
                assertTrue( lowMutantCell.getHeigth() == 0);
                assertTrue( highOpponentCell.getHeigth() == 2);
                assertTrue( startOpponentCell.getHeigth() == 2);
                assertTrue( lowOpponentCell.getHeigth() == 0);
                assertTrue( highMutantCell.getTop().equals( mutantWorker ) );
                assertTrue( highMutantCell.getPlaceableAt(0).isBlock() );
                assertTrue( highMutantCell.getPlaceableAt(1).isBlock() );
                assertTrue( startMutantCell.getTop().isBlock() );
                assertTrue( lowMutantCell.getTop() == null );
                assertTrue( highOpponentCell.getTop().isBlock() );
                assertTrue( highOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( startOpponentCell.getTop().equals( opponentWorker ) );
                assertTrue( startOpponentCell.getPlaceableAt(0).isBlock() );
                assertTrue( lowOpponentCell.getTop() == null );
                assertTrue( highMutantCell.repOk() );
                assertTrue( startMutantCell.repOk() );
                assertTrue( lowMutantCell.repOk() );
                assertTrue( highOpponentCell.repOk() );
                assertTrue( startOpponentCell.repOk() );
                assertTrue( lowOpponentCell.repOk() );
                assertTrue( mutantWorker.position().equals( highMutantCell ));
                assertTrue( opponentWorker.position().equals( startOpponentCell ));
            } catch (LoseException l) {
                checkLose = true;
            }
            assertTrue( !checkLose );

            // reset board situation
            highMutantCell.removeWorker();
            mutantWorker.place(startMutantCell);


            /*  mutantPower with activeOnOpponentMovement == true,
                mutantWorker moves with a LevelDirection == hotLastMoveDirection of mutantPower,
                opponentWorker moves with a LevelDirection == opponentDeniedDirection of mutantPower,
                mutantPower's mustObey == true:
                opponent loses  */
            mutantPower.setActiveOnOpponentMovement( true );
            mutantPower.setHotLastMoveDirection( LevelDirection.UP );
            mutantPower.setOpponentDeniedDirection(LevelDirection.UP);
            mutantPower.setMustObey( true );

            mutantMove = new Move( mutantWorker.position(), highMutantCell );
            checkExecute = mutantMyMove.executeMove( mutantMove, mutantWorker );
            assertTrue( checkExecute );
            opponentMove = new Move( opponentWorker.position(), highOpponentCell );
            try {
                check = mutantAdversaryMove.checkMove(opponentMove, opponentWorker);
                assertTrue( !check );
            } catch (LoseException l) {
                checkLose = true;
            }
            assertTrue( checkLose );
            assertTrue( highMutantCell.getHeigth() == 3);
            assertTrue( startMutantCell.getHeigth() == 1);
            assertTrue( lowMutantCell.getHeigth() == 0);
            assertTrue( highOpponentCell.getHeigth() == 2);
            assertTrue( startOpponentCell.getHeigth() == 2);
            assertTrue( lowOpponentCell.getHeigth() == 0);
            assertTrue( highMutantCell.getTop().equals( mutantWorker ) );
            assertTrue( highMutantCell.getPlaceableAt(0).isBlock() );
            assertTrue( highMutantCell.getPlaceableAt(1).isBlock() );
            assertTrue( startMutantCell.getTop().isBlock() );
            assertTrue( lowMutantCell.getTop() == null );
            assertTrue( highOpponentCell.getTop().isBlock() );
            assertTrue( highOpponentCell.getPlaceableAt(0).isBlock() );
            assertTrue( startOpponentCell.getTop().equals( opponentWorker ) );
            assertTrue( startOpponentCell.getPlaceableAt(0).isBlock() );
            assertTrue( lowOpponentCell.getTop() == null );
            assertTrue( highMutantCell.repOk() );
            assertTrue( startMutantCell.repOk() );
            assertTrue( lowMutantCell.repOk() );
            assertTrue( highOpponentCell.repOk() );
            assertTrue( startOpponentCell.repOk() );
            assertTrue( lowOpponentCell.repOk() );
            assertTrue( mutantWorker.position().equals( highMutantCell ));
            assertTrue( opponentWorker.position().equals( startOpponentCell ));

            // reset board situation
            highMutantCell.removeWorker();
            mutantWorker.place(startMutantCell);

        }
        catch ( OutOfBoardException e) {
            e.printStackTrace();
            checkError = true;
        }
        catch (WinException e) {
            e.printStackTrace();
            checkError = true;
        }
        assertTrue( !checkError );

    }
}