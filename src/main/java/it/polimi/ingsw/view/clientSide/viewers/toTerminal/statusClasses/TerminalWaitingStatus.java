package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;

public class TerminalWaitingStatus implements TerminalStatusViewer {
    StatusViewer myStatusViewer;

    public TerminalWaitingStatus(StatusViewer statusViewer){
        myStatusViewer = statusViewer;
    }

    @Override
    public void print() { System.out.println("[Terminal]: waiting"); }
}
