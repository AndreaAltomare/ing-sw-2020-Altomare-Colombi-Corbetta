package it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.TerminalViewer;
import it.polimi.ingsw.view.exceptions.CheckQueueException;

/**
 * Abstract class implemented by classes representing thee various <code>ViewStatus</code> for the Terminal.
 */
public abstract class TerminalStatusViewer implements SpecificStatusViewer {

    protected TerminalViewer myTerminalViewer;

    /**
     * Method that set the <code>TerminalViewer</code> to which this refers.
     *
     * @param terminalViewer (the <code>TerminalViewer</code> to which this refers).
     */
    public void setMyTerminalViewer(TerminalViewer terminalViewer){ myTerminalViewer = terminalViewer; }


    /**
     * Method to print the status representation.
     *
     * @throws CheckQueueException (if there is another Event to be served on the BlockingQueue).
     */
    public abstract void print() throws CheckQueueException;
}
