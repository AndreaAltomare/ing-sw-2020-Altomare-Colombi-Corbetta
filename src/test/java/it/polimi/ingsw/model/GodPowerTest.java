package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for GodPower class, aimed to verify it works properly
 *
 * @author Marco
 */
class GodPowerTest {

    GodPower godPower;

    /**
     * Initialization before method's test
     */
    @BeforeEach
    void setUp() {

        godPower = new GodPower();

    }

    /**
     * Reset attribute after method's test
     */
    @AfterEach
    void tearDown() {

        godPower = null;

    }

    /**
     * Check if the name is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void getNameAndSetName() {
        String name = "GodName";

        assertTrue(godPower.getName() == null);

        godPower.setName(name);
        assertTrue(godPower.getName().equals(name));

    }

    /**
     * Check if the epithet is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void getEpithetAndSetEpithet() {
        String epithet = "God Epithet";

        assertTrue(godPower.getEpithet() == null);

        godPower.setEpithet(epithet);
        assertTrue(godPower.getEpithet().equals(epithet));

    }

    /**
     * Check if the description is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void getDescriptionAndSetDescription() {
        String description = "God Description";

        assertTrue(godPower.getDescription() == null);

        godPower.setDescription(description);
        assertTrue(godPower.getDescription().equals(description));

    }

    /**
     * Check if MovementsLeft is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void getMovementsLeftAndSetMovementsLeft() {
        Integer zeroInteger = 0;
        Integer posInteger = 10;
        Integer negInteger = -10;
        int falseInteger = 7;

        assertTrue(godPower.getMovementsLeft() == null);

        godPower.setMovementsLeft(zeroInteger);
        assertTrue(godPower.getMovementsLeft().equals(zeroInteger));

        godPower.setMovementsLeft(posInteger);
        assertTrue(godPower.getMovementsLeft().equals(posInteger));

        //TODO: wait the help to understand how manage negative values and then change
        godPower.setMovementsLeft(negInteger);
        assertTrue(godPower.getMovementsLeft().equals(negInteger));

        godPower.setMovementsLeft(falseInteger);
        assertTrue(godPower.getMovementsLeft().equals(falseInteger));

    }

    /**
     * Check if ConstructionsLeft is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void getConstructionLeftAndSetConstructionLeft() {
        Integer zeroInteger = 0;
        Integer posInteger = 10;
        Integer negInteger = -10;
        int falseInteger = 7;

        assertTrue(godPower.getConstructionLeft() == null);

        godPower.setConstructionLeft(zeroInteger);
        assertTrue(godPower.getConstructionLeft().equals(zeroInteger));

        godPower.setConstructionLeft(posInteger);
        assertTrue(godPower.getConstructionLeft().equals(posInteger));

        //TODO: wait the help to understand how manage negative values and then change
        godPower.setConstructionLeft(negInteger);
        assertTrue(godPower.getConstructionLeft().equals(negInteger));

        godPower.setConstructionLeft(falseInteger);
        assertTrue(godPower.getConstructionLeft().equals(falseInteger));

    }

    /**
     * Check if MustObey is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void isMustObeyAndSetMustObey() {

        assertTrue(!godPower.isMustObey());

        godPower.setMustObey(true);
        assertTrue(godPower.isMustObey());

    }

    /**
     * Check if StartingSpaceDenied is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void isStartingSpaceDeniedAndSetStartingSpaceDenied() {

        assertTrue(!godPower.isStartingSpaceDenied());

        godPower.setStartingSpaceDenied(true);
        assertTrue(godPower.isStartingSpaceDenied());

    }

    /**
     * Check if SameSpaceDenied is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void isSameSpaceDeniedAndSetSameSpaceDenied() {

        assertTrue(!godPower.isSameSpaceDenied());

        godPower.setSameSpaceDenied(true);
        assertTrue(godPower.isSameSpaceDenied());

    }

    /**
     * Check if ActiveOnMyMovement is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void isActiveOnMyMovementAndSetActiveOnMyMovement() {

        assertTrue(!godPower.isActiveOnMyMovement());

        godPower.setActiveOnMyMovement(true);
        assertTrue(godPower.isActiveOnMyMovement());

    }

    /**
     * Check if ActiveOnOpponentMovement is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void isActiveOnOpponentMovementAndSetActiveOnOpponentMovement() {

        assertTrue(!godPower.isActiveOnOpponentMovement());

        godPower.setActiveOnOpponentMovement(true);
        assertTrue(godPower.isActiveOnOpponentMovement());

    }


    /**
     * Check if ActiveOnMyConstruction is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void isActiveOnMyConstructionAndSetActiveOnMyConstruction() {

        assertTrue(!godPower.isActiveOnMyConstruction());

        godPower.setActiveOnMyConstruction(true);
        assertTrue(godPower.isActiveOnMyConstruction());

    }

    /**
     * Check if HotLastMoveDirection is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void getHotLastMoveDirectionAndSetHotLastMoveDirection() {

        assertTrue(godPower.getHotLastMoveDirection() == null);

        godPower.setHotLastMoveDirection(LevelDirection.UP);
        assertTrue(godPower.getHotLastMoveDirection() == LevelDirection.UP);

    }


    /**
     * Check if MoveIntoOpponentSpace is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void isMoveIntoOpponentSpaceAndSetMoveIntoOpponentSpace() {

        assertTrue(!godPower.isMoveIntoOpponentSpace());

        godPower.setMoveIntoOpponentSpace(true);
        assertTrue(godPower.isMoveIntoOpponentSpace());

    }

    /**
     * Check if ForceOpponentInto is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void getForceOpponentIntoAndSetForceOpponentInto() {

        assertTrue(godPower.getForceOpponentInto() == null);

        godPower.setForceOpponentInto(FloorDirection.NORTH);
        assertTrue(godPower.getForceOpponentInto() == FloorDirection.NORTH);

    }

    /**
     * Check if DeniedDirection is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void getDeniedDirectionAndSetDeniedDirection() {

        assertTrue(godPower.getDeniedDirection() == null);

        godPower.setDeniedDirection(LevelDirection.DOWN);
        assertTrue(godPower.getDeniedDirection() == LevelDirection.DOWN);

    }

    /**
     * Check if OpponentDeniedDirection is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void getOpponentDeniedDirectionAndSetOpponentDeniedDirection() {

        assertTrue(godPower.getOpponentDeniedDirection() == null);

        godPower.setOpponentDeniedDirection(LevelDirection.SAME);
        assertTrue(godPower.getOpponentDeniedDirection() == LevelDirection.DOWN);

    }

    /**
     * Check if BuildBeforeMovement is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void isBuildBeforeMovementAndSetBuildBeforeMovement() {

        assertTrue(!godPower.isBuildBeforeMovement());

        godPower.setBuildBeforeMovement(true);
        assertTrue(godPower.isBuildBeforeMovement());

    }

    /**
     * Check if DomeAtAnyLevel is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void isDomeAtAnyLevelAndSetDomeAtAnyLevel() {

        assertTrue(!godPower.isDomeAtAnyLevel());

        godPower.setDomeAtAnyLevel(true);
        assertTrue(godPower.isDomeAtAnyLevel());

    }

    /**
     * Check if ForceConstructionOnSameSpace is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void isForceConstructionOnSameSpaceAndSetForceConstructionOnSameSpace() {

        assertTrue(!godPower.isForceConstructionOnSameSpace());

        godPower.setForceConstructionOnSameSpace(true);
        assertTrue(godPower.isForceConstructionOnSameSpace());

    }

    /**
     * Check if NewVictoryCondition is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void isNewVictoryConditionAndSetNewVictoryCondition() {

        assertTrue(!godPower.isNewVictoryCondition());

        godPower.setNewVictoryCondition(true);
        assertTrue(godPower.isNewVictoryCondition());

    }

}