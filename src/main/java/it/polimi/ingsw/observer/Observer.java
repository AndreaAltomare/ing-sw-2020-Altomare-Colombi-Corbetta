package it.polimi.ingsw.observer;

/**
 * Observer Interface for Observer Pattern
 * [Listener]
 *
 * @param <T>
 * @see <a href="https://github.com/emanueledelsozzo/ingsoft-prova-finale-2020/blob/master/ese_Socket_Serialization/TrisDistributedMVC/src/main/java/it/polimi/ingsw/observer/Observer.java">github.com/emanueledelsozzo/.../Observer.java</a>
 * @author AndreaAltomare
 */
public interface Observer<T> {
    void update(T message);
    void update(T message, String nickname); // method used to update just a certain Player [for Unicast communications]
}
