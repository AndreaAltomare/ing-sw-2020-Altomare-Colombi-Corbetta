package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;

/**
 * Class to represent the <code>TerminalStatusViewer</code> for the ViewStatus READY .
 */
public class TerminalReadyStatus extends TerminalStatusViewer {

    StatusViewer myStatusViewer;

    /**
     * Constructor.
     *
     * @param readyViewer (the StatusViewer to which this refers).
     */
    public TerminalReadyStatus(StatusViewer readyViewer){
        myStatusViewer = readyViewer;
    }

    /**
     * Method to print the status representation.
     */
    @Override
    public void print() { System.out.println("[Terminal]: ready"); }
}
