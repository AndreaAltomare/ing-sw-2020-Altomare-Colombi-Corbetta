package it.polimi.ingsw.view.clientSide.viewers.subTurnViewers;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses.NoActionSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses.OpponentPlaceWorkerSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses.OpponentSelectWorkerSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalSubTurnViewer;

public class OpponentSelectWorkerViewer extends SubTurnViewer {
    public OpponentSelectWorkerViewer(ViewSubTurn viewSubTurn) {
        super(viewSubTurn);
    }

    @Override
    public TerminalSubTurnViewer toTerminal() {
        return null;
    }

    @Override
    public GUISubTurnViewer toGUI()   {
        return new OpponentSelectWorkerSubTurn(this);
    }

    @Override
    public CLISubTurnViewer toCLI()  {
        return null;
    }
}
