package it.polimi.ingsw.view.clientSide.viewers.toGUI.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.gamePanel.board.boardSubTurn.BoardSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.helperPanels.utilities.ImagePanel;

import javax.swing.*;

public abstract class GUISubTurnViewer implements SpecificSubTurnViewer {

    protected ViewSubTurn viewSubTurn;
    protected SubTurnViewer parent;

    protected GUISubTurnViewer(SubTurnViewer parent){
        parent = parent;
        viewSubTurn = parent.getMySubTurn();
    }

    public JPanel getSubTurnPanel(){
        return new ImagePanel(0.9, 0.9, 0.05, 0.05, "/img/background/subTurnPanel/noActionPanel.png");
    }

    public BoardSubTurn getBoardSubTurn(){
        return new BoardSubTurn(this);
    }

    protected boolean isMyTurn(){
        return viewSubTurn.isMyTurn();
    }

    public Executer getMyExecuter(){
        return viewSubTurn.getExecuter();
    }
}
