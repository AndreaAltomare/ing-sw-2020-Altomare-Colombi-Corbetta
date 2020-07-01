package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Challenger has chosen the Start Player for this game.
 * [VCEvent]
 */
public class SetStartPlayerEvent extends EventObject {
    private String startPlayer;

    /**
     * Constructs a SetStartPlayerEvent to notify the Server the player chosen
     * to be the starting one.
     *
     * @param startPlayer (the player chosen to be the starting one).
     */
    public SetStartPlayerEvent(String startPlayer) {
        super(new Object());
        this.startPlayer = startPlayer;
    }

    public String getStartPlayer() {
        return startPlayer;
    }

    public void setStartPlayer(String startPlayer) {
        this.startPlayer = startPlayer;
    }
}
