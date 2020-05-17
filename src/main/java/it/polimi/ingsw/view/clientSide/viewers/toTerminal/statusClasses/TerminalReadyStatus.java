package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.ReadyViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;

import java.util.List;

public class TerminalReadyStatus extends TerminalStatusViewer {

    StatusViewer myStatusViewer;

    public TerminalReadyStatus(StatusViewer readyViewer){
        myStatusViewer = readyViewer;
    }

    @Override
    public void print() { System.out.println("[Terminal]: ready"); }
}
