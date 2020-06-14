package it.polimi.ingsw.connection.utility;

import it.polimi.ingsw.connection.client.ClientConnection;
import it.polimi.ingsw.view.events.QuitEvent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is responsible for detecting network problems
 * for the Client-Server connection.
 *
 * It implements the Ping message system: it keeps listening
 * to Ping messages from Server every fixed period of time.
 *
 * It also defines what action(s) needs to be taken when the Timer's
 * time period elapses (both when received and when not received a
 * Ping message from the Server).
 *
 * @author AndreaAltomare
 */
public class ClientConnectionManager implements Runnable {
    /* General */
    private volatile boolean running; // tells if this ClientConnectionManager instance is running
    /* Timer handling */
    private final int TIMER_INITIAL_DELAY = 20000; // time in milliseconds (20 secs)
    private final int TIMER_TIME_PERIOD = 1000; // time in milliseconds
    private final int MAXIMUM_TIMEOUTS_NUMBER = 20; // todo rimettere a 20
    private TimeoutCounter timeoutCounter;
    private Timer timer;
    /* Socket references */
    private final ClientConnection clientConnection;


    /**
     * Constructor.
     *
     * @param clientConnection (Socket by which checking the proper functioning of the network)
     */
    public ClientConnectionManager(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        this.running = false;
    }

    /**
     * Run() method for ClientConnectionManager class.
     *
     * An instance of ClientConnectionManager starts running.
     */
    @Override
    public void run() {
        /* 0- Now this ClientConnectionManager instance is running */
        running = true;

        /* 1- Instantiate Timer */
        timer = new Timer();

        /* 2- Create Lambda expression */
        TimeExpiredInterface timeExpiredHandler = (responseReceived) -> {
            if(!responseReceived) {
                System.err.println("Server unreachable!"); // todo vedere che succede con questi messaggi quando è attiva la GUI
                clientConnection.update(new QuitEvent()); // if no Ping message is received, notify the Server the willing to quit...
                clientConnection.closeConnection(); // ...and close the connection.
            }
        };

        /* 3- Start the Timer */
        this.timeoutCounter = new TimeoutCounter(MAXIMUM_TIMEOUTS_NUMBER, timeExpiredHandler);
        TimerTask task = this.timeoutCounter;
        timer.schedule(task, TIMER_INITIAL_DELAY, TIMER_TIME_PERIOD);
    }

    /**
     * Method called when a Ping message is received.
     * Notify TimeoutCounter that a message was received.
     *
     * @param o (PingMessage object)
     */
    public synchronized void pingMessageReceived(PingMessage o) {
        timeoutCounter.setResponseReceived(true);
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        if(timer != null)
            timer.cancel();
    }
}
