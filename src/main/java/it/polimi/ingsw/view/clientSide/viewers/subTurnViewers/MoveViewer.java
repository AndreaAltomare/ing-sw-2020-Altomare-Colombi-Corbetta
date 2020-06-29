package it.polimi.ingsw.view.clientSide.viewers.subTurnViewers;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.CLIMovePhase;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses.WTerminalMovePhase;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses.MoveSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalSubTurnViewer;

/**
 * Class that offers the <code>SubTurnViewer</code> for the MOVE.
 *
 * @see SubTurnViewer
 * @author giorgio
 */
public class MoveViewer extends SubTurnViewer {

    /**
     * Constructor.
     *
     * @param viewSubTurn (the <code>ViewSubTurn</code> to which this referrs to).
     */
    public MoveViewer (ViewSubTurn viewSubTurn) {
        super(viewSubTurn);
    }

    /**
     * Method to the Terminal's representation of this Status.
     */
    @Override
    public TerminalSubTurnViewer toTerminal()  {
        return null;
    }

    /**
     * Method to the GUI's representation of this Status.
     */
    @Override
    public GUISubTurnViewer toGUI()   {
        return new MoveSubTurn(this);
    }

    /**
     * Method to the WTerminal's representation of this Status.
     */
    @Override
    public WTerminalSubTurnViewer toWTerminal()  {
        return new WTerminalMovePhase( this );
    }

    /**
     * Method to the CLI's representation of this Status.
     */
    @Override
    public CLISubTurnViewer toCLI() { return new CLIMovePhase( this ); }
}
