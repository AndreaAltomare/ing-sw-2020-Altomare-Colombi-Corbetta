package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.statusViewers.GameOverViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;

//todo: maybe to eliminate
public class CLIGameOverViewer extends CLIStatusViewer {

    private GameOverViewer gameOverViewer;

    public CLIGameOverViewer(GameOverViewer gameOverViewer) {
        this.gameOverViewer = gameOverViewer;
    }

    /**
     * If MyCLISubTUrnViewer != null, calls the show method on myCLISubTurnViewer
     */
    @Override
    public void show() {
        if (this.getMyCLISubTurnViewer() != null) {
            this.getMyCLISubTurnViewer().show();
        }
    }

}
