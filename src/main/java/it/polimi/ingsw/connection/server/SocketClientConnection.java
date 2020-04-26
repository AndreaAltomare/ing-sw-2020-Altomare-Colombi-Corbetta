package it.polimi.ingsw.connection.server;

import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class for Server-Client communication management.
 * It enables Server to manage multi-client connection.
 *
 * @see <a href="https://github.com/emanueledelsozzo/ingsoft-prova-finale-2020/blob/master/ese_Socket_Serialization/TrisDistributedMVC/src/main/java/it/polimi/ingsw/server/SocketClientConnection.java">github.com/emanueledelsozzo/.../SocketClientConnection.java</a>
 * @author AndreaALtomare
 */
public class SocketClientConnection extends Observable<Object> implements ClientConnection, Runnable {
    private Socket socket;
    private ObjectOutputStream out;
    private ServerConnection server;

    private boolean active = true;

    /**
     * Socket for Server-Client communication initialization.
     *
     * @param socket (Server-Client connection Socket)
     * @param server (ServerConnection object)
     */
    public SocketClientConnection(Socket socket, ServerConnection server) {
        this.socket = socket;
        this.server = server;
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
        catch(IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Asynchronous data forwarding to Client.
     *
     * @param message (Data to send)
     */
    @Override
    public void asyncSend(final Object message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }

    /**
     * Close connection with Client and unregister it from connected Clients.
     */
    private void close() {
        closeConnection();
        System.out.println("\nUnregistering Client...");
        server.unregisterConnection(this);
        System.out.println("Done!");
    }

    /**
     * Inform Client that Connection is being closed.
     */
    @Override
    public synchronized void closeConnection() {
        send("Connection closed!");
        try {
            socket.close();
        }
        catch (IOException ex) {
            System.err.println("Error when closing socket!");
        }
        active = false;
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
     * Run() method for Server-Client Connection Manager Object.
     */
    @Override
    public void run() {
        Scanner in;
        String nickname;

        try {
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            // todo send an event to notify connection has established and a nickname needs to be submitted

            /* 1- Ask for the Player's nickname */
            send("Welcome!\nType your nickname");
            String read = in.nextLine();
            // Handle duplicates
            while(!server.addClient(this, read)) {
                send("This nickname is already taken! Please try again.");
                read = in.nextLine();
            }

            nickname = read; // TODO: handle the case in which the submitted nickname is duplicated
            //server.addClient(this, nickname);

            /* 2- If this is the first Client, crate a lobby */ // TODO: maybe refactor this into a more readable method
            send("Searching for a free lobby to join in...\n");
            /* Synchronize to ServerConnection */
            synchronized (server) { // synchronized (server.serverLock)
                int requiredNumberOfPlayers = -1; // initialized to error condition

                // TODO: check here what happen if Client suddenly disconnects...
                /* If this is the first Client to connect, make it choose for the number of player */
                if (server.getNumberOfPlayers() < server.MINIMUM_CLIENTS_REQUIRED) {
                    while(requiredNumberOfPlayers != 2 && requiredNumberOfPlayers != 3) {
                        send("You are the first player!\n\nChoose the number of player for this game (you included).\nType 2 or 3");
                        requiredNumberOfPlayers = in.nextInt(); // TODO: write a try-catch block to handle the case in which user does not submit an actual number

                        if(requiredNumberOfPlayers != 2 && requiredNumberOfPlayers != 3)
                            send("Wrong choice! Your answer must either be 2 or 3.\n"); // warn the Client about the wrong choice
                    }

                    server.setNumberOfPlayers(requiredNumberOfPlayers);
                    //server.setLobbyCreated(true);
                    send("Your choice has been registered!\n\nWaiting for other players...\n");
                }
                else {
                    send("Waiting for other players...\n");
                }
            }

            /* 3- Join a lobby */
            server.lobby(this, nickname);

            /* 4- Keep listening to te Client while connection is active */
            while(isActive()) {
                read = in.nextLine();

                if(read.equals("quit")) {
                    // TODO: notify Controller about the Player's quit
                    System.out.println("\n" + nickname + " is being disconnected.");
                    //close(); // close the connection with the Client
                    break;
                }

                notify(read);
            }
        }
        catch (IOException | NoSuchElementException ex) {
            System.err.println("Error!" + ex.getMessage());
        }
        finally {
            close(); // close the connection with the Client
        }
    }
}
