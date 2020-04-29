package it.polimi.ingsw;

import it.polimi.ingsw.connection.ConnectionSettings;
import it.polimi.ingsw.connection.server.ServerConnection;
import it.polimi.ingsw.storage.ResourceManager;

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
        ConnectionSettings connectionSettings;
        final int DEFAULT_PORT = 9999; // default port
        int port = DEFAULT_PORT;

        // todo verificare che funzioni con l'argomento
        // todo verificare che funzioni senza l'argomento
        // todo verificare che funzioni con i file di configurazione presenti
        // todo verificare che funzioni senza i file di configurazione presenti
        /* Retrieve connection configuration settings */
        if(args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        else {
            connectionSettings = ResourceManager.serverConnectionSettings();
            if (connectionSettings != null)
                port = connectionSettings.getPort();
        }

        /* Start connection */
        try {
            System.out.println("Starting Server...");
            server = new ServerConnection(port);
            server.run();
        }
        catch (IOException ex) {
            System.err.println("Impossible to initialize the Server: " + ex.getMessage() + "!");
        }
    }
}
