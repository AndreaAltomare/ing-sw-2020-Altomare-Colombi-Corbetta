package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.statusViewers.PlayingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;

public class CLIPlayingViewer extends CLIStatusViewer {

    private PlayingViewer playingViewer;

    public CLIPlayingViewer(PlayingViewer playingViewer) {
        this.playingViewer = playingViewer;
    }

    @Override
    public void show() {
        if ( this.getMyCLISubTurnViewer() != null) {
            this.getMyCLISubTurnViewer().show();
        }
    }
}
