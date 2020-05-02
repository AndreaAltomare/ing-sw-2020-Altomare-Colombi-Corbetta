package it.polimi.ingsw.view.serverSide.interfaces;

import it.polimi.ingsw.controller.events.CardSelectedEvent;
import it.polimi.ingsw.controller.events.WorkerPlacedEvent;
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
    public void onWorkerPlacement(WorkerPlacedEvent workerPlaced);
    public void onCardSelection(CardSelectedEvent cardSelected);
}
