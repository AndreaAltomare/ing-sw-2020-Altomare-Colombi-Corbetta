package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;

import java.util.Map;

public class GamePreparationViewer implements StatusViewer {

    Map<String, Executer> myExecuters;

    public GamePreparationViewer(Map<String, Executer> executers){
        myExecuters = executers;
    }

    @Override
    public Map<String, Executer> getMyExecuters() { return myExecuters; }

    @Override
    public TerminalStatusViewer toTerminal() { return null; }

    @Override
    public GUIStatusViewer toGUI() {
        return null;
    }

    @Override
    public CLIStatusViewer toCLI() {
        return null;
    }
}
