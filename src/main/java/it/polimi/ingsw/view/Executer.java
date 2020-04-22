package it.polimi.ingsw.view;

/**
 * Interface to implement the events to be send to the Controller
 *
 * @author giorgio
 */
interface Executer {

    //Funzione che esegue il comando da eseguire.
    //Potrebbe sia essere blocante che lanciare un thread parallelo.
    void doIt();
}
