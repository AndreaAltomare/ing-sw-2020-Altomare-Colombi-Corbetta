package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton;

import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.UndoExecuter;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements.PanelImageButton;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.sounds.SoundEffect;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Panel representing the upper part of the screen (the title part) containing the button to activate and turn off the audio and the undo button.
 */
public class TitlePanel extends SubPanel implements UndoExecuter.UndoExecuterListenerUpdate {

    private static final int minH = 20;

    private BufferedImage title;

    private PanelImageButton soundButton;
    private PanelImageButton undoButton;

    /**
     * method that initializes all the parameters once inserted into a parent.
     */
    private void init(){
        Dimension minDim = getParent().getMinimumSize();
        try {
            title = ImageIO.read(getClass().getResource("/img/logo/Santorini.png"));
        } catch (IOException e) {
            if(View.debugging)
                e.printStackTrace();
            return;
        }
        Dimension newMinDim = new Dimension(
                (int) (minDim.getWidth() + minH * (((double) title.getWidth())/title.getHeight())),
                (int) (minDim.getHeight() + minH)
        );
        this.getParent().setMinimumSize(newMinDim);
    }


    /**
     * private constructor that construct this to be ready to be inserted into a parent.
     */
    private TitlePanel() {
        super(1, 0.2, 0, 0);
        this.setOpaque(false);

        JButton soundRealButton = new JButton();
        soundRealButton.addActionListener(actionEvent -> {
            SoundEffect.setEnabled(!SoundEffect.getEnabled());
            if(SoundEffect.getEnabled())
                soundButton.setButtonImg("/img/trappings/speaker.png", "sound on");
            else
                soundButton.setButtonImg("/img/trappings/noSound.png", "mute");
        });
        soundButton = new PanelImageButton(0.1, 0.4, 0.85, 0.1, soundRealButton, "/img/trappings/speaker.png", "sound");
        if(SoundEffect.getEnabled())
            soundButton.setButtonImg("/img/trappings/speaker.png", "sound on");
        else
            soundButton.setButtonImg("/img/trappings/noSound.png", "mute");
        add(soundButton);

        JButton undoRealButton = new JButton();
        undoRealButton.addActionListener(actionEvent -> {
            try {
                UndoExecuter.undoIt();
            } catch (CannotSendEventException ignore) {
            }
        });
        undoButton = new PanelImageButton(0.1, 0.4, 0.15, 0.1, undoRealButton, "/img/trappings/none.png", "");
        add(undoButton);

        UndoExecuter.getInstance().registerListener(this);
    }


    /**
     * public constructor that construct this and insert it into the specified parent.
     *
     * @param parent  (JPanel that will contain this).
     */
    public TitlePanel(JPanel parent) {
        this();

        parent.add(this);

        init();
    }

    /**
     * Overrided method from SubPanel.
     * It's used to correctly represent the component and the panel itself.
     * It shouldn't be called by the user itself, but it's called by Swing when representing this.
     *
     * @param g ( the <code>Graphics</code> object to protect)
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Dimension panelDimension = getSize();
        Dimension titleDimension = new Dimension(
                (int) (panelDimension.getHeight() *(((double) title.getWidth())/title.getHeight())),
                (int) panelDimension.getHeight()
        );
        g.drawImage(title, (int)((panelDimension.getWidth() - titleDimension.getWidth())/2), 0, (int)titleDimension.getWidth(), (int)titleDimension.getHeight(), this);

    }

    /**
     * Light method to be called when the undo is available.
     */
    @Override
    public void undoLightAvailable() {
        undoButton.setButtonImg("/img/trappings/undo.png", "undo");
    }

    /**
     * Heavy method to be called when the undo is available.
     */
    @Override
    public void undoWeightAvailable() {}

    /**
     * method to be called when the undo has expired.
     */
    @Override
    public void undoExpired() {
        undoButton.setButtonImg("/img/trappings/none.png", "");
    }
}
