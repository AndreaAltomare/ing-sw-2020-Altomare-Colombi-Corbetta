package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.GameResumingResponseEvent;
import it.polimi.ingsw.view.events.QuitEvent;
import it.polimi.ingsw.view.events.UndoActionEvent;
import it.polimi.ingsw.view.exceptions.AlreadySetException;

import java.util.EventObject;

public class ResumingExecuter extends Executer {
    private boolean resume;
    private boolean done = false;

    public void setResume(boolean _resume) throws AlreadySetException {
        if(done)
            throw new AlreadySetException();
        resume = _resume;
        done = true;
    }

    /**
     * Method that reset the executer with initial values.
     */
    public void clear(){
        resume = false;
    }

    /**
     * constructor
     */
    public ResumingExecuter(){

        this.clear();
    }

    /**
     * Static method that returns aunivoque string for each type of Exeuter (in this specific case "[Executer]").
     *
     * @return (Stirng univocally identifying the Executer type).
     */
    public static String myType(){ return Executer.myType() + "\tResume"; }

    @Override
    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     */
    public EventObject getMyEvent() {
        return new GameResumingResponseEvent(resume);
    }

    public void send(EventObject event) throws NullPointerException{
        if(event == null) return;
        if(!done) throw new NullPointerException();
        getSender().send((GameResumingResponseEvent)event);
    }
}
