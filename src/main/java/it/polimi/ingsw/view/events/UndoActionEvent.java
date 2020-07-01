package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Players wants to undo an action.
 * [VCEvent]
 */
public class UndoActionEvent extends EventObject {

    /**
     * Constructs a UndoActionEvent to notify the Server the player's intention
     * to undo the last action.
     */
    public UndoActionEvent() {
        super(new Object());
    }
}
