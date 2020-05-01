package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Block was built successfully.
 * [MVEvent]
 */
public class BlockBuiltEvent extends EventObject {
    // TODO: 30/04/20 cella: int x, int y; 
    // TODO: 30/04/20 block: BlockType block; 

    public BlockBuiltEvent(Object o) {
        super(o);
    }
}
