package it.polimi.ingsw.view;

import sun.jvm.hotspot.types.WrongTypeException;

/**
 * Interface for all the data rapresentations available to the View side
 *
 * @author giorgio
 */
interface ViewObject {

    //Funzione che ritorna la stringa identificativa dell'oggetto
    String getId();

    //Funzione che ritorna la prossima stringa identificativa valida per un oggetto dello stesso tipo
    String getNextId();

    //Funzione che ritorna la base dell'id per questa classe
    String getBaseId();

    //Funzione che cerca l'oggetto con id getBaseId+i
    //Lancia NotFoundException se non lo trova;
    ViewObject find(int i) throws NotFoundException;

    //Funzione che cerca l'oggetto con id s;
    //Lancia NotFoundException se non lo trova;
    //Lancia WrongTypeException se quello cercato è di tipo diverso
    ViewObject find(String s) throws NotFoundException, WrongTypeException;

    //Funzione che notifica all'oggetto un evento
    boolean notifyEvent(String message);

    //Funzione che popola l'oggetto
    boolean populate(String message);

    //Funzione che restituisce la rappresentazione dell'oggetto come stringa
    String toString();

    //Funzione che ritorna la rappresentazione dell'oggetto per la visualizzazione a Terminale
    String toTerminal();

    //Funzione che ritorna la rappresentazione dell'oggetto per la visualizzazione a CLI
    Object toCLI();

    //Funzione che ritorna la rappresentazione dell'oggetto per la visualizzazione a GUI
    Object toGUI();

}
