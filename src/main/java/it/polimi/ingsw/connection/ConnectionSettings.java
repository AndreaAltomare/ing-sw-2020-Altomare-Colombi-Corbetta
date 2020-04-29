package it.polimi.ingsw.connection;

import java.io.Serializable;

/**
 * Bean class to encapsulate Connection settings data.
 *
 * @author AndreaAltomare
 */
public class ConnectionSettings implements Serializable {
    private String ip;
    private int port;

    /* Default constructor */
    public ConnectionSettings() {}

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
