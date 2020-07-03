package it.polimi.ingsw.view.clientSide.viewers.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalSubTurnViewer;


/**
 * Class used to retrieve the various viewer for CLI, GUI, WTerminal, and all the Viewers connected to one SubTurn.
 *
 * @author giorgio
 */
public abstract class SubTurnViewer {

    /**
     * Reference to the SubTurn to which this refers to.
     */
    private ViewSubTurn mySubTurn;

    /**
     * Constructor.
     *
     * @param viewSubTurn (the ViewSubTurn to which this refers to).
     */
    public SubTurnViewer (ViewSubTurn viewSubTurn){
        mySubTurn = viewSubTurn;
    }

    /**
     * Method to retrieve the ViewSubTurn to which this refers to.
     *
     * @return (the ViewSubTurn to which this refers to).
     */
    public ViewSubTurn getMySubTurn(){
        return mySubTurn;
    }

    /**
     * Method to the Terminal's representation of this Status.
     *
     * @return A TerminalSubTurnViewer (actually always null)
     */
    public TerminalSubTurnViewer toTerminal() {
        return null;
    }

    /**
     * Method to the GUI's representation of this Status.
     *
     * @return A GUISubTurnViewer object (actually always null)
     */
    public GUISubTurnViewer toGUI() {
        return null;
    }

    /**
     * Method to the WTerminal's representation of this Status.
     *
     * @return A WTerminalSubTurnViewer object (actually always null)
     */
    public WTerminalSubTurnViewer toWTerminal() {
        return null;
    }

    /**
     * Method to the CLI's representation of this Status.
     *
     * @return A CLISubTurnViewer object (actually always null)
     */
    public CLISubTurnViewer toCLI() {
        return null;
    }

}
