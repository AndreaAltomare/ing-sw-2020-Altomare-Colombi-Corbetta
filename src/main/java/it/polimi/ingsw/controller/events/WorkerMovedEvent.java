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
