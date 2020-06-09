package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton;

import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.BackgroundPanel;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class PlayerPanel extends ImagePanel {

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

    /*@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension dimension = getSize();

        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(Color.GREEN);
        g2d.fill(new RoundRectangle2D.Double(0, 0, dimension.getWidth(), dimension.getHeight(), 50, 50));
    }*/
}
