package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Player's Turn Status has changed.
 * [MVEvent]
 */
public class TurnStatusChangedEvent extends EventObject {
    private String playerNickname;
    // TODO: 30/04/20 view.serverSide.ClientSubTurn part // [Andrea: non si possono usare i turni (StateType) definiti nel Model?]

    public TurnStatusChangedEvent(String playerNickname) {
        super(new Object());
        this.playerNickname = playerNickname;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }
}
