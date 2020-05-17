package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses.GUIWaitingtatus;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.TerminalWaitingStatus;

import java.util.Map;

public class WaitingViewer extends StatusViewer {
    public WaitingViewer(Map<String, Executer> executers){
        myExecuters = executers;
        myCLI = null;
        myGUI = new GUIWaitingtatus(this);
        myTerminal = new TerminalWaitingStatus(this);
    }
}
