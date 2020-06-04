package it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.WTerminalViewer;

public abstract class WTerminalStatusViewer implements SpecificStatusViewer {

    private WTerminalViewer myWTerminalViewer;
    private WTerminalSubTurnViewer myWTerminalSubTurnViewer = null;

    public void setMyWTerminalViewer(WTerminalViewer myWTerminalViewer) {
        this.myWTerminalViewer = myWTerminalViewer;
    }

    public WTerminalViewer getMyWTerminalViewer() {
        return myWTerminalViewer;
    }

    public void setMyWTerminalSubTurnViewer(WTerminalSubTurnViewer myWTerminalSubTurnViewer) {
        this.myWTerminalSubTurnViewer = myWTerminalSubTurnViewer;
        myWTerminalSubTurnViewer.setMyWTerminalStatusViewer(this);
    }

    public WTerminalSubTurnViewer getMyWTerminalSubTurnViewer() {
        return myWTerminalSubTurnViewer;
    }

    public abstract ViewStatus getViewStatus();

    public abstract void show();

}
