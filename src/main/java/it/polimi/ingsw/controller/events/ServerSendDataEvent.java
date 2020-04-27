package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Server has sent the data requested from Client.
 * [MVEvent]
 */
public class ServerSendDataEvent extends EventObject {
    // TODO per Giorgio: Inserire i todo per indicare gli attributi necessari

    public ServerSendDataEvent(Object o) {
        super(o);
    }
}
