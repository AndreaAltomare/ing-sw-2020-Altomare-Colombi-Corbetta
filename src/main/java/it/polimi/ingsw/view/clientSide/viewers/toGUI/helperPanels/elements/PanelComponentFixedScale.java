package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements;

import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;

import java.awt.*;

/**
 * Panel which extends SubPanel used to contain a Component and to represent it with fixed scale. Similar to PanelComponent, but with fixed scale representation.
 *
 * @see SubPanel
 *
 */
public class PanelComponentFixedScale extends SubPanel {

    private Component component;
    // scale = y/x
    private double scale;

    /**
     * Construcctor with the parameters of the SubPanel and the component to be contained
     *
     * @param xDimRapp  ((double) width relative dimension referring to the parent in which is contained).
     * @param yDimRapp  ((double) height relative dimension referring to the parent in which is contained).
     * @param xPosRapp  ((double) width relative position referring to the parent in which is contained).
     * @param yPosRapp  ((double) height relative position referring to the parent in which is contained).
     * @param component ((Component) component to be contained into this).
     * @param scale     ((double) the scale with which it's to be represented the Component).
     */
    public PanelComponentFixedScale(double xDimRapp, double yDimRapp, double xPosRapp, double yPosRapp, Component component, double scale) {
        super(xDimRapp, yDimRapp, xPosRapp, yPosRapp);
        this.component = component;
        this.scale = scale;
        this.add(component);
    }

    /**
     * getter of the scale.
     *
     * @return ((double) the scale with which it's to be represented the Component).
     */
    public double getScale() {
        return scale;
    }

    /**
     * setter of the scale
     *
     * @param scale     ((double) the scale with which it's to be represented the Component).
     */
    public void setScale(double scale){
        this.scale = scale;
    }

    /**
     * setter of the component.
     * this will contain only one Component per time, so it'll remove the previoious component.
     *
     * @param component (Component to be contained).
     */
    public void setComponent(Component component){ this.component = component; }

    /**
     * getter of the component.
     *
     * @return (the Component contained)
     */
    public Component getComponent(){ return this.component; }

    /**
     * Overrided method from SubPanel.
     * It's used to correctly represent the component and the panel itself.
     * It shouldn't be called by the user itself, but it's called by Swing when representing this.
     *
     * @param g ( the <code>Graphics</code> object to protect)
     */
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

