package it.polimi.ingsw.model.gameRoom;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.IslandBoard;
import it.polimi.ingsw.model.gameRoom.GameRoom;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.turn.MovementManager;
import it.polimi.ingsw.model.player.worker.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Unit test for GameRoom class, aimed to verify it works properly
 */
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
    void addPlayerTest() {
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
    void removePlayerTest() {
        boolean playerFound = false;

        // add a new player
        playerFound = gameRoom.addPlayer(playerName);
        assertTrue(playerFound);

        //remove the player
        gameRoom.removePlayer(playerName);
        playerFound = false;
        for(Player obj : gameRoom.getPlayersList())
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
    void getPlayersTest() {
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
    void setupGameTest() {
        assertTrue(true); // in the future a setupGameTest can will be implemented and a test be added
    }

    /**
     * Check if the chosen Challenger Player is set correctly
     *
     * Black Box and White Box
     */
    @Test
    void chooseChallengerTest() {
        final String NOT_REGISTER_PLAYER_NAME = "not register name";

        // with player in Game Room
        gameRoom.addPlayer(playerName);
        gameRoom.chooseChallenger(playerName);
        assertEquals(gameRoom.getChallenger().getNickname(), playerName);

        // with player out Game Room
        gameRoom.chooseChallenger(NOT_REGISTER_PLAYER_NAME);
        assertNotEquals(gameRoom.getChallenger().getNickname(), NOT_REGISTER_PLAYER_NAME);
    }

    /**
     * Check if the chosen Starting Player is set correctly
     *
     * Black Box and White Box
     */
    @Test
    void chooseStartingPlayerTest() {
        final String NOT_REGISTER_PLAYER_NAME = "not register name";

        // with player in Game Room
        gameRoom.addPlayer(playerName);
        gameRoom.chooseStartingPlayer(playerName);
        assertEquals(gameRoom.getStartingPlayer().getNickname(), playerName);

        // with player out Game Room
        gameRoom.chooseStartingPlayer(NOT_REGISTER_PLAYER_NAME);
        assertNotEquals(gameRoom.getStartingPlayer().getNickname(), NOT_REGISTER_PLAYER_NAME);
    }

    /**
     * Check if, given an index, the method returns a certain Player
     *
     * Black Box and White Box
     */
    @Test
    void getPlayerTest() {
        Integer index;

        // initialize players list
        for(Integer i=0;i<5;i++)
            gameRoom.addPlayer(("cpu" + i.toString()));

        index = 1;
        assertEquals(gameRoom.getPlayer(index).getNickname(),"cpu" + index.toString());
    }

}