package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.exceptions.LoseException;
import it.polimi.ingsw.model.move.MoveOutcomeType;
import it.polimi.ingsw.model.persistence.GameState;
import it.polimi.ingsw.model.persistence.board.BoardState;
import it.polimi.ingsw.model.persistence.board.CellState;
import it.polimi.ingsw.model.persistence.board.PlaceableData;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.turn.StateType;
import it.polimi.ingsw.model.player.worker.Worker;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class for the testing of the Model.
 *
 * @author giorgio
 */
class ModelTest {

    /**
     * Auxiliary method to clea the state -in particular the Worker's state will create some troubles if left unclean
     */
    void cleanAll(){

        new Model().clearOperations();
    }

    /**
     * Test that checks weather the Board has the x size of 5;
     */
    @Test
    void getBoardXSize() {
        cleanAll();
        Model model = new Model();

        assertEquals(5, model.getBoardXSize());
    }

    /**
     * Test that checks weather the Board has the y size of 5;
     */
    @Test
    void getBoardYSize() {
        cleanAll();
        Model model = new Model();

        assertEquals(5, model.getBoardYSize());
    }


    /**
     * Check if the game starts in the correct way
     */
    @Test
    void startGame() {
        cleanAll();
        Model model = new Model();

        ArrayList<String> workerList = new ArrayList<>(6);

        ArrayList<String> playerListString = new ArrayList<>(6);

        playerListString.add("Player1");
        playerListString.add("Player2");
        playerListString.add("Player3");

        model.initialize(new Controller(model, playerListString), playerListString);

        model.setPlayerCard("Artemis", playerListString.get(0));
        model.setPlayerCard("Atlas", playerListString.get(1));
        model.setPlayerCard("Apollo", playerListString.get(2));

        workerList.add(model.placeWorker(0, 0, playerListString.get(0)).getWorker());
        workerList.add(model.placeWorker(0, 1, playerListString.get(0)).getWorker());

        workerList.add(model.placeWorker(1, 0, playerListString.get(1)).getWorker());
        workerList.add(model.placeWorker(1, 1, playerListString.get(1)).getWorker());

        workerList.add(model.placeWorker(2, 0, playerListString.get(2)).getWorker());
        workerList.add(model.placeWorker(2, 1, playerListString.get(2)).getWorker());

        model.sortPlayers(playerListString);

        try {
            model.startGame();
        } catch (LoseException e) {
            fail();
        }

        model.setGameStarted(true);

        assertEquals(model.getPlayingPlayer(), playerListString.get(0));

        assertTrue(model.hasGameStarted());

        model.stopGame();

        assertFalse(model.hasGameStarted());

        cleanAll();
    }

    /**
     * checks if the hasGameStarted retrieves the correct value set with setGameStarted.
     */
    @Test
    void setGameStarted(){
        cleanAll();
        Model model = new Model();

        model.setGameStarted(true);

        assertTrue(model.hasGameStarted());

        model.setGameStarted(false);

        assertFalse(model.hasGameStarted());

        cleanAll();
    }

    /**
     * Check that it sets the observers either if no card is set and doesn't throw exceptions.
     */
    @Test
    void registerTurnObservers() {
        cleanAll();
        boolean excepted = false;

        Model model = new Model();

        try{
            model.registerTurnObservers();
        }catch(Exception e){
            excepted = true;
        }
        if(excepted)
            fail();

        excepted = false;

        ArrayList<String> playerListString = new ArrayList<>(6);

        playerListString.add("Player1");
        playerListString.add("Player2");
        playerListString.add("Player3");

        try{
            model.registerTurnObservers();
        }catch(Exception e){
            excepted = true;
        }
        if(excepted)
            fail();

        model.initialize(new Controller(model, playerListString), playerListString);

        model.setPlayerCard("Artemis", playerListString.get(0));
        model.setPlayerCard("Atlas", playerListString.get(1));
        model.setPlayerCard("Apollo", playerListString.get(2));

        try{
            model.registerTurnObservers();
        }catch(Exception e){
            fail();
        }

        assertTrue(true);

        cleanAll();
    }

