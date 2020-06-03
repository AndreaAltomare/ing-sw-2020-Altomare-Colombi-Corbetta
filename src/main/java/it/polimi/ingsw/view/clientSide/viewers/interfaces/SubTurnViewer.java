package it.polimi.ingsw.view.clientSide.viewers.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalSubTurnViewer;

public abstract class SubTurnViewer {

    protected ViewSubTurn mySubTurn;

    public SubTurnViewer (ViewSubTurn viewSubTurn){
        mySubTurn = viewSubTurn;
    }

    public ViewSubTurn getMySubTurn(){
        return mySubTurn;
    }

    public TerminalSubTurnViewer toTerminal() {
        return null;
    }

    public GUISubTurnViewer toGUI() {
        return null;
    }

    public WTerminalSubTurnViewer toWTerminal() {
        return null;
    }

    public CLISubTurnViewer toCLI() { return null; }

}
