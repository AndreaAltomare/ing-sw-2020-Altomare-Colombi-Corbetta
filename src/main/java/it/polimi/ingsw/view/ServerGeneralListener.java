package it.polimi.ingsw.view;

/**
 * Interface for general information exchange (from Controller [Server]).
 *
 * MVEvent (Events form Server)
 *
 * @author Giorgio Corbetta
 */
public interface ServerGeneralListener {
    public void onStatusChange(ApplicationStatus applicationStatus);
    public void serverSendData(); // Game match general data (to respond to viewRequestData, receive data from Server)
}
