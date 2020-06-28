package it.polimi.ingsw.view.clientSide.viewers.subTurnViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.FirstPlayerExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses.CLIFirstPlayerChoosePhase;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses.GUIResumeRequest;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses.FirstPlayerSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses.WTerminalFirstPlayerChoosePhase;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import java.util.Map;

public class FirstPlayerViewer extends SubTurnViewer {
    public FirstPlayerViewer (ViewSubTurn viewSubTurn) {
        super(viewSubTurn);
    }

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
