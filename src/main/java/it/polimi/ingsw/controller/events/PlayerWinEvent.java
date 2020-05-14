package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Player has won.
 * [MVEvent]
 */
public class PlayerWinEvent extends EventObject {
    private String playerNickname;
    private String winnerMessage; // TODO: maybe this message (form Server) is useless: the View has the responsibility to render a proper message to show to the end-user
    private String losersMessage; // TODO: maybe this message (form Server) is useless: the View has the responsibility to render a proper message to show to the end-user
    // this Event will be sent in broadcast

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
