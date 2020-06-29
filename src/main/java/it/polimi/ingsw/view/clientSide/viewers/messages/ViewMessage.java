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

    /**
     * constructor.
     *
     * @param payload (the payload of the message).
     * @param messageType (the type of the message).
     */
    public ViewMessage(String payload, MessageType messageType){
        this.payload = payload;
        this.messageType = messageType;
        logMessage.add(this);
    }

    /**
     * Method to retrieve the payload of this.
     *
     * @return (the payload of this).
     */
    public String getPayload(){ return payload; }

    /**
     * Method to retrieve the type of this.
     *
     * @return (the type of this).
     */
    public MessageType getMessageType(){ return messageType; }

    /**
     * Method to notify all the Viewers this message.
     */
    public void sendMessage(){ Viewer.sendAllMessage(this); }

    /**
     * Method to build a new message and notify it to all the Viewers.
     *
     * @param payload (the payload of the message).
     * @param messageType (the type of the message).
     */
    public static void populateAndSend(String payload, MessageType messageType){ new ViewMessage(payload, messageType).sendMessage(); }

    /**
     * Method to retrieve the list of all the messages of given type generated during this execution.
     *
     * @param getTypes (the type searched).
     * @return (the list of all the messages of given type generated during this execution).
     */
    public List<ViewMessage> getLogMessage(List<MessageType> getTypes){
        List<ViewMessage> ret = new ArrayList<ViewMessage>();

        for (ViewMessage m: logMessage) {
            if(getTypes.contains(m.getMessageType())){
                ret.add(m);
            }
        }
        return ret;
    }

    /**
     * Method to retrieve the list of all the messages generated during this execution.
     *
     * @return (the list of all the messages generated during this execution).
     */
    public List<ViewMessage> getLogMessage(){
        return getLogMessage(Arrays.asList(MessageType.values()));
    }
}
