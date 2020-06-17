package it.polimi.ingsw.model.card.build;

import it.polimi.ingsw.model.move.BuildMove;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.player.worker.Worker;

public interface BuildChecker {
    boolean checkBuild(BuildMove move, Worker worker, BuildMove lastMove, int constructionLeft, Card parentCard);
}
