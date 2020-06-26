package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities;

import javax.swing.*;
import java.awt.*;

/**
 * <code>JPanel</code> that ensures a properly resizing and has a fixed (relatve) dimension and position referred to the parent's size.
 */
public class SubPanel extends JPanel {

    private double xDimRapp;
    private double yDimRapp;
    private double xPosRapp;
    private double yPosRapp;

    /**
     * constructor that set this' parameters.
     *
     * @param xDimRapp  ((double) width relative dimension referring to the parent in which is contained).
     * @param yDimRapp  ((double) height relative dimension referring to the parent in which is contained).
     * @param xPosRapp  ((double) width relative position referring to the parent in which is contained).
     * @param yPosRapp  ((double) height relative position referring to the parent in which is contained).
     */
    public SubPanel(double xDimRapp, double yDimRapp, double xPosRapp, double yPosRapp){
        this.xDimRapp = xDimRapp;
        this.yDimRapp = yDimRapp;
        this.yPosRapp = yPosRapp;
        this.xPosRapp = xPosRapp;
    }

    /**
     * Method to set the parameters.
     *
     * @param xDimRapp  ((double) width relative dimension referring to the parent in which is contained).
     * @param yDimRapp  ((double) height relative dimension referring to the parent in which is contained).
     * @param xPosRapp  ((double) width relative position referring to the parent in which is contained).
     * @param yPosRapp  ((double) height relative position referring to the parent in which is contained).
     */
    public void setMyRapp(double xDimRapp, double yDimRapp, double xPosRapp, double yPosRapp){
        this.xDimRapp = xDimRapp;
        this.yDimRapp = yDimRapp;
        this.yPosRapp = yPosRapp;
        this.xPosRapp = xPosRapp;
    }

    /**
     * Overrided method from SubPanel.
     * It's used to correctly represent the component and the panel itself.
     * It shouldn't be called by the user itself, but it's called by Swing when representing this.
     *
     * @param g ( the <code>Graphics</code> object to protect)
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Dimension parentDimension = getParent().getSize();

        this.setBounds(
                (int) (parentDimension.getWidth()*xPosRapp),    //x
                (int) (parentDimension.getHeight()*yPosRapp),   //y
                (int) (parentDimension.getWidth()*xDimRapp),    //width
                (int) (parentDimension.getHeight()*yDimRapp)    //height
        );
    }
}
