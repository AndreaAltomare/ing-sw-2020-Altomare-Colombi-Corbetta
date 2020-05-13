package it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.TerminalViewer;
import it.polimi.ingsw.view.exceptions.CheckQueueException;

public abstract class TerminalStatusViewer implements SpecificStatusViewer {

    protected TerminalViewer myTerminalViewer;

    public void setMyTerminalViewer(TerminalViewer terminalViewer){ myTerminalViewer = terminalViewer; }

    public abstract void print() throws CheckQueueException;
}
