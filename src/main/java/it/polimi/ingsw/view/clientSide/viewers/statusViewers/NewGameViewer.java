package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses.GUINewGame;

import java.util.Map;

public class NewGameViewer extends StatusViewer {
    public NewGameViewer(Map<String, Executer> executers){
            myExecuters = executers;
            myWTerminal = null;
            myCLI = null;
            myGUI = new GUINewGame(this);
            myTerminal = null;
    }
}
