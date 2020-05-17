package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.MoveOutcomeType;
import it.polimi.ingsw.model.PlaceableType;

import java.util.EventObject;

/**
 * Event: Block was built successfully.
 * [MVEvent]
 */
public class BlockBuiltEvent extends EventObject {
    private MoveOutcomeType moveOutcome; // a more precise outcome of an executed Move
    private int x;
    private int y;
    private PlaceableType blockType;

    public BlockBuiltEvent(int x, int y, PlaceableType blockType, MoveOutcomeType moveOutcome) {
        super(new Object());
        this.x = x;
        this.y = y;
        this.blockType = blockType;
        this.moveOutcome = moveOutcome;
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

    public MoveOutcomeType getMoveOutcome() {
        return moveOutcome;
    }
}
