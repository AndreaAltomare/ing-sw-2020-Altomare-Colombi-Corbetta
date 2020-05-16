package it.polimi.ingsw.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class provides and implements a unified and simplified
 * public interface by which Controller can query Model's
 * data and modify them.
 * By using this class, Controller can handle a Game match flow.
 *
 * A Facade Design Pattern is applied to let the Controller interact
 * with the Model subsystem with ease.
 *
 * @author AndreaAltomare
 */
public class Model {
    public final int WORKERS_PER_PLAYER;
    private volatile Boolean gameStarted; // tell if the game has started
    private GeneralGameRoom gameRoom;
    private Board board;
    private List<String> cards;

    /**
     * Constructor.
     * All members are instantiated here.
     */
    public Model() {
        this.gameStarted = false;
        this.gameRoom = new GameRoom();
        this.board = new IslandBoard();
        this.cards = new ArrayList<>();
        this.WORKERS_PER_PLAYER = 2;
    }

    /**
     * By this method, the game Model is initialized for the new Game.
     */
    public void initialize(List<String> players) {
        List<Player> playerList = players.stream().map(p -> new Player(p)).collect(Collectors.toList());
        gameRoom.setPlayers(playerList);
    }

    /**
     * Tell whether the game has started or not.
     *
     * @return (Game has started ? true : false)
     */
    public synchronized Boolean hasGameStarted() {
        return gameStarted;
    }

    public synchronized void setGameStarted(Boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    /**
     * Gets the X size of the board.
     *
     * @return Board's X size
     */
    public int getBoardXSize() {
        return board.getXDim();
    }

    /**
     * Gets the Y size of the board.
     *
     * @return Board's Y size
     */
    public int getBoardYSize() {
        return board.getYDim();
    }

    /**
     * Gets the Workers associated to every Player in the game.
     *
     * @return A Map with a List of Workers associated to each Player.
     */
    public Map<String, List<String>> getWorkersToPlayers() {
        Map<String, List<String>> map = new HashMap<>();

        for(Player player : gameRoom.players) {
            List<String> workers = player.getWorkers().stream().map(w -> w.getWorkerId()).collect(Collectors.toList());
            map.put(player.getNickname(),workers);
        }

        return map;
    }

    /**
     * Set the Challenger Player.
     *
     * @param challenger (Challenger's nickname)
     */
    public void setChallenger(String challenger) {
        gameRoom.chooseChallenger(challenger);
    }

    /**
     * Sets Cards for this game.
     *
     * @param cardsInGame (List of Cards' name)
     */
    public void setCardsInGame(List<String> cardsInGame) {
        this.cards = new ArrayList<>(cardsInGame);
    }

    public List<String> getCards() {
        return cards;
    }

    /**
     * Set the Card for a given Player.
     *
     * @param cardName (Card's name)
     * @param playerNickname (Player's nickname)
     */
    public void setPlayerCard(String cardName, String playerNickname) {
        gameRoom.getPlayer(playerNickname).chooseCard(cardName);
    }

    /**
     * Set the Start Player.
     *
     * @param startPlayer (Start Player's nickname)
     */
    public void setStartPlayer(String startPlayer) {
        gameRoom.chooseStartingPlayer(startPlayer);
    }

    /**
     * Place a new Worker onto the Game Board.
     *
     * @param x (X axis position)
     * @param y (Y axis position)
     * @param playerNickname (Player who wants to place a new Worker)
     * @return The new placed Worker's ID
     */
    public String placeWorker(int x, int y, String playerNickname) {
        boolean placed = true;

        /* 1- Get the Player and instantiate a new Worker */
        Player player = gameRoom.getPlayer(playerNickname);
        Worker worker = new Worker(player);

        /* 2- Try to place th Worker */
        try {
            placed = board.getCellAt(x, y).placeOn(worker);
        }
        catch (OutOfBoardException ex) {
            placed = false;
        }

        /* 3- If the Worker was correctly placed, register it among the Player's Workers */
        if(placed) {
            player.registerWorker(worker);
            return worker.getWorkerId();
        }
        else {
            worker = null;
            return null;
        }
    }

    /**
     *
     * @return Challenger's nickname
     */
    public String challenger() {
        Player challenger = gameRoom.getChallenger();

        if(challenger != null)
            return challenger.getNickname();
        else
            return "CHALLENGER_NOT_SET_YET";
    }

    /**
     *
     * @return Start Player's nickname
     */
    public String startPlayer() {
        Player startPlayer = gameRoom.getStartingPlayer();

        if(startPlayer != null)
            return startPlayer.getNickname();
        else
            return "START_PLAYER_NOT_SET_YET";
    }
}
