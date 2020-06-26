package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.board.placeables.Placeable;
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

class ModelTest {

    void cleanAll(){

        Worker.resetIdAndColorIndex();
    }

    /**
     * Test that checks weather the Board has the x size of 5;
     */
    @Test
    void getBoardXSize() {
        Model model = new Model();

        assertEquals(5, model.getBoardXSize());
    }

    /**
     * Test that checks weather the Board has the y size of 5;
     */
    @Test
    void getBoardYSize() {
        Model model = new Model();

        assertEquals(5, model.getBoardYSize());
    }


    /**
     * Check if the game starts in the coorrect way
     */
    @Test
    void startGame() {
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

    @Test
    void setGameStarted(){
        Model model = new Model();

        model.setGameStarted(true);

        assertTrue(model.hasGameStarted());

        model.setGameStarted(false);

        assertFalse(model.hasGameStarted());

        cleanAll();
    }

    @Test
    void registerTurnObservers() {
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

    @Test
    void sortPlayers() {
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

    @Test
    void getWorkersToPlayers() {
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

    @Test
    void setChallenger() {

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

    @Test
    void setCardsInGame() {
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

    @Test
    void setStartPlayer() {
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

    @Test
    void placeWorker() {
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
        /*try {
            assertNull(model.placeWorker(0, 0, "notExistingPlayer"));
        }catch(NullPointerException e){
            excepted = true;
        }
        assertTrue(excepted);*/
        assertNotNull(model.placeWorker(0, 1, playerListString.get(0)));
        assertNull(model.placeWorker(0, 1, playerListString.get(0)));
        assertNotNull(model.placeWorker(0, 2, playerListString.get(0)));

        assertNotNull(model.placeWorker(0, 0, playerListString.get(1)));
        assertNull(model.placeWorker(0, 1, playerListString.get(1)));
        assertNull(model.placeWorker(0, 0, playerListString.get(0)));

        cleanAll();
    }

    @Test
    void getPlayingPlayerState() {
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

        tmpOutcome = model.moveWorker(workerList.get(0), 1, 0, playerListString.get(0)).get(0).getMoveOutcome();
        assertFalse(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        assertEquals(model.getPlayingPlayerState(), StateType.CONSTRUCTION);
/*

        tmpOutcome = model.moveWorker("mu", 1, 0, playerListString.get(0)).get(0).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED);
        assertNull(model.moveWorker(workerList.get(0), 1, 0, playerListString.get(1)));

        tmpOutcome = model.moveWorker(workerList.get(0), 0, 0, playerListString.get(0)).get(0).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED);

        tmpOutcome = model.moveWorker(workerList.get(0), -1, 0, playerListString.get(0)).get(0).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED);

        tmpOutcome = model.moveWorker(workerList.get(0), 0, 1, playerListString.get(0)).get(0).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED);

        tmpOutcome = model.moveWorker(workerList.get(0), 2, 0, playerListString.get(0)).get(0).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED);

        tmpOutcome = model.moveWorker(workerList.get(0), 1, 0, playerListString.get(0)).get(0).getMoveOutcome();
        assertTrue(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED);

        assertEquals(model.getPlayingPlayerState(), StateType.CONSTRUCTION);
*/

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

    @Test
    void restoreGameState() {
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

        MoveOutcomeType tmpOutcome = model.moveWorker(workerList.get(0), 1, 0, playerListString.get(0)).get(0).getMoveOutcome();
        assertFalse(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);

        model.changeTurnStatus(StateType.CONSTRUCTION, playerListString.get(0));

        //LOADING NEW STATUS

        GameState gameState = model.createGameState();

        BoardState boardState = new BoardState();
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
                cellStates[3][2].setBuilding(building);
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
        gameState.setBoard(new BoardState());

        gameState.getPlayers();

        model.setGameState(gameState);

        tmpOutcome = model.buildBlock(workerList.get(0), 4, 3, PlaceableType.DOME, playerListString.get(0)).getMoveOutcome();
        assertFalse(tmpOutcome != MoveOutcomeType.EXECUTED && tmpOutcome != MoveOutcomeType.LOSS && tmpOutcome != MoveOutcomeType.WIN && tmpOutcome != MoveOutcomeType.TURN_SWITCHED && tmpOutcome != MoveOutcomeType.TURN_OVER);


    }

    @Test
    void handlePlayerLoss() {
    }

    @Test
    void handlePlayerWin() {
    }

    @Test
    void removePlayer() {
    }


    @Test
    void getLastGameState() {
    }

    @Test
    void setGameState() {
    }

    @Test
    void createGameState() {
    }

    @Test
    void getLastBoardState() {
    }

    @Test
    void setBoardState() {
    }

    @Test
    void createBoardState() {
    }

    @Test
    void getLastPlayersState() {
    }

    @Test
    void createPlayersState() {
    }

    @Test
    void clearOperations() {
    }

    @Test
    void getMainWorkerMoved() {
    }

    @Test
    void setMainWorkerMoved() {
    }
}