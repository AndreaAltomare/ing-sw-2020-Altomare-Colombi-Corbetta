package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.ReadyViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;

import java.util.List;

public class TerminalReadyStatus implements TerminalStatusViewer {

    ReadyViewer myStatusViewer;

    public TerminalReadyStatus(ReadyViewer readyViewer){
        myStatusViewer = readyViewer;
    }

    @Override
    public void print() { System.out.println("[Terminal]: ready"); }
}
