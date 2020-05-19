package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses.CLINumberPlayerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses.GUINumberPlayerStatus;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.TerminalNumberPlayerViewer;

import java.util.Map;

public class NumberPlayerViewer extends StatusViewer {

    public NumberPlayerViewer(Map<String, Executer> executers){
        myExecuters = executers;
        myCLI = new CLINumberPlayerViewer(this);
        myGUI = new GUINumberPlayerStatus(this);
        myTerminal = new TerminalNumberPlayerViewer(this);
    }
}