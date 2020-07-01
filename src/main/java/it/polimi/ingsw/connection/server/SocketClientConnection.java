package it.polimi.ingsw.connection.server;

import it.polimi.ingsw.chat.ChatMessageEvent;
import it.polimi.ingsw.connection.utility.ConnectionManager;
import it.polimi.ingsw.connection.utility.PingResponse;
import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.view.events.QuitEvent;
import it.polimi.ingsw.view.events.SetNicknameEvent;
import it.polimi.ingsw.view.events.SetPlayersNumberEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class for Server-Client communication management.
 * It enables Server to manage multi-client connection.
 *
 * @see <a href="https://github.com/emanueledelsozzo/ingsoft-prova-finale-2020/blob/master/ese_Socket_Serialization/TrisDistributedMVC/src/main/java/it/polimi/ingsw/server/SocketClientConnection.java">github.com/emanueledelsozzo/.../SocketClientConnection.java</a>
 * @author AndreaAltomare
 */
public class SocketClientConnection extends Observable<Object> implements ClientConnection, Runnable {
    /* General */
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    /* Server-Client communication */
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ServerConnection server;
    private static final int NICKNAME_MIN_LENGTH = 3;
    private static final String UNREGISTERED_NICKNAME = "UNKNOWN"; // unique nickname (identifier) for unregistered Client
    private String nickname; // Player's (Client) unique nickname whose this connection is associated with
    /* Network-related */
    private ConnectionManager connectionManager;
    /* Connection properties */
    private boolean active = true;
    private boolean lobbyFull = false; // tells if a connection is closed because the lobby is full

    /**
     * Socket for Server-Client communication initialization.
     *
     * @param socket (Server-Client connection Socket)
     * @param server (ServerConnection object)
     */
    public SocketClientConnection(Socket socket, ServerConnection server) {
        this.socket = socket;
        this.server = server;
        this.nickname = UNREGISTERED_NICKNAME;
        this.connectionManager = new ConnectionManager(this);
    }

