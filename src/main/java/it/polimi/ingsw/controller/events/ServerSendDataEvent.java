package it.polimi.ingsw.controller.events;

import java.util.EventObject;

// TODO: ricordarsi che è una Bean class
/**
 * Event: Server has sent the data requested from Client.
 * [MVEvent]
 */
public class ServerSendDataEvent extends EventObject {
    // TODO: 30/04/20 int boardXSize, int boardYSize
    // TODO: 30/04/20 String player1, String player2, String player3
    // TODO: 30/04/20 String worker1Player1, String worker2Player1, String worker1Player2, String worker2Player2, String worker1Player3, String worker2Player3
    // TODO: 30/04/20 List<String> cardName
    // TODO: 30/04/20 List<String> cardEpiteth
    // TODO: 30/04/20 List<String> cardDescript

    public ServerSendDataEvent(Object o) {
        super(o);
    }
}
