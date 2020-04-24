package it.polimi.ingsw.view.clientSide.viewCore.data;

import it.polimi.ingsw.view.exceptions.NotFoundException;

/**
 * Interface made to manage the data for the view
 *
 * @author giorgio
 */
interface DataStorager {

    //Funzione che prende un messaggio, lo analizza e:
    // se il messaggio è una request crea il ViewObject corrispondente;
    // se il messaggio è un event lo gira al ViewObject destinatario (che eseguirà i cambiamenti necessari);
    //Viene chiamato dal ViewManager.
    void getIn(String message);

    //Funzione che permette di trovare il ViewObject corrispondente all'id.
    //Se non lo trovo lo richiedo a ViewManager e ritorno una volta che l'ho creato.
    ViewObject find (String id);

    //Funzione che cerca il ViewObject caratterizzato dall'id.
    //Se non lo trova lancia la NotFoundException.
    ViewObject search (String id) throws NotFoundException;
}
