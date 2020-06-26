package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Player has won.
 * [MVEvent]
 */
public class PlayerWinEvent extends EventObject {
    private String playerNickname;
    private String winnerMessage;
    private String losersMessage;
    // this Event will be sent in broadcast

    /**
     * Constructs a PlayerWinEvent to inform the View about the event occurred.
     *
     * @param playerNickname Player who won
     * @param winnerMessage Custom message for the winner Player
     * @param losersMessage Custom message for the loser Players
     */
    public PlayerWinEvent(String playerNickname, String winnerMessage, String losersMessage) {
        super(new Object());
        this.playerNickname = playerNickname;
        this.winnerMessage = winnerMessage;
        this.losersMessage = losersMessage;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public String getWinnerMessage() {
        return winnerMessage;
    }

    public String getLosersMessage() {
        return losersMessage;
    }
}
