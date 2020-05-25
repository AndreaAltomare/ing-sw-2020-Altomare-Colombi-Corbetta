package it.polimi.ingsw.view.clientSide.viewers.subTurnViewers;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.CLIOpponentPlaceWorkerPhase;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses.OpponentPlaceWorkerSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses.PlaceWorkerSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalSubTurnViewer;

public class OpponentPlaceWorkerViewer extends SubTurnViewer {
    public OpponentPlaceWorkerViewer(ViewSubTurn viewSubTurn) {
        super(viewSubTurn);
    }

    @Override
    public TerminalSubTurnViewer toTerminal() {
        return null;
    }

    @Override
    public GUISubTurnViewer toGUI()  {
        return new OpponentPlaceWorkerSubTurn(this);
    }

    @Override
    public CLISubTurnViewer toCLI()  {
        return new CLIOpponentPlaceWorkerPhase( this );
    }
}
