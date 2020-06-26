package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

/**
 * Class to represent the Panel at the side of the board when the sub turn is OPPONENT_MOVE.
 */
public class OpponentMoveSubTurnPanel extends SubTurnPlayingPanel {

    /**
     * constructor.
     *
     * @param name (name of the player which is the turn).
     */
    public OpponentMoveSubTurnPanel(String name){
        super(name, true, false, false, false);
    }
}