package it.polimi.ingsw.model.persistence.players;

import java.io.Serializable;
import java.util.Map;

/**
 * Bean class to enable serialization/deserialization of Players' information
 * by JSON files, and to encapsulate the actual state of them at a certain point.
 *
 * @author AndreaAltomare
 */
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
