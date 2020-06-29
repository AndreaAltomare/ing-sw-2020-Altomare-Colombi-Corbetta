package it.polimi.ingsw.view.clientSide.viewCore.interfaces;

/**
 * Interface implemented by the Class(es) to be notified with the events to be send to Server.
 */
public interface ViewSender {
    /*public void send(SetNicknameEvent event);
    public void send(SetPlayersNumberEvent event);
    public void send(CardsChoosingEvent event);
    public void send(CardSelectionEvent event);


    public void send(EventObject event);
    public void send(PlaceWorkerEvent event);
    public void send (TurnStatusChangeEvent event);
    public void send(MoveWorkerEvent event);
    public void send(SelectWorkerEvent event);
    public void send(BuildBlockEvent event);*/

    /**
     * Method called to send an Object (event) to the Server.
     *
     * @param o (the Object (event) to be send)
     */
    public void send(Object o);

}
