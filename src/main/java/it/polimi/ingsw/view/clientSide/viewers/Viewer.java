package it.polimi.ingsw.view.clientSide.viewers;

/**
 * Interface for the various visualizer
 *
 * @author giorgio
 */
interface Viewer {

    //Funzione che lancia l'esecuzione.
    void start();

    //Fuzione che forza un refresh della view
    void refresh();

    //Funzione che segnala al Viewer di controllare lo stato ASAP
    void checkStatus();

    //Funzione che segnala al Viewer di controllare i visualizable ASAP
    void checkVisualizables();
}
