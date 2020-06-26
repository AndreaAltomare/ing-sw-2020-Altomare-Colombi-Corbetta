package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;

/**
 * Class to represent the Panel at the side of the board when the sub turn is BUILD_DOME and the playing player is the actual player.
 *
 * @see SubTurnPlayingPanel
 */
public class BuildDomeSubTurnPanel extends SubTurnPlayingPanel {


    /**
     * constructor
     */
    public BuildDomeSubTurnPanel(){
        super (ViewNickname.getMyNickname(), false, false, false, true);
    }
}