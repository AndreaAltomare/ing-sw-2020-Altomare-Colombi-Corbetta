package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewWorker;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.GamePreparationViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.exceptions.NotFoundException;

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

    /**
     * assign a CLIColor to each workers' color of each player if there isn't a CLISubTurnViewer,
     * or invokes the show methods on CLISubTurnViewer if there is one
     */
    @Override
    public void show() {
        ViewWorker[] workers;

        if ( this.getMyCLISubTurnViewer() == null){
            for (ViewPlayer viewPlayer : ViewPlayer.getPlayerList()) {
                try {
                    workers = viewPlayer.getWorkers();
                    this.getMyCLIViewer().assignWorkerCLIColor( workers[0].getColor() );
                } catch (NotFoundException e) {
                    e.printStackTrace(); //todo: deleter after testing (exception ignored)
                }
            }
        } else {
            this.getMyCLISubTurnViewer().show();
        }
    }
}
