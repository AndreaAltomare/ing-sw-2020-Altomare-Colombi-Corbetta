package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses.GUIReadyStatus;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.TerminalReadyStatus;

import java.util.Map;

public class ReadyViewer extends StatusViewer {
    public ReadyViewer(Map<String, Executer> executers) {

        myExecuters = executers;
        myCLI = null;
        myGUI = new GUIReadyStatus(this);
        myTerminal = new TerminalReadyStatus(this);
    }
}
