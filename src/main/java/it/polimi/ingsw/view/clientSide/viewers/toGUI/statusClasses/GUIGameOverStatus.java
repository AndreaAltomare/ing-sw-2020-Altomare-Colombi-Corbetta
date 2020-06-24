package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelComponent;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.TitlePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds.SoundEffect;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIGameOverStatus extends GUIStatusViewer {

    StatusViewer myStatusViewer;
    public GUIGameOverStatus(StatusViewer statusViewer){ myStatusViewer = statusViewer; }

    public boolean hasJPanel(){ return true; }

    public JPanel getJPanel(){
        JPanel panel = new BackgroundPanel("/img/background/gameOverBackground.png");
        new TitlePanel(panel);

        JButton quitter = new JButton();
        quitter.setOpaque(false);
        quitter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Viewer.exitAll();
            }
        });

        panel.add(new PanelImageButton(0.6, 0.15, 0.2, 0.8 , quitter, "/img/trappings/redButton.png", "quit"));

        JLabel credits = new JLabel("<html> sounds from: <a href=\"www.Zapslat.com\"> Zapslat.com </a> </html>");
        //label.setText("<html>" +  +"</html>");
        JPanel textPanel = new PanelComponent(0.9, 0.1, 0.4, 0.7, credits);
        textPanel.setOpaque(false);
        panel.add(textPanel);

        return panel;
    }

    public void onLoad(){
        SoundEffect.startLoopMusic("/statusSounds/gameOver.wav");
    }

    public void onClose(){
        SoundEffect.stopLoopingMusic();
    }

    public boolean setFrameTitle(){
        return true;
    }

    public String getTitle(){
        return "SANTORINI";
    }

}
