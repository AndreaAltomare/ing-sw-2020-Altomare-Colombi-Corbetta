package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.subTurnPanel;

import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelComponent;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;

import javax.swing.*;
import java.awt.*;

public class OpponentSelectWorkerSubTurnPanel extends PlayerSubTurnPanel {
    /*public OpponentSelectWorkerSubTurnPanel(String playerName)  {
        super(0.9, 0.9, 0.05, 0.05, "/img/background/subTurnPanel/opponentSelectWorker.png");

        JLabel playerNameLabel = new JLabel(playerName);
        playerNameLabel.setFont(new Font("Serif", Font.ITALIC,20));
        playerNameLabel.setForeground(Color.YELLOW);

        JPanel namePanel = new PanelComponent(0.5, 0.8, 0.25, 0, playerNameLabel);
        JPanel namePanelImage = new ImagePanel(0.5, 0.2, 0.25, 0.12, "/img/trappings/god_name.png");
        namePanelImage.add(namePanel);
        namePanel.setOpaque(false);
        add(namePanelImage);
    }*/

    public OpponentSelectWorkerSubTurnPanel(String playerName){
        super("/img/background/subTurnPanel/opponentSelectWorker.png", playerName);
    }
}
