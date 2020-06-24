package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;

/**
 * Class to represent the Panel at the side of the board when the sub turn is SELECTWORKER and the playing player is the actual player. *
 *
 * @see PlayerSubTurnPanel
 */
public class SelectWorkerSubTurnPanel extends PlayerSubTurnPanel {

    /**
     * constructor
     */
    public SelectWorkerSubTurnPanel(){ super("/img/background/subTurnPanel/selectWorker.png", ViewNickname.getMyNickname()); }
}
