package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.GameState;

import java.util.EventObject;

/**
 * Event: A saved game can be resumed.
 * [MVEvent]
 */
public class GameResumingEvent extends EventObject {
    private GameState gameState;

    public GameResumingEvent() {
        super(new Object());
        this.gameState = null;
    }

    public GameResumingEvent(GameState gameState) {
        super(new Object());
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }
}
