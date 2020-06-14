package it.polimi.ingsw.connection.client;

import it.polimi.ingsw.chat.ChatMessageEvent;
import it.polimi.ingsw.chat.ChatMessageListener;
import it.polimi.ingsw.connection.utility.ClientConnectionManager;
import it.polimi.ingsw.connection.utility.PingMessage;
import it.polimi.ingsw.connection.utility.PingResponse;
import it.polimi.ingsw.observer.MVEventSubject;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.clientSide.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.EventObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class handle connection aspects
 * of the distributed application.
 *
 * It is used by the Client as a "Network Handler"
 * to forward and receive messages and data to/from Server.
 *
 * @see <a href="https://github.com/emanueledelsozzo/ingsoft-prova-finale-2020/blob/master/ese_Socket_Serialization/TrisDistributedMVC/src/main/java/it/polimi/ingsw/client/Client.java">github.com/emanueledelsozzo/.../Client.java</a>
 * @author AndreaAltomare
 */
public class ClientConnection extends MVEventSubject implements Observer<Object> {
    // TODO: re-implement Listener interfaces [...done?]
    /* General */
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    /* Client-Server communication */
    private String ip;
    private int port;
    private ObjectInputStream socketIn;
    private ObjectOutputStream socketOut;
    private ChatMessageListener chatMessageHandler;
    /* Network-related */
    private ClientConnectionManager connectionManager;
    /* Connection properties */
    private boolean active = true;

    /**
     * Socket information initialization, for Client-Server communication.
     *
     * @param ip (IP address)
     * @param port (Port for Socket connection)
     */
    public ClientConnection(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.connectionManager = new ClientConnectionManager(this);
        //this.view = view;
    }

    // TODO: check if it works correctly
    // TODO: Maybe this method is useless
    /*
     * Start the View for user interaction.
     *
     * @return The thread in which the View is running
     */
    /*public Thread startView() {
        Thread t = new Thread(view);
        t.start();
        return t;
    }*/

