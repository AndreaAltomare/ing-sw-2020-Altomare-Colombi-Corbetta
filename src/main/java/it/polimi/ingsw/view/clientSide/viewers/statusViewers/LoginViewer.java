package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses.CLILoginViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.WTerminalLoginViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses.GUILoginStatus;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.TerminalLoginStatus;

import java.util.Map;

public class LoginViewer extends StatusViewer {

    public LoginViewer(Map<String, Executer> executers){
        myExecuters = executers;
        myWTerminal = new WTerminalLoginViewer(this);
        myCLI = new CLILoginViewer( this );
        myGUI = new GUILoginStatus(this);
        myTerminal = new TerminalLoginStatus(this);
    }
}
