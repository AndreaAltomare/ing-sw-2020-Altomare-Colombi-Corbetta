package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton.TitlePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds.SoundEffect;

import javax.swing.*;

/**
 * Class to represent the <code>GUIStatusViewer</code> for the ViewStatus WAITING .
 */
public class GUIWaitingtatus extends GUIStatusViewer {

    StatusViewer myStatusViewer;
    /**
     * constructor.
     *
     * @param statusViewer (the <code>StatusViewer</code> to which this refers).
     */
    public GUIWaitingtatus(StatusViewer statusViewer){ myStatusViewer = statusViewer; }

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
        JPanel panel = new BackgroundPanel("/img/background/sized_waiting_background.png");
        new TitlePanel(panel);
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
        SoundEffect.startLoopMusic("/statusSounds/waiting.wav");
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
     * Method that returns the title to be set on the frame with the nickname of the player.
     *
     * @return ("SANTORINI- " + ViewNickname.getMyNickname()).
     * @see GUIStatusViewer
     */
    @Override
    public String getTitle(){
        return "SANTORINI- " + ViewNickname.getMyNickname();
    }
}
