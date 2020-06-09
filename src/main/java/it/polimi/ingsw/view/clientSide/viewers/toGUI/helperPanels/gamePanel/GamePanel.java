package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.BodyPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.PlayerPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.TitlePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends BackgroundPanel {
    private static GamePanel last;

    private BodyPanel bodyPanel;

    public GamePanel(){
        super("/img/background/sized_waiting_background.png");
        new TitlePanel(this);
        bodyPanel = new BodyPanel();
        add(bodyPanel);
        last = this;
    }

    public void setSubTurn(GUISubTurnViewer subTurnViewer){
        JPanel subTurnContainer = bodyPanel.getSubTurnPanel();
        subTurnContainer.removeAll();
        subTurnContainer.add(subTurnViewer.getSubTurnPanel());

        JPanel playerPanel = bodyPanel.getPlayerPanel();

        playerPanel.removeAll();
        playerPanel.add( PlayerPanel.buildNew(subTurnViewer.getGodName()));

        ViewBoard.getBoard().toGUI().setMySubTurn(subTurnViewer.getBoardSubTurn());
        ViewBoard.getBoard().toGUI().setVisible(true);
        Viewer.setAllRefresh();


    }

    public static GamePanel getLast(){
        return last;
    }
}
