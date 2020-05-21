package it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.skeleton;

import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;

import javax.swing.*;

public class BoardPanel extends JPanel {
    public BoardPanel(){
        super();
        //add (new ImagePanel(1,1,0,0, "/img/board/boardScalata.png"));
    }

    public double getWidthOnWeight(){
        return 1;
    }
}
