package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.card.adversaryMove.AdversaryMove;
import it.polimi.ingsw.model.card.build.MyConstruction;
import it.polimi.ingsw.model.card.move.MyMove;
import it.polimi.ingsw.model.card.win.MyVictory;
import it.polimi.ingsw.model.move.FloorDirection;
import it.polimi.ingsw.model.move.LevelDirection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit test for Card class, aimed to verify it works properly
 *
 * @author Marco
 */
class CardTest {

    GodPower godPower;
    Card card;


    /**
     * Initialization before method's test
     */
    @BeforeEach
    void setUp() {

        godPower = new GodPower();
        godPower.setName( "GodPower" );
        godPower.setEpithet( "GodPower's Epithet" );
        godPower.setDescription( "GodPower's Description" );
        godPower.setMovementsLeft( 1 );
        godPower.setConstructionLeft( 1 );
        godPower.setHotLastMoveDirection( LevelDirection.NONE );
        godPower.setForceOpponentInto( FloorDirection.NONE );
        godPower.setDeniedDirection( LevelDirection.NONE );
        godPower.setOpponentDeniedDirection( LevelDirection.NONE );
        card = new Card(    godPower,
                            CardParser.getMoveCheckers(godPower),
                            CardParser.getMoveExecutor(godPower),
                            CardParser.getBuildCheckers(godPower),
                            CardParser.getBuildExecutor(godPower),
                            CardParser.getWinCheckers(godPower),
                            CardParser.getAdversaryMoveCheckers(godPower));

    }

    /**
     * Reset after test
     */
    @AfterEach
    void tearDown() {

        godPower = null;
        card = null;
    }

    /**
     * Check if resetForStart() can reset the internal variables
     * Methods used:        setMovementExecuted( boolean )          of  Card
     *                      setConstructionExecuted( boolean )      of  Card
     *                      hasMovementExecuted                     of  Card
     *                      hasConstructionExecuted                 of  Card
     *                      getMyMove()                             of  Card
     *                      getMyConstruction()                     of  Card
     *                      decreaseMovesLeft()                     of  MyMove
     *                      getMovesLeft()                          of  MyMove
     *                      decreaseConstructionLeft()              of  MyConstruction
     *                      getConstructionLeft()                   of  MyConstruction
     *
     * Black Box and White Box
     */
    @Test
    void resetForStart() {

        card.getMyMove().decreaseMovesLeft();
        card.getMyConstruction().decreaseConstructionLeft();
        card.setMovementExecuted(true);
        card.setConstructionExecuted(true);

        card.resetForStart();

        assertTrue( card.getMyMove().getMovesLeft() == 1 );
        assertTrue( card.getMyConstruction().getConstructionLeft() == 1 );
        assertTrue( !card.hasExecutedMovement() );
        assertTrue( !card.hasExecutedConstruction() );

    }

    /**
     * Check if getMyMove() can return the correct MyMove after initialization
     * Methods used:        getLastMove()               of  MyMove
     *                      getMovesLeft()              of  MyMove
     *                      getGodPower()               of  MyMove
     *
     * Black Box and White Box
     */
    @Test
    void getMyMove() {
        MyMove myMoveReturned;

        myMoveReturned = card.getMyMove();
        assertTrue( myMoveReturned != null );
        assertTrue( myMoveReturned.getLastMove() == null );
        assertTrue( myMoveReturned.getMovesLeft() == 1 );
        assertTrue( myMoveReturned.getGodPower().equals(godPower) );

    }

    /**
     * Check if getMyConstruction() can return the correct MyConstruction after initialization
     * Methods used:        getLastMove()               of  MyConstruction
     *                      getConstructionLeft()       of  MyConstruction
     *                      getGodPower()               of  MyConstruction
     *
     * Black Box and White Box
     */
    @Test
    void getMyConstruction() {
        MyConstruction myConstructionReturned;

        myConstructionReturned = card.getMyConstruction();
        assertTrue( myConstructionReturned != null );
        assertTrue( myConstructionReturned.getLastMove() == null );
        assertTrue( myConstructionReturned.getConstructionLeft() == 1 );
        assertTrue( myConstructionReturned.getGodPower().equals(godPower) );

    }

    /**
     * Check if getMyVictory() can return the correct MyVictory after initialization
     * Methods used:        getGodPower()               of  MyVictory
     *
     * Black Box and White Box
     */
    @Test
    void getMyVictory() {
        MyVictory myVictoryReturned;

        myVictoryReturned = card.getMyVictory();
        assertTrue( myVictoryReturned != null );
        assertTrue( myVictoryReturned.getGodPower().equals(godPower) );

    }

    /**
     * Check if getAdversaryMove() can return the correct AdversaryMove after initialization
     *
     * Black Box and White Box
     */
    @Test
    void getAdversaryMove() {
        AdversaryMove adversaryMoveReturned;

        adversaryMoveReturned = card.getAdversaryMove();
        assertTrue( adversaryMoveReturned != null );

    }

    /**
     * Check if hasExecutedMovement() and setMovementExecuted() can return and set the correct value
     *
     * Black Box and White Box
     */
    @Test
    void hasExecutedMovementAndSetMovementExecuted() {

        assertTrue( !card.hasExecutedMovement() );

        card.setMovementExecuted(true);
        assertTrue( card.hasExecutedMovement() );
    }

    /**
     * Check if hasExecutedConstruction() and setConstructionExecuted() can return and set the correct value
     *
     * Black Box and White Box
     */
    @Test
    void hasExecutedConstructionAndSetConstructionExecuted() {

        assertTrue( !card.hasExecutedConstruction() );

        card.setConstructionExecuted(true);
        assertTrue( card.hasExecutedConstruction() );


    }

    /**
     * Check if isTurnCompleted() and setTurnCompleted() can return and set the correct value
     *
     * Black Box and White Box
     */
    @Test
    void isTurnCompletedAndSetTurnCompleted() {

        assertTrue( !card.isTurnCompleted() );

        card.setTurnCompleted(true);
        assertTrue( card.isTurnCompleted() );

    }

    /**
     * Check if getGodPower(), getName(), getEpithet(), getDescription() can return the correct value after initialization
     * Methods used:        getName()           of  GodPower
     *                      getEpithet()        of  GodPower
     *                      getDescription()    of  GodPower
     *
     * Black Box and White Box
     */
    @Test
    void getGodPower() {

        assertTrue(card.getGodPower().equals(godPower));
        assertTrue(card.getName().equals(godPower.getName()));
        assertTrue(card.getEpithet().equals(godPower.getEpithet()));
        assertTrue(card.getDescription().equals(godPower.getDescription()));

    }

}