    /**
     * Checks the right flow of the turns.
     */
    @Test
    void sortPlayers() {
        cleanAll();
        Model model = new Model();

        ArrayList<String> workerList = new ArrayList<>(6);

        ArrayList<String> playerListString = new ArrayList<>(6);

        playerListString.add("Player1");
        playerListString.add("Player2");
        playerListString.add("Player3");

        model.initialize(new Controller(model, playerListString), playerListString);

        model.setPlayerCard("Artemis", playerListString.get(0));
        model.setPlayerCard("Atlas", playerListString.get(1));
        model.setPlayerCard("Apollo", playerListString.get(2));

        workerList.add(model.placeWorker(0, 0, playerListString.get(0)).getWorker());
        workerList.add(model.placeWorker(0, 1, playerListString.get(0)).getWorker());

        workerList.add(model.placeWorker(1, 0, playerListString.get(1)).getWorker());
        workerList.add(model.placeWorker(1, 1, playerListString.get(1)).getWorker());

        workerList.add(model.placeWorker(2, 0, playerListString.get(2)).getWorker());
        workerList.add(model.placeWorker(2, 1, playerListString.get(2)).getWorker());

        assertEquals(model.getPlayingPlayer(), "");

        model.sortPlayers(playerListString);

        try {
            model.startGame();
        } catch (LoseException e) {
            fail();
        }

        model.setGameStarted(true);

        assertEquals(model.getPlayingPlayer(), playerListString.get(0));

        assertTrue(model.hasGameStarted());

        model.switchPlayer();

        assertEquals(model.getPlayingPlayer(), playerListString.get(1));

        model.switchPlayer();

        assertEquals(model.getPlayingPlayer(), playerListString.get(2));

        model.switchPlayer();

        assertEquals(model.getPlayingPlayer(), playerListString.get(0));

        cleanAll();



        model = new Model();

        workerList = new ArrayList<>(6);

        playerListString = new ArrayList<>(6);

        playerListString.add("Player1");
        playerListString.add("Player2");

        model.initialize(new Controller(model, playerListString), playerListString);

        model.setPlayerCard("Artemis", playerListString.get(0));
        model.setPlayerCard("Atlas", playerListString.get(1));

        workerList.add(model.placeWorker(0, 0, playerListString.get(0)).getWorker());
        workerList.add(model.placeWorker(0, 1, playerListString.get(0)).getWorker());

        workerList.add(model.placeWorker(1, 0, playerListString.get(1)).getWorker());
        workerList.add(model.placeWorker(1, 1, playerListString.get(1)).getWorker());

        model.sortPlayers(playerListString);

        try {
            model.startGame();
        } catch (LoseException e) {
            fail();
        }

        model.setGameStarted(true);

        assertEquals(model.getPlayingPlayer(), playerListString.get(0));

        assertTrue(model.hasGameStarted());

        model.switchPlayer();

        assertEquals(model.getPlayingPlayer(), playerListString.get(1));

        model.switchPlayer();

        assertEquals(model.getPlayingPlayer(), playerListString.get(0));

        model.switchPlayer();

        assertEquals(model.getPlayingPlayer(), playerListString.get(1));

        cleanAll();
    }

