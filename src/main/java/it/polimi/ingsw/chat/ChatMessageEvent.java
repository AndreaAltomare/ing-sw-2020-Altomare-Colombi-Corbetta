package it.polimi.ingsw.chat;

import java.util.EventObject;

/**
 * Event: Chat message.
 */
public class ChatMessageEvent extends EventObject {
    private final String sender; // Player who sent the message
    private final String message; // chat message

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
