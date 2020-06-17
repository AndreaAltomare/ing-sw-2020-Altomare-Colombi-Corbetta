package it.polimi.ingsw.model.card.move;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.worker.Worker;

public interface MoveChecker {
    boolean checkMove(Move move, Worker worker, Cell startingPosition, int movesLeft, Card parentCard);
}
