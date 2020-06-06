package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.BoardState;

import java.util.EventObject;

/**
 * Event: The game is over.
 * [MVEvent]
 */
public class GameResumingEvent extends EventObject {
    private BoardState boardState;

    public GameResumingEvent() {
        super(new Object());
        this.boardState = null;
    }

    public GameResumingEvent(BoardState boardState) {
        super(new Object());
        this.boardState = boardState;
    }

    public BoardState getBoardState() {
        return boardState;
    }
}
