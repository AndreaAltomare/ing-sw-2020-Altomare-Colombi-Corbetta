package it.polimi.ingsw.view.events;

import java.util.EventObject;

/**
 * Event: Player has selected a Worker to make a move.
 * [VCEvent]
 */
public class SelectWorkerEvent extends EventObject {
    private String workerId; // univocal Worker identifier

    /**
     * Constructs a SelectWorkerEvent to notify the Server the intention
     * of the Player to Select the given Worker.
     *
     * @param workerId (the Id of the Worker the Player wants to Select).
     */
    public SelectWorkerEvent(String workerId) {
        super(new Object());
        this.workerId = workerId;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }
}
