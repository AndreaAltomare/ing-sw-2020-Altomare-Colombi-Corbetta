package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.GameOverViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;

public class CLIGameOverViewer extends CLIStatusViewer {
    private final ViewStatus viewStatus = ViewStatus.GAME_OVER;

    private GameOverViewer gameOverViewer;

    public CLIGameOverViewer(GameOverViewer gameOverViewer) {
        this.gameOverViewer = gameOverViewer;
    }

    @Override
    public ViewStatus getViewStatus() {
        return viewStatus;
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
