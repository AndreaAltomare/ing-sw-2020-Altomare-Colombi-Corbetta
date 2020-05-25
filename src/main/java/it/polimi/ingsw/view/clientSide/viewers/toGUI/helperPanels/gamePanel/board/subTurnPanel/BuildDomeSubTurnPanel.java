package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;

public class BuildDomeSubTurnPanel extends SubTurnPlayingPanel {
    public BuildDomeSubTurnPanel(){
        super (ViewNickname.getMyNickname(), false, false, false, true);
    }
}