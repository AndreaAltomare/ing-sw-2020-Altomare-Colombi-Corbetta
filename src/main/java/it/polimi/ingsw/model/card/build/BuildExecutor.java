package it.polimi.ingsw.model.card.build;

import it.polimi.ingsw.model.BuildMove;
import it.polimi.ingsw.model.Move;
import it.polimi.ingsw.model.OutOfBoardException;
import it.polimi.ingsw.model.Worker;

public interface BuildExecutor {
    boolean executeBuild(BuildMove move, Worker worker) throws OutOfBoardException;
}
