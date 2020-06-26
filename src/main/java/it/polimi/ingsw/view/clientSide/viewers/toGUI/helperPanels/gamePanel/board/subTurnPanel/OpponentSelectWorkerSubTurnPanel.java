package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

/**
 * Class to represent the Panel at the side of the board when the sub turn is OPPONENT_SELECTWORKER.
 *
 * @see PlayerSubTurnPanel
 */
public class OpponentSelectWorkerSubTurnPanel extends PlayerSubTurnPanel {

    /**
     * constructor.
     *
     * @param name (name of the player which is the turn).
     */
    public OpponentSelectWorkerSubTurnPanel(String name){ super("/img/background/subTurnPanel/opponentSelectWorker.png", name); }
}
