package it.polimi.ingsw.view.serverSide.interfaces;

import it.polimi.ingsw.view.serverSide.ClientStatus;

/**
 * Interface for general information exchange (from Controller [Server]).
 *
 * MVEvent (Events form Server)
 *
 * @author Giorgio Corbetta
 */
public interface ServerGeneralListener {
    public void onStatusChange(ClientStatus clientStatus);
    public void onNextStatus();
    public void serverSendData(); // Game match general data (to respond to viewRequestData, receive data from Server)
}
