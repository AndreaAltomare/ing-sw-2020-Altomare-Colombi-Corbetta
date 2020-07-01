package it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificStatusViewer;

public abstract class CLIStatusViewer implements SpecificStatusViewer {

    private CLISubTurnViewer myCLISubTurnViewer = null;

    public void setMyCLISubTurnViewer(CLISubTurnViewer myCLISubTurnViewer) {
        this.myCLISubTurnViewer = myCLISubTurnViewer;
    }

    public CLISubTurnViewer getMyCLISubTurnViewer() {
        return myCLISubTurnViewer;
    }

    public abstract void show();
}
