package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class GameRoomTest {
    private GameRoom gameRoom;
    private String playerName = "cpu1";

    /**
     * Class test methods initialization
     */
    @BeforeEach
    void setUp() {
        gameRoom = new GameRoom();
    }

    /**
     * Executed after every test method
     */
    @AfterEach
    void tearDown() {
        gameRoom = null;
    }

    /**
     * Check if players are correctly added
     *
     * Black Box and White Box
     */
    @Test
    void addPlayer() {
        boolean playerFound = false;
        playerFound = gameRoom.addPlayer(playerName);

        assertTrue(playerFound);
    }

    /**
     * Check if a player previously added is now removed correctly
     *
     * Black Box and White Box
     */
    @Test
    void removePlayer() {
        boolean playerFound = false;

        // add a new player
        playerFound = gameRoom.addPlayer(playerName);
        assertTrue(playerFound);

        //remove the player
        gameRoom.removePlayer(playerName);
        playerFound = false;
        for(Player obj : gameRoom.players)
            if(obj.getNickname().equals(playerName))
                playerFound = true;

        assertFalse(playerFound);
    }

    /**
     * Check if every player added can be found by using the Iterator returned by the tested method
     *
     * Black Box and White Box
     */
    @Test
    void getPlayers() {
        boolean playerFound = false;
        List<String> name = new ArrayList<>();
        List<String> foundNames = new ArrayList<>();

        // initialize name list
        for(Integer i=1;i<=5;i++)
            name.add(("cpu" + i.toString()));

        // add players
        for(String str : name) {
            playerFound = false;
            playerFound = gameRoom.addPlayer(str);
            assertTrue(playerFound);
        }

        // get iterator
        Iterator<Player> it = gameRoom.getPlayers();
        // get nicknames
        while(it.hasNext()) {
            foundNames.add(it.next().getNickname());
        }

        // check if every and only string in name array is provided by the iterator
        for(String str : name) {
            assertTrue(foundNames.contains(str));
        }
        for(String str : foundNames)
            assertTrue(name.contains(str));
    }

    @Test
    void setupGame() {
    }

    @Test
    void chooseChallenger() {
    }

    @Test
    void chooseStartingPlayer() {
    }

    @Test
    void getPlayer() {
    }
}