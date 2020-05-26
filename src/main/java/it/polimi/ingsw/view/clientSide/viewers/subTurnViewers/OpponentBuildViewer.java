package it.polimi.ingsw.view.clientSide.viewers.subTurnViewers;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.CLIOpponentBuildPhase;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses.NoActionSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses.OpponentBuildSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalSubTurnViewer;

public class OpponentBuildViewer extends SubTurnViewer {
    public OpponentBuildViewer(ViewSubTurn viewSubTurn) {
        super(viewSubTurn);
    }

    @Override
    public TerminalSubTurnViewer toTerminal() {
        return null;
    }

    @Override
    public GUISubTurnViewer toGUI()  {
        return new OpponentBuildSubTurn(this);
    }

    @Override
    public CLISubTurnViewer toCLI()  {
        return new CLIOpponentBuildPhase( this );
    }
}
