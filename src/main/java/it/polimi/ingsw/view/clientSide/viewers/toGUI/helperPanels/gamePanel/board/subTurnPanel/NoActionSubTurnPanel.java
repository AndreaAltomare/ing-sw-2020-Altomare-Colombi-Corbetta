package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;

/**
 * Class to represent the Panel at the side of the board when the sub turn does not allow the player to do anything.
 */
public class NoActionSubTurnPanel extends ImagePanel {

    public NoActionSubTurnPanel(){
        super(0.9, 0.9, 0.05, 0.05, "/img/background/subTurnPanel/noActionPanel.png");
    }
}
