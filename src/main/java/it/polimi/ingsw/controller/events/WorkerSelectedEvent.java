package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Worker has been correctly selected.
 * [MVEvent]
 */
public class WorkerSelectedEvent extends EventObject {
    private boolean success; // tell if the move was successful
    private String playerNickname;
    private String worker;

    public WorkerSelectedEvent(String playerNickname, String worker, boolean success) {
        super(new Object());
        this.playerNickname = playerNickname;
        this.worker = worker;
        this.success = success;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public String getWorker() {
        return worker;
    }

    public boolean success() {
        return success;
    }
}
