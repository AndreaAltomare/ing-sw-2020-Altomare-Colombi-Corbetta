package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Block was removed successfully.
 * [MVEvent]
 */
public class BlockRemovedEvent extends EventObject {
    // TODO per Giorgio: Inserire i todo per indicare gli attributi necessari

    public BlockRemovedEvent(Object o) {
        super(o);
    }
}
