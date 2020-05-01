package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.PlaceableType;

import java.util.EventObject;

/**
 * Event: Block was built successfully.
 * [MVEvent]
 */
public class BlockBuiltEvent extends EventObject {
    private int x;
    private int y;
    private PlaceableType blockType;

    public BlockBuiltEvent(int x, int y, PlaceableType blockType) {
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
