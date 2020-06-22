package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.controller.events.RequireStartPlayerEvent;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.SetStartPlayerEvent;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import java.util.ArrayList;
import java.util.EventObject;

public class FirstPlayerExecuter extends Executer {

    private ArrayList<String> playerList;
    private String myChoice;

    @Override
    public void clear() {
        playerList = null;
        myChoice = null;
    }

    public void populate(RequireStartPlayerEvent event){
        clear();
        playerList = new ArrayList<String>(event.getPlayers());
    }

    public ArrayList<String> getPlayerList(){
        return new ArrayList<>(playerList);
    }

    public void set(String choosed){
        myChoice = choosed;
    }

    public void setRandom(){
        try {
            myChoice = playerList.get(0);
        }catch (Exception e){
            myChoice = null;
        }
    }

    public EventObject getMyEvent()throws CannotSendEventException {
        if(myChoice == null) throw new CannotSendEventException("You have to choose a player!");
        return new SetStartPlayerEvent(myChoice);
    }
}
