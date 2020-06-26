package it.polimi.ingsw.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable Class for Observer Pattern
 * [Subject]
 *
 * @param <T>
 * @see <a href="https://github.com/emanueledelsozzo/ingsoft-prova-finale-2020/blob/master/ese_Socket_Serialization/TrisDistributedMVC/src/main/java/it/polimi/ingsw/observer/Observable.java">github.com/emanueledelsozzo/.../Observable.java</a>
 * @author AndreaAltomare
 */
public class Observable<T> {
    private final List<Observer<T>> observers = new ArrayList<>();

    /**
     * Adds an observer.
     *
     * @param observer Observer to add
     */
    public void addObserver(Observer<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Removes an observer.
     *
     * @param observer Observer to remove
     */
    public void removeObserver(Observer<T> observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * Notifies all the listeners with a message.
     *
     * @param message Message
     */
    protected void notify(T message){
        synchronized (observers) {
            for(Observer<T> observer : observers){
                observer.update(message);
            }
        }
    }

    /**
     * This method is used to update just a certain Player.
     *
     * @param message (Object message to notify)
     * @param nickname (Player's (unique) nickname)
     */
    protected void notify(T message, String nickname) {
        synchronized (observers) {
            for(Observer<T> observer : observers) {
                observer.update(message, nickname);
            }
        }
    }
}
