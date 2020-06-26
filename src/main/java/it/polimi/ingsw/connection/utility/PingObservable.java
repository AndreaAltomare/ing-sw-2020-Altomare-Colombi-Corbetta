package it.polimi.ingsw.connection.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable class for Ping (response) messages.
 * [Subject]
 *
 * @author AndreaAltomare
 */
public class PingObservable {
    private final List<PingObserver> observers = new ArrayList<>();

    /**
     * Adds an Observer for Ping messages.
     *
     * @param observer Observer to add
     */
    public void addPingObserver(PingObserver observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Removes an Observer for Ping messages.
     *
     * @param observer Observer to remove
     */
    public void removePingObserver(PingObserver observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Notifies all the Observers that a Ping message
     * was received.
     *
     * @param message Ping message
     */
    protected void notifyPingObservers(PingResponse message){
        synchronized (observers) {
            for(PingObserver observer : observers){
                observer.update(message);
            }
        }
    }
}
