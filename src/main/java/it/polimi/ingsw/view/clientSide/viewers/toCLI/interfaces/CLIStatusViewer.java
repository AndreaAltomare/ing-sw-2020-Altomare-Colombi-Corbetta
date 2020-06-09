package it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;

public abstract class CLIStatusViewer implements SpecificStatusViewer {
    private CLIViewer myCLIViewer;
    private CLISubTurnViewer myCLISubTurnViewer = null;

    public void setMyCLIViewer( CLIViewer myCLIViewer) { this.myCLIViewer = myCLIViewer;}

    public CLIViewer getMyCLIViewer() {
        return this.myCLIViewer;
    }

    public void setMyCLISubTurnViewer(CLISubTurnViewer myCLISubTurnViewer) {
        this.myCLISubTurnViewer = myCLISubTurnViewer;
        myCLISubTurnViewer.setMyCLIStatusViewer(this);
    }

    public CLISubTurnViewer getMyCLISubTurnViewer() {
        return myCLISubTurnViewer;
    }

    public abstract ViewStatus getViewStatus();

    public abstract void show();
}
