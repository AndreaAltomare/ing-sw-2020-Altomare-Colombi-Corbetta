package it.polimi.ingsw.controller.events;

import java.util.EventObject;

/**
 * Event: Worker has been correctly selected.
 * [MVEvent]
 */
public class WorkerSelectedEvent extends EventObject {
    private String playerNickname;
    private String worker;

    public WorkerSelectedEvent(String playerNickname, String worker) {
        super(new Object());
        this.playerNickname = playerNickname;
        this.worker = worker;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public String getWorker() {
        return worker;
    }
}
