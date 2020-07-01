package it.polimi.ingsw.view.clientSide.viewers.statusViewers;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses.CLIPlayingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.WTerminalPlayingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses.GUIPlaying;

import java.util.Map;

/**
 * Class representing the <code>StatusViewer</code> of the PLAYING status.
 *
 * @see StatusViewer
 * @author giorgio
 */
public class PlayingViewer extends StatusViewer {

    /**
     * constructor.
     *
     * @param executers (the map of Executer used in this Status).
     */
    public PlayingViewer(Map<String, Executer> executers){

        myExecuters = executers;
        myWTerminal = new WTerminalPlayingViewer( this );
        myCLI = new CLIPlayingViewer( this );
        myGUI = new GUIPlaying(this );
        myTerminal = null;
    }
}
