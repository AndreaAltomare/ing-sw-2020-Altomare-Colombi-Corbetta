package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;

/**
 * Class to represent the Panel at the side of the board when the sub turn is MOVE and the playing player is the actual player.
 *
 * @see SubTurnPlayingPanel
 */
public class MoveSubTurnPanel extends SubTurnPlayingPanel {

    /**
     * constructor.
     */
    public MoveSubTurnPanel(){
        super (ViewNickname.getMyNickname(), true, false, false, false);
    }
}
