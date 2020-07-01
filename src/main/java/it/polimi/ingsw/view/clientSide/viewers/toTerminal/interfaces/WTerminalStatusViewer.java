package it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificStatusViewer;

public abstract class WTerminalStatusViewer implements SpecificStatusViewer {

    private WTerminalSubTurnViewer myWTerminalSubTurnViewer = null;

    public void setMyWTerminalSubTurnViewer(WTerminalSubTurnViewer myWTerminalSubTurnViewer) {
        this.myWTerminalSubTurnViewer = myWTerminalSubTurnViewer;
    }

    public WTerminalSubTurnViewer getMyWTerminalSubTurnViewer() {
        return myWTerminalSubTurnViewer;
    }

    public abstract void show();

}
