package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses.CLIResumeGameViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses.GUIResumeRequest;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.WTerminalResumeGameViewer;

import java.util.Map;

public class RequestResumingViewer extends StatusViewer {
    public RequestResumingViewer(Map<String, Executer> executers){
        myExecuters = executers;
        myWTerminal = new WTerminalResumeGameViewer(this);
        myCLI = new CLIResumeGameViewer(this );
        myGUI = new GUIResumeRequest(this);
        myTerminal = null;
    }
}
