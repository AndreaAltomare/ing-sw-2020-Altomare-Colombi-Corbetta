package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.statusViewers.GamePreparationViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;

/**
 * Class that represents the <code>WTerminalStatusViewer</code> GameViewer on the Windows Terminal
 *
 * @see WTerminalStatusViewer
 * @author Marco
 */
public class WTerminalGamePreparationViewer extends WTerminalStatusViewer {

    private GamePreparationViewer gamePreparationViewer;

    /**
     * Constructor to set correct <code>StatusViewer</code>
     *
     * @see GamePreparationViewer
     * @param gamePreparationViewer <code>StatusViewer</code> linked at this class
     */
    public WTerminalGamePreparationViewer(GamePreparationViewer gamePreparationViewer) {
        this.gamePreparationViewer = gamePreparationViewer;
    }

    /**
     * Methods which calls the show method on myWTerminalSubTurnViewer if only MyWTerminalSubTUrnViewer != null,
     * or doesn't anything if it isn't
     *
     * @see WTerminalStatusViewer
     */
    @Override
    public void show() {

        if ( this.getMyWTerminalSubTurnViewer() != null){
            this.getMyWTerminalSubTurnViewer().show();
            }
    }
}
