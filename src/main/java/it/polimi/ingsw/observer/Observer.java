package it.polimi.ingsw.observer;

/**
 * Observer Interface for Observer Pattern
 * [Listener]
 *
 * @param <T> Type of object
 * @see <a href="https://github.com/emanueledelsozzo/ingsoft-prova-finale-2020/blob/master/ese_Socket_Serialization/TrisDistributedMVC/src/main/java/it/polimi/ingsw/observer/Observer.java">github.com/emanueledelsozzo/.../Observer.java</a>
 * @author AndreaAltomare
 */
public interface Observer<T> {

    /**
     * Updates listener with a message.
     *
     * @param message Message
     */
    void update(T message);

    /**
     * Updates a specific listener with a message.
     *
     * @param message Message
     * @param nickname Specific Player's nickname
     */
    void update(T message, String nickname); // method used to update just a certain Player [for Unicast communications]
}
