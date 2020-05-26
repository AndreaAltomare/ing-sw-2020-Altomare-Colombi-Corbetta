package it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificSubTurnViewer;

public abstract class CLISubTurnViewer implements SpecificSubTurnViewer {

    public abstract void show();

    public abstract ViewSubTurn getSubTurn();

    public void setMyCLIStatusViewer( CLIStatusViewer myCLIStatusViewer) {}
}
