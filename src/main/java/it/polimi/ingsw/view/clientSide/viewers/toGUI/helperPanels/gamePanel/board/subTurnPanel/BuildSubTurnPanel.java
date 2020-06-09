package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;

public class BuildSubTurnPanel extends SubTurnPlayingPanel {
    public BuildSubTurnPanel(){
        super (ViewNickname.getMyNickname(), false, true, false, false);
    }
}
