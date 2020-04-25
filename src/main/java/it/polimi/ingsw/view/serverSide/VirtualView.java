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

    public VirtualView(String playerNickname, ClientConnection c) {
        this.playerNickname = playerNickname;
        this.connection = c;
        c.addObserver(new MessageReceiver());
        //c.asyncSend("Scrivi qualcosa [metodo send ASINCRONO]");
        //c.send("Scrivi qualcosa [metodo send SINCRONO]");
    }

    // TODO: check if simple string-based communication works properly
    private class MessageReceiver implements Observer<String> {

        @Override
        public void update(String message) {
            System.out.println("Received: " + message);
            // TODO: if this thing works, insert here operation to call proper event method handler
        }
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
}
