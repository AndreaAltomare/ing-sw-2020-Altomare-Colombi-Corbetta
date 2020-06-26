package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.GamePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds.SoundEffect;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.subTurnClasses.NoActionSubTurn;

import javax.swing.*;

/**
 * Class to represent the <code>GUIStatusViewer</code> for the ViewStatus PLAYING .
 */
public class GUIPlaying extends GUIStatusViewer {
    private GamePanel gamePanel;

    private StatusViewer myStatusViewer;

    /**
     * constructor.
     *
     * @param statusViewer (the <code>StatusViewer</code> to which this refers).
     */
    public GUIPlaying(StatusViewer statusViewer) {
        myStatusViewer = statusViewer;
    }

    /**
     * Method that says this has a <code>JPanel</code> to be shown.
     *
     * @return (true).
     * @see GUIStatusViewer
     */
    @Override
    public boolean hasJPanel() {
        gamePanel = GamePanel.getLast();
        setSubTurn(new NoActionSubTurn(null));
        return false;
    }

    /**
     * method that returns the <code>Jpanel</code> referring to this that needs to be shown.
     *
     * @return (the <code>JPanel</code> that represents this)
     * @see GUIStatusViewer
     */
    @Override
    public JPanel getJPanel(){
        return gamePanel;
    }

    /**
     * setter method for the actual subTurnViewer of this.
     *
     * @param subTurnViewer (<code>subTurnViewer</code> that this refers to).
     */
    @Override
    public void setSubTurn(GUISubTurnViewer subTurnViewer){
        gamePanel.setSubTurn(subTurnViewer);
    }

    /**
     * method that is executed on the loading of the Status.
     * It starts playing the music relative to this status.
     *
     * @see GUIStatusViewer
     */
    @Override
    public void onLoad(){
        SoundEffect.startLoopMusic("/statusSounds/playing.wav");
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
}
