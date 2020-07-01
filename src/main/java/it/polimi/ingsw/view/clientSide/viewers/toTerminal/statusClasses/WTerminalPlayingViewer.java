package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.statusViewers.PlayingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;

/**
 * Class that represents the <code>WTerminalStatusViewer</code> PLaying on the Windows Terminal
 *
 * @see WTerminalStatusViewer
 * @author Marco
 */
public class WTerminalPlayingViewer extends WTerminalStatusViewer {

    private PlayingViewer playingViewer;

    /**
     * Constructor to set correct <code>StatusViewer</code>
     *
     * @see PlayingViewer
     * @param playingViewer  <code>StatusViewer</code> linked at this class
     */
    public WTerminalPlayingViewer(PlayingViewer playingViewer) {
        this.playingViewer = playingViewer;
    }

    /**
     * Methods which calls the show method on myWTerminalSubTurnViewer if only MyWTerminalSubTUrnViewer != null,
     * or doesn't anything if it isn't
     *
     * @see WTerminalStatusViewer
     */
    @Override
    public void show() {
        if ( this.getMyWTerminalSubTurnViewer() != null) {
            this.getMyWTerminalSubTurnViewer().show();
        }
    }
}
