package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities;

import it.polimi.ingsw.view.clientSide.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BackgroundPanel extends JPanel {

    private BufferedImage backgroundImg;

    public BackgroundPanel(String fileName){
        super();
        try {
            backgroundImg = ImageIO.read(getClass().getResource(fileName));
        }catch(IOException e){
            if(View.debugging)
                e.printStackTrace();
            backgroundImg = null;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(backgroundImg!=null) {
            Graphics2D g2d = (Graphics2D) g;
            Dimension dimension = getSize();
            g2d.setPaint(new TexturePaint(backgroundImg, new Rectangle(dimension.width, dimension.height)));
            g2d.fill(new Rectangle2D.Double(0, 0, dimension.width, dimension.height));
        }
    }
}
