package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities;

import it.polimi.ingsw.view.clientSide.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Panel with the background image.
 */
public class BackgroundPanel extends JPanel {

    private BufferedImage backgroundImg;

    /**
     * constructor that construct this with as background image the image with the given name (and path).
     *
     * @param fileName (name of the image. Will pe work as "./src/main/resources/" + filename).
     */
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

    /**
     * Overrided method from SubPanel.
     * It's used to correctly represent the component and the panel itself.
     * It shouldn't be called by the user itself, but it's called by Swing when representing this.
     *
     * @param g ( the <code>Graphics</code> object to protect)
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(backgroundImg!=null) {
            Graphics2D g2d = (Graphics2D) g;
            Dimension dimension = getSize();
            g2d.setPaint(new TexturePaint(backgroundImg, new Rectangle(dimension.width, dimension.height)));
            g2d.fill(new Rectangle2D.Double(0, 0, dimension.width, dimension.height));
        }
    }

    /**
     * method to change the image in the background.
     *
     * @param fileName (name of the image. Will pe work as "./src/main/resources/" + filename).
     */
    public void setBackgroundImg(String fileName){
        try {
            backgroundImg = ImageIO.read(getClass().getResource(fileName));
        }catch(IOException e){
            if(View.debugging)
                e.printStackTrace();
            backgroundImg = null;
        }
    }

    /**
     * Method that returns weather the background image is correctly set or not.
     *
     * @return (true iif background image has been set correctly).
     */
    public boolean isSetImg(){
        return backgroundImg!=null;
    }
}
