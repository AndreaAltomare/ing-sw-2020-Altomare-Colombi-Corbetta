package it.polimi.ingsw.view.serverSide.interfaces;

import it.polimi.ingsw.model.Worker;

/**
 * Interface for Game match preparation.
 *
 * MVEvent (Events form Server)
 *
 * @author Giorgio Corbetta
 */
public interface GamePreparationListener {
    public void onWorkerPlacement(Worker worker, int x, int y);
    public void onCardSelection(String cardName);
}
