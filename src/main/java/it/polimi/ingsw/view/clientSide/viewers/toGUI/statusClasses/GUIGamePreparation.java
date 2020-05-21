package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.GamePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GUIGamePreparation extends GUIStatusViewer {
    StatusViewer myStatusViewer;
    public GUIGamePreparation(StatusViewer statusViewer){ myStatusViewer = statusViewer; }

    public boolean hasJPanel() {
        return true;
    }

    public JPanel getJPanel(){
        return new GamePanel();
    }

}
