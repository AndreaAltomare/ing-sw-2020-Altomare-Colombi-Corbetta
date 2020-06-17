package it.polimi.ingsw.model.card.win;

import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.worker.Worker;

public interface WinChecker {
    boolean checkWin(Move move, Worker worker);
}
