package it.polimi.ingsw.view.serverSide;

import it.polimi.ingsw.connection.server.ClientConnection;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.observer.VCEventSubject;

/**
 * Virtual View represents the generic View scenario for the MVC pattern.
 * It works with Controller (and Model) and provides a Server-side View.
 *
 * It relies on the Connection Layer to handle network communication.
 *
 * An Observer Pattern is applied to let VCEvents Listeners to be properly
 * notified when a VCEvent occurs.
 * So this class is an Observable for VCEvents.
 *
 * @see <a href="https://github.com/emanueledelsozzo/ingsoft-prova-finale-2020/blob/master/ese_Socket_Serialization/RockPaperScissorsLizardSpockDistributedMVC/src/main/java/it/polimi/ingsw/view/RemoteView.java">github.com/emanueledelsozzo/.../RemoteView.java</a>
 * @author AndreaAltomare
 */
public class VirtualView extends VCEventSubject implements Observer<Object> {
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
    }


    /**
     * This class is the general Observer for Object messages received from
     * the Connection Layer.
     *
     * @author AndreaAltomare
     */
    private class MessageReceiver implements Observer<Object> {

        /**
         * Notify VCEvents Listeners when an (Event) Object is received form
         * the Connection Layer.
         *
         * @param event (Event Object)
         */
        @Override
        public void update(Object event) {
            System.out.println("I'm " + playerNickname + "'s VirtualView and I received an event.");
            VirtualView.this.notifyVCEventsListeners(event, playerNickname); // notify Controller with Player's nickname information - IMPORTANT!!
        }
    }

    public String getPlayerNickname() {
        return playerNickname;
    }


    /**
     * Wrapper method to update MVEvents Listeners (by sending
     * the Event Object to the network) when a MVEvent occurs.
     *
     * @param o (Event Object)
     */
    @Override
    public void update(Object o) {
        // todo write instruction to send via connection handler the MVEvent object
        connection.asyncSend(o); // sent the [MVEvent] Object to the network via Socket
        //c.asyncSend("Write something... [ASYNCHRONOUS send method]");
        //c.send("Write something... [SYNCHRONOUS send method]");
    }
}
