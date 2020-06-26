package it.polimi.ingsw.model.persistence;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.persistence.board.BoardState;
import it.polimi.ingsw.model.persistence.players.PlayersState;

import java.io.Serializable;

/**
 * <p>Bean class to enable serialization/deserialization of a game state
 * by JSON files, and to encapsulate the actual state of the game at a certain point.
 *
 * <p>{@code GameState} objects are used for state persistence issues,
 * when it comes to deal with game savings/loadings.
 *
 * <p>A <i>Memento</i> Design Pattern was applied to let te state of the Game
 * to be saved without any concern related encapsulation violation.
 * A {@code GameState} memento object is created by the Model;
 * when a previously saved Game needs to be resumed, a {@code GameState} object
 * is passed to the Model to let it restoring fundamental data and to resume
 * the saved Game.
 *
 * <p>Here are the main participants involved when working with a
 * {@code GameState} memento.
 * <ul>
 *  <li>A {@code GameState} memento is created by {@link
 *  Model#createGameState() createGameState()} in {@code Model} class.</li>
 *  <li>A {@code GameState} memento is saved by calling {@link
 *  it.polimi.ingsw.storage.ResourceManager#saveGameState(GameState)
 *  ResourceManager.saveGameState(gameState)} method.</li>
 * </ul>
 *
 * @author AndreaAltomare
 */
public class GameState implements Serializable {
    private boolean gameStarted;
    private BoardState board;
    private PlayersState players;

    /* Default Constructor */
    public GameState() {}

    public BoardState getBoard() {
        return board;
    }

    public void setBoard(BoardState board) {
        this.board = board;
    }

    public PlayersState getPlayers() {
        return players;
    }

    public void setPlayers(PlayersState players) {
        this.players = players;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }
}
