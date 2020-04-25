package it.polimi.ingsw.connection.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.view.serverSide.VirtualView;
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

    private List<ClientConnection> connections = new ArrayList<ClientConnection>();
    private Map<String, ClientConnection> waitingConnection = new HashMap<>();
    private Map<ClientConnection, ClientConnection> playingConnection = new HashMap<>();

    public final int MINIMUM_CLIENTS_REQUIRED = 2; // public because it can never change (by definition of Game match)
    public final int MAXIMUM_CLIENTS_REQUIRED = 3; // public because it can never change (by definition of Game match)
    public final Object serverLock = new Object(); // public lock to synchronize some ServerConnection operation
    private int numberOfPlayers;
    private boolean lobbyCreated;

    /**
     * Constructor (Initialization of ServerSocket).
     *
     * @throws IOException (Exception handled by ServerApp)
     */
    public ServerConnection() throws IOException {
        numberOfPlayers = -1; // initialized to an non-valid value
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
                registerConnection(socketConnection);
                executor.submit(socketConnection);
            }
            catch (IOException ex) {
                System.out.println("Connection error!");
            }
        }
    }

    /**
     * Register connection.
     *
     * @param c (Server-Client communication Socket)
     */
    public synchronized void registerConnection(ClientConnection c) {
        connections.add(c);
    }

    /**
     * Unregister connection.
     *
     * @param c (Server-Client communication Socket)
     */
    public synchronized void unregisterConnection(ClientConnection c) {
        connections.remove(c);
        ClientConnection playerConnection = playingConnection.get(c);

        if(playerConnection != null) {
            playerConnection.closeConnection();
            playingConnection.remove(c);
            playingConnection.remove(playerConnection); // TODO: non e' ridondante questo metodo? c e playerConnection dovrebbero essere lo stesso oggetto di classe ClientConnection...
        }

        //playingConnection.remove(c);
        //playingConnection.remove(playerConnection); // TODO: non e' ridondante questo metodo? c e playerConnection dovrebbero essere lo stesso oggetto di classe ClientConnection...

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
        waitingConnection.put(nickname, c);

        /* Create a new game lobby if it has not been created yet
        * and the number of Client connected is equal to the number of players required.
        */
        if(!lobbyCreated && waitingConnection.size() == numberOfPlayers) {
            List<String> keys = new ArrayList<>(waitingConnection.keySet());
            List<ClientConnection> clients = new ArrayList<>(MAXIMUM_CLIENTS_REQUIRED); // todo not sure if this is the right way to use this Constructor method
            List<VirtualView> virtualViews = new ArrayList<>(MAXIMUM_CLIENTS_REQUIRED); // TODO: NOT SURE if this is the right (semantic) way to instantiate the VirtualView

            /* 1- Get ClientConnection(s) */
            for (String key : keys) {
                clients.add(waitingConnection.get(key));
            }

            /* 2- Instantiate VirtualView(s) */
            for (ClientConnection client : clients) {
                virtualViews.add(new VirtualView(keys.get(0), waitingConnection.get(keys.get(0))));
            }

            /* 3- Instantiate Model */
            Model model = new Model();

            /* 4- Instantiate Controller */
            Controller controller = new Controller(model);

            // TODO: trovare il modo di registrare gli observer per il Controller e per le VirtualView
            /* 5- */
        }
    }

    public synchronized void addClient(ClientConnection c, String nickname) {
        waitingConnection.put(nickname, c);
    }

    public int getWaitingConnectionSize() {
        return waitingConnection.size();
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    // TODO: maybe "synchronized" here can trigger problems
    public synchronized void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public boolean isLobbyCreated() {
        return lobbyCreated;
    }

    // TODO: maybe "synchronized" here can trigger problems
    public synchronized void setLobbyCreated(boolean lobbyCreated) {
        this.lobbyCreated = lobbyCreated;
    }
}