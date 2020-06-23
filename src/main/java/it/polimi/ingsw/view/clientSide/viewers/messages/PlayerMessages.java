package it.polimi.ingsw.view.clientSide.viewers.messages;

import it.polimi.ingsw.chat.ChatMessageEvent;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.ChatPanel;

import java.util.ArrayList;

public class PlayerMessages {

    private static ChatPanel chatPanel;

    public static void setChatPanel(ChatPanel chatPanel){
        PlayerMessages.chatPanel = chatPanel;
    }

    public static void addMsg(String msg){
        if(PlayerMessages.chatPanel == null)
            return;
        PlayerMessages.chatPanel.addMessage(ViewNickname.getMyNickname() + " : " + msg);
    }

    public static void addMsg(ChatMessageEvent msg){
        if(PlayerMessages.chatPanel == null)
            return;
        PlayerMessages.chatPanel.addMessage(msg.getSender() + " : " + msg.getMessage());
    }

}
