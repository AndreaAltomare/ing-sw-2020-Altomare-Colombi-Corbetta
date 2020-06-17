package it.polimi.ingsw.model.card.build;

import it.polimi.ingsw.model.move.BuildMove;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import it.polimi.ingsw.model.player.worker.Worker;

public interface BuildExecutor {
    boolean executeBuild(BuildMove move, Worker worker) throws OutOfBoardException;
}
