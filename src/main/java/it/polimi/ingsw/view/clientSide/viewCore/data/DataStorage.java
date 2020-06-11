package it.polimi.ingsw.view.clientSide.viewCore.data;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongViewObjectException;

/**
 * Interface made to manage the data for the view
 *
 * @author giorgio
 */
public interface DataStorage {

    //Funzione che prende un messaggio, lo analizza e:
    // se il messaggio è una request crea il ViewObject corrispondente;
    // se il messaggio è un event lo gira al ViewObject destinatario (che eseguirà i cambiamenti necessari);
    //Viene chiamato dal ViewManager.
    void getIn(String message);

    //Funzione che permette di trovare il ViewObject corrispondente all'id.
    //Se non lo trovo lo richiedo a ViewManager e ritorno una volta che l'ho creato.
    ViewObject find (String id);

    /**
     * Method that searches for the ViewObject with id equals to id.
     *
     * @param id (the tring identifier searched)
     * @return (the ViewObject with toString() equals to id)
     * @throws NotFoundException (iif the searched object is not found)
     */
    static ViewObject search(String id) throws NotFoundException{
        try {
            return ViewBoard.search(id);
        } catch (Exception ignored) { }
        try {
            return ViewCard.search(id);
        } catch (Exception ignored) { }
        try {
            return ViewCell.search(id);
        } catch (Exception ignored) { }
        try {
            return ViewPlayer.search(id);
        } catch (Exception ignored) { }
        try {
            return ViewWorker.search(id);
        } catch (Exception ignored) { }
        try {
            return ViewObject.search(id);
        } catch (Exception ignored) { }
        try {
            return ViewNickname.search(id);
        } catch (Exception ignored) { }
        throw new NotFoundException();
    }
}
