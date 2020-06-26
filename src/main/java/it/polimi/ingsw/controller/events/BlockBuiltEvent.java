package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.move.MoveOutcomeType;
import it.polimi.ingsw.model.board.placeables.PlaceableType;

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

    /**
     * Constructs a BlockBuiltEvent to inform the View about the event occurred.
     *
     * @param x X position
     * @param y Y position
     * @param blockType Type of block built
     * @param moveOutcome Move's outcome
     */
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
