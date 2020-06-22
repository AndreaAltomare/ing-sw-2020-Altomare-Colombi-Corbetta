package it.polimi.ingsw.connection.server;

import it.polimi.ingsw.observer.Observer;

/**
 * Interface for operation with a Server-Client connection.
 *
 * @see <a href="https://github.com/emanueledelsozzo/ingsoft-prova-finale-2020/blob/master/ese_Socket_Serialization/TrisDistributedMVC/src/main/java/it/polimi/ingsw/server/ClientConnection.java">github.com/emanueledelsozzo/.../ClientConnection.java</a>
 * @author AndreaAltomare
 */
public interface ClientConnection {
    void unregisterAndClose();
    void closeConnection();
    void closeConnection(boolean additionalOperations);
    void destroySocket();
    void addObserver(Observer<Object> observer);
    void asyncSend(Object message);
    void send(Object message);
}
