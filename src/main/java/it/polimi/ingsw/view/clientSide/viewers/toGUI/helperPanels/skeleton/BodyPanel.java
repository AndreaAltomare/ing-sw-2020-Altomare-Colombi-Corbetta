package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;

import javax.swing.*;
import java.awt.*;

/**
 * <code>SubPanel</code> to represent the middle panel inside the <code>GamePanel</code>.
 * It contains the Board and 2 side Panels, and ensures the right resize to its content.
 *
 * This class is intended to grant the correct resize to its content, and does not populate its Panels by itself a part of the board's panel.
 */
public class BodyPanel extends SubPanel {

    protected JPanel subTurnPanel = new JPanel();
    private JPanel playerPanel = new JPanel();
    private JPanel boardPanel = new JPanel();

    /**
     * Constructor.
     */
    public BodyPanel(){
        super(1, 0.7, 0, 0.2);
        this.setOpaque(false);

        add(subTurnPanel);
        add(playerPanel);
        add(boardPanel);
        boardPanel.add(ViewBoard.getBoard().toGUI());
        subTurnPanel.setOpaque(false);
        playerPanel.setOpaque(false);
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

        Dimension myDim = getSize();


        double rapp = 1.0;

        int panelWMin = 10;

        double hypX, hypY;
        double panelW, panelH;

        panelH = myDim.getHeight();

        hypY = myDim.getHeight();
        hypX = hypY * rapp;
        panelW = (myDim.getWidth() - hypX)/2;
        if(panelW<panelWMin){
            panelW = panelWMin;
            hypX = myDim.getWidth() - 2*panelW;
            hypY = hypX/rapp;

        }


        if(ViewBoard.getBoard()!=null)
            ViewBoard.getBoard().toGUI();
        else
            return;

        subTurnPanel.setBounds(0, 0, (int)panelW, (int)panelH);
        playerPanel.setBounds((int)(panelW+hypX), 0, (int)panelW, (int)panelH);
        boardPanel.setBounds((int)(myDim.getWidth()-hypX)/2, (int)(myDim.getHeight()-hypY)/2, (int)hypX, (int)hypY);

    }

    /**
     * getter returning the board's Panel entailed into this.
     *
     * @return (the Panel containing the Board)
     */
    public JPanel getBoardPanel(){
        return boardPanel;
    }

    /**
     * getter returning the subTurn's Panel -left panel- entailed into this.
     *
     * @return (the Panel at the left of the Board)
     */
    public JPanel getSubTurnPanel(){
        return subTurnPanel;
    }

    /**
     * getter returning the paler's Panel -right panel- entailed into this.
     * Now this Panel contains the chat system.
     *
     * @return (the Panel at the right of the Board)
     */
    public JPanel getPlayerPanel() { return playerPanel; }

}
