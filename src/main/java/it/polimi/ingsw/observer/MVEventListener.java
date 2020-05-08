package it.polimi.ingsw.observer;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.view.serverSide.ClientStatus;

/**
 * Listener interface for Controller-generated events.
 *
 * MVEvent (Events form Server)
 *
 * @author Giorgio Corbetta
 */
public interface MVEventListener {
    /* Server general listener */
    public void update(NextStatusEvent nextStatus);
    public void update(ServerSendDataEvent serverSentData); // Game match general data (to respond to viewRequestData, receive data from Server)
    public void update(InvalidNicknameEvent invalidNickname);
    public void update(RequirePlayersNumberEvent requirePlayersNumber);
    public void update(LobbyFullEvent lobbyFull);
    public void update(PlayerWinEvent playerWin);
    public void update(PlayerLoseEvent playerLose);
    public void update(TurnStatusChangedEvent turnStatusChange);
    public void update(ServerQuitEvent serverQuit);

    /* Game preparation listener */
    public void update(WorkerPlacedEvent workerPlaced);
    public void update(CardSelectedEvent cardSelected);
    public void update(CardsInformationEvent cardsInformation);

    /* Move executed listener */
    public void update(WorkerMovedEvent workerMoved);
    public void update(BlockBuiltEvent blockBuilt);
    public void update(WorkerRemovedEvent workerRemoved);
    public void update(BlockRemovedEvent blockRemoved);
    public void update(WorkerSelectedEvent workerSelected);

    /* Message listener */
    public void update(MessageEvent message);

    /* Error message listener */
    public void update(ErrorMessageEvent message);

    /* Generic update method */
    public void update(Object o);
}
