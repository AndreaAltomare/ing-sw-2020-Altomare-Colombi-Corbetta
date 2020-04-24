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
public class SocketClientConnection extends Observable<String> implements ClientConnection, Runnable {
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
    private synchronized void send(Object message) {
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
        System.out.println("Unregistering Client...");
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
            send("Welcome!\nType your nickname");
            String read = in.nextLine();
            nickname = read;
            server.lobby(this, nickname); // TODO: to modify
            while(isActive()) {
                read = in.nextLine();
                notify(read);
            }
        }
        catch (IOException | NoSuchElementException ex) {
            System.err.println("Error!" + ex.getMessage());
        }
        finally {
            close();
        }
    }
}
