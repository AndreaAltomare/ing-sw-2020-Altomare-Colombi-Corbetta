package it.polimi.ingsw.view.clientSide.viewers.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;

import java.util.Map;

/**
 * Class used to retrieve the various viewer for CLI, GUI, WTerminal, and all the Viewers connected to one Status
 *
 * @author giorgio
 */
public abstract class StatusViewer {

    /**
     * map of the Executers (related to this Status) to Strings describing them
     */
    protected Map<String, Executer> myExecuters;

    /**
     * the Terminal's representation of this Status.
     */
    protected TerminalStatusViewer myTerminal;

    /**
     * the GUI's representation of this Status.
     */
    protected GUIStatusViewer myGUI;

    /**
     * the WTerminal's representation of this Status.
     */
    protected WTerminalStatusViewer myWTerminal;

    /**
     * the CLI's representation of this Status.
     */
    protected CLIStatusViewer myCLI;

    /**
     * Method to retrieve a map mapping the Executers (related to this Status) to Strings
     *
     * @return A Map mapping the Executers
     */
    public Map<String, Executer> getMyExecuters() {
        return myExecuters;
    }

    /**
     * Method to the Terminal's representation of this Status.
     *
     * @return A TerminalStatusViewer object
     */
    public TerminalStatusViewer toTerminal(){ return myTerminal; }

    /**
     * Method to the GUI's representation of this Status.
     *
     * @return A GUIStatusViewer object
     */
    public GUIStatusViewer toGUI(){ return myGUI; }

    /**
     * Method to the WTerminal's representation of this Status.
     *
     * @return A WTerminalStatusViewer object
     */
    public WTerminalStatusViewer toWTerminal(){ return myWTerminal; }

    /**
     * Method to the CLI's representation of this Status.
     *
     * @return A CLIStatusViewer object
     */
    public CLIStatusViewer toCLI() { return  myCLI; }
}
