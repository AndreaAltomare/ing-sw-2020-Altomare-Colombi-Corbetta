package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.BoardState;

import java.util.EventObject;

/**
 * Event: Action-Undo confirmation.
 * [MVEvent]
 */
public class UndoOkEvent extends EventObject {
    private BoardState boardState;

    public UndoOkEvent(BoardState boardState) {
        super(new Object());
        this.boardState = boardState;
    }

    public BoardState getBoardState() {
        return boardState;
    }
}
