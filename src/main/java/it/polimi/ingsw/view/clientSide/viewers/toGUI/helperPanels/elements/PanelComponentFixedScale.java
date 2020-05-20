package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements;

import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;

import javax.swing.*;
import java.awt.*;

public class PanelComponentFixedScale extends SubPanel {

    private Component component;
    // scale = y/x
    private double scale;

    public PanelComponentFixedScale(double xDimRapp, double yDimRapp, double xPosRapp, double yPosRapp, Component component, double scale) {
        super(xDimRapp, yDimRapp, xPosRapp, yPosRapp);
        this.component = component;
        this.scale = scale;
        this.add(component);
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale){
        this.scale = scale;
    }

    public void setComponent(Component component){ this.component = component; }

    public Component getComponent(){ return this.component; }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension dimension = getSize();

        double dx, dy;

        dx = dimension.getWidth();
        dy = dx * scale;
        if(dy > dimension.getHeight()){
            dy = dimension.getHeight();
            dx = dy/scale;
        }
        component.setBounds((int)(dimension.getWidth()-dx)/2, (int)(dimension.getHeight()-dy)/2, (int)(dx), (int)(dy));
    }


}

