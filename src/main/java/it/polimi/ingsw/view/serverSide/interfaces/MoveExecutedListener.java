package it.polimi.ingsw.view.serverSide.interfaces;

import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.observer.GeneralListener;

/**
 * Interface for Move execution.
 *
 * MVEvent (Events form Server)
 *
 * @author Giorgio Corbetta
 */
public interface MoveExecutedListener extends GeneralListener {
    public void onWorkerMovement(Worker worker, int x, int y);
    public void onWorkerConstruction(Worker worker, int x, int y, PlaceableType block);
    public void onWorkerRemoval(Worker worker, int x, int y);
}
