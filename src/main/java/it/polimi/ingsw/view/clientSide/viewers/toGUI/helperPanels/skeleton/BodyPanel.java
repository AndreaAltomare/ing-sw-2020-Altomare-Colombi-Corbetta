package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewBoard;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.SubPanel;

import javax.swing.*;
import java.awt.*;

public class BodyPanel extends SubPanel {

    private int cont =0;

    protected SubTurnPanel subTurnPanel = new SubTurnPanel();
    protected PlayerPanel playerPanel = new PlayerPanel();
    protected JPanel boardPanel = new JPanel();

    public BodyPanel(){
        super(1, 0.7, 0, 0.2);
        this.setOpaque(false);

        add(subTurnPanel);
        add(playerPanel);
        add(boardPanel);
        boardPanel.add(ViewBoard.getBoard().toGUI());
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Dimension myDim = getSize();


        //double rapp = boardPanel.getWidthOnWeight();
        double rapp = 1.0;

        double hypX, hypY;
        double panelW, panelH;

        panelH = myDim.getHeight();

        hypY = myDim.getHeight();
        hypX = hypY * rapp;
        panelW = (myDim.getWidth() - hypX)/2;
        if(panelW<Math.max(playerPanel.getMinimumSize().getWidth(), subTurnPanel.getSize().getWidth())){
            panelW = Math.max(playerPanel.getMinimumSize().getWidth(), subTurnPanel.getSize().getWidth());
            hypX = myDim.getWidth() - 2*panelW;
            hypY = hypX/rapp;

        }


        ViewBoard.getBoard().toGUI();

        subTurnPanel.setBounds(0, 0, (int)panelW, (int)panelH);
        playerPanel.setBounds((int)(panelW+hypX), 0, (int)panelW, (int)panelH);
        boardPanel.setBounds((int)(myDim.getWidth()-hypX)/2, (int)(myDim.getHeight()-hypY)/2, (int)hypX, (int)hypY);

    }


}
