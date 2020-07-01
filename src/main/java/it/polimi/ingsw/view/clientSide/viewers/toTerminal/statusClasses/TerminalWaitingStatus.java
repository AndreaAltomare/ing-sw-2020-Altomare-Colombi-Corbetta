package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.TerminalViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;
import it.polimi.ingsw.view.exceptions.CheckQueueException;

/**
 * Class to represent the <code>TerminalStatusViewer</code> for the ViewStatus WAITING .
 */
public class TerminalWaitingStatus extends TerminalStatusViewer {
    StatusViewer myStatusViewer;

    /**
     * Constructor.
     *
     * @param statusViewer (the StatusViewer to which this refers).
     */
    public TerminalWaitingStatus(StatusViewer statusViewer){
        myStatusViewer = statusViewer;
    }

    /**
     * Method to print the status representation.
     */
    @Override
    public void print() throws CheckQueueException {
        System.out.println("[Terminal]: waiting");
        while (true) {
            super.myTerminalViewer.waitTimeOut(1000);
            System.out.println("...");
        }
    }
}
