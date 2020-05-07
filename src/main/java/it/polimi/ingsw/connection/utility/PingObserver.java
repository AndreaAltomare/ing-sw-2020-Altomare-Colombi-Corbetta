package it.polimi.ingsw.connection.utility;

/**
 * Observer interface for Ping (response) messages.
 * [Listener]
 *
 * @author AndreaAltomare
 */
public interface PingObserver {
    void update(PingResponse message); // A PingResponse object is passed as argument because in certain implementations it can deliver some connection info.
}
