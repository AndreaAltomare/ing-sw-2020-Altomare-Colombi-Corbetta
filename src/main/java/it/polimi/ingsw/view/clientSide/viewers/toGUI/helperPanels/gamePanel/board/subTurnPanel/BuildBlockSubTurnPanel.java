package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;

public class BuildBlockSubTurnPanel extends SubTurnPlayingPanel {
    public BuildBlockSubTurnPanel(){
        super (ViewNickname.getMyNickname(), false, false, true, false);
    }
}