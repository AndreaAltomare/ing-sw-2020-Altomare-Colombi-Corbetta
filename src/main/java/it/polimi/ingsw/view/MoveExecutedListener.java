package it.polimi.ingsw.view;

import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.model.Worker;

/**
 * Interface for Move execution.
 *
 * MVEvent (Events form Server)
 */
public interface MoveExecutedListener {
    public void onWorkerMovement(Worker worker, int x, int y);
    public void onWorkerConstruction(Worker worker, int x, int y, PlaceableType block);
    public void onWorkerRemoval(Worker worker, int x, int y);
}
