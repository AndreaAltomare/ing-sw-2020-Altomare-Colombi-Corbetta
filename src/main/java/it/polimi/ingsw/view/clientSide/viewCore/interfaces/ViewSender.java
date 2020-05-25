package it.polimi.ingsw.view.clientSide.viewCore.interfaces;

import it.polimi.ingsw.view.events.*;

import java.util.EventObject;

public interface ViewSender {
    public void send(SetNicknameEvent event);
    public void send(SetPlayersNumberEvent event);
    public void send(CardsChoosingEvent event);
    public void send(CardSelectionEvent event);


    public void send(EventObject event);
    public void send(PlaceWorkerEvent event);
    public void send (TurnStatusChangeEvent event);
    public void send(MoveWorkerEvent event);
    public void send(SelectWorkerEvent event);
    public void send(BuildBlockEvent event);


}
