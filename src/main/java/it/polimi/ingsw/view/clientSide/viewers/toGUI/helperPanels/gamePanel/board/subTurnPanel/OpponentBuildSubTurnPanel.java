package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

/**
 * Class to represent the Panel at the side of the board when the sub turn is OPPONENT_BUILD.
 */
public class OpponentBuildSubTurnPanel extends SubTurnPlayingPanel {


    /**
     * constructor.
     *
     * @param name (name of the player which is the turn).
     */
    public OpponentBuildSubTurnPanel(String name){
        super(name, false, true, false, false);
    }
}
