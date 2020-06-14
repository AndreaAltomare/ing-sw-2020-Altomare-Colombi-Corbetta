package it.polimi.ingsw.connection.utility;

/**
 * Specialized Runtime Exception to reset the Server
 * (disconnecting all the Players) when it needs to be done.
 *
 * @author AndreaAltomare
 */
public class ServerResetException extends RuntimeException {
    private final String message;

    /**
     * Default constructor.
     */
    public ServerResetException() {
        this.message = "Server is going to be reset.";
    }

    /**
     * Custom message constructor.
     *
     * @param message Custom message
     */
    public ServerResetException(String message) {
        this.message = message;
    }
}
