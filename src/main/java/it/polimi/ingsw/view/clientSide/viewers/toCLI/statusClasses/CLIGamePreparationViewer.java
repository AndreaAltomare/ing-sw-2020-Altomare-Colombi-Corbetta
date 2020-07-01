package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;


import it.polimi.ingsw.view.clientSide.viewers.statusViewers.GamePreparationViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;

/**
 * Class that represents the <code>CLIStatusViewer</code> gamePreparation on the CLI
 *
 * @see CLIStatusViewer
 * @author Marco
 */
public class CLIGamePreparationViewer extends CLIStatusViewer {

    private GamePreparationViewer gamePreparationViewer;

    /**
     * Constructor to set correct <code>StatusViewer</code>
     *
     * @see GamePreparationViewer
     * @param gamePreparationViewer <code>StatusViewer</code> linked at this class
     */
    public CLIGamePreparationViewer(GamePreparationViewer gamePreparationViewer) {
        this.gamePreparationViewer = gamePreparationViewer;
    }

    /**
     * Methods which calls the show method on myCLISubTurnViewer if only MyCLISubTUrnViewer != null,
     * or doesn't anything if it isn't
     *
     * @see CLIStatusViewer
     */
    @Override
    public void show() {

        if ( this.getMyCLISubTurnViewer() != null){
            this.getMyCLISubTurnViewer().show();
        }
    }
}
