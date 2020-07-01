package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses.CLINumberPlayerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.WTerminalNumberPlayerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses.GUINumberPlayerStatus;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.TerminalNumberPlayerViewer;

import java.util.Map;

/**
 * Class representing the <code>StatusViewer</code> of the NUMBER_PLAYER status.
 *
 * @see StatusViewer
 * @author giorgio
 */
public class NumberPlayerViewer extends StatusViewer {

    /**
     * constructor.
     *
     * @param executers (the map of Executer used in this Status).
     */
    public NumberPlayerViewer(Map<String, Executer> executers){
        myExecuters = executers;
        myWTerminal = new WTerminalNumberPlayerViewer(this);
        myCLI = new CLINumberPlayerViewer( this );
        myGUI = new GUINumberPlayerStatus(this);
        myTerminal = new TerminalNumberPlayerViewer(this);
    }
}