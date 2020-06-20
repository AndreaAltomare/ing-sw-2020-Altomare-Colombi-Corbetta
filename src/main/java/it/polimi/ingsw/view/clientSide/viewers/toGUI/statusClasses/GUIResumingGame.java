package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.GamePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;

import javax.swing.*;

public class GUIResumingGame extends GUIStatusViewer {
    public boolean hasJPanel() {
        return true;
    }

    public JPanel getJPanel(){
        GamePanel gamePanel;
        gamePanel = new GamePanel();
        return gamePanel;
    }
}
