package it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;

public abstract class CLIStatusViewer implements SpecificStatusViewer {

    private CLIViewer myCLIViewer;

    public void setMyCLIViewer( CLIViewer myCLIViewer ) {
        this.myCLIViewer = myCLIViewer;
    }

    public CLIViewer getMyCLIViewer() {
        return myCLIViewer;
    }

    public abstract void show();

}