    /**
     * Asynchronous data reading from Server.
     *
     * @param socketIn (Input stream for data received)
     * @return A thread to manage asynchronous reading
     */
    public Thread asyncReadFromSocket(final ObjectInputStream socketIn) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(isActive()) {
                        Object inputObject = socketIn.readObject();
                        if(!pingHandler(inputObject) && !chatMessage(inputObject))
                            notifyMVEventsListeners(inputObject); // actually notify the View as a MVEventListener
                    }
                }
                catch(SocketTimeoutException ex) {
                    System.err.println("Socket timed out.\nMESSAGE: " + ex.getMessage());
                    setActive(false);
                }
                catch (Exception ex) {
                    //System.err.println("ERROR: " + ex.getMessage());
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    /**
     * Private class for asynchronous data forwarding to Server.
     *
     * @author AndreaAltomare
     */
    private class AsyncSocketWriter implements Runnable {
        private Object objectToWrite; // object to write to the socket
        private ObjectOutputStream socketOut; // output socket

        public AsyncSocketWriter(final Object objectToWrite, final ObjectOutputStream socketOut) {
            this.objectToWrite = objectToWrite;
            this.socketOut = socketOut;
        }

        @Override
        public void run() {
            try {
                if (isActive()) {
                    socketOut.writeObject(objectToWrite);
                    socketOut.flush(); // To ensure data is sent
                }
                else
                    System.err.println("Connection not active.");
            }
            catch(SocketTimeoutException ex) {
                System.err.println("Socket timed out.\nMESSAGE: " + ex.getMessage());
                setActive(false);
            }
            catch (Exception ex) {
                //System.err.println("ERROR: " + ex.getMessage());
                setActive(false);
            }
        }
    }

    /**
     * Tell if the connection is active.
     *
     * @return (Connection is active ? true : false)
     */
    public synchronized boolean isActive() {
        return active;
    }

    /**
     * Set connection activity (boolean) information.
     *
     * @param active (Connection activity boolean information)
     */
    public synchronized void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Run() method for Client-Server Connection Manager Object (Network Handler).
     *
     * @throws IOException (Exception handled by ClientApp)
     */
    public void run() throws IOException {
        System.out.println("Connecting to the Server...");
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established.\n");
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketIn = new ObjectInputStream(socket.getInputStream());
        //Scanner stdin = new Scanner(System.in);

        /* Add observers */ // todo check if this system works (since reading and writing from/to socket is done by threads)
        //this.addMVEventsListener(view);
        //view.addObserver(this);

        try {
            Thread t0 = asyncReadFromSocket(socketIn);
            //Thread t1 = startView();
            //Thread t1 = asyncWriteToSocket(stdin, socketOut); // todo: maybe this is useless, to remove
            t0.join();
            //t1.join();
        }
        catch(InterruptedException | NoSuchElementException ex) {
            System.err.println("ERROR: " + ex.getMessage());
            System.out.println("Connection closed from the client side.");
        }
        finally {
            // TODO: forse questi metodi danno problemi
            //stdin.close();
            connectionManager.stop();
            socketIn.close();
            socketOut.close();
            socket.close();
            System.out.println("Connection closed."); // todo [debug]
        }
    }



    /**
     * Takes an Object message as an argument,
     * determines if it is a PingMessage,
     * then respond to the Server with a PingResponse.
     *
     * Returns true if the Object argument is an actual PingMessage.
     *
     * @param o (Object message)
     * @return (Object o is an actual PingMessage ? true : false)
     */
    public boolean pingHandler(Object o) {
        if(o instanceof PingMessage) {
            executor.submit(new AsyncSocketWriter(new PingResponse(),socketOut));

            /* When the first Ping message from the Server is received,
             * check the connection by starting the ClientConnectionManager.
             */
            if(!connectionManager.isRunning())
                executor.submit(connectionManager);
            else
                connectionManager.pingMessageReceived((PingMessage) o);

            return true;
        }
        else
            return false;
    }

    /**
     * Takes an Object message as an argument,
     * determines if it is a ChatMessageEvent,
     * then call the {@code update(...)} method on the Chat Message Handler (Listener).
     *
     * Returns true if the Object argument is an actual ChatMessageEvent.
     *
     * @param o (Object message)
     * @return (Object o is an actual ChatMessageEvent ? true : false)
     */
    public boolean chatMessage(Object o) {
        if(o instanceof ChatMessageEvent) {
            chatMessageHandler.update((ChatMessageEvent) o);
            return true;
        }
        else
            return false;
    }

    /**
     * This method send Event Object generated from View
     * to the Server by using Socket communication methods.
     *
     * @param o (Event Object to send)
     */
    @Override
    public void update(Object o) {
        /* Asynchronously write to the Socket */
        // todo usare gli executor (check se funziona: se non funziona, anche per problemi di caduta di connessione, istanziare un nuovo Thread alla maniera "tradizionale"
        executor.submit(new AsyncSocketWriter(o,socketOut));
    }

    /* Maybe it can be useful in future implementations (like a chat system) */
    @Override
    public void update(Object o, String nickname) {}

    /**
     * This method closes the connection between Client and Server.
     */
    public void closeConnection() {
        setActive(false);
    }





    // TODO: MAYBE it's a useless method. Needs to be removed.
    /*
     * Asynchronous data forwarding to Server.
     *
     * @param stdin (STDIN Scanner for User input)
     * @param socketOut (PrintWriter for writing on Output stream, to send data)
     * @return A thread to manage asynchronous writing
     */
    /*public Thread asyncWriteToSocket(final Scanner stdin, final PrintWriter socketOut) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(isActive()) { // TODO: add code to actually send to socket the Event Object generated from the View
                        String inputLine = stdin.nextLine();
                        socketOut.println(inputLine);
                        socketOut.flush(); // To ensure data is sent
                    }
                }
                catch(Exception ex) {
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }*/

    public void setChatMessageHandler(ChatMessageListener chatMessageHandler) {
        this.chatMessageHandler = chatMessageHandler;
    }
}
