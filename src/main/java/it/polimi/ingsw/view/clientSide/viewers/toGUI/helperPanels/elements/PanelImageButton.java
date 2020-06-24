package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements;

import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Class that extends the SubPanel and contains a Button with an image.
 *
 * @see SubPanel
 */
public class PanelImageButton extends SubPanel {

    private JButton button;
    private BufferedImage image;

    /**
     * Constructor with the parameters for the subPanel, the image, the button and a String to be shown when it is not possible to load the image.
     *
     * @param xDimRapp  ((double) width relative dimension referring to the parent in which is contained).
     * @param yDimRapp  ((double) height relative dimension referring to the parent in which is contained).
     * @param xPosRapp  ((double) width relative position referring to the parent in which is contained).
     * @param yPosRapp  ((double) height relative position referring to the parent in which is contained).
     * @param button    ((JButton) the button to be inserted).
     * @param fileName  ((String) the name of the image to be loaded. The path of the image must refer to the /main/resources).
     * @param defaultValue  ((String) the string to be printed if it's not able to load the image).
     */
    public PanelImageButton(double xDimRapp, double yDimRapp, double xPosRapp, double yPosRapp, JButton button, String fileName, String defaultValue) {
        super(xDimRapp, yDimRapp, xPosRapp, yPosRapp);
        setOpaque(false);
        this.button= button;
        try {
            image = ImageIO.read(getClass().getResource(fileName));
        }catch(IOException | IllegalArgumentException e){
            if(View.debugging)
                e.printStackTrace();
            image = null;
            button.setText(defaultValue);
        }
        button.setOpaque(false);
        setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        this.add(button);
    }

    /**
     * Method that allows to set the image of the button.
     *
     * @param fileName  ((String) the name of the image to be loaded. The path of the image must refer to the /main/resources).
     * @param defaultValue  ((String) the string to be printed if it's not able to load the image).
     */
    public void setButtonImg(String fileName, String defaultValue){
        try {
            image = ImageIO.read(getClass().getResource(fileName));
        }catch(IOException e){
            if(View.debugging)
                e.printStackTrace();
            image = null;
            button.setText(defaultValue);
        }
    }

    /**
     * Method that return weather an image has been set or not
     *
     * @return (true iif the image has been set or it is not -and so it shows the string-).
     */
    public boolean isSetImg(){
        return image!=null;
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

        if(image!=null) {
            Dimension dimension = getSize();
            button.setBounds(0, 0, (int) (dimension.getWidth()), (int) (dimension.getHeight()));

            double dx, dy, scale;
            scale = ((double) image.getHeight()) / image.getWidth();

            dx = dimension.getWidth();
            dy = dx * scale;
            if (dy > dimension.getHeight()) {
                dy = dimension.getHeight();
                dx = dy / scale;
                if(dx>dimension.getHeight()){
                    dx = dimension.getWidth();
                    dy = dimension.getHeight();
                }
            }
            button.setIcon(new ImageIcon(image.getScaledInstance((int) dx, (int) dy, Image.SCALE_SMOOTH)));
        }
    }
}
