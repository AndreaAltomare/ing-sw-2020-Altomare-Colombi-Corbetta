package it.polimi.ingsw.view.clientSide.viewCore.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.data.DataStorage;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.interfaces.Addressable;

/**
 * Interface implemented by all the Object addressable by a unique String.
 */
public interface ClientAddressable extends Addressable {

    /**
     * Method to retrieve the <code>ClientAddressable</code> identified by the given <code>Stging</code>.
     *
     * @param s (the <code>String</code> identifying the <code>ClientAddressable</code> searched).
     * @return  (the -unique- <code>ClientAddressable</code> identified by the given <code>String</code>. Returns null if not found).
     */
    static ClientAddressable searchByString(String s){
        try {
            return DataStorage.search(s);
        } catch (NotFoundException e) {
            return ViewStatus.searchByString(s);
        }
    }
}
