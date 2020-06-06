package it.polimi.ingsw.model;

import java.io.Serializable;

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
