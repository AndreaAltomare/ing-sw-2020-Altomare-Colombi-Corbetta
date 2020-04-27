package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Player has won.
 * [MVEvent]
 */
public class PlayerWinEvent extends EventObject {
    // TODO per Giorgio: Inserire i todo per indicare gli attributi necessari

    public PlayerWinEvent(Object o) {
        super(o);
    }
}
