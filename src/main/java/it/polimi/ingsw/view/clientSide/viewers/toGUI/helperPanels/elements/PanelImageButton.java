package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements;

import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PanelImageButton extends SubPanel {

    private JButton button;
    private BufferedImage image;

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

    public void setBackgroundImg(String fileName, String defaultValue){
        try {
            image = ImageIO.read(getClass().getResource(fileName));
        }catch(IOException e){
            if(View.debugging)
                e.printStackTrace();
            image = null;
            button.setText(defaultValue);
        }
    }

    public boolean isSetImg(){
        return image!=null;
    }

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
