package it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificSubTurnViewer;

public interface CLISubTurnViewer extends SpecificSubTurnViewer {

    public abstract void show();

    public abstract ViewSubTurn getSubTurn();
}
