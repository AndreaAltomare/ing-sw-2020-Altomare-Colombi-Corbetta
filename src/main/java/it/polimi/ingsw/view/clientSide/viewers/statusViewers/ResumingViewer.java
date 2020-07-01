package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses.GUIResumingGame;

import java.util.Map;

/**
 * Class representing the <code>StatusViewer</code> of the RESUMING status.
 *
 * @see StatusViewer
 * @author giorgio
 */
public class ResumingViewer extends StatusViewer {

    /**
     * constructor.
     *
     * @param executers (the map of Executer used in this Status).
     */
    public ResumingViewer(Map<String, Executer> executers){
        myExecuters = executers;
        myWTerminal = null;
        myCLI = null;
        myGUI = new GUIResumingGame();
        myTerminal = null;
    }
}