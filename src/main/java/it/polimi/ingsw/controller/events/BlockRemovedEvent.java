package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.PlaceableType;

import java.util.EventObject;

/**
 * Event: Block was removed successfully.
 * [MVEvent]
 */
public class BlockRemovedEvent extends EventObject {
    private int x;
    private int y;
    private PlaceableType blockType;

    public BlockRemovedEvent(int x, int y, PlaceableType blockType) {
        super(new Object());
        this.x = x;
        this.y = y;
        this.blockType = blockType;
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
}
