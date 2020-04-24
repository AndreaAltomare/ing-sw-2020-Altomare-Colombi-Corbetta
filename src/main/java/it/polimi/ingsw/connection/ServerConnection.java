package it.polimi.ingsw.connection;

import it.polimi.ingsw.view.serverSide.interfaces.GamePreparationListener;
import it.polimi.ingsw.view.serverSide.interfaces.MessageListener;
import it.polimi.ingsw.view.serverSide.interfaces.MoveExecutedListener;
import it.polimi.ingsw.view.serverSide.interfaces.ServerGeneralListener;

/**
 * This class handle connection aspects
 * of the distributed application.
 *
 * It is used by the Server as a "Network Handler"
 * to forward and receive messages and data to/from Clients.
 *
 * @author AndreaAltomare
 */
public class ServerConnection implements MoveExecutedListener, GamePreparationListener, ServerGeneralListener, MessageListener {
}
