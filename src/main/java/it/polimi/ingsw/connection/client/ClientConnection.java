package it.polimi.ingsw.connection.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.EventObject;

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
public class ClientConnection {
    // TODO: re-implement Listener interfaces
    private String ip;
    private int port;

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
                    while(isActive()) { // TODO: modify the if-then-else (or switch-case) istanceof condition to meet the actual communication protocol
                        Object inputObject = socketIn.readObject();
                        if(inputObject instanceof String) {
                            System.out.println((String)inputObject);
                        }
                        else if(inputObject instanceof EventObject) {
                            System.out.println("Event received.");
                        }
                        else {
                            throw new IllegalArgumentException();
                        }
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

    /**
     * Asynchronous data forwarding to Server.
     *
     * @param stdin (STDIN Scanner for User input)
     * @param socketOut (PrintWriter for writing on Output stream, to send data)
     * @return A thread to manage asynchronous writing
     */
    public Thread asyncWriteToSocket(final Scanner stdin, final PrintWriter socketOut) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(isActive()) {
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
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established.");
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        Scanner stdin = new Scanner(System.in);

        try {
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t1 = asyncWriteToSocket(stdin, socketOut);
            t0.join();
            t1.join();
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
}