    /**
     * checks it entails correctly the workers to the players.
     */
    @Test
    void getWorkersToPlayers() {
        cleanAll();
        Model model = new Model();

        ArrayList<String> workerList = new ArrayList<>(6);

        ArrayList<String> playerListString = new ArrayList<>(6);

        playerListString.add("Player1");
        playerListString.add("Player2");
        playerListString.add("Player3");

        model.initialize(new Controller(model, playerListString), playerListString);

        model.setPlayerCard("Artemis", playerListString.get(0));
        model.setPlayerCard("Atlas", playerListString.get(1));
        model.setPlayerCard("Apollo", playerListString.get(2));

        workerList.add(model.placeWorker(0, 0, playerListString.get(0)).getWorker());
        workerList.add(model.placeWorker(0, 1, playerListString.get(0)).getWorker());

        workerList.add(model.placeWorker(1, 0, playerListString.get(1)).getWorker());
        workerList.add(model.placeWorker(1, 1, playerListString.get(1)).getWorker());

        workerList.add(model.placeWorker(2, 0, playerListString.get(2)).getWorker());
        workerList.add(model.placeWorker(2, 1, playerListString.get(2)).getWorker());

        model.sortPlayers(playerListString);

        try {
            model.startGame();
        } catch (LoseException e) {
            fail();
        }

        model.setGameStarted(true);

        Map<String, List<String>> workerMap = model.getWorkersToPlayers();

        int i = 0;

        for(String player: playerListString){
            for (String worker: workerMap.get(player)){
                assertEquals(worker, workerList.get(i));
                i++;
            }
        }

        cleanAll();
    }

    /**
     * Checks if it set properly the challenger.
     */
    @Test
    void setChallenger() {
        cleanAll();

        Model model = new Model();

        model.setChallenger("pippo");

        assertEquals(model.challenger(), "CHALLENGER_NOT_SET_YET");

        ArrayList<String> playerListString = new ArrayList<>(6);

        playerListString.add("Player1");
        playerListString.add("Player2");
        playerListString.add("Player3");

        model.initialize(new Controller(model, playerListString), playerListString);

        model.setChallenger(playerListString.get(2));

        assertEquals(model.challenger(), playerListString.get(2));

        cleanAll();
    }

    /**
     * Checks that it'll set all and only the cards to be set.
     */
    @Test
    void setCardsInGame() {
        cleanAll();
        Model model = new Model();

        ArrayList<String> cardsList = new ArrayList<>();
        List<String> retrieved;

        cardsList.add("Apollo");

        model.setCardsInGame(cardsList);

        retrieved = model.getCards();
        assertEquals(retrieved.size(), cardsList.size());
        assertTrue(retrieved.containsAll(cardsList));

        cardsList.add("Artemis");

        model.setCardsInGame(cardsList);

        retrieved = model.getCards();
        assertEquals(retrieved.size(), cardsList.size());
        assertTrue(retrieved.containsAll(cardsList));

        cardsList.add("Athena");

        model.setCardsInGame(cardsList);

        retrieved = model.getCards();
        assertEquals(retrieved.size(), cardsList.size());
        assertTrue(retrieved.containsAll(cardsList));

        cardsList.add("Atlas");

        model.setCardsInGame(cardsList);

        retrieved = model.getCards();
        assertEquals(retrieved.size(), cardsList.size());
        assertTrue(retrieved.containsAll(cardsList));


        cardsList.add("Demeter");

        model.setCardsInGame(cardsList);

        retrieved = model.getCards();
        assertEquals(retrieved.size(), cardsList.size());
        assertTrue(retrieved.containsAll(cardsList));


        cardsList.add("Hephaestus");

        model.setCardsInGame(cardsList);

        retrieved = model.getCards();
        assertEquals(retrieved.size(), cardsList.size());
        assertTrue(retrieved.containsAll(cardsList));


        cardsList.add("Hermes");

        model.setCardsInGame(cardsList);

        retrieved = model.getCards();
        assertEquals(retrieved.size(), cardsList.size());
        assertTrue(retrieved.containsAll(cardsList));


        cardsList.add("Minotaur");

        model.setCardsInGame(cardsList);

        retrieved = model.getCards();
        assertEquals(retrieved.size(), cardsList.size());
        assertTrue(retrieved.containsAll(cardsList));

        cardsList.add("Prometheus");

        model.setCardsInGame(cardsList);

        retrieved = model.getCards();
        assertEquals(retrieved.size(), cardsList.size());
        assertTrue(retrieved.containsAll(cardsList));

        cardsList.add("Pan");

        model.setCardsInGame(cardsList);

        retrieved = model.getCards();
        assertEquals(retrieved.size(), cardsList.size());
        assertTrue(retrieved.containsAll(cardsList));

        cardsList.add("New");

        model.setCardsInGame(cardsList);

        retrieved = model.getCards();
        assertEquals(retrieved.size(), cardsList.size());
        assertTrue(retrieved.containsAll(cardsList));

        cleanAll();
    }

