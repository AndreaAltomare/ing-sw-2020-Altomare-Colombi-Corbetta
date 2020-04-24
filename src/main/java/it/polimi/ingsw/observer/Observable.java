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

    public void addObserver(Observer<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void removeObserver(Observer<T> observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    protected void notify(T message){
        synchronized (observers) {
            for(Observer<T> observer : observers){
                observer.update(message);
            }
        }
    }
}
