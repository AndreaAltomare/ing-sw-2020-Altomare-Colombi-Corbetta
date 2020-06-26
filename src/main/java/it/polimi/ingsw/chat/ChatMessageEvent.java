package it.polimi.ingsw.chat;

import java.util.EventObject;

/**
 * Event: Chat message.
 *
 * @author AndreaAltomare
 */
public class ChatMessageEvent extends EventObject {
    private final String sender; // Player who sent the message
    private final String message; // chat message

    /**
     * Constructor.
     * Takes both the sender and the message.
     *
     * @param sender Nickname of the Player who sent the message
     * @param message Message sent
     */
    public ChatMessageEvent(String sender, String message) {
        super(new Object());
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "[" + sender + "]:  " + message;
    }
}
