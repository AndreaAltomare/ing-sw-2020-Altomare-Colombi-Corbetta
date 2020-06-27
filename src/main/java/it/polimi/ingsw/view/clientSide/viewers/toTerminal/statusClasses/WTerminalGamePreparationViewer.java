package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.GamePreparationViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;

public class WTerminalGamePreparationViewer extends WTerminalStatusViewer {

    private final ViewStatus viewStatus = ViewStatus.GAME_PREPARATION;

    private GamePreparationViewer gamePreparationViewer;

    public WTerminalGamePreparationViewer(GamePreparationViewer gamePreparationViewer) {
        this.gamePreparationViewer = gamePreparationViewer;
    }

    @Override
    public ViewStatus getViewStatus() {
        return viewStatus;
    }

    /**
     * Invokes the show methods on CLISubTurnViewer if there is one
     */
    @Override
    public void show() {

        if ( this.getMyWTerminalSubTurnViewer() != null){
            this.getMyWTerminalSubTurnViewer().show();
            }
    }
}
