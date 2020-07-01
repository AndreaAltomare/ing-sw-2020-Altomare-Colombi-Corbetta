package it.polimi.ingsw.view.clientSide.viewers.messages;

import it.polimi.ingsw.chat.ChatMessageEvent;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.ChatPanel;

/**
 * Class to manage the Chat system.
 *
 * @author giorgio
 */
public class PlayerMessages {

    private static ChatPanel chatPanel;

    /**
     * Method that will add the ChatPanel to which notify
     * the arrival of messages.
     *
     * @param chatPanel (the ChatPanel to which notify the arrival of messages).
     */
    public static void setChatPanel(ChatPanel chatPanel){
        PlayerMessages.chatPanel = chatPanel;
    }

    /**
     * Method called on the sending of a new message (from the player) to show it.
     *
     * @param msg (the body of the message).
     */
    public static void addMsg(String msg){
        if(PlayerMessages.chatPanel == null)
            return;
        PlayerMessages.chatPanel.addMessage(ViewNickname.getMyNickname() + " : " + msg);
    }

    /**
     * Method called on the arrival of a message to show it.
     *
     * @param msg (the ChatMessageEvent recived).
     */
    public static void addMsg(ChatMessageEvent msg){
        if(PlayerMessages.chatPanel == null)
            return;
        PlayerMessages.chatPanel.addMessage(msg.getSender() + " : " + msg.getMessage());
    }

}
