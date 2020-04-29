package it.polimi.ingsw.controller.interfaces;

import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.observer.GeneralListener;
import it.polimi.ingsw.view.events.*;

/**
 * Interface for Move submission from View.
 *
 * VCEvent (Events form Client)
 *
 * @author AndreaAltomare
 */
public interface MoveListener extends GeneralListener {
    public void onWorkerPlacement(PlaceWorkerEvent workerToPlace, String playerNickname);
    public void onWorkerSelection(SelectWorkerEvent selectedWorker, String playerNickname);
    public void onWorkerMovement(MoveWorkerEvent move, String playerNickname);
    public void onWorkerConstruction(BuildBlockEvent build, String playerNickname);
    public void onWorkerRemove(RemoveWorkerEvent workerToRemove, String playerNickname);
    public void onBlockRemove(RemoveBlockEvent blockToRemove, String playerNickname);
}
