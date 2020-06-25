package it.polimi.ingsw.view.clientSide.viewers.toGUI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.GamePanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces.GUIStatusViewer;

import javax.swing.*;

/**
 * Class to represent the <code>GUIStatusViewer</code> for the ViewStatus RESUMING .
 */
public class GUIResumingGame extends GUIStatusViewer {

    /**
     * Method that says this has a <code>JPanel</code> to be shown.
     *
     * @return (true).
     * @see GUIStatusViewer
     */
    @Override
    public boolean hasJPanel() {
        return true;
    }

    /**
     * method that returns the <code>Jpanel</code> referring to this that needs to be shown.
     *
     * @return (the <code>JPanel</code> that represents this)
     * @see GUIStatusViewer
     */
    @Override
    public JPanel getJPanel(){
        GamePanel gamePanel;
        gamePanel = new GamePanel();
        return gamePanel;
    }
}
