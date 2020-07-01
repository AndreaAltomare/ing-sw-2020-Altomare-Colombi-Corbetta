package it.polimi.ingsw.view.clientSide.viewers.subTurnViewers;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.CLIPlaceWorkerPhase;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses.WTerminalPlaceWorkerPhase;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses.PlaceWorkerSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalSubTurnViewer;

/**
 * Class that offers the <code>SubTurnViewer</code> for the PLACEWORKER.
 *
 * @see SubTurnViewer
 * @author giorgio
 */
public class PlaceWorkerViewer extends SubTurnViewer {

    /**
     * Constructor.
     *
     * @param viewSubTurn (the <code>ViewSubTurn</code> to which this referrs to).
     */
    public PlaceWorkerViewer (ViewSubTurn viewSubTurn){
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
    public GUISubTurnViewer toGUI() {
        return new PlaceWorkerSubTurn(this);
    }

    /**
     * Method to the WTerminal's representation of this Status.
     */
    @Override
    public WTerminalSubTurnViewer toWTerminal()  {
        return new WTerminalPlaceWorkerPhase( this );
    }

    /**
     * Method to the CLI's representation of this Status.
     */
    @Override
    public CLISubTurnViewer toCLI() { return new CLIPlaceWorkerPhase( this ); }
}
