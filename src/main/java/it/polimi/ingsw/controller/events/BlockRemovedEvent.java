package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Block was removed successfully.
 * [MVEvent]
 */
public class BlockRemovedEvent extends EventObject {
    // TODO: 30/04/20 cell: int x, int y;
    // TODO: 30/04/20 block BlockType; 

    public BlockRemovedEvent(Object o) {
        super(o);
    }
}
