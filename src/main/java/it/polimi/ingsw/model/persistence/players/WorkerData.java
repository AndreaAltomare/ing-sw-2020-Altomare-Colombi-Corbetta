package it.polimi.ingsw.model.persistence.players;

import it.polimi.ingsw.model.player.worker.ChooseType;
import it.polimi.ingsw.model.player.worker.Color;

import java.io.Serializable;

/**
 * Bean class to enable serialization/deserialization of a Worker's information
 * by JSON files, and to encapsulate the actual state of it at a certain point.
 *
 * @author AndreaAltomare
 */
public class WorkerData implements Serializable {
    private String workerId;
    private ChooseType chosen;
    private Color color;

    /* Default Constructor */
    public WorkerData() {}

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public ChooseType getChosen() {
        return chosen;
    }

    public void setChosen(ChooseType chosen) {
        this.chosen = chosen;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
