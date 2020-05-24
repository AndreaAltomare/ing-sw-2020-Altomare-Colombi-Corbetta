package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;

public class SelectWorkerSubTurnPanel extends PlayerSubTurnPanel {
    public SelectWorkerSubTurnPanel(){
        super("/img/background/subTurnPanel/selectWorker.png", ViewNickname.getMyNickname());
    }
}
