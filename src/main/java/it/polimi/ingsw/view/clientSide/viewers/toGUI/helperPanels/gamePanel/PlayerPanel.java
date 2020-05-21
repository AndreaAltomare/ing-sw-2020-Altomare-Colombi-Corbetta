package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class PlayerPanel extends JPanel {

    private final int myBorders = 5;

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        setOpaque(false);

        Dimension dimension = getSize();

        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(Color.DARK_GRAY);
        g2d.fill(new RoundRectangle2D.Double(myBorders, myBorders, dimension.getWidth(), dimension.getHeight(), 50, 50));



    }
}
