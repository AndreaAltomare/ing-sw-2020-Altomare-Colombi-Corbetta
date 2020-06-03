package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.GamePreparationViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;

public class CLIGamePreparationViewer extends CLIStatusViewer {

    private final ViewStatus viewStatus = ViewStatus.GAME_PREPARATION;

    private GamePreparationViewer gamePreparationViewer;

    public CLIGamePreparationViewer(GamePreparationViewer gamePreparationViewer) {
        this.gamePreparationViewer = gamePreparationViewer;
    }

    @Override
    public ViewStatus getViewStatus() {
        return viewStatus;
    }

    @Override
    public void show() {

        if ( this.getMyCLISubTurnViewer() == null){
            for (ViewPlayer viewPlayer : ViewPlayer.getPlayerList()) {
                this.getMyCLIViewer().assignPlayerColor(viewPlayer);
            }
        } else {
            this.getMyCLISubTurnViewer().show();
        }
    }
}
