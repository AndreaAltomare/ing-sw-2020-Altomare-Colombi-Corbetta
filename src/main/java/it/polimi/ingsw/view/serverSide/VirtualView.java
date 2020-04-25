package it.polimi.ingsw.view.serverSide;

import it.polimi.ingsw.model.PlaceableType;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.view.serverSide.interfaces.GamePreparationListener;
import it.polimi.ingsw.view.serverSide.interfaces.MessageListener;
import it.polimi.ingsw.view.serverSide.interfaces.MoveExecutedListener;
import it.polimi.ingsw.view.serverSide.interfaces.ServerGeneralListener;

/**
 * Virtual View represents the generic View scenario for the MVC pattern.
 * It works with Controller (and Model) and provides a Server-side View.
 *
 * It relies on the Connection Layer to handle network communication.
 *
 * @author Giorgio Corbetta
 */
public class VirtualView implements MoveExecutedListener, GamePreparationListener, ServerGeneralListener, MessageListener {
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
