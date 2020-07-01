package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses.CLIWaitingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.WTerminalWaitingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses.GUIWaitingtatus;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.TerminalWaitingStatus;

import java.util.Map;

/**
 * Class representing the <code>StatusViewer</code> of the WAITING status.
 *
 * @see StatusViewer
 * @author giorgio
 */
public class WaitingViewer extends StatusViewer {

    /**
     * constructor.
     *
     * @param executers (the map of Executer used in this Status).
     */
    public WaitingViewer(Map<String, Executer> executers){
        myExecuters = executers;
        myWTerminal = new WTerminalWaitingViewer(this);
        myCLI = new CLIWaitingViewer( this );
        myGUI = new GUIWaitingtatus(this);
        myTerminal = new TerminalWaitingStatus(this);
    }
}
