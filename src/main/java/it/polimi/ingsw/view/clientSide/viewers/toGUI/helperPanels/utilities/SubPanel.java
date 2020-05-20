package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities;

import javax.swing.*;
import java.awt.*;

public class SubPanel extends JPanel {

    private final double xDimRapp;
    private final double yDimRapp;
    private final double xPosRapp;
    private final double yPosRapp;

    public SubPanel(double xDimRapp, double yDimRapp, double xPosRapp, double yPosRapp){
        this.xDimRapp = xDimRapp;
        this.yDimRapp = yDimRapp;
        this.yPosRapp = yPosRapp;
        this.xPosRapp = xPosRapp;
    }



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
