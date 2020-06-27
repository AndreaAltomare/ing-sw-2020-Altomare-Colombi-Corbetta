package it.polimi.ingsw;

import it.polimi.ingsw.connection.ConnectionSettings;
import it.polimi.ingsw.connection.server.ServerConnection;
import it.polimi.ingsw.storage.ResourceManager;

import java.io.IOException;
import java.net.URISyntaxException;

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
        boolean defaultSettings = true;

        /* Initialize information to properly work with resources */
        try {
            ResourceManager.initializeResources();
        }
        catch (URISyntaxException ex) {
            System.out.println("Something went wrong when analyzing the application path.\nApplication is closing...");
            System.exit(1);
        }
        catch (IOException ex) {
            System.out.println("Something went wrong when getting the canonical path for the application.\nApplication is closing...");
            System.exit(1);
        }

        /* Retrieve connection configuration settings */
        if(args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
                defaultSettings = false;
            }
            catch(Exception ex) {
                System.out.println("Bad arguments.\nServer is going to be initialized with default connection settings.\n\n");
            }
        }

        if(defaultSettings) {
            connectionSettings = ResourceManager.serverConnectionSettings();
            if (connectionSettings != null)
                port = connectionSettings.getPort();
        }

        /* Start connection */
        try {
            System.out.println("Starting Server...");
            System.out.println("Listening on PORT [" + port + "]");
            server = new ServerConnection(port);
            server.run();
        }
        catch (IOException ex) {
            System.err.println("Impossible to initialize the Server: " + ex.getMessage() + "!");
        }
    }
}
