package it.polimi.ingsw.view.serverSide.interfaces;

import it.polimi.ingsw.controller.events.*;
import it.polimi.ingsw.observer.GeneralListener;
import it.polimi.ingsw.view.serverSide.ClientStatus;

/**
 * Interface for general information exchange (from Controller [Server]).
 *
 * MVEvent (Events form Server)
 *
 * @author Giorgio Corbetta
 */
public interface ServerGeneralListener extends GeneralListener {
    public void onStatusChange(ClientStatus clientStatus);
    public void onNextStatus(NextStatusEvent nextStatus);
    public void serverSendData(ServerSendDataEvent serverSentData); // Game match general data (to respond to viewRequestData, receive data from Server)
    public void onInvalidNickname(InvalidNicknameEvent invalidNickname);
    public void onLobbyFull(LobbyFullEvent lobbyFull);
    public void onPlayerWin(PlayerWinEvent playerWin);
    public void onPlayerLose(PlayerLoseEvent playerLose);
    public void onTurnStatusChange(TurnStatusChangedEvent turnStatusChange);
}
