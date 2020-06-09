package it.polimi.ingsw.view.clientSide.viewCore.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.data.DataStorage;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.interfaces.Addressable;

public interface ClientAddressable extends Addressable {

    public static ClientAddressable searchByString(String s){
        try {
            return DataStorage.search(s);
        } catch (NotFoundException e) {
            return ViewStatus.searchByString(s);
        }
    }
}
