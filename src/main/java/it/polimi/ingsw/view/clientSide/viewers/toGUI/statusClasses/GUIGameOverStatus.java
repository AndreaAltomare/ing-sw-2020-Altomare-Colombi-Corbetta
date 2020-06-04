package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.TitlePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds.SoundEffect;

import javax.swing.*;

public class GUIGameOverStatus extends GUIStatusViewer {

    StatusViewer myStatusViewer;
    public GUIGameOverStatus(StatusViewer statusViewer){ myStatusViewer = statusViewer; }

    public boolean hasJPanel(){ return true; }

    public JPanel getJPanel(){
        JPanel panel = new BackgroundPanel("/img/background/gameOverBackground.png");
        new TitlePanel(panel);
        return panel;
    }

    public void onLoad(){
        SoundEffect.startLoopMusic("/statusSounds/gameOver.wav");
    }

    public void onClose(){
        SoundEffect.stopLoopingMusic();
    }

}
