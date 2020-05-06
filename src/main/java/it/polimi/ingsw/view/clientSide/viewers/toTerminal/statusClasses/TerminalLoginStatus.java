package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;

public class TerminalLoginStatus implements TerminalStatusViewer {
    @Override
    public void print() {
        System.out.println("[Terminal]:\tlogin");
    }
}
