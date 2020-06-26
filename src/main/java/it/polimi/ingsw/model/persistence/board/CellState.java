package it.polimi.ingsw.model.persistence.board;

import java.io.Serializable;
import java.util.Deque;

/**
 * Bean class to enable serialization/deserialization of a cell state
 * by JSON files, and to encapsulate the actual state of a cell at a certain point.
 *
 * @author AndreaAltomare
 */
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
