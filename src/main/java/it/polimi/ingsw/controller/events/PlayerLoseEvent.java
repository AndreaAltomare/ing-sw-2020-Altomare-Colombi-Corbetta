package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Player has lost.
 * [MVEvent]
 */
public class PlayerLoseEvent extends EventObject {
    // TODO per Giorgio: Inserire i todo per indicare gli attributi necessari

    public PlayerLoseEvent(Object o) {
        super(o);
    }
}
