package it.polimi.ingsw.model.card.adversaryMove;

import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.LoseException;
import it.polimi.ingsw.model.Move;
import it.polimi.ingsw.model.Worker;

public interface AdversaryMoveChecker {
    boolean checkMove(Move move, Worker worker, Card parentCard) throws LoseException;
}
