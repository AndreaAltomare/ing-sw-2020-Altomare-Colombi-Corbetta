package it.polimi.ingsw.controller.undo;

import java.util.TimerTask;

/**
 * Timeout(s) counter class to check if a certain
 * amount of time is elapsed and then to trigger
 * a specified task.
 *
 * @author AndreaAltomare
 */
public class TimeoutCounter extends TimerTask {
    private final int MAX_RETRIES_NUMBER;
    private int timeoutCounter;
    private final Object undoLock;

    /**
     * To work with this class it's necessary to provide
     * the maximum number of Timeouts (retries) beyond which
     * the [Time Expired] task is being triggered.
     *
     * @param maxRetriesNumber (Maximum number of Timeouts)
     * @param undoLock Lock object for undo
     */
    public TimeoutCounter(int maxRetriesNumber, Object undoLock) {
        this.MAX_RETRIES_NUMBER = maxRetriesNumber;
        this.timeoutCounter = 0;
        this.undoLock = undoLock;
    }

    /**
     * Run() method for TimeoutCounter class.
     *
     * It defines the task executed when a time period
     * of a Timer elapses.
     */
    @Override
    public void run() {
        //System.out.println("Printed debug string");
        if(check(++timeoutCounter)) {
            synchronized (undoLock) {
                undoLock.notifyAll();
                this.cancel();
            }
        }
    }

    /**
     * Check if the number of timeouts has
     * exceeded the threshold.
     *
     * @param i (Number of timeouts)
     * @return (Threshold exceeded ? true : false)
     */
    private boolean check(int i) {
        return i > MAX_RETRIES_NUMBER;
    }
}
