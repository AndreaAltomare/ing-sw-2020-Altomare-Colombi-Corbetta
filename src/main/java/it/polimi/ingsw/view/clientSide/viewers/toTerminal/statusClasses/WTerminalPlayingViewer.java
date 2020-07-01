package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.statusViewers.PlayingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;

public class WTerminalPlayingViewer extends WTerminalStatusViewer {

    private PlayingViewer playingViewer;

    public WTerminalPlayingViewer(PlayingViewer playingViewer) {
        this.playingViewer = playingViewer;
    }

    @Override
    public void show() {
        if ( this.getMyWTerminalSubTurnViewer() != null) {
            this.getMyWTerminalSubTurnViewer().show();
        }
    }
}
