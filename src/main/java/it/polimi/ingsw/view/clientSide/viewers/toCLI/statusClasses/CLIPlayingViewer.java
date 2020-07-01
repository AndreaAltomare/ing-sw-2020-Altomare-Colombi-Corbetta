package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.statusViewers.PlayingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;

/**
 * Class that represents the <code>CLIStatusViewer</code> Playing on the CLI
 *
 * @see CLIStatusViewer
 * @author Marco
 */
public class CLIPlayingViewer extends CLIStatusViewer {

    private PlayingViewer playingViewer;

    /**
     * Constructor to set correct <code>StatusViewer</code>
     *
     * @see PlayingViewer
     * @param playingViewer  <code>StatusViewer</code> linked at this class
     */
    public CLIPlayingViewer(PlayingViewer playingViewer) {
        this.playingViewer = playingViewer;
    }

    /**
     * Methods which calls the show method on myCLISubTurnViewer if only MyCLISubTUrnViewer != null,
     * or doesn't anything if it isn't
     *
     * @see CLIStatusViewer
     */
    @Override
    public void show() {
        if ( this.getMyCLISubTurnViewer() != null) {
            this.getMyCLISubTurnViewer().show();
        }
    }
}
