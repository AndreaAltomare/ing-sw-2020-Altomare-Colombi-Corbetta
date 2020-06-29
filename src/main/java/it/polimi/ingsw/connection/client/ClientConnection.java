package it.polimi.ingsw.connection.client;

import it.polimi.ingsw.chat.ChatMessageEvent;
import it.polimi.ingsw.chat.ChatMessageListener;
import it.polimi.ingsw.connection.utility.ClientConnectionManager;
import it.polimi.ingsw.connection.utility.PingMessage;
import it.polimi.ingsw.connection.utility.PingResponse;
import it.polimi.ingsw.observer.MVEventSubject;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.NoSuchElementException;
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
                    ViewMessage.populateAndSend("Socket timed out.", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
                    if(View.debugging)
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

        /**
         * Constructs a class for asynchronous writing on socket.
         *
         * @param objectToWrite Object-to-write's reference
         * @param socketOut Output stream which to write on
         */
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
                else{
                    ViewMessage.populateAndSend("Connection not active.", ViewMessage.MessageType.FROM_SERVER_ERROR);
                    if(View.debugging)
                        System.err.println("Connection not active.");
                }
            }
            catch(SocketTimeoutException ex) {
                ViewMessage.populateAndSend("Socket timed out.", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
                if(View.debugging)
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
        if(View.debugging)
            System.out.println("Connecting to the Server...");
        Socket socket = new Socket(ip, port);
        if(View.debugging)
            System.out.println("Connection established.\n");
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketIn = new ObjectInputStream(socket.getInputStream());

        try {
            Thread t0 = asyncReadFromSocket(socketIn);
            //Thread t1 = asyncWriteToSocket(stdin, socketOut);
            t0.join();
            //t1.join();
        }
        catch(InterruptedException | NoSuchElementException ex) {
            ViewMessage.populateAndSend("Connection closed from the client side.", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
            if(View.debugging) {
                System.err.println("ERROR: " + ex.getMessage());
                System.out.println("Connection closed from the client side.");
            }
        }
        finally {
            //stdin.close();
            connectionManager.stop();
            socketIn.close();
            socketOut.close();
            socket.close();
            if(View.debugging)
                System.out.println("Connection closed.");
            ViewMessage.populateAndSend("Connection closed.", ViewMessage.MessageType.FATAL_ERROR_MESSAGE);
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


    /**
     * Set the handler for chat messages.
     *
     * @param chatMessageHandler Chat message handler
     */
    public void setChatMessageHandler(ChatMessageListener chatMessageHandler) {
        this.chatMessageHandler = chatMessageHandler;
    }
}
