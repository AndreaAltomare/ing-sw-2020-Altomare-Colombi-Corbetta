package it.polimi.ingsw.connection;

import it.polimi.ingsw.view.GamePreparationListener;
import it.polimi.ingsw.view.MessageListener;
import it.polimi.ingsw.view.MoveExecutedListener;
import it.polimi.ingsw.view.ServerGeneralListener;

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
