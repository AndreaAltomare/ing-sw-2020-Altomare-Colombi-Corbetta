package it.polimi.ingsw.controller.undo;

import it.polimi.ingsw.view.events.QuitEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is responsible for undo-related timing operation.
 *
 * @author AndreaAltomare
 */
public class UndoManager implements Runnable {
    /* Timer handling */
    private final int TIMER_INITIAL_DELAY = 0; // time in milliseconds
    private final int TIMER_TIME_PERIOD = 1000; // time in milliseconds
    private final int MAXIMUM_TIMEOUTS_NUMBER = 5; // todo rimettere a 6 // equivalent to six seconds, so Clients have the time to request an undo without caring (practically) about network-related delays.
    private Timer timer;
    private TimeoutCounter task; // It's a TimerTask
    /* Other references */
    private Boolean active; // tells if the manager is waiting for an UNdo-Action or not.
    private final Object undoLock;

    public UndoManager(Object undoLock) {
        this.active = false;
        this.undoLock = undoLock;
    }

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void stop() {
        active = false;
        if(timer != null)
            timer.cancel();
    }
}
