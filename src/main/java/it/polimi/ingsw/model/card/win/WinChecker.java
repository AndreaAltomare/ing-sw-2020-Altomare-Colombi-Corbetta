package it.polimi.ingsw.model.card.win;

import it.polimi.ingsw.model.Move;
import it.polimi.ingsw.model.Worker;

public interface WinChecker {
    boolean checkWin(Move move, Worker worker);
}
