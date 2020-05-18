package it.polimi.ingsw.view.clientSide.viewers.messages;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private static List<ViewMessage> logMessage = new ArrayList<ViewMessage>();

    public ViewMessage(String payload, MessageType messageType){
        this.payload = payload;
        this.messageType = messageType;
        logMessage.add(this);
    }

    public String getPayload(){ return payload; }

    public MessageType getMessageType(){ return messageType; }

    public void sendMessage(){ Viewer.sendAllMessage(this); }

    public static void populateAndSend(String payload, MessageType messageType){ new ViewMessage(payload, messageType).sendMessage(); }

    public List<ViewMessage> getLogMessage(List<MessageType> getTypes){
        List<ViewMessage> ret = new ArrayList<ViewMessage>();

        for (ViewMessage m: logMessage) {
            if(getTypes.contains(m.getMessageType())){
                ret.add(m);
            }
        }
        return ret;
    }

    public List<ViewMessage> getLogMessage(){
        return getLogMessage(Arrays.asList(MessageType.values()));
    }
}
