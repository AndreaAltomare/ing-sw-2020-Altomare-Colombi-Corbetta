package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;

import java.util.Map;

public class ResumingViewer extends StatusViewer {
    public ResumingViewer(Map<String, Executer> executers){
        myExecuters = executers;
        myWTerminal = null;
        myCLI = null;
        myGUI = null;
        myTerminal = null;
    }
}