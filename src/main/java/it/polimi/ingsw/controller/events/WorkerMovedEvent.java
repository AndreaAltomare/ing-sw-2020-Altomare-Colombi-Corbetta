package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.move.MoveOutcomeType;

import java.util.EventObject;

/**
 * Event: Worker has been successfully moved.
 * [MVEvent]
 */
public class WorkerMovedEvent extends EventObject {
    //private boolean success; // tell if the move was successful
    private MoveOutcomeType moveOutcome; // a more precise outcome of an executed Move
    private String worker;
    private int initialX, initialY;
    private int finalX, finalY;

    /**
     * Constructs a WorkerMovedEvent to inform the View about the event occurred.
     *
     * @param worker Moved Worker
     * @param initialX Worker's initial X position on the Board
     * @param initialY Worker's initial Y position on the Board
     * @param finalX Worker's arrival X position on the Board
     * @param finalY Worker's arrival Y position on the Board
     * @param moveOutcome Move's outcome
     */
    public WorkerMovedEvent(String worker, int initialX, int initialY, int finalX, int finalY, MoveOutcomeType moveOutcome) {
        super(new Object());
        this.worker = worker;
        this.initialX = initialX;
        this.initialY = initialY;
        this.finalX = finalX;
        this.finalY = finalY;
        this.moveOutcome = moveOutcome;
    }

    public String getWorker() {
        return worker;
    }

    public int getInitialX() {
        return initialX;
    }

    public int getInitialY() {
        return initialY;
    }

    public int getFinalX() {
        return finalX;
    }

    public int getFinalY() {
        return finalY;
    }

    public MoveOutcomeType getMoveOutcome() {
        return moveOutcome;
    }
}
