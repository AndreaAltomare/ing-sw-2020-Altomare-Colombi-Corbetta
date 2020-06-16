package it.polimi.ingsw.model.card.move;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Move;
import it.polimi.ingsw.model.Worker;

public interface MoveChecker {
    boolean checkMove(Move move, Worker worker, Cell startingPosition, int movesLeft, Card parentCard);
}
