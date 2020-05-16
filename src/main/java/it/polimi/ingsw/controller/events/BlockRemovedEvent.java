package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.PlaceableType;

import java.util.EventObject;

/**
 * Event: Block was removed successfully.
 * [MVEvent]
 */
public class BlockRemovedEvent extends EventObject {
    private boolean success; // tell if the move was successful
    private int x;
    private int y;
    private PlaceableType blockType;

    public BlockRemovedEvent(int x, int y, PlaceableType blockType, boolean success) {
        super(new Object());
        this.x = x;
        this.y = y;
        this.blockType = blockType;
        this.success = success;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public PlaceableType getBlockType() {
        return blockType;
    }

    public boolean success() {
        return success;
    }
}
