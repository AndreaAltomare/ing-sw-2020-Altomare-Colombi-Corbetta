package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Deque;

public class CellState implements Serializable {
    private Deque<PlaceableData> building;

    /* Default Constructor */
    public CellState() {}

    public Deque<PlaceableData> getBuilding() {
        return building;
    }

    public void setBuilding(Deque<PlaceableData> building) {
        this.building = building;
    }
}
