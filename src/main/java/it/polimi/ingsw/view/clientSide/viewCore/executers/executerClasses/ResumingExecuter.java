package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.GameResumingResponseEvent;
import it.polimi.ingsw.view.events.QuitEvent;
import it.polimi.ingsw.view.events.UndoActionEvent;
import it.polimi.ingsw.view.exceptions.AlreadySetException;

import java.util.EventObject;

/**
 * Class to execute the Resuming.
 *
 * @see Executer
 * @author giorgio
 */
public class ResumingExecuter extends Executer {
    private boolean resume;
    private boolean done = false;

    /**
     * Method to set the resuming:
     * call it with true to resume the previous status;
     * call it with false to start a new game.
     *
     * @param _resume (true iif want to resume the game)
     * @throws AlreadySetException (if there has already been a previous set).
     */
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

    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     */
    @Override
    public EventObject getMyEvent() {
        return new GameResumingResponseEvent(resume);
    }

    /**
     * Method that sends the event to the Server.
     *
     * @param event (EventObject to be submitted to the server)
     */
    public void send(EventObject event) throws NullPointerException{
        if(event == null) return;
        if(!done) throw new NullPointerException();
        getSender().send((GameResumingResponseEvent)event);
    }
}
