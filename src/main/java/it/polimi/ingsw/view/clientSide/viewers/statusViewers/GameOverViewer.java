package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses.CLIGameOverViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses.GUIGameOverStatus;

import java.util.Map;

/**
 * Class representing the <code>StatusViewer</code> of the GAME_OVER status.
 *
 * @see StatusViewer
 * @author giorgio
 */
public class GameOverViewer extends StatusViewer {

    /**
     * constructor.
     *
     * @param executers (the map of Executer used in this Status).
     */
    public GameOverViewer(Map<String, Executer> executers){
        myExecuters = executers;
        myWTerminal = null;
        myCLI = new CLIGameOverViewer( this );
        myGUI = new GUIGameOverStatus(this);
        myTerminal = null;
    }
}
