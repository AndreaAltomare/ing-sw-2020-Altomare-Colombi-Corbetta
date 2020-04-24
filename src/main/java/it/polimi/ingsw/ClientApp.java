package it.polimi.ingsw;

import it.polimi.ingsw.connection.client.ClientConnection;

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
        String ip = "127.0.0.1";
        int port = 9999;

        ClientConnection client = new ClientConnection(ip, port);
        try {
            client.run();
        }
        catch(IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
