package it.polimi.ingsw.view.clientSide.viewers.subTurnViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.FirstPlayerExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.CLIFirstPlayerChoosePhase;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses.FirstPlayerSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses.WTerminalFirstPlayerChoosePhase;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

/**
 * Class that offers the <code>SubTurnViewer</code> for the CHOOSE_FIRST_PLAYER.
 *
 * @see SubTurnViewer
 * @author giorgio
 */
public class FirstPlayerViewer extends SubTurnViewer {

    /**
     * Constructor.
     *
     * @param viewSubTurn (the <code>ViewSubTurn</code> to which this referrs to).
     */
    public FirstPlayerViewer (ViewSubTurn viewSubTurn) {
        super(viewSubTurn);
    }

    /**
     * Method to the GUI's representation of this Status.
     */
    @Override
    public GUISubTurnViewer toGUI(){
        GUISubTurnViewer ret = new FirstPlayerSubTurn(this);
        if(ret!= null)
            return ret;
        else{
            FirstPlayerExecuter executer = (FirstPlayerExecuter)getMySubTurn().getExecuter();
            executer.setRandom();
            try {
                executer.doIt();
            } catch (CannotSendEventException ignore) {
            }
            Viewer.setAllStatusViewer(ViewStatus.getActual().getNext().getViewer());
            return null;
        }
    }

    /**
     * Method to the WTerminal's representation of this Status.
     */
    @Override
    public WTerminalSubTurnViewer toWTerminal(){
        WTerminalSubTurnViewer ret = new WTerminalFirstPlayerChoosePhase(this);
        if(ret!= null)
            return ret;
        else{
            FirstPlayerExecuter executer = (FirstPlayerExecuter)getMySubTurn().getExecuter();
            executer.setRandom();
            try {
                executer.doIt();
            } catch (CannotSendEventException ignore) {
            }
            Viewer.setAllStatusViewer(ViewStatus.getActual().getNext().getViewer());
            return null;
        }
    }

    /**
     * Method to the CLI's representation of this Status.
     */
    @Override
    public CLISubTurnViewer toCLI() {
        CLISubTurnViewer ret = new CLIFirstPlayerChoosePhase(this);
        if(ret!= null)
            return ret;
        else{
            FirstPlayerExecuter executer = (FirstPlayerExecuter)getMySubTurn().getExecuter();
            executer.setRandom();
            try {
                executer.doIt();
            } catch (CannotSendEventException ignore) {
            }
            Viewer.setAllStatusViewer(ViewStatus.getActual().getNext().getViewer());
            return null;
        }
    }
}
