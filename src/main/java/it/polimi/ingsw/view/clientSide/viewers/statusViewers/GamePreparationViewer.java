package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses.CLIGamePreparationViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses.GUIGamePreparation;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;

import java.util.Map;

public class GamePreparationViewer extends StatusViewer {

    public GamePreparationViewer(Map<String, Executer> executers){
        myExecuters = executers;
        myCLI = new CLIGamePreparationViewer( this );
        myGUI = new GUIGamePreparation(this);
        myTerminal = null;
    }
}
