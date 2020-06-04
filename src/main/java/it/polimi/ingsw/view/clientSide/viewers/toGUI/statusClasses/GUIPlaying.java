package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.GUIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.GamePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.BodyPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.TitlePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds.SoundEffect;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses.NoActionSubTurn;
import it.polimi.ingsw.view.exceptions.CheckQueueException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GUIPlaying extends GUIStatusViewer {
    private GamePanel gamePanel;

    private StatusViewer myStatusViewer;

    public GUIPlaying(StatusViewer statusViewer) {
        myStatusViewer = statusViewer;
    }

    public boolean hasJPanel() {
        gamePanel = GamePanel.getLast();
        setSubTurn(new NoActionSubTurn(null));
        return false;
    }

    public JPanel getJPanel(){
        return gamePanel;
    }

    public void setSubTurn(GUISubTurnViewer subTurnViewer){
        gamePanel.setSubTurn(subTurnViewer);
    }

    public void onLoad(){
        SoundEffect.startLoopMusic("playing.wav");
    }

    public void onClose(){
        SoundEffect.stopLoopingMusic();
    }
}
