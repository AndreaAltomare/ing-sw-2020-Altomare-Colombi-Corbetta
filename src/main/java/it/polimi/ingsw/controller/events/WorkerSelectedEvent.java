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

    /**
     * Constructs a WorkerSelectedEvent to inform the View about the event occurred.
     *
     * @param playerNickname Player who selected the Worker
     * @param worker Selected Worker
     * @param success True if Worker was actually selected
     */
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