    /**
     * Synchronous data forwarding to Client.
     *
     * @param message (Data to send)
     */
    @Override
    public synchronized void send(Object message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush(); // To ensure data is sent
        }
        catch(SocketTimeoutException ex) {
            System.err.println("Socket timed out.\nMessage: " + ex.getMessage());
            //setActive(false);
        }
        catch(IOException ex) {
            System.err.println(ex.getMessage());
            //setActive(false);
        }
    }

    /**
     * Asynchronous data forwarding to Client.
     *
     * @param message (Data to send)
     */
    @Override
    public void asyncSend(final Object message) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        });
    }

    /**
     * Public method for externally close the connection.
     */
    public void unregisterAndClose() {
        close();
    }

    /**
     * Close connection with Client and unregister it from connected Clients.
     */
    private void close() {
        closeConnection();
        System.out.println("\nUnregistering " + nickname + "'s Client...");
        server.unregisterConnection(this);
        System.out.println("Done!");
    }

    /**
     * Close socket connection with the Client.
     */
    @Override
    public synchronized void closeConnection() {
        try {
            socket.close();
        }
        catch (IOException ex) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }

    /**
     * Method called when a connection needs to be closed
     * because the lobby is full.
     *
     * @param lobbyFull Lobby is full (boolean parameter)
     */
    @Override
    public void closeConnection(boolean lobbyFull) {
        this.lobbyFull = lobbyFull;
        closeConnection();
    }

    /**
     * Takes an Object message as an argument,
     * determines if it is a QuitEvent,
     * then execute quitting operation for the Client.
     *
     * Returns true if the Object argument is an actual QuitEvent
     * and so the connection with the Client was closed.
     *
     * @param o (Object message)
     * @return (Object o is an actual QuitEvent ? true : false)
     */
    public boolean quitHandler(Object o) {
        if(o instanceof QuitEvent) {
            if(!server.quitAndReset(this)) {
                if(isActive()) {
                    System.out.println("\n" + nickname + " is being disconnected...");
                    send(new MessageEvent("Server closed the connection."));
                    close();
                }
            }
            return true;
        }
        else
            return false;
    }

    /**
     * Takes an Object message as an argument,
     * determines if it is a PingResponse,
     * then notify the Connection Manager that
     * a Ping response message was received.
     *
     * Returns true if the Object argument is an actual PingResponse.
     *
     * @param o (Object message)
     * @return (Object o is an actual PingResponse ? true : false)
     */
    public boolean pingHandler(Object o) {
        if(o instanceof PingResponse) {
            connectionManager.pingResponseReceived((PingResponse) o);
            return true;
        }
        else
            return false;
    }

    /**
     * Takes an Object message as an argument,
     * determines if it is a ChatMessageEvent,
     * then send the chat message to all playing-players
     * by using {@code sendAll(...)} method in ServerConnection.
     *
     * Returns true if the Object argument is an actual ChatMessageEvent.
     *
     * @param o (Object message)
     * @return (Object o is an actual ChatMessageEvent ? true : false)
     */
    public boolean chatMessageHandler(Object o) {
        if(o instanceof ChatMessageEvent) {
            server.sendAll(o, this);
            return true;
        }
        else
            return false;
    }

    /**
     * Tell if the connection is active.
     *
     * @return (Connection is active ? true : false)
     */
    private synchronized boolean isActive() {
        return active;
    }

    /**
     * Set connection activity (boolean) information.
     *
     * @param active (Connection activity boolean information)
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Run() method for Server-Client Connection Manager Object.
     */
    @Override
    public void run() {
        Object read;
        SetNicknameEvent nicknameRead = null; // null initialization

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            /* 1- Ask for the Player's nickname */
            send(new NextStatusEvent("Welcome!\nType your nickname"));
            read = in.readObject();
            // check if a QuitEvent arrives
            if (quitHandler(read))
                return;

            if(read instanceof SetNicknameEvent)
                nicknameRead = (SetNicknameEvent)read;
            // Handle duplicates and invalid submissions
            while(!(read instanceof SetNicknameEvent) || nicknameRead.getNickname().length() < NICKNAME_MIN_LENGTH || nicknameRead.getNickname().equals(UNREGISTERED_NICKNAME) || !server.addClient(this, nicknameRead.getNickname())) {
                send(new InvalidNicknameEvent());
                read = in.readObject();
                // check if a QuitEvent arrives
                if (quitHandler(read))
                    return;

                if(read instanceof SetNicknameEvent)
                    nicknameRead = (SetNicknameEvent)read;
            }

            nickname = nicknameRead.getNickname();
            //server.addClient(this, nickname);

            /* 2- If this is the first Client, crate a lobby */
            send(new MessageEvent("Searching for a free lobby to join in...\n"));
            /* Synchronize to ServerConnection */
            synchronized (server) { // synchronized (server.serverLock)
                int requiredNumberOfPlayers = -1; // initialized to error condition
                SetPlayersNumberEvent setNumberEvent = null; // null initialization

                /* If this is the first Client to connect, make it choose for the number of player */
                if (server.getNumberOfPlayers() < server.MINIMUM_CLIENTS_REQUIRED) {
                    send(new RequirePlayersNumberEvent());
                    read = in.readObject();
                    // check if a QuitEvent arrives
                    if (quitHandler(read))
                        return;

                    if(read instanceof SetPlayersNumberEvent) {
                        setNumberEvent = (SetPlayersNumberEvent) read;
                        requiredNumberOfPlayers = setNumberEvent.getNumberOfPlayers();
                    }
                    // handle invalid submissions
                    while(!(read instanceof  SetPlayersNumberEvent) || (requiredNumberOfPlayers != server.MINIMUM_CLIENTS_REQUIRED && requiredNumberOfPlayers != server.MAXIMUM_CLIENTS_REQUIRED)) {
                        send(new ErrorMessageEvent("Wrong choice! Your answer must either be 2 or 3.\n")); // warn the Client about the wrong choice

                        send(new RequirePlayersNumberEvent());
                        read = in.readObject();
                        // check if a QuitEvent arrives
                        if (quitHandler(read))
                            return;

                        if(read instanceof SetPlayersNumberEvent) {
                            setNumberEvent = (SetPlayersNumberEvent) read;
                            requiredNumberOfPlayers = setNumberEvent.getNumberOfPlayers();
                        }
                    }

                    server.setNumberOfPlayers(requiredNumberOfPlayers);
                    send(new NextStatusEvent("Your choice has been registered!\n\nWaiting for other players...\n"));
                    //send(new MessageEvent("Your choice has been registered!\n\nWaiting for other players...\n"));
                }
                else {
                    send(new NextStatusEvent("Waiting for other players...\n"));
                    //send(new MessageEvent("Waiting for other players...\n"));
                }
            }

            /* 3- Join a lobby */
            server.lobby(this, nickname);

            /* 4- Execute Connection manager to handle network problems */
            executor.submit(connectionManager);

            /* 5- Keep listening to te Client while connection is active */
            while(isActive()) {
                read = in.readObject();
                if(!pingHandler(read) && !chatMessageHandler(read)) // if the Object read is a Ping response or a Chat message, do not notify the Game Controller
                    notify(read); // notify also in case of QuitEvents, to let the Controller take action on it

                if(read instanceof QuitEvent) {
                    quitHandler(read);
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex) {
            System.err.println("Error! Serialized class not found.");
        }
        catch(SocketTimeoutException ex) {
            System.err.println("Socket timed out.\nMessage: " + ex.getMessage());
        }
        catch (IOException | NoSuchElementException ex) {
            //System.err.println("Error! " + ex.getMessage());
            System.err.println("Connection stopped!");
        }
        finally {
            /* Stop ping system */
            connectionManager.stop();
            /* Close connection with an error message just if it is still active (so the error is Server-side) */
            if(isActive()) {
                send(new ErrorMessageEvent("An error on Server occurred. You are being disconnected...")); // Inform the Client that connection is being closed since an error occurred.
                //close(); // close the connection with the Client
            }
            if(!lobbyFull)
                quitHandler(new QuitEvent()); // close the connection
        }
    }


    /**
     * Sets Server-Client socket to null
     * (i.e. destroys it).
     */
    @Override
    public void destroySocket() {
        this.socket = null;
    }


    public String getNickname() {
        return nickname;
    }
}
