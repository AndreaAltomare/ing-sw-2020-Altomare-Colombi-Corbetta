package it.polimi.ingsw;

import it.polimi.ingsw.connection.ConnectionSettings;
import it.polimi.ingsw.connection.client.ClientConnection;
import it.polimi.ingsw.connection.server.ServerConnection;
import it.polimi.ingsw.storage.ResourceManager;

import java.io.IOException;

/**
 * Client-side Application
 *
 * @see <a href="https://github.com/emanueledelsozzo/ingsoft-prova-finale-2020/blob/master/ese_Socket_Serialization/TrisDistributedMVC/src/main/java/it/polimi/ingsw/ClientApp.java">github.com/emanueledelsozzo/.../ClientApp.java</a>
 * @author AndteaAltomare
 */
public class ClientApp {

    /**
     * Start Client-side application.
     *
     * @param args (main() arguments)
     */
    public static void main(String[] args) {
        ClientConnection client;
        ConnectionSettings connectionSettings;
        final String DEFAULT_IP = "127.0.0.1";
        final int DEFAULT_PORT = 9999; // default port
        String ip = DEFAULT_IP;
        int port = DEFAULT_PORT;

        // todo verificare che funzioni con gli argomenti
        // todo verificare che funzioni senza gli argomenti
        // todo verificare che funzioni con i file di configurazione presenti
        // todo verificare che funzioni senza i file di configurazione presenti
        /* Retrieve connection configuration settings */
        if(args.length >= 2) {
            ip = new String(args[0]); // not sure if without "new String(...)" statement i'd get a new reference
            port = Integer.parseInt(args[1]);
        }
        else {
            connectionSettings = ResourceManager.clientConnectionSettings();
            if (connectionSettings != null) {
                ip = connectionSettings.getIp();
                port = connectionSettings.getPort();
            }
        }

        System.out.println("Stampa di prova:");
        System.out.println(ip);
        System.out.println(port);

        client = new ClientConnection(ip, port);
        try {
            client.run();
        }
        catch(IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
