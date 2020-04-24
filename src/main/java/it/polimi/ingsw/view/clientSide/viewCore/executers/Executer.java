package it.polimi.ingsw.view.clientSide.viewCore.executers;

/**
 * Interface to implement the events to be send to the Controller
 *
 * @author giorgio
 */
public interface Executer {

    //Funzione che esegue il comando da eseguire.
    //Potrebbe sia essere blocante che lanciare un thread parallelo.
    public void doIt();
}
