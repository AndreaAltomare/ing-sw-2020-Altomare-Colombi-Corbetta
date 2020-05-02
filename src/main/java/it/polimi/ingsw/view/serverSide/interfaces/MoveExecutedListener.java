package it.polimi.ingsw.view.serverSide.interfaces;

import it.polimi.ingsw.controller.events.*;
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
    public void onWorkerMovement(WorkerMovedEvent workerMoved);
    public void onWorkerConstruction(BlockBuiltEvent blockBuilt);
    public void onWorkerRemoval(WorkerRemovedEvent workerRemoved);
    public void onBlockRemoval(BlockRemovedEvent blockRemoved);
    public void onWorkerSelection(WorkerSelectedEvent workerSelected);
}
