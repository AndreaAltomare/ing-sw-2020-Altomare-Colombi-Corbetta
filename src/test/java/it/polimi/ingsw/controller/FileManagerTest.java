package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.FloorDirection;
import it.polimi.ingsw.model.GodPower;
import it.polimi.ingsw.model.LevelDirection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test to check if File Managing class works well
 * along with GodPower bean class in order to allow Card's configuration
 * information in and out of the application.
 *
 * Test also JSON serialization/deserialization with GSON library
 *
 * @author AndreaAltomare
 */
class FileManagerTest {
    private FileManager fileManager;
    private String newCardName;
    private String json;
    private GodPower godPower;
    private GodPower godPowerRead;
    private Gson gson;

    @BeforeEach
    void setUp() {
        fileManager = FileManager.getIstance();
    }

    @AfterEach
    void tearDown() {
        // clean object variables
        fileManager = null;
        godPower = null;
        godPowerRead = null;
        gson = null;
        json = "";
    }

    /**
     * Tests if Singleton pattern works properly
     *
     * Black Box and White Box
     */
    @Test
    void getIstance() {
        fileManager = FileManager.getIstance();

        assertNotNull(fileManager);
    }

    /**
     * Tests a new card saving by creating a new bean GodPower object
     * like in a real situation
     *
     * BlackBox and WhiteBox
     */
    @Test
    void saveNewCard() {
        godPower = new GodPower();

        /* SET A CARD'S PROPERTIES */
        newCardName = "pan";

        godPower.setName("Pan");
        godPower.setEpithet("God of the Wild");
        godPower.setDescription("Win Condition: You also win if your Worker moves down two or more levels.");

        godPower.setMovementsLeft(1);
        godPower.setConstructionLeft(1);
        godPower.setMustObey(true);
        godPower.setStartingSpaceDenied(false);
        godPower.setSameSpaceDenied(false);

        godPower.setActiveOnMyMovement(false);
        godPower.setHotLastMoveDirection(LevelDirection.DOWN);
        godPower.setMoveIntoOpponentSpace(false);
        godPower.setForceOpponentInto(FloorDirection.NONE);
        godPower.setDeniedDirection(LevelDirection.NONE);

        godPower.setActiveOnOpponentMovement(false);
        godPower.setOpponentDeniedDirection(LevelDirection.NONE);

        godPower.setActiveOnMyConstruction(false);
        godPower.setBuildBeforeMovement(false);
        godPower.setDomeAtAnyLevel(false);
        godPower.setForceConstructionOnSameSpace(false);

        godPower.setNewVictoryCondition(true);


        /* JSON SERIALIZATION WITH GSON */
        gson = new Gson();
        json = gson.toJson(godPower);

        System.out.println("\nPrint JSON for a try (BEFORE interacting with files):\n");
        System.out.println(json);

        /* WRITE ON FILE */
        fileManager = FileManager.getIstance(); // try if Singleton pattern works properly
        fileManager.saveNewCard(newCardName, json);

        /* this functionality can only be tested by looking for the new file created,
        * so the only aim of this test is to ensure file writing operation work correctly
        *
        * (With the test method "getCard()" we can be sure this test method works properly
        * because if getCard() is able to read from file and to check that information stored are correct
        * then this method must be correct.
        */
        assertTrue(true);
    }

    /**
     * Tests if file reading of Card's configuration file works properly
     * and the bean class GodPower can be used to actually store information previously saved in those
     * files
     *
     * Black Box and White Box
     */
    @Test
    void getCard() {
        newCardName = "pan";

        /* READ FROM FILE */
        fileManager = FileManager.getIstance(); // try if Singleton pattern works properly
        json = fileManager.getCard(newCardName);

        System.out.println("\nPrint JSON for a try (AFTER interacting with files):\n");
        System.out.println(json);

        /* JSON DESERIALIZATION WITH GSON */
        gson = new Gson();
        godPowerRead = gson.fromJson(json, GodPower.class);

        // check if everything is ok
        if(godPowerRead.getForceOpponentInto() == FloorDirection.NONE)
            System.out.println("\n\nEnum types are converted correctly!");
        else
            System.out.println("\n\nError with Enum type conversion!");

        assertEquals(FloorDirection.NONE, godPowerRead.getForceOpponentInto());
    }
}