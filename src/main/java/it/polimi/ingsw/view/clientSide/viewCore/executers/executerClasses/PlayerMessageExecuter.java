package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.chat.ChatMessageEvent;
import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.messages.PlayerMessages;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.events.UndoActionEvent;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import java.util.EventObject;

/**
 * Class to execute the Player Message chat.
 *
 * @see Executer
 * @author giorgio
 */
public class PlayerMessageExecuter extends Executer {

    private String payload;

    /**
     * Method to reset this
     */
    @Override
    public void clear() {
        payload = null;
    }

    /**
     * Constructor.
     */
    public PlayerMessageExecuter (){
        this.clear();
    }

    /**
     * Method to set the message .
     *
     * @param msg (the message).
     */
    public void setMessage(String msg){
        payload = msg;
    }

    /**
     * Method that returns the event of this Executer, null if it canot send the message.
     *
     * @return (The event associated to this Executer)
     */
    @Override
    public ChatMessageEvent getMyEvent(){
        if(View.debugging)
            System.out.println(payload);
        if(ViewStatus.getActual() != ViewStatus.PLAYING)
            return null;
        PlayerMessages.addMsg(payload);
        return new ChatMessageEvent(ViewNickname.getMyNickname(), payload);
    }

    /*public void send(ChatMessageEvent event) throws NullPointerException{
        if(event == null) return;
        getSender().send((ChatMessageEvent)event);
    }*/

}
