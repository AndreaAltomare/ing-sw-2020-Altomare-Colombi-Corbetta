package it.polimi.ingsw.model.card.move;

import it.polimi.ingsw.model.*;

public interface MoveExecutor {
    boolean executeMove(Move move, Worker worker, Card parentCard) throws OutOfBoardException, WinException;
}
