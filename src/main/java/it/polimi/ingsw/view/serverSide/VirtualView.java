package it.polimi.ingsw.view.serverSide;

import it.polimi.ingsw.connection.server.ClientConnection;
import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.serverSide.interfaces.GamePreparationListener;
import it.polimi.ingsw.view.serverSide.interfaces.MessageListener;
import it.polimi.ingsw.view.serverSide.interfaces.MoveExecutedListener;
import it.polimi.ingsw.view.serverSide.interfaces.ServerGeneralListener;

import java.util.EventObject;

/**
 * Virtual View represents the generic View scenario for the MVC pattern.
 * It works with Controller (and Model) and provides a Server-side View.
 *
 * It relies on the Connection Layer to handle network communication.
 *
 * @author Giorgio Corbetta
 */
public class VirtualView extends Observable<Object> implements MoveExecutedListener, GamePreparationListener, ServerGeneralListener, MessageListener {
    //private Player player;
    private String  playerNickname; // Player's unique nickname
    private ClientConnection connection;

    /**
     * Constructor: Every VirtualView is bounded to an unique Player (by the nickname)
     * and to its Server-Client Socket connection.
     *
     * @param playerNickname (Player's unique nickname [ID])
     * @param c (Server-Client Socket connection reference)
     */
    public VirtualView(String playerNickname, ClientConnection c) {
        this.playerNickname = playerNickname;
        this.connection = c;
        c.addObserver(new MessageReceiver());
        //c.asyncSend("Scrivi qualcosa [metodo send ASINCRONO]");
        //c.send("Scrivi qualcosa [metodo send SINCRONO]");
    }

    // TODO: check if simple string-based communication works properly
    // TODO: maybe can be transformed into a instance object attribute
    private class MessageReceiver implements Observer<Object> {

        // TODO: write Javadoc here
        @Override
        public void update(Object message) {
            System.out.println("\n(From VirtualView) Received: " + (String)message);
            System.out.println("I'm " + playerNickname + "'s VirtualView");
            // TODO: if this thing works, insert here operation to call proper event method handler
            VirtualView.this.notify(message, playerNickname); // notify Controller with Player's nickname information - IMPORTANT!!
        }

        // TODO: write Javadoc here
        @Override
        public void update(Object message, String info) {
            VirtualView.this.notify(message, info);
        }
    }

    /**
     * General update() method for Observer Pattern.
     *
     * @param o (Object object)
     */
    @Override
    public void update(Object o) {
        // todo code
    }

    /**
     * General update() method for Observer Pattern.
     * Additional info provided.
     *
     * @param o (Object message)
     * @param info (Additional information)
     */
    @Override
    public void update(Object o, String info) {
        // todo code
    }





    @Override
    public void onWorkerPlacement(Worker worker, int x, int y) {

    }

    @Override
    public void onCardSelection(String cardName) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onWorkerMovement(Worker worker, int x, int y) {

    }

    @Override
    public void onWorkerConstruction(Worker worker, int x, int y, PlaceableType block) {

    }

    @Override
    public void onWorkerRemoval(Worker worker, int x, int y) {

    }

    @Override
    public void onStatusChange(ClientStatus clientStatus) {

    }

    @Override
    public void onNextStatus() {

    }

    @Override
    public void serverSendData() {

    }

    public String getPlayerNickname() {
        return playerNickname;
    }
}
