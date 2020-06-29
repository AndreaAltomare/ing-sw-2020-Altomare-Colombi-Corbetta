package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses.CLIGamePreparationViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.WTerminalGamePreparationViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses.GUIGamePreparation;

import java.util.Map;

/**
 * Class representing the <code>StatusViewer</code> of the GAME_PREPARATION status.
 *
 * @see StatusViewer
 * @author giorgio
 */
public class GamePreparationViewer extends StatusViewer {

    /**
     * constructor.
     *
     * @param executers (the map of Executer used in this Status).
     */
    public GamePreparationViewer(Map<String, Executer> executers){
        myExecuters = executers;
        myWTerminal = new WTerminalGamePreparationViewer( this );
        myCLI = new CLIGamePreparationViewer( this );
        myGUI = new GUIGamePreparation(this);
        myTerminal = null;
    }
}
