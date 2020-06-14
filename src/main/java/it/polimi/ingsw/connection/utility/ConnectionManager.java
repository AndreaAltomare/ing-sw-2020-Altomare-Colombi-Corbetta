package it.polimi.ingsw.connection.utility;

import it.polimi.ingsw.connection.server.SocketClientConnection;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.events.QuitEvent;

import java.io.ObjectOutputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is responsible for detecting network problems
 * for the Server-Client connection.
 *
 * It implements the Ping message system: it keeps pinging
 * the Client (associated to a provided socket) every fixed
 * period of time, then it listens to the Ping response.
 *
 * It also defines what action(s) needs to be taken when the Timer's
 * time period elapses (both when received and when not received a
 * Ping response from the Client).
 *
 * @author AndreaAltomare
 */
public class ConnectionManager extends PingObservable implements Runnable {
    /* Timer handling */
    private final int TIMER_INITIAL_DELAY = 1000; // time in milliseconds
    private final int TIMER_TIME_PERIOD = 1000; // time in milliseconds
    private final int MAXIMUM_TIMEOUTS_NUMBER = 10; // todo rimettere a 10
    private Timer timer;
    /* Socket references */
    private final SocketClientConnection socketClient;


    /**
     * Constructor.
     *
     * @param socketClient (Socket by which checking the proper functioning of the network)
     */
    public ConnectionManager(SocketClientConnection socketClient) {
        this.socketClient = socketClient;
    }

    /**
     * Run() method for ConnectionManager class.
     *
     * An instance of ConnectionManager starts running.
     */
    @Override
    public void run() {
        /* 1- Instantiate Timer */
        timer = new Timer();

        /* 2- Create Lambda expression */
        TimeExpiredInterface timeExpiredHandler = (responseReceived) -> {
            if(responseReceived) {
                socketClient.asyncSend(new PingMessage()); // send another ping (and wait for the response)
            }
            else {
                System.err.println("Player " + socketClient.getNickname() + " is unreachable!");
                socketClient.quitHandler(new QuitEvent()); // close the connection
            }
        };

        /* 3- Start the Timer */
        TimerTask task = new TimeoutCounter(MAXIMUM_TIMEOUTS_NUMBER, timeExpiredHandler);
        this.addPingObserver((PingObserver) task); // add observer for Ping response messages
        socketClient.asyncSend(new PingMessage()); // send the first ping message
        timer.schedule(task, TIMER_INITIAL_DELAY, TIMER_TIME_PERIOD);
    }

    /**
     * Method called when a Ping response is received.
     * Notify Ping Observer(s)
     * (so notify and update Timeout Counter).
     *
     * @param o (PingResponse object)
     */
    public synchronized void pingResponseReceived(PingResponse o) {
        notifyPingObservers(o); // notify TimerCount
    }

    public void stop() {
        if(timer != null)
            timer.cancel();
    }
}
