package it.polimi.ingsw.model.persistence.board;

import it.polimi.ingsw.model.board.placeables.Placeable;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.player.worker.Worker;

import java.io.Serializable;

/**
 * Bean class to enable serialization/deserialization of a placeable's information
 * by JSON files, and to encapsulate the actual state of a placeable at a certain point.
 *
 * @author AndreaAltomare
 */
public class PlaceableData implements Serializable {
    private PlaceableType placeableType;
    private String workerId;

    /* Default Constructor */
    public PlaceableData() {}

    public PlaceableType getPlaceableType() {
        return placeableType;
    }

    public void setPlaceableType(PlaceableType placeableType) {
        this.placeableType = placeableType;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    /**
     * Sets the Placeable type by taking the Placeable as input.
     *
     * @param placeable Placeable object
     */
    public void setPlaceable(Placeable placeable) {
        if (placeable.isBlock())
            placeableType = PlaceableType.BLOCK;
        else if (placeable.isDome())
            placeableType = PlaceableType.DOME;
        else if (placeable.isWorker()) {
            placeableType = PlaceableType.WORKER;
            workerId = ((Worker)placeable).getWorkerId();
        }
        else
            placeableType = PlaceableType.NONE;
    }
}
