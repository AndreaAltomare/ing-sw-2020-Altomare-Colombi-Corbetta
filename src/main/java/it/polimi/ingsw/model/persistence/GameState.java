package it.polimi.ingsw.model.persistence;

import it.polimi.ingsw.model.persistence.board.BoardState;
import it.polimi.ingsw.model.persistence.players.PlayersState;

import java.io.Serializable;

/**
 * Bean class to enable serialization/deserialization of a game state
 * by JSON files, and to encapsulate the actual state of the game at a certain point.
 *
 * GameState objects are used for state persistence issues,
 * when it comes to deal with game savings/loadings.
 *
 * // TODO: to write about Memento Pattern being applied here.
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
