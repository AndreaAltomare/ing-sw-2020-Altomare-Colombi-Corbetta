package it.polimi.ingsw.model.card.build;

import it.polimi.ingsw.model.BuildMove;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Move;
import it.polimi.ingsw.model.Worker;

public interface BuildChecker {
    boolean checkBuild(BuildMove move, Worker worker, BuildMove lastMove, int constructionLeft, Card parentCard);
}
