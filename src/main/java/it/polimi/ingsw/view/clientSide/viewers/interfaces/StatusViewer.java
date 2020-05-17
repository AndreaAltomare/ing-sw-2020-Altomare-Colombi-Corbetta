package it.polimi.ingsw.view.clientSide.viewers.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;

import java.util.Map;

/**
 * Interface used in the ApplicationStatus to return the various viewer for CLI, GUI and Terminal
 */
public abstract class StatusViewer {

    protected Map<String, Executer> myExecuters;

    protected TerminalStatusViewer myTerminal;

    protected GUIStatusViewer myGUI;

    protected CLIStatusViewer myCLI;

    public Map<String, Executer> getMyExecuters() {
        return myExecuters;
    }

    public TerminalStatusViewer toTerminal(){ return myTerminal; }

    public GUIStatusViewer toGUI(){ return myGUI; }

    public CLIStatusViewer toCLI(){ return myCLI; }
}
