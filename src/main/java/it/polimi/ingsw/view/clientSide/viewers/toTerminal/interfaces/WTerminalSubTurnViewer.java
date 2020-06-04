package it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;

public abstract class WTerminalSubTurnViewer implements SpecificSubTurnViewer {

    public abstract void show();

    public abstract ViewSubTurn getSubTurn();

    public void setMyWTerminalStatusViewer(WTerminalStatusViewer myWTerminalStatusViewer) {
        System.out.printf("\n\t[ERROR: you can set WTerminalStatusViewer %s to WTerminalSubTurnViewer %s!]\n",
                                myWTerminalStatusViewer.getViewStatus().toString(),
                                this.getSubTurn().toString());
    }
}
