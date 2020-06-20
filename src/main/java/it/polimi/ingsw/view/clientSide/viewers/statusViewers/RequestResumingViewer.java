package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses.GUIResumeRequest;

import java.util.Map;

public class RequestResumingViewer extends StatusViewer {
    public RequestResumingViewer(Map<String, Executer> executers){
        myExecuters = executers;
        myWTerminal = null;
        myCLI = null;
        myGUI = new GUIResumeRequest(this);
        myTerminal = null;
    }
}
