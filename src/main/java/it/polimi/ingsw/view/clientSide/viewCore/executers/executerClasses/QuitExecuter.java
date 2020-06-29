package it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.events.QuitEvent;


import java.util.EventObject;

/**
 * Class to execute the Quitting.
 *
 * @see Executer
 * @author giorgio
 */
public class QuitExecuter extends Executer {

    /**
     * Method that reset the executer with initial values.
     */
    @Override
    public void clear(){}

    /**
     * constructor
     */
    public QuitExecuter(){
        this.clear();
    }

    /**
     * Static method that returns aunivoque string for each type of Exeuter (in this specific case "[Executer]").
     *
     * @return (Stirng univocally identifying the Executer type).
     */
    public static String myType(){ return Executer.myType() + "\tQuit"; }

    /**
     * Method that returns the event of this Executer
     *
     * @return (The event associated to this Executer)
     */
    @Override
    public EventObject getMyEvent() {
        return new QuitEvent();
    }
}
