package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Player has lost.
 * [MVEvent]
 */
public class PlayerLoseEvent extends EventObject {
    // TODO: 30/04/20 String playerName
    // TODO: 30/04/20 String message

    public PlayerLoseEvent(Object o) {
        super(o);
    }
}