    /**
     * Check if it sets properly the starting player.
     */
    @Test
    void setStartPlayer() {
        cleanAll();
        Model model = new Model();

        model.setStartPlayer("pippo");

        assertEquals(model.startPlayer(), "START_PLAYER_NOT_SET_YET");

        ArrayList<String> playerListString = new ArrayList<>(6);

        playerListString.add("Player1");
        playerListString.add("Player2");
        playerListString.add("Player3");

        model.initialize(new Controller(model, playerListString), playerListString);

        model.setStartPlayer(playerListString.get(2));

        assertEquals(model.startPlayer(), playerListString.get(2));

        assertTrue(playerListString.size() == model.players().size() && playerListString.containsAll(model.players()));

        cleanAll();
    }

    /**
     * Method to check the reliability of placeWorker.
     */
    @Test
    void placeWorker() {
        cleanAll();
        boolean excepted = false;

        Model model = new Model();

        ArrayList<String> workerList = new ArrayList<>(6);

        ArrayList<String> playerListString = new ArrayList<>(6);

        playerListString.add("Player1");
        playerListString.add("Player2");
        playerListString.add("Player3");

        model.initialize(new Controller(model, playerListString), playerListString);

        model.setPlayerCard("Artemis", playerListString.get(0));
        model.setPlayerCard("Atlas", playerListString.get(1));
        model.setPlayerCard("Apollo", playerListString.get(2));

        assertNull(model.placeWorker(6, 1, playerListString.get(0)));
        try {
            assertNull(model.placeWorker(0, 0, "notExistingPlayer"));
        }catch(NullPointerException e){
            excepted = true;
        }
        assertTrue(excepted);
        assertNotNull(model.placeWorker(0, 1, playerListString.get(0)));
        assertNull(model.placeWorker(0, 1, playerListString.get(0)));
        assertNotNull(model.placeWorker(0, 2, playerListString.get(0)));

        assertNotNull(model.placeWorker(0, 0, playerListString.get(1)));
        assertNull(model.placeWorker(0, 1, playerListString.get(1)));
        assertNull(model.placeWorker(0, 0, playerListString.get(0)));

        cleanAll();
    }

