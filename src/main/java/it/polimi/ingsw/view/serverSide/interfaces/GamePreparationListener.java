package it.polimi.ingsw.view.serverSide.interfaces;

import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.observer.GeneralListener;

/**
 * Interface for Game match preparation.
 *
 * MVEvent (Events form Server)
 *
 * @author Giorgio Corbetta
 */
public interface GamePreparationListener extends GeneralListener {
    public void onWorkerPlacement(Worker worker, int x, int y);
    public void onCardSelection(String cardName);
}
