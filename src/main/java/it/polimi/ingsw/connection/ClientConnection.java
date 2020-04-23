package it.polimi.ingsw.connection;

import it.polimi.ingsw.controller.CardSelectionListener;
import it.polimi.ingsw.controller.ClientGeneralListener;
import it.polimi.ingsw.controller.MoveListener;
import it.polimi.ingsw.controller.TurnStatusChangeListener;

/**
 * This class handle connection aspects
 * of the distributed application.
 *
 * It is used by the Client as a "Network Handler"
 * to forward and receive messages and data to/from Server.
 *
 * @author AndreaAltomare
 */
public class ClientConnection implements MoveListener, CardSelectionListener, TurnStatusChangeListener, ClientGeneralListener {
}