    /**
     * method that checks the right flow of the turn.
     */
    @Test
    void getPlayingPlayerState() {
        cleanAll();
        Model model = new Model();

        ArrayList<String> workerList = new ArrayList<>(6);

        ArrayList<String> playerListString = new ArrayList<>(6);

        assertNull(model.getPlayingPlayerReference());

        playerListString.add("Player1");
        playerListString.add("Player2");
        playerListString.add("Player3");

        model.initialize(new Controller(model, playerListString), playerListString);

        model.setPlayerCard("Apollo", playerListString.get(0));
        model.setPlayerCard("Artemis", playerListString.get(1));
        model.setPlayerCard("Apollo", playerListString.get(2));

        workerList.add(model.placeWorker(0, 0, playerListString.get(0)).getWorker());
        workerList.add(model.placeWorker(0, 1, playerListString.get(0)).getWorker());

        workerList.add(model.placeWorker(0, 2, playerListString.get(1)).getWorker());
        workerList.add(model.placeWorker(0, 3, playerListString.get(1)).getWorker());

        workerList.add(model.placeWorker(4, 4, playerListString.get(2)).getWorker());
        workerList.add(model.placeWorker(4, 3, playerListString.get(2)).getWorker());

        assertEquals(model.getPlayingPlayerState(), StateType.NONE);

        model.sortPlayers(playerListString);
        model.setStartPlayer(playerListString.get(0));

        try {
            model.startGame();
        } catch (LoseException e) {
            fail();
        }

        model.setGameStarted(true);

        assertEquals(model.getPlayingPlayer(), playerListString.get(0));

        Player player = model.getPlayingPlayerReference();
        assertEquals(player.getNickname(), playerListString.get(0));
        assertEquals(player.getCard().getName(), "Apollo");
        for (Worker w: player.getWorkers()){
            workerList.contains(w.getWorkerId());
            if(!workerList.get(0).equals(w.getWorkerId()))
                if(!workerList.get(1).equals(w.getWorkerId()))
                    fail();
        }

        assertTrue(model.hasGameStarted());

        model.registerTurnObservers();

        //GAME NOW IS STARTED

        assertEquals(model.getPlayingPlayerState(), StateType.MOVEMENT);

        assertFalse(model.selectWorker("mu", playerListString.get(0)).success());
        assertNull(model.selectWorker(workerList.get(0), "mu"));
        assertNull(model.selectWorker(workerList.get(0), playerListString.get(1)));
        assertNull(model.selectWorker(workerList.get(2), playerListString.get(1)));

        assertNotNull(model.selectWorker(workerList.get(0), playerListString.get(0)));

        assertEquals(model.getPlayingPlayerState(), StateType.MOVEMENT);


        assertNull(model.moveWorker("mu", 1, 0, "mu"));

        MoveOutcomeType tmpOutcome = model.moveWorker("mu", 1, 0, playerListString.get(0)).get(0).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);
        assertNull(model.moveWorker(workerList.get(0), 1, 0, playerListString.get(1)));

