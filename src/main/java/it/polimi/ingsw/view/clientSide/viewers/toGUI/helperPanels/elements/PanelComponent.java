package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.elements;

import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;

import java.awt.*;

/**
 * Class that extends SubPanel and is made to contain ONE single component and draw it in the desired place.
 * @see SubPanel
 */
public class PanelComponent extends SubPanel {

    private Component component;

    /**
     * Construcctor with the parameters of the SubPanel and the component to be contained
     *
     * @param xDimRapp  ((double) width relative dimension referring to the parent in which is contained).
     * @param yDimRapp  ((double) height relative dimension referring to the parent in which is contained).
     * @param xPosRapp  ((double) width relative position referring to the parent in which is contained).
     * @param yPosRapp  ((double) height relative position referring to the parent in which is contained).
     * @param component ((Component) component to be contained into this).
     */
    public PanelComponent(double xDimRapp, double yDimRapp, double xPosRapp, double yPosRapp, Component component) {
        super(xDimRapp, yDimRapp, xPosRapp, yPosRapp);
        this.component = component;
        this.add(component);
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
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension dimension = getSize();

        component.setBounds(0, 0, (int)dimension.getWidth(), (int)dimension.getHeight());
    }
}
