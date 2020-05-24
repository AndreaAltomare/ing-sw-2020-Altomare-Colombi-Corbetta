package it.polimi.ingsw.view.clientSide.viewCore.interfaces;

import it.polimi.ingsw.view.events.PlaceWorkerEvent;

import java.util.EventObject;

public interface ViewSender {
    public void send(EventObject event);
    public void send(PlaceWorkerEvent event);
}
