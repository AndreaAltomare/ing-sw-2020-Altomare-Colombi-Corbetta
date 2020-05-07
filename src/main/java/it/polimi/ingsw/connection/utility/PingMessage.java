package it.polimi.ingsw.connection.utility;

import java.io.Serializable;

/**
 * This class encapsulate a Ping message.
 * It is used for Client liveness detection
 * and so to detect network problems.
 *
 * @author AndreaAltomare
 */
public class PingMessage implements Serializable {

    public PingMessage() {}
}
