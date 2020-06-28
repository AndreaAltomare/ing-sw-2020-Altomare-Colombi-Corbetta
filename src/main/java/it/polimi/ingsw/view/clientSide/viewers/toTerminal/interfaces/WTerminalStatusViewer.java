package it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.WTerminalViewer;

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
