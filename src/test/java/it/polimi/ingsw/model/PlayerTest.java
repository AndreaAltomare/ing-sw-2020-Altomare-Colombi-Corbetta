package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    //todo: can be used for integration tests
    /*
    @Test
    void executeMove() {
    }
    */

    /**
     * check if startTurn() can correctly reset the internal status of Model's Classes
     * Methods used:        chooseCard(String)                      of  Player
     *                      registerWorker(Worker)                  of  Player
     *                      getCard()                               of  Player
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
        } catch (LoseException e) {
            e.printStackTrace();
            checkLose = true;
        }
        assertTrue( !checkLose );

    }

    /**
     * Check if chooseState( StateType ) can correctly evaluate lose condition in correct state
     * of movement and of construction
     * Methods used:        chooseCard(String)              of  Player
     *                      registerWorker(Worker)          of  Player
     *                      getTop()                        of  Cell
     *                      removePLaceable()               of  Cell
     *                      buildBlock()                    of  Cell
     *                      buildDome()                     of  Cell
     *                      place(Cell)                     of  Worker
     *
     * Black Box and White Box
     */
    @Test //todo: error for a problem ( I think ) in checkForLostConstruction()
    void chooseState() {
        Player opponent = new Player("opponent");
        Worker opponentWorker = new Worker(opponent);
        boolean checkLose = false;

        player.chooseCard("apollo"); // because there is a condition where Apollo can move but not build
        player.registerWorker(worker1);  // use only one worker for simplicity



        //* MOVEMENT *//

        /* LoseException when all Workers can't move but build*/
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: BB     nearCornerCell3: BBB
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildBlock();
        nearCornerCell2.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        try {
            player.chooseState(StateType.MOVEMENT);
        } catch (LoseException e) {
            checkLose = true;
        }
        assertTrue( checkLose );

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
        checkLose = false;


        /* no LoseException when all Workers can move but not build*/
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: W (opponent)     nearCornerCell3: BBBD
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        opponentWorker.place(nearCornerCell2);
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildDome();
        try {
            player.chooseState(StateType.MOVEMENT);
        } catch (LoseException e) {
            checkLose = true;
        }
        assertTrue( !checkLose );

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
        checkLose = false;



        //* CONSTRUCTION *//

        /* LoseException when all Workers can't build but move*/
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: W (opponent)     nearCornerCell3: BBBD
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        opponentWorker.place(nearCornerCell2);
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildDome();
        try {
            player.chooseState(StateType.CONSTRUCTION);
        } catch (LoseException e) {
            checkLose = true;
        }
        assertTrue( checkLose );

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
        checkLose = false;


        /* no LoseException when all Workers can build but not move */
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: BB     nearCornerCell3: BBB
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildBlock();
        nearCornerCell2.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        try {
            player.chooseState(StateType.CONSTRUCTION);
        } catch (LoseException e) {
            checkLose = true;
        }
        assertTrue( !checkLose );

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
        checkLose = false;
    }

    /**
     * Check if switchState( TurnManager ) can correctly evaluate the lose condition
     * of movement (checkForLostByMovement()) and of construction (checkForLostByConstruction())
     * Methods used:        chooseCard(String)              of  Player
     *                      registerWorker(Worker)          of  Player
     *                      getMovementManager()            of  Player
     *                      getConstructionManager()        of  Player
     *                      buildBlock()                    of  Cell
     *                      buildDome()                     of  Cell
     *                      place(Cell)                     of  Worker
     *
     * Black Box and White Box
     */
    @Test //todo: error for a problem ( I think ) in checkForLostConstruction()
    void switchState() {
        boolean checkLose = false;

        player.chooseCard("default" );
        player.registerWorker(worker1);
        player.registerWorker(worker2);



        //* checkForLostByMovement *//

        /* no LoseException when a Worker can move*/
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: BB     nearCornerCell3: BBB
        // edgeCell: W          nearEdgeCell1:          nearEdgeCell2:          nearEdgeCell3:
        worker1.place(cornerCell);
        nearCornerCell1.buildDome();
        nearCornerCell2.buildBlock();
        nearCornerCell2.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        nearCornerCell3.buildBlock();
        worker2.place(edgeCell);
        try {
            player.switchState( player.getMovementManager() );
        } catch (LoseException e) {
            checkLose = true;
        }
        assertTrue( !checkLose );


        /* LoseException when all Workers can't move*/
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: BB     nearCornerCell3: BBB
        // edgeCell: W          nearEdgeCell1: D        nearEdgeCell2: BB       nearEdgeCell3: BBB
        nearEdgeCell1.buildDome();
        nearEdgeCell2.buildBlock();
        nearEdgeCell2.buildBlock();
        nearEdgeCell3.buildBlock();
        nearEdgeCell3.buildBlock();
        nearEdgeCell3.buildBlock();
        try {
            player.switchState( player.getMovementManager() );
        } catch (LoseException e) {
            checkLose = true;
        }
        assertTrue( checkLose );

        // reset checkLose
        checkLose = false;



        //* checkForLostByConstruction *//

        /* no LoseException when a Worker can build a Block build but not move*/
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: BBBD   nearCornerCell3: BBBD
        // edgeCell: W          nearEdgeCell1: D        nearEdgeCell2: BB       nearEdgeCell3: BBBD
        nearCornerCell2.buildBlock();
        nearCornerCell2.buildDome();
        nearCornerCell3.buildDome();
        nearEdgeCell3.buildDome();
        try {
            player.switchState( player.getConstructionManager() );
        } catch (LoseException e) {
            checkLose = true;
        }
        assertTrue( !checkLose );


        /* no LoseException when a Worker can build a Dome build but not move*/
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: BBBD   nearCornerCell3: BBBD
        // edgeCell: W          nearEdgeCell1: D        nearEdgeCell2: BBB      nearEdgeCell3: BBBD
        nearEdgeCell2.buildBlock();
        try {
            player.switchState( player.getConstructionManager() );
        } catch (LoseException e) {
            checkLose = true;
        }
        assertTrue( !checkLose );


        /* LoseException when all Worker can't build */
        // cornerCell: W        nearCornerCell1: D      nearCornerCell2: BBBD   nearCornerCell3: BBBD
        // edgeCell: W          nearEdgeCell1: D        nearEdgeCell2: BBBD     nearEdgeCell3: BBBD
        nearEdgeCell2.buildDome();
        try {
            player.switchState( player.getConstructionManager() );
        } catch (LoseException e) {
            checkLose = true;
        }
        assertTrue( checkLose );

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

        //TODO: MOST IMPORTANT: to do todo in MyMove for Apollo's Power (the worker must be opponent's Worker)
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
     * Check if chooseWorker( Worker ) ( and checkForLostMove() ) can correctly change the Worker's status when it is possible
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


        /* Player can choose a Worker if it can't move */
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


        /* Player can choose a Worker if its status is NOT_CHOSEN */
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
        assertTrue( player.isStartingPlayer() == false );

        //after set
        player.setStartingPlayer( true );
        assertTrue( player.isStartingPlayer() == true );
    }

}