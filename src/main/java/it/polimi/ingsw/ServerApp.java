package it.polimi.ingsw;

import it.polimi.ingsw.connection.server.ServerConnection;

import java.io.IOException;

/**
 * Server-side Application
 *
 * @see <a href="https://github.com/emanueledelsozzo/ingsoft-prova-finale-2020/blob/master/ese_Socket_Serialization/TrisDistributedMVC/src/main/java/it/polimi/ingsw/ServerApp.java">github.com/emanueledelsozzo/.../ServerApp.java</a>
 * @author AndreaAltomare
 */
public class ServerApp {

    /**
     * Start Server-side application.
     *
     * @param args (main() arguments)
     */
    public static void main(String[] args) {
        ServerConnection server;

        try {
            System.out.println("Starting Server...");
            server = new ServerConnection();
            server.run();
        }
        catch (IOException ex) {
            System.err.println("Impossible to initialize the Server: " + ex.getMessage() + "!");
        }
    }
}
