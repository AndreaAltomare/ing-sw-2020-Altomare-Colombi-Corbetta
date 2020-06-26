package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities;

import it.polimi.ingsw.view.clientSide.View;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * SubPanel containing an image.
 *
 * @see SubPanel
 */
public class ImagePanel extends SubPanel {

    private BufferedImage backgroundImg;

    /**
     * constructor with SubPanel parameters and the image name.
     *
     * @param xDimRapp  ((double) width relative dimension referring to the parent in which is contained).
     * @param yDimRapp  ((double) height relative dimension referring to the parent in which is contained).
     * @param xPosRapp  ((double) width relative position referring to the parent in which is contained).
     * @param yPosRapp  ((double) height relative position referring to the parent in which is contained).
     * @param fileName  ((String) name of the image. Will pe work as "./src/main/resources/" + filename).
     */
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
     * method to change the image.
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
     * Method that returns if the image has not been properly set.
     *
     * @return (true iif the image has not been properly set).
     */
    public boolean isNotSetImg(){
        return backgroundImg == null;
    }
}
