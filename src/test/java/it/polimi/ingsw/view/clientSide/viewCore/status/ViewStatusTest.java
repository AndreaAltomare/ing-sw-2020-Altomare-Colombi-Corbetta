package it.polimi.ingsw.view.clientSide.viewCore.status;

import it.polimi.ingsw.view.serverSide.ClientStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewStatusTest {

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
            assertSame(ViewStatus.searchByString(clientTested.toString()), viewTested);
            assertSame(ViewStatus.searchByString(clientTested.getId()), viewTested);

            viewNewStatus();
            clientNewStatus();
        }
    }

    @Test
    void getExecuters() {
        assertTrue(true);
        //It should be tested for each status
    }

    @Test
    void getViewer() {
        assertTrue(true);
        //It should be tested for each status
    }

    @Test
    void onLoad() {
        assertTrue(true);
        //It should be tested for each status
    }

    @Test
    void setStatus() {
        assertTrue(true);
        //It should be tested for each status
    }

    @Test
    void nextStatus() {
        assertTrue(true);
        //It should be tested for each status
    }
}