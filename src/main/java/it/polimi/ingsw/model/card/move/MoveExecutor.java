package it.polimi.ingsw.model.card.move;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import it.polimi.ingsw.model.exceptions.WinException;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.worker.Worker;

public interface MoveExecutor {
    boolean executeMove(Move move, Worker worker, Card parentCard) throws OutOfBoardException, WinException;
}
