package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewWorker;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.GamePreparationViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.exceptions.NotFoundException;

public class CLIGamePreparationViewer extends CLIStatusViewer {

    private GamePreparationViewer gamePreparationViewer;

    public CLIGamePreparationViewer(GamePreparationViewer gamePreparationViewer) {
        this.gamePreparationViewer = gamePreparationViewer;
    }

    /**
     * Invokes the show methods on CLISubTurnViewer if there is one
     */
    @Override
    public void show() {

        if ( this.getMyCLISubTurnViewer() != null){
            this.getMyCLISubTurnViewer().show();
        }
    }
}
