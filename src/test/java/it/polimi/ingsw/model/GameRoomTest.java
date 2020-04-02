package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertTrue;

class GameRoomTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addPlayer() {
        boolean playerFound = false;
        GameRoom gameRoom = new GameRoom();
        gameRoom.addPlayer("cpu1");

        for(Player obj : gameRoom.players)
            if(obj.getNickname().equals("cpu1"))
                playerFound = true;

        //assertEquals(true, playerFound);
        assertTrue(playerFound);
    }

    @Test
    void removePlayer() {
    }

    @Test
    void getPlayers() {
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