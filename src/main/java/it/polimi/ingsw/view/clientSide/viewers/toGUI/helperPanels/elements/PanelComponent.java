package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements;

import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;

import javax.swing.*;
import java.awt.*;

public class PanelComponent extends SubPanel {

    private Component component;

    public PanelComponent(double xDimRapp, double yDimRapp, double xPosRapp, double yPosRapp, Component component) {
        super(xDimRapp, yDimRapp, xPosRapp, yPosRapp);
        this.component = component;
        this.add(component);
    }

    public void setComponent(Component component){ this.component = component; }

    public Component getComponent(){ return this.component; }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension dimension = getSize();

        component.setBounds(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());
    }
}
