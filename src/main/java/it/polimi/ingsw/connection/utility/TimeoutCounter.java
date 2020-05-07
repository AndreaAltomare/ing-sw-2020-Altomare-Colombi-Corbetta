package it.polimi.ingsw.connection.utility;

import java.util.TimerTask;

/**
 * Timeout(s) counter class to check if a certain
 * amount of time is elapsed and then to trigger
 * a specified task.
 *
 * @author AndreaAltomare
 */
public class TimeoutCounter extends TimerTask implements PingObserver {
    private final int MAX_RETRIES_NUMBER;
    private int timeoutCounter;
    private TimeExpiredInterface timeExpired;
    private boolean responseReceived;

    /**
     * To work with this class it's necessary to provide
     * the maximum number of Timeouts (retries) beyond which
     * the [Time Expired] task is being triggered.
     *
     * @param maxRetriesNumber (Maximum number of Timeouts)
     * @param timeExpired (TimeExpiredInterface functional interface object-instance)
     */
    public TimeoutCounter(int maxRetriesNumber, TimeExpiredInterface timeExpired) {
        this.MAX_RETRIES_NUMBER = maxRetriesNumber;
        this.timeoutCounter = 0;
        this.timeExpired = timeExpired;
        this.responseReceived = false;
    }

    /**
     * Run() method for TimeoutCounter class.
     *
     * It defines the task executed when a time period
     * of a Timer elapses.
     */
    @Override
    public void run() {
        //System.out.println("Timeouts passati: " + (timeoutCounter + 1)); // [debug]
        if(check(++timeoutCounter)) {
            //System.out.println("Time elapsed"); // [debug]
            timeExpired.handle(responseReceived); // Timer's time period is elapsed. Trigger TimeExpired handling method.
            if(!responseReceived)
                this.cancel(); // Cancels this timer task.
            else
                reset(); // Reset timeout counter
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

    /**
     * Reset timeout counter.
     */
    public void reset() {
        timeoutCounter = 0;
        responseReceived = false;
    }

    /**
     * When a Ping response is received, update
     * the state by setting responseReceived to true.
     *
     * @param m (PingResponse object)
     */
    @Override
    public void update(PingResponse m) {
        responseReceived = true;
    }

    public boolean responseReceived() {
        return responseReceived;
    }

    public void setResponseReceived(boolean responseReceived) {
        this.responseReceived = responseReceived;
    }
}
