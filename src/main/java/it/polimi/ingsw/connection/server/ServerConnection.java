package it.polimi.ingsw.connection.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.events.LobbyFullEvent;
import it.polimi.ingsw.controller.events.NextStatusEvent;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.view.serverSide.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
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
    // TODO: maybe to remove (pass already EventObjects from VirtualView) or to implement Listener interfaces
    private final int PORT;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(128);

    private List<ClientConnection> connections = new ArrayList<>();
    private Map<String, ClientConnection> waitingConnection = new HashMap<>();
    private Map<String, ClientConnection> playingConnection = new HashMap<>();

    public final int MINIMUM_CLIENTS_REQUIRED = 2; // public because it can never change (by definition of Game match)
    public final int MAXIMUM_CLIENTS_REQUIRED = 3; // public because it can never change (by definition of Game match)
    public final Object serverLock = new Object(); // public lock to synchronize some ServerConnection operation // TODO: maybe it's useless: to remove.
    private int numberOfPlayers;
    private boolean lobbyCreated;

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
        // TODO: handle the case in which the client disconnects before the lobby is created
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
        if(lobbyCreated) {
            c.send(new LobbyFullEvent());
            c.closeConnection();
        }

        //waitingConnection.put(nickname, c);
        System.out.println("Player " + nickname + " has connected!");

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
            for (String key : keys) {
                virtualViews.add(new VirtualView(key, waitingConnection.get(key))); // here: VirtualView is instantiated and become an Observer for its ClientConnection
            }

            /* 3- Instantiate Model */
            Model model = new Model();

            /* 4- Instantiate Controller */
            Controller controller = new Controller(model);

            /* 5- Register observers */
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
                client.send(new NextStatusEvent("Joined lobby.\nWaiting for for the game to start...\n")); // todo sobstitute with NextStatus event and (MAYBE) send a (generic) message also...
            }
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
        if(!(waitingConnection.containsKey(nickname) || playingConnection.containsKey(nickname))) {
            waitingConnection.put(nickname, c);
            return true;
        }
        else {
            return false;
        }
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

    /**
     * Tell if there is a Player connection with the provided nickname.
     *
     * @param nickname (Provided nickname)
     * @return (Provided nickname is already taken ? true : false)
     */
    public synchronized boolean nicknameTaken(String nickname) {
        return waitingConnection.containsKey(nickname) || playingConnection.containsKey(nickname);
    }
}
