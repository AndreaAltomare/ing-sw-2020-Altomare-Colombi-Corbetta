package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.persistence.GameState;

import java.util.EventObject;

/**
 * Event: A saved game can be resumed.
 * [MVEvent]
 */
public class GameResumingEvent extends EventObject {
    private GameState gameState;

    /**
     * Constructs a GameResumingEvent to inform the View about the event occurred.
     */
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
