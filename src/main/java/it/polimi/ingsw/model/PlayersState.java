package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Map;

public class PlayersState implements Serializable {
    private Map<String, PlayerData> data;

    /* Default Constructor */
    public PlayersState() {}

    public Map<String, PlayerData> getData() {
        return data;
    }

    public void setData(Map<String, PlayerData> data) {
        this.data = data;
    }
}
