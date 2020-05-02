package it.polimi.ingsw.connection.client;

import it.polimi.ingsw.observer.MVEventSubject;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.clientSide.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.EventObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//implements MoveListener, CardSelectionListener, TurnStatusChangeListener, ClientGeneralListener // todo passare direttamente EventObject alla View

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
    // TODO: re-implement Listener interfaces
    private ExecutorService executor = Executors.newFixedThreadPool(128);
    private String ip;
    private int port;
    private View view;
    ObjectInputStream socketIn;
    PrintWriter socketOut;

    private boolean active = true;

    /**
     * Socket information initialization, for Client-Server communication.
     *
     * @param ip (IP address)
     * @param port (Port for Socket connection)
     */
    public ClientConnection(String ip, int port, View view) {
        this.ip = ip;
        this.port = port;
        this.view = view;
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
                        notifyMVEventsListeners(inputObject); // actually notify the View as a MVEventListener
                    }
                }
                catch (Exception ex) {
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    // todo add javadoc
    private class AsyncSocketWriter implements Runnable {
        private Object objectToWrite; // object to write to the socket
        private PrintWriter socketOut; // output socket

        public AsyncSocketWriter(final Object objectToWrite, final PrintWriter socketOut) {
            this.objectToWrite = objectToWrite;
            this.socketOut = socketOut;
        }

        @Override
        public void run() {
            try {
                if (isActive()) {
                    socketOut.println(objectToWrite);
                    socketOut.flush(); // To ensure data is sent
                }
            }
            catch (Exception ex) {
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
        socketIn = new ObjectInputStream(socket.getInputStream());
        socketOut = new PrintWriter(socket.getOutputStream());
        Scanner stdin = new Scanner(System.in);

        /* Add observers */ // todo check if this system works (since reading and writing from/to socket is done by threads)
        this.addMVEventsListener(view);
        view.addObserver(this);

        try {
            Thread t0 = asyncReadFromSocket(socketIn);
            //Thread t1 = asyncWriteToSocket(stdin, socketOut); // todo: maybe this is useless, to remove
            t0.join();
            //t1.join();
        }
        catch(InterruptedException | NoSuchElementException ex) {
            System.out.println("Connection closed from the client side.");
        }
        finally {
            stdin.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
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
}
