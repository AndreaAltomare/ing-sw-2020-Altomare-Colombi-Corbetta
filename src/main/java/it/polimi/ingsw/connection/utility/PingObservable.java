package it.polimi.ingsw.connection.utility;

import it.polimi.ingsw.observer.Observer;

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

    public void addPingObserver(PingObserver observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void removePingObserver(PingObserver observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    protected void notifyPingObservers(PingResponse message){
        synchronized (observers) {
            for(PingObserver observer : observers){
                observer.update(message);
            }
        }
    }
}
