package it.polimi.ingsw.controller.interfaces;

import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.model.Worker;

/**
 * Interface for Move submission from View.
 *
 * VCEvent (Events form Client)
 *
 * @author AndreaAltomare
 */
public interface MoveListener {
    public void onWorkerMovement(Worker worker, int x, int y);
    public void onWorkerConstruction(Worker worker, int x, int y, PlaceableType block);
}
