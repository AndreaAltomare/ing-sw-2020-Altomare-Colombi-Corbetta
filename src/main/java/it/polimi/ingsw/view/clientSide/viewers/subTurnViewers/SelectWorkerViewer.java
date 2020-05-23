package it.polimi.ingsw.view.clientSide.viewers.subTurnViewers;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalSubTurnViewer;

public class SelectWorkerViewer implements SubTurnViewer {

    protected ViewSubTurn mySubTurn;

    public SelectWorkerViewer (ViewSubTurn viewSubTurn){
        mySubTurn = viewSubTurn;
    }

    @Override
    public TerminalSubTurnViewer toTerminal() {
        return null;
    }

    @Override
    public GUISubTurnViewer toGUI() {
        return null;
    }

    @Override
    public CLISubTurnViewer toCLI() {
        return null;
    }
}
