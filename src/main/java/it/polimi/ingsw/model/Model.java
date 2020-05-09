package it.polimi.ingsw.model;

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
    private volatile Boolean gameStarted; // tel if the game has started

    public synchronized Boolean getGameStarted() {
        return gameStarted;
    }

    public synchronized void isGameStarted(Boolean gameStarted) {
        this.gameStarted = gameStarted;
    }
}
