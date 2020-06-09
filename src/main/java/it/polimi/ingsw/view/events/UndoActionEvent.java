package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Players wants to undo an action.
 * [VCEvent]
 */
public class UndoActionEvent extends EventObject {

    public UndoActionEvent() {
        super(new Object());
    }
}
