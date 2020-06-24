package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;

/**
 * Class to represent the Panel at the side of the board when the sub turn is PLACEWORKER and the playing player is the actual player.
 */
public class PlaceWorkerSubTurnPanel extends ImagePanel {


    /**
     * constructor
     */
    public PlaceWorkerSubTurnPanel(){
        super(0.9, 0.9, 0.05, 0.05, "/img/background/subTurnPanel/canPositionPanel.png");
    }
}
