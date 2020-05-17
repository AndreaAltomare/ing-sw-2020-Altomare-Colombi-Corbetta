package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.TerminalViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;
import it.polimi.ingsw.view.exceptions.CheckQueueException;

public class TerminalWaitingStatus extends TerminalStatusViewer {
    StatusViewer myStatusViewer;

    public TerminalWaitingStatus(StatusViewer statusViewer){
        myStatusViewer = statusViewer;
    }

    @Override
    public void print() throws CheckQueueException {
        System.out.println("[Terminal]: waiting");
        while (true) {
            super.myTerminalViewer.waitTimeOut(1000);
            System.out.println("...");
        }
    }
}
