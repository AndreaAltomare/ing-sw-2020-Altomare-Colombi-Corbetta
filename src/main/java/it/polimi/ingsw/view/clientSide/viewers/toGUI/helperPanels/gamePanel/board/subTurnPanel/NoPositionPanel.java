package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;

/**
 * Class to represent the Panel at the side of the board when the sub turn is OPPONENT_PLACEWORKER.
 */
public class NoPositionPanel extends ImagePanel {

    /**
     * constructor.
     */
    public NoPositionPanel()  {
        super(0.9, 0.9, 0.05, 0.05, "/img/background/subTurnPanel/noPositionPanel.png");
    }
}
