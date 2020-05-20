package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SubTurnPanel extends JPanel {

    public SubTurnPanel(){
        super();
        this.setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension dimension = getSize();

        Graphics2D g2d = (Graphics2D) g;

        g2d.setPaint(Color.GREEN);
        g2d.fill(new RoundRectangle2D.Double(0, 0, dimension.getWidth(), dimension.getHeight(), 50, 50));
    }
}
