package it.polimi.ingsw.view.clientSide.viewers.messages;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;

/**
 * Class used to send messages from the viewCore to the user (passing by viewers)
 *
 * @author giorgio
 */
public class ViewMessage {

    public enum MessageType{
        FATAL_ERROR_MESSAGE,
        CHANGE_STATUS_MESSAGE,
        WIN_MESSAGE,
        LOOSE_MESSAGE,
        FROM_SERVER_MESSAGE,
        FROM_SERVER_ERROR,
        EXECUTER_ERROR_MESSAGE;
    }

    private String payload;
    private MessageType messageType;

    public ViewMessage(String payload, MessageType messageType){
        this.payload = payload;
        this.messageType = messageType;
    }

    public String getPayload(){ return payload; }

    public MessageType getMessageType(){ return messageType; }

    public void sendMessage(){ Viewer.sendAllMessage(this); }

    public static void populateAndSend(String payload, MessageType messageType){ new ViewMessage(payload, messageType).sendMessage(); }
}
