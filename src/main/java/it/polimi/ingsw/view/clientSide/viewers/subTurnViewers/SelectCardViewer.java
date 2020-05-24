package it.polimi.ingsw.view.clientSide.viewers.subTurnViewers;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses.SelectCardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalSubTurnViewer;

public class SelectCardViewer extends SubTurnViewer {

    public SelectCardViewer (ViewSubTurn viewSubTurn){
        super(viewSubTurn);
    }

    @Override
    public TerminalSubTurnViewer toTerminal() {
        return null;
    }

    @Override
    public GUISubTurnViewer toGUI() {
        return new SelectCardSubTurn(this);
    }

    @Override
    public CLISubTurnViewer toCLI() {
        return null;
    }
}