        tmpOutcome = model.moveWorker(workerList.get(0), 0, 0, playerListString.get(0)).get(0).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        tmpOutcome = model.moveWorker(workerList.get(0), -1, 0, playerListString.get(0)).get(0).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        tmpOutcome = model.moveWorker(workerList.get(0), 0, 1, playerListString.get(0)).get(0).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        tmpOutcome = model.moveWorker(workerList.get(0), 2, 0, playerListString.get(0)).get(0).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        tmpOutcome = model.moveWorker(workerList.get(1), 1, 1, playerListString.get(0)).get(0).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);



        tmpOutcome = model.moveWorker(workerList.get(0), 1, 0, playerListString.get(0)).get(0).getMoveOutcome();
        assertFalse(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        assertEquals(model.getPlayingPlayerState(), StateType.CONSTRUCTION);



        tmpOutcome = model.buildBlock("mu", 0, 0, PlaceableType.BLOCK, playerListString.get(0)).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        assertNull(model.buildBlock(workerList.get(0), 0, 0, PlaceableType.BLOCK, playerListString.get(1)));

        tmpOutcome = model.buildBlock(workerList.get(0), 1, 0, PlaceableType.BLOCK, playerListString.get(0)).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        tmpOutcome =model.buildBlock(workerList.get(0), -1, 0, PlaceableType.BLOCK, playerListString.get(0)).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        tmpOutcome = model.buildBlock(workerList.get(0), 0, 1, PlaceableType.BLOCK, playerListString.get(0)).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        tmpOutcome = model.buildBlock(workerList.get(0), 3, 0, PlaceableType.BLOCK, playerListString.get(0)).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        tmpOutcome = model.buildBlock(workerList.get(0), 0, 0, PlaceableType.DOME, playerListString.get(0)).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        tmpOutcome = model.buildBlock(workerList.get(0), 0, 0, PlaceableType.BLOCK, playerListString.get(0)).getMoveOutcome();
        assertFalse(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        assertFalse(model.removeWorker(workerList.get(0), 1, 0,  playerListString.get(0)).success());

        assertFalse(model.removeBlock(workerList.get(0), 0, 0,  playerListString.get(0)).success());


        model.switchPlayer();

        assertNotNull(model.selectWorker(workerList.get(2), playerListString.get(1)));

        assertEquals(model.getPlayingPlayerState(), StateType.MOVEMENT);

        //assertFalse(model.changeTurnStatus(StateType.CONSTRUCTION, playerListString.get(1)).success());


        assertNull(model.changeTurnStatus(StateType.CONSTRUCTION, playerListString.get(0)));

        assertNull(model.changeTurnStatus(StateType.MOVEMENT, playerListString.get(0)));

        assertTrue(model.changeTurnStatus(StateType.MOVEMENT, playerListString.get(1)).success());

        tmpOutcome = model.moveWorker(workerList.get(2), 1, 1, playerListString.get(1)).get(0).getMoveOutcome();
        assertFalse(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);
        assertEquals(tmpOutcome, model.getMoveOutcome());

        assertEquals(model.getPlayingPlayerState(), StateType.MOVEMENT);

        assertTrue(model.changeTurnStatus(StateType.CONSTRUCTION, playerListString.get(1)).success());
        model.setMoveOutcome(tmpOutcome);
        assertEquals(tmpOutcome, model.getMoveOutcome());

        tmpOutcome = MoveOutcomeType.NOT_EXECUTED;

        model.setMoveOutcome(tmpOutcome);
        assertEquals(tmpOutcome, model.getMoveOutcome());


        cleanAll();

    }

    /**
     * Method that executes a small game and verify that all works fine.
     * It checks the game restoring, the undo, the victory and the loss conditions.
     */
    @Test
    void restoreGameState() {
        cleanAll();
        Model model = new Model();

        ArrayList<String> workerList = new ArrayList<>(6);

        ArrayList<String> playerListString = new ArrayList<>(6);

        assertNull(model.getPlayingPlayerReference());

        playerListString.add("Player1");
        playerListString.add("Player2");
        playerListString.add("Player3");

        model.initialize(new Controller(model, playerListString), playerListString);

        model.setPlayerCard("Atlas", playerListString.get(0));
        model.setPlayerCard("Artemis", playerListString.get(1));
        model.setPlayerCard("Apollo", playerListString.get(2));

        workerList.add(model.placeWorker(3, 2, playerListString.get(0)).getWorker());
        workerList.add(model.placeWorker(2, 2, playerListString.get(0)).getWorker());

        workerList.add(model.placeWorker(4, 4, playerListString.get(1)).getWorker());
        workerList.add(model.placeWorker(4, 0, playerListString.get(1)).getWorker());

        workerList.add(model.placeWorker(1, 1, playerListString.get(2)).getWorker());
        workerList.add(model.placeWorker(1, 0, playerListString.get(2)).getWorker());

        assertEquals(model.getPlayingPlayerState(), StateType.NONE);

        model.sortPlayers(playerListString);
        model.setStartPlayer(playerListString.get(0));

        try {
            model.startGame();
        } catch (LoseException e) {
            fail();
        }

        model.setGameStarted(true);

        assertEquals(model.getPlayingPlayer(), playerListString.get(0));

        Player player = model.getPlayingPlayerReference();
        assertEquals(player.getNickname(), playerListString.get(0));
        assertEquals(player.getCard().getName(), "Atlas");
        for (Worker w: player.getWorkers()){
            workerList.contains(w.getWorkerId());
            if(!workerList.get(0).equals(w.getWorkerId()))
                if(!workerList.get(1).equals(w.getWorkerId()))
                    fail();
        }

        model.setChallenger(playerListString.get(0));

        assertTrue(model.hasGameStarted());

        assertNull(model.getLastBoardState());
        assertNull(model.getLastPlayersState());

        //GAME NOW IS STARTED

        assertEquals(model.getPlayingPlayerState(), StateType.MOVEMENT);

        assertNotNull(model.selectWorker(workerList.get(0), playerListString.get(0)));

        MoveOutcomeType tmpOutcome = model.moveWorker(workerList.get(0), 4, 2, playerListString.get(0)).get(0).getMoveOutcome();
        assertFalse(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        assertEquals(model.getPlayingPlayerState(), StateType.CONSTRUCTION);

        model.changeTurnStatus(StateType.CONSTRUCTION, playerListString.get(0));

        //LOADING NEW STATUS

        GameState gameState = model.createGameState();

        BoardState boardState = gameState.getBoard();
        boardState.setXSize(5);
        boardState.setYSize(5);

        {
            CellState[][] cellStates = boardState.getBoard();

            {
                Deque<PlaceableData> building = new ArrayDeque<>();

                PlaceableData placeableData = new PlaceableData();

                placeableData.setPlaceableType(PlaceableType.BLOCK);

                building.add(placeableData);
                building.add(placeableData);
                building.add(placeableData);
                cellStates[0][0].setBuilding(building);
            }
            {
                Deque<PlaceableData> building = new ArrayDeque<>();

                PlaceableData placeableData = new PlaceableData();

                placeableData.setPlaceableType(PlaceableType.BLOCK);

                building.add(placeableData);
                building.add(placeableData);

                placeableData.setPlaceableType(PlaceableType.WORKER);

                placeableData.setWorkerId("łWorker]\t" + 5);

                building.add(placeableData);
                cellStates[1][0].setBuilding(building);
            }

            {
                Deque<PlaceableData> building = new ArrayDeque<>();

                PlaceableData placeableData = new PlaceableData();

                placeableData.setPlaceableType(PlaceableType.BLOCK);

                building.add(placeableData);
                building.add(placeableData);

                placeableData.setPlaceableType(PlaceableType.WORKER);

                placeableData.setWorkerId("łWorker]\t" + 4);

                building.add(placeableData);
                cellStates[1][1].setBuilding(building);
            }

            {
                Deque<PlaceableData> building = new ArrayDeque<>();

                PlaceableData placeableData = new PlaceableData();

                placeableData.setPlaceableType(PlaceableType.WORKER);

                placeableData.setWorkerId("łWorker]\t" + 1);

                building.add(placeableData);
                cellStates[2][2].setBuilding(building);
            }
            {
                Deque<PlaceableData> building = new ArrayDeque<>();

                PlaceableData placeableData = new PlaceableData();

                placeableData.setPlaceableType(PlaceableType.WORKER);

                placeableData.setWorkerId("łWorker]\t" + 0);

                building.add(placeableData);
                cellStates[4][2].setBuilding(building);
            }
            {
                Deque<PlaceableData> building = new ArrayDeque<>();

                PlaceableData placeableData = new PlaceableData();

                placeableData.setPlaceableType(PlaceableType.WORKER);

                placeableData.setWorkerId("łWorker]\t" + 3);

                building.add(placeableData);
                cellStates[4][0].setBuilding(building);
            }
            {
                Deque<PlaceableData> building = new ArrayDeque<>();

                PlaceableData placeableData = new PlaceableData();

                placeableData.setPlaceableType(PlaceableType.WORKER);

                placeableData.setWorkerId("łWorker]\t" + 2);

                building.add(placeableData);
                cellStates[4][4].setBuilding(building);
            }
            {
                Deque<PlaceableData> building = new ArrayDeque<>();

                PlaceableData placeableData = new PlaceableData();

                placeableData.setPlaceableType(PlaceableType.DOME);

                building.add(placeableData);
                cellStates[3][0].setBuilding(building);
            }
            {
                Deque<PlaceableData> building = new ArrayDeque<>();

                PlaceableData placeableData = new PlaceableData();

                placeableData.setPlaceableType(PlaceableType.DOME);

                building.add(placeableData);
                cellStates[3][1].setBuilding(building);
            }
            {
                Deque<PlaceableData> building = new ArrayDeque<>();

                PlaceableData placeableData = new PlaceableData();

                placeableData.setPlaceableType(PlaceableType.DOME);

                building.add(placeableData);
                cellStates[3][3].setBuilding(building);
            }
            {
                Deque<PlaceableData> building = new ArrayDeque<>();

                PlaceableData placeableData = new PlaceableData();

                placeableData.setPlaceableType(PlaceableType.DOME);

                building.add(placeableData);
                cellStates[3][4].setBuilding(building);
            }
            {
                Deque<PlaceableData> building = new ArrayDeque<>();

                PlaceableData placeableData = new PlaceableData();

                placeableData.setPlaceableType(PlaceableType.DOME);

                building.add(placeableData);
                cellStates[4][1].setBuilding(building);
            }

            boardState.setBoard(cellStates);
        }
        gameState.setBoard(boardState);

        model.setGameState(gameState);

        tmpOutcome = model.buildBlock(workerList.get(0), 4, 3, PlaceableType.DOME, playerListString.get(0)).getMoveOutcome();
        assertFalse(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        assertEquals(3, model.getLastPlayersState().getData().size());

        assertTrue(model.players().contains(playerListString.get(1)));

        model.handlePlayerLoss(playerListString.get(1));

        model.removePlayer(playerListString.get(1));
        assertFalse(model.players().contains(playerListString.get(1)));

        model.getLastGameState();



        model.switchPlayer();

        assertEquals(model.getPlayingPlayer(), playerListString.get(2));

        assertEquals(model.getPlayingPlayerState(), StateType.MOVEMENT);

        assertNotNull(model.selectWorker(workerList.get(5), playerListString.get(2)));

        model.createGameState();

        tmpOutcome = model.moveWorker(workerList.get(5), 2, 0, playerListString.get(2)).get(0).getMoveOutcome();
        assertFalse(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        model.restoreGameState();

        gameState = model.getLastGameState();

        assertNotNull(model.selectWorker(workerList.get(5), playerListString.get(2)));

        model.moveWorker(workerList.get(5), 0, 0, playerListString.get(2));
        tmpOutcome = model.getMainWorkerMoved().getMoveOutcome();
        assertFalse(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        assertEquals(model.getPlayingPlayerState(), StateType.CONSTRUCTION);

        model.handlePlayerWin(playerListString.get(2));

        assertNotNull(model.getLastBoardState());
        assertNotNull(model.getLastPlayersState());

        assertFalse(model.hasGameStarted());

        cleanAll();

        model = new Model();

        assertNull(model.getPlayingPlayerReference());

        ArrayList<String> pl2 = new ArrayList<>(2);

        pl2.add("Player1");
        pl2.add("Player3");

        model.initialize(new Controller(model, playerListString), pl2);

        assertNull(model.getLastBoardState());
        assertNull(model.getLastPlayersState());

        model.setGameState(gameState);
        model.restoreGameState();

        assertNotNull(model.getLastBoardState());
        assertNotNull(model.getLastPlayersState());

        //model.registerTurnObservers();

        assertEquals(2, model.getLastPlayersState().getData().size());

        model.setGameStarted(true);

        assertNotNull(model.selectWorker(workerList.get(5), playerListString.get(2)));

        model.moveWorker(workerList.get(5), 0, 0, playerListString.get(2));
        tmpOutcome = model.getMainWorkerMoved().getMoveOutcome();
        assertFalse(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        assertEquals(model.getPlayingPlayerState(), StateType.CONSTRUCTION);

        model.handlePlayerLoss(playerListString.get(0));

        //assertFalse(model.hasGameStarted());

        cleanAll();

    }

    @Test
    void setMainWorkerMoved() {
        cleanAll();
        Model model = new Model();

        model.setMainWorkerMoved(null);
        assertNull(model.getMainWorkerMoved());
    }
}