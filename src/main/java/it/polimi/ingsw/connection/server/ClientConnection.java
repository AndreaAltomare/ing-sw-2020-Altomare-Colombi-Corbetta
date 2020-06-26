package it.polimi.ingsw.connection.server;

import it.polimi.ingsw.observer.Observer;

/**
 * Interface for operation with a Server-Client connection.
 *
 * @see <a href="https://github.com/emanueledelsozzo/ingsoft-prova-finale-2020/blob/master/ese_Socket_Serialization/TrisDistributedMVC/src/main/java/it/polimi/ingsw/server/ClientConnection.java">github.com/emanueledelsozzo/.../ClientConnection.java</a>
 * @author AndreaAltomare
 */
public interface ClientConnection {

    /**
     * Unregister and close the connection.
     */
    void unregisterAndClose();

    /**
     * Close the connection.
     */
    void closeConnection();

    /**
     * Close the connection.
     * Boolean parameter tells if additional operations
     * needs to be made.
     *
     * @param additionalOperations Additional operations are required
     */
    void closeConnection(boolean additionalOperations);

    /**
     * Destroy connection socket.
     */
    void destroySocket();

    /**
     * Add an observer.
     *
     * @param observer Observer
     */
    void addObserver(Observer<Object> observer);

    /**
     * Send a message asynchronously.
     *
     * @param message Message to send
     */
    void asyncSend(Object message);

    /**
     * Send a message synchronously.
     *
     * @param message Message to send
     */
    void send(Object message);
}
