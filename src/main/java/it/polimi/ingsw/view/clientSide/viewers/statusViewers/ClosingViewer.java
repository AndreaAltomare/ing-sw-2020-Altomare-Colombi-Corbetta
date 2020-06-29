package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;

import java.util.Map;

/**
 * Class representing the <code>StatusViewer</code> of the CLOSING status.
 *
 * @see StatusViewer
 * @author giorgio
 */
public class ClosingViewer extends StatusViewer {


    /**
     * constructor.
     *
     * @param executers (the map of Executer used in this Status).
     */
    public ClosingViewer(Map<String, Executer> executers){
        myExecuters = executers;
        myWTerminal = null;
        myCLI = null;
        myGUI = null;
        myTerminal = null;
    }
}
