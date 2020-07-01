package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.SetNicknameEvent;
import it.polimi.ingsw.view.exceptions.AlreadySetException;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.EventObject;

/**
 * Class to execute the Set Nickname.
 *
 * @see Executer
 * @author giorgio
 */
public class SetNicknameExecuter extends Executer {
    private String nickname;

    /**
     * Method that reset the executer with initial values.
     */
    @Override
    public void clear(){
        nickname = null;
    }

    /**
     * constructor
     */
    public SetNicknameExecuter(){
        this.clear();
    }

    /**
     * Method that sets the nickname of the player.
     *
     * @param nickname (Sting identifying the worker).
     * @throws WrongParametersException (if nickname is not correct).
     */
    public void setNickname(String nickname)throws WrongParametersException {
        //todo check correcntess of nickname
        if (nickname==null || nickname.equals(""))throw new WrongParametersException("invalid nickname");
        this.nickname = nickname;
    }

    /**
     * Static method that returns aunivoque string for each type of Exeuter (in this specific case "[Executer]").
     *
     * @return (Stirng univocally identifying the Executer type).
     */
    public static String myType(){ return Executer.myType() + "\tSetNickname"; }

    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     * @throws CannotSendEventException (if the Executer doesn't have all the information needed  by the Event)
     */
    @Override
    public EventObject getMyEvent()throws CannotSendEventException {
        if(nickname == null) throw new CannotSendEventException("No valid nickname set");
        SetNicknameEvent setNicknameEvent = new SetNicknameEvent(nickname);
        nickname = null;
        try {
            ViewNickname.setNickname(setNicknameEvent.getNickname());
            //ViewNickname.populate(setNicknameEvent);
        } catch (WrongParametersException e) {
            throw new CannotSendEventException("Cannot set local nickname");
        } catch (AlreadySetException e) {
            throw new CannotSendEventException("Exists an already set nickname");
        }
        return setNicknameEvent;
    }

    /*public void send(EventObject event) throws NullPointerException{
        if(event == null) throw new NullPointerException();
        getSender().send((SetNicknameEvent)event);
    }*/
}
