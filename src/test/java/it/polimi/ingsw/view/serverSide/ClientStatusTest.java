package it.polimi.ingsw.view.serverSide;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientStatusTest {

    ViewStatus viewTested = ViewStatus.READY;
    ClientStatus clientTested = ClientStatus.READY;

    ViewStatus viewNewStatus(){
        return viewTested = viewTested.getNext();
    }

    ClientStatus clientNewStatus(){
        return clientTested = clientTested.getNext();
    }

    @Test
    void integration() {
        while(clientTested != null){
            //Integration test
            assertEquals(clientTested.toString(), viewTested.toString());
            assertEquals(clientTested.getId(), viewTested.getId());

            //searchByString test
            assertSame(ClientStatus.searchByString(viewTested.toString()), clientTested);
            assertSame(ClientStatus.searchByString(viewTested.getId()), clientTested);

            viewNewStatus();
            clientNewStatus();
        }
    }

    @Test
    void getClassId() {
        assertTrue(ClientStatus.getClassId().equals(ViewStatus.getClassId()));
    }

}