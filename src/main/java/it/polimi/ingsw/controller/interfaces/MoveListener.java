package it.polimi.ingsw.controller.interfaces;

import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.observer.GeneralListener;

/**
 * Interface for Move submission from View.
 *
 * VCEvent (Events form Client)
 *
 * @author AndreaAltomare
 */
public interface MoveListener extends GeneralListener {
    public void onWorkerMovement(Worker worker, int x, int y);
    public void onWorkerConstruction(Worker worker, int x, int y, PlaceableType block);
}
