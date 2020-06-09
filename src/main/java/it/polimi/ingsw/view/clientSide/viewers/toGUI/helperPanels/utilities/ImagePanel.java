package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities;

import it.polimi.ingsw.view.clientSide.View;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImagePanel extends SubPanel {

    private BufferedImage backgroundImg;

    public ImagePanel(double xDimRapp, double yDimRapp, double xPosRapp, double yPosRapp, String fileName) {
        super(xDimRapp, yDimRapp, xPosRapp, yPosRapp);
        try {
            backgroundImg = ImageIO.read(getClass().getResource(fileName));
        }catch(IOException e){
            if(View.debugging)
                e.printStackTrace();
            backgroundImg = null;
        }
        setOpaque(false);
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

    public void setBackgroundImg(String fileName){
        try {
            backgroundImg = ImageIO.read(getClass().getResource(fileName));
        }catch(IOException e){
            if(View.debugging)
                e.printStackTrace();
            backgroundImg = null;
        }
    }

    public boolean isSetImg(){
        return backgroundImg!=null;
    }
}
