package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.GamePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds.SoundEffect;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GUIGamePreparation extends GUIStatusViewer {
    GamePanel gamePanel;
    StatusViewer myStatusViewer;

    public GUIGamePreparation(StatusViewer statusViewer) {
        myStatusViewer = statusViewer;
    }

    public boolean hasJPanel() {
        return true;
    }

    public JPanel getJPanel(){
        gamePanel = new GamePanel();
        return gamePanel;
    }

    public void setSubTurn(GUISubTurnViewer subTurnViewer){
        gamePanel.setSubTurn(subTurnViewer);
    }

    public void onLoad(){
        SoundEffect.startLoopMusic("/statusSounds/preparation.wav");
    }

    public void onClose(){
        SoundEffect.stopLoopingMusic();
    }

}
