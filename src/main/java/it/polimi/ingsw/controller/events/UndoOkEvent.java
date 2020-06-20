package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.persistence.board.BoardState;
import it.polimi.ingsw.model.player.turn.StateType;

import java.util.EventObject;

/**
 * Event: Action-Undo confirmation.
 * [MVEvent]
 */
public class UndoOkEvent extends EventObject {
    private String playerNickname;
    private StateType stateType;
    private BoardState boardState;

    public UndoOkEvent(BoardState boardState, String playerNickname, StateType stateType) {
        super(new Object());
        this.boardState = boardState;
        this.playerNickname = playerNickname;
        this.stateType = stateType;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public StateType getStateType() {
        return stateType;
    }
}
