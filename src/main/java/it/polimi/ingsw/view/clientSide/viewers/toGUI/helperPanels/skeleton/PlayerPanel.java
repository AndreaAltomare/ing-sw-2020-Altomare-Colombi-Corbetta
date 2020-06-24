package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton;

import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;

//No longer used because substituted by ChatPanel
/**
 * Panel intended to go into the Panel on the right of the Board and to show the card of the current player.
 */
public class PlayerPanel extends ImagePanel {

    /**
     * constructor that sets the image as the card of the passed god.
     * It sets the image as the image into "/godCards" with name = godName + ".png"
     * If it doesn't find it, it set the background image to be the /img/background/subTurnPanel/noActionPanel.png .
     *
     * @param godName (name of the god).
     */
    public PlayerPanel(String godName){
        super(0.9, 0.9, 0.05, 0.05,"/godCards/" + godName + ".png");
        if (!isSetImg()){
            setBackgroundImg("/img/background/subTurnPanel/noActionPanel.png");
        }
        this.setOpaque(false);
        this.setVisible(true);
    }

    public static ImagePanel buildNew(String godName){
        try{
            return new PlayerPanel(godName);
        }catch (Exception e){
            return new ImagePanel(0.9, 0.9, 0.05, 0.05,"/img/background/subTurnPanel/noActionPanel.png");
        }
    }
}
