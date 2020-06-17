package it.polimi.ingsw.model.card.adversaryMove;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.exceptions.LoseException;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.worker.Worker;

public interface AdversaryMoveChecker {
    boolean checkMove(Move move, Worker worker, Card parentCard) throws LoseException;
}
