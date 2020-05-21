package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.GamePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.BodyPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.TitlePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GUIPlaying extends GUIStatusViewer {

    StatusViewer myStatusViewer;

    public GUIPlaying(StatusViewer statusViewer) {
        myStatusViewer = statusViewer;
    }

    public boolean hasJPanel() {
        return true;
    }

    public JPanel getJPanel(){
        return new GamePanel();
    }




}
