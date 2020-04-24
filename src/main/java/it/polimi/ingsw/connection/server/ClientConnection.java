package it.polimi.ingsw.connection.server;

import it.polimi.ingsw.observer.Observer;

/**
 * Interface for operation with a Server-Client connection.
 *
 * @see <a href="https://github.com/emanueledelsozzo/ingsoft-prova-finale-2020/blob/master/ese_Socket_Serialization/TrisDistributedMVC/src/main/java/it/polimi/ingsw/server/ClientConnection.java">github.com/emanueledelsozzo/.../ClientConnection.java</a>
 * @author AndreaAltomare
 */
public interface ClientConnection {
    void closeConnection();
    void addObserver(Observer<String> observer);
    void asyncSend(Object message);
}
