package it.polimi.ingsw.model.persistence.board;

import it.polimi.ingsw.model.board.placeables.Placeable;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.player.worker.Worker;

import java.io.Serializable;

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

    public void setPlaceable(Placeable placeable) { // todo vedere se funziona con la serializzazione/deserializzazione GSON
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
