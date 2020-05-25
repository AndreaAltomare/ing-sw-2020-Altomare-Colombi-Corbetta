package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;

public class MoveSubTurnPanel extends SubTurnPlayingPanel {
    public MoveSubTurnPanel(){
        super (ViewNickname.getMyNickname());
    }
}
