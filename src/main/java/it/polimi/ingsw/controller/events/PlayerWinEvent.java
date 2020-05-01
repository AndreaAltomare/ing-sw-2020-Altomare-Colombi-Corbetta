package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Player has won.
 * [MVEvent]
 */
public class PlayerWinEvent extends EventObject {
    // TODO: 30/04/20 String playerName 
    // TODO: 30/04/20 String winnerMessage 
    // TODO: 30/04/20 String losersMessage 

    public PlayerWinEvent(Object o) {
        super(o);
    }
}
