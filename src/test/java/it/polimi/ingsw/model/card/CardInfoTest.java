package it.polimi.ingsw.model.card;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for CardInfo class, aimed to verify it works properly
 *
 * @author Marco
 */
class CardInfoTest {

    final String NAME = "name of God/Goddess";
    final String EPITHET = "epithet of God/Goddess";
    final String DESCRIPTION = "description of God/Goddess";
    CardInfo cardInfo;

    /**
     * Initialization before method's test
     */
    @BeforeEach
    void setUp() {

        cardInfo = new CardInfo(NAME, EPITHET, DESCRIPTION);

    }

    /**
     * Reset after test
     */
    @AfterEach
    void tearDown() {

        cardInfo = null;

    }

    /**
     * Check if the name is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void getNameAndSetName() {
        final String NEW_NAME = "new name";

        assertTrue( cardInfo.getName().equals(NAME) );

        cardInfo.setName(NEW_NAME);
        assertTrue( cardInfo.getName().equals(NEW_NAME) );

    }

    /**
     * Check if the epithet is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void getEpithetAndSetEpithet() {
        final String NEW_EPITHET = "new epithet";

        assertTrue( cardInfo.getEpithet().equals(EPITHET) );

        cardInfo.setEpithet(NEW_EPITHET);
        assertTrue( cardInfo.getEpithet().equals(NEW_EPITHET) );

    }

    /**
     * Check if the description is correctly get and set
     *
     * Black Box and White Box
     */
    @Test
    void getDescriptionAndSetDescription() {
        final String NEW_DESCRIPTION = "new description";

        assertTrue( cardInfo.getDescription().equals(DESCRIPTION) );

        cardInfo.setDescription(NEW_DESCRIPTION);
        assertTrue( cardInfo.getDescription().equals(NEW_DESCRIPTION) );
    }

}