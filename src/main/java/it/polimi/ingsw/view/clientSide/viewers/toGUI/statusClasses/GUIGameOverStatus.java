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

/**
 * Class to represent the <code>GUIStatusViewer</code> for the ViewStatus GAME_OVER .
 */
public class GUIGameOverStatus extends GUIStatusViewer {

    StatusViewer myStatusViewer;

    /**
     * constructor.
     *
     * @param statusViewer (the <code>StatusViewer</code> to which this refers).
     */
    public GUIGameOverStatus(StatusViewer statusViewer){ myStatusViewer = statusViewer; }

    /**
     * Method that says this has a <code>JPanel</code> to be shown.
     *
     * @return (true).
     * @see GUIStatusViewer
     */
    @Override
    public boolean hasJPanel(){ return true; }

    /**
     * method that returns the <code>Jpanel</code> referring to this that needs to be shown.
     *
     * @return (the <code>JPanel</code> that represents this)
     * @see GUIStatusViewer
     */
    @Override
    public JPanel getJPanel(){
        JPanel panel = new BackgroundPanel("/img/background/gameOverBackground.png");
        new TitlePanel(panel);

        JButton quitter = new JButton();
        quitter.setOpaque(false);
        quitter.addActionListener(e -> Viewer.exitAll());

        panel.add(new PanelImageButton(0.6, 0.15, 0.2, 0.8 , quitter, "/img/trappings/redButton.png", "quit"));

        JLabel credits = new JLabel("<html> sounds from: <a href=\"www.Zapslat.com\"> Zapslat.com </a> </html>");
        //label.setText("<html>" +  +"</html>");
        JPanel textPanel = new PanelComponent(0.9, 0.1, 0.4, 0.7, credits);
        textPanel.setOpaque(false);
        panel.add(textPanel);

        return panel;
    }

    /**
     * method that is executed on the loading of the Status.
     * It starts playing the music relative to this status.
     *
     * @see GUIStatusViewer
     */
    @Override
    public void onLoad(){
        SoundEffect.startLoopMusic("/statusSounds/gameOver.wav");
    }

    /**
     * method that is executed on the closing of the Status.
     * It stops playing the music relative to this status.
     *
     * @see GUIStatusViewer
     */
    @Override
    public void onClose(){
        SoundEffect.stopLoopingMusic();
    }

    /**
     * Method that says this needs to set the frame title.
     *
     * @return (true).
     * @see GUIStatusViewer
     */
    @Override
    public boolean setFrameTitle(){
        return true;
    }

    /**
     * Method that returns the title to be set on the frame.
     *
     * @return ("SANTORINI").
     * @see GUIStatusViewer
     */
    @Override
    public String getTitle(){
        return "SANTORINI";
    }

}
