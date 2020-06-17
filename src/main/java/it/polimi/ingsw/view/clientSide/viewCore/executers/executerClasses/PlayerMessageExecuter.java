package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.chat.ChatMessageEvent;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.UndoActionEvent;

import java.util.EventObject;

public class PlayerMessageExecuter extends Executer {

    private String payload;

    @Override
    public void clear() {
        payload = null;
    }

    public PlayerMessageExecuter (){
        this.clear();
    }

    public void setMessage(String msg){
        payload = msg;
    }

    public ChatMessageEvent getMyEvent(){
        return new ChatMessageEvent(ViewNickname.getMyNickname(), payload);
    }

    public void send(ChatMessageEvent event) throws NullPointerException{
        if(event == null) return;
        getSender().send((ChatMessageEvent)event);
    }

}
