package it.polimi.ingsw.connection.server;

import it.polimi.ingsw.view.serverSide.interfaces.GamePreparationListener;
import it.polimi.ingsw.view.serverSide.interfaces.MessageListener;
import it.polimi.ingsw.view.serverSide.interfaces.MoveExecutedListener;
import it.polimi.ingsw.view.serverSide.interfaces.ServerGeneralListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//implements MoveExecutedListener, GamePreparationListener, ServerGeneralListener, MessageListener

/**
 * This class handle connection aspects
 * of the distributed application.
 *
 * It is used by the Server as a "Network Handler"
 * to forward and receive messages and data to/from Clients.
 *
 * @see <a href="https://github.com/emanueledelsozzo/ingsoft-prova-finale-2020/blob/master/ese_Socket_Serialization/TrisDistributedMVC/src/main/java/it/polimi/ingsw/server/Server.java">github.com/emanueledelsozzo/.../Server.java</a>
 * @author AndreaAltomare
 */
public class ServerConnection {
    // TODO: maybe to remove (pass already EventObjects from VirtualView) or to implement Listener interfaces
    private static final int PORT = 9999;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private Map<String, ClientConnection> waitingConnection = new HashMap<>();
    private Map<ClientConnection, ClientConnection> playingConnection = new HashMap<>();

    /**
     * Constructor (Initialization of ServerSocket).
     *
     * @throws IOException (Exception handled by ServerApp)
     */
    public ServerConnection() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    /**
     * Multi-client connection management.
     */
    public void run() {
        while(true) {
            try {
                Socket newSocket = serverSocket.accept();
                SocketClientConnection socketConnection = new SocketClientConnection(newSocket, this);
                executor.submit(socketConnection);
            }
            catch (IOException ex) {
                System.out.println("Connection error!");
            }
        }
    }

    /**
     * Unregister connection.
     *
     * @param c (Server-Client communication Socket)
     */
    public synchronized void unregisterConnection(ClientConnection c) {
        ClientConnection playerConnection = playingConnection.get(c);

        if(playerConnection != null) {
            playerConnection.closeConnection();
        }

        playingConnection.remove(c);
        playingConnection.remove(playerConnection); // TODO: non e' ridondante questo metodo? c e playerConnection dovrebbero essere lo stesso oggetto di classe ClientConnection...

        //waitingConnection.keySet().removeIf(s -> waitingConnection.get(s) == c);
        Iterator<String> iterator = waitingConnection.keySet().iterator();
        while(iterator.hasNext()) {
            if(waitingConnection.get(iterator.next()) == c) {
                iterator.remove();
            }
        }
    }

    // TODO: modify Javadoc too
    /**
     * Wait for another player.
     *
     * @param c (Server-Client communication Socket)
     * @param nickname (Player's nickname)
     */
    public synchronized void lobby(ClientConnection c, String nickname) {
        // TODO: Write code to adapt to the proper GameRoom management and scenario (look at SassoCartForbiceLizardSpock_DistributedMVC su github Del Sozzo
    }
}
