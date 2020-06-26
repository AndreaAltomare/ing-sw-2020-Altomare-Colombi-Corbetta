package it.polimi.ingsw.connection.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.events.LobbyFullEvent;
import it.polimi.ingsw.controller.events.MessageEvent;
import it.polimi.ingsw.controller.events.NextStatusEvent;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.view.serverSide.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private final int PORT; // can be useful
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);

    private List<ClientConnection> connections = new ArrayList<>();
    private Map<String, ClientConnection> waitingConnection = new HashMap<>(); // In case of Multiple Games (Waiting list)
    private Map<String, ClientConnection> playingConnection = new HashMap<>(); // In case of Multiple Games (Playing list)

    public final int MINIMUM_CLIENTS_REQUIRED = 2; // public because it can never change (by definition of Game match)
    public final int MAXIMUM_CLIENTS_REQUIRED = 3; // public because it can never change (by definition of Game match)
    private int numberOfPlayers;
    private volatile boolean lobbyCreated;

    private Model model;
    private Controller controller;
    List<VirtualView> virtualViews;

    /**
     * Constructor (Initialization of ServerSocket).
     *
     * @throws IOException (Exception handled by ServerApp)
     */
    public ServerConnection(int port) throws IOException {
        System.out.println("Initialization...");
        numberOfPlayers = -1; // initialized to an non-valid value
        this.PORT = port;
        this.serverSocket = new ServerSocket(port);
        this.model = null;
        this.controller = null;
        this.virtualViews = null;
    }

    /**
     * Multi-client connection management.
     */
    public void run() {
        System.out.println("Server is ready.\n");

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
        /* 1- Unregister Observer(s) */
        if(!lobbyCreated || (lobbyCreated && !playingConnection.containsValue(c))) {
            /* 2- Remove connection entries (and items) */
            connections.remove(c);
            waitingConnection.keySet().removeIf(s -> waitingConnection.get(s) == c);
            playingConnection.keySet().removeIf(s -> playingConnection.get(s) == c);
        }
    }


    /**
     * If the waiting list has reached the requested number of Players,
     * create a lobby and prepare the game.
     *
     * If a lobby has been already created, inform the Player he/she cannot
     * join now.
     *
     * @param c (Server-Client communication Socket)
     * @param nickname (Player's nickname)
     */
    public synchronized void lobby(ClientConnection c, String nickname) {
        if(lobbyCreated) {
            c.send(new LobbyFullEvent());
            c.closeConnection(true);
            unregisterConnection(c);
        }
        else {
            System.out.println("Player " + nickname + " has connected!");
        }

        /* Create a new game lobby if it has not been created yet
        * and the number of Client connected is equal to the number of players required.
        */
        if(!lobbyCreated && waitingConnection.size() == numberOfPlayers) {
            List<String> keys = new ArrayList<>(waitingConnection.keySet());
            List<ClientConnection> clients = new ArrayList<>(MAXIMUM_CLIENTS_REQUIRED);
            virtualViews = new ArrayList<>(MAXIMUM_CLIENTS_REQUIRED);

            /* 1- Get ClientConnection(s) */
            for (String key : keys) {
                clients.add(waitingConnection.get(key));
            }

            /* 2- Instantiate VirtualView(s) */
            for (String key : keys) {
                virtualViews.add(new VirtualView(key, waitingConnection.get(key))); // here: VirtualView is instantiated and become an Observer for its ClientConnection
            }

            /* 3- Instantiate Model */
            model = new Model();

            /* 4- Instantiate Controller */
            controller = new Controller(model, keys); // "keys" are the Players' nicknames

            /* 5- Register observers */
            // By registering observers, references to Controller and VirtualView(s) are not lost (even when Lobby(...) method is finished running)
            for (VirtualView vw : virtualViews) {
                controller.addObserver(vw); // Observer(s) to the Controller
                vw.addVCEventsListener(controller); // Observer to the VirtualView(s)
            }

            /* 6- Now all Clients in "waiting" must be considered in a "playing" status */
            for (String key : keys) {
                playingConnection.put(key, waitingConnection.get(key));
            }

            /* 7- Clear Waiting Player Map */
            waitingConnection.clear();

            /* 8- Set Lobby Created to True */
            lobbyCreated = true;

            /* 9- Alert all clients they joined the lobby successfully */
            for(ClientConnection client : clients) {
                client.send(new NextStatusEvent("Joined lobby.\nWaiting for for the game to start...\n"));
                //client.send(new MessageEvent("Joined lobby.\nWaiting for for the game to start...\n"));
            }

            /* 10- Start a new game match */
            executor.submit(controller);
        }
    }

    /**
     * If the nickname provided for the Client is not taken yet,
     * register the Client connection.
     *
     * @param c (Client connection)
     * @param nickname (Provided nickname)
     * @return (Client connection was registered ? true : false)
     */
    public synchronized boolean addClient(ClientConnection c, String nickname) {
        if(!nicknameTaken(nickname)) {
            waitingConnection.put(nickname, c);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Tell if there is a Player connection with the provided nickname.
     *
     * @param nickname (Provided nickname)
     * @return (Provided nickname is already taken ? true : false)
     */
    public synchronized boolean nicknameTaken(String nickname) {
        return waitingConnection.containsKey(nickname) || playingConnection.containsKey(nickname);
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public synchronized void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
     * Sends an Object to all playing clients.
     *
     * @param o Object-message to send
     */
    public synchronized void sendAll(Object o) {
        playingConnection.values().forEach(c -> c.asyncSend(o));
    }

    /**
     * Sends an Object to all playing clients but the sender one.
     *
     * @param o Object-message to send
     * @param sender Client who sent the message
     */
    public synchronized void sendAll(Object o, ClientConnection sender) {
        playingConnection.values().forEach(c -> {
                                                    if(c != sender) {
                                                        c.asyncSend(o);
                                                    }
        });
    }

    /**
     * If the game has already started
     * and the Client who quit was a playing player:
     * quits the connection with every Client
     * and resets the Server to accept new ones;
     *
     * else: do not reset the Server.
     *
     * @param c Connection of the Client who quit
     * @return True if all connection have been closed and the Server has been reset.
     */
    public synchronized boolean quitAndReset(ClientConnection c) {
        if(lobbyCreated && playingConnection.containsValue(c)) {
            quit();
            reset();
            return true;
        }
        return false;
    }

    /**
     * Reset the Server in order to be ready to start
     * a new game with new Players (Clients).
     */
    private void reset() {
        /* Reset connections */
        connections.clear();
        waitingConnection.clear();
        playingConnection.clear();
        /* Reset flags and basic data */
        numberOfPlayers = -1; // initialized to a non-valid value
        lobbyCreated = false;
        /* Reset game-system objects */
        model = null;
        controller = null;
        virtualViews.clear();
    }

    /**
     * Notify Clients that the connection is being closed,
     * then close the connections.
     */
    private void quit() {
        notifyClients();
        closeConnections();
    }

    /**
     * Close the connection with every playing Player's Client.
     */
    private void closeConnections() {
        playingConnection.values().forEach(ClientConnection::unregisterAndClose);
        playingConnection.values().forEach(ClientConnection::destroySocket);
        playingConnection.values().forEach(c -> c = null);
    }

    /**
     * Notify every playing Client about the imminent connection closing.
     */
    private void notifyClients() {
        /* Send a quit message from Server */
        playingConnection.values().forEach(c -> {
            c.send(new MessageEvent("A Player left the game. Server closed the connection.")); // can be parameterized
        });
    }
}
