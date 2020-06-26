package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Player has lost.
 * [MVEvent]
 */
public class PlayerLoseEvent extends EventObject {
    private String playerNickname; // Player who has lost
    private String message; // this message is to be intended for all players, to notify that the player [playerNickname] has lost.
    // this Event will be sent in broadcast

    /**
     * Constructs a PlayerLoseEvent to inform the View about the event occurred.
     *
     * @param playerNickname Player who has lost the game
     * @param message Custom message
     */
    public PlayerLoseEvent(String playerNickname, String message) {
        super(new Object());
        this.playerNickname = playerNickname;
        this.message = message;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
