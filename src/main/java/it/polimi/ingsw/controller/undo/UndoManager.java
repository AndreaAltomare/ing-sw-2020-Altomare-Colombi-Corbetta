package it.polimi.ingsw.controller.undo;

import java.util.Timer;

/**
 * This class is responsible for undo-related timing operation.
 *
 * @author AndreaAltomare
 */
public class UndoManager implements Runnable {
    /* Timer handling */
    private final int TIMER_INITIAL_DELAY = 0; // time in milliseconds
    private final int TIMER_TIME_PERIOD = 1000; // time in milliseconds
    private final int MAXIMUM_TIMEOUTS_NUMBER = 5; // equivalent to five seconds. [So Clients have the time to request an undo without caring (practically) about network-related delays.]
    private Timer timer;
    private TimeoutCounter task; // It's a TimerTask
    /* Other references */
    private Boolean active; // tells if the manager is waiting for an UNdo-Action or not.
    private final Object undoLock;

    public UndoManager(Object undoLock) {
        this.active = false;
        this.undoLock = undoLock;
    }

    /**
     * {@code Run()} method: starts the activity for Undo handling.
     */
    @Override
    public void run() {
        active = true;
        /* 1- Instantiate Timer */
        timer = new Timer();

        /* 2- Start the Timer */
        task = new TimeoutCounter(MAXIMUM_TIMEOUTS_NUMBER, undoLock);
        timer.schedule(task, TIMER_INITIAL_DELAY, TIMER_TIME_PERIOD);
    }

    /**
     * Method called when an Undo-Action is received.
     * Timer is cancelled and any thread waiting for
     * Undo-Action is woken up.
     */
    public void undoReceived() {
        active = false;
        if(timer != null) {
            synchronized (undoLock) {
                System.out.println("Undo-Action received.");
                timer.cancel();
                undoLock.notifyAll();
            }
        }
    }

    /**
     *
     * @return True if the Controller is waiting for an Undo-action request
     */
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Stops the Undo Handler ({@code UndoManager})
     * by terminating the timer
     * (calls {@code cancel()} method).
     */
    public void stop() {
        active = false;
        if(timer != null)
            timer.cancel();
    }
}